package com.financemanager.app.util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import kotlin.math.abs

/**
 * Helper class for extracting text and amounts from receipt images using ML Kit OCR
 */
class ReceiptOcrHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    
    companion object {
        private const val TAG = "ReceiptOCR"
    }
    
    /**
     * SIMPLIFIED 3-STEP OCR:
     * 1. Extract all amounts from receipt
     * 2. Sort by likely candidates (total amounts with GST)
     * 3. Return for user selection dialog
     */
    suspend fun extractAmountFromReceipt(imageFile: File): Double? {
        return try {
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            val image = InputImage.fromBitmap(bitmap, 0)
            val result = textRecognizer.process(image).await()
            
            Log.d(TAG, "=== OCR SCAN START (Simplified) ===")
            Log.d(TAG, "Recognized text:\n${result.text}")
            Log.d(TAG, "Total lines: ${result.text.lines().size}")
            
            // Get all candidates, return top one
            val candidates = extractAmountCandidates(result.text)
            val topAmount = candidates.firstOrNull()?.amount
            
            Log.d(TAG, "Top candidate: ${topAmount?.let { "₹$it" } ?: "NONE"}")
            Log.d(TAG, "Total candidates: ${candidates.size}")
            Log.d(TAG, "=== OCR SCAN END ===")
            
            topAmount
        } catch (e: Exception) {
            Log.e(TAG, "OCR Error: ${e.message}", e)
            null
        }
    }
    
    /**
     * Extract all amount candidates for user selection
     * Returns sorted list with total amounts (including GST) prioritized
     */
    suspend fun extractAmountWithCandidates(imageFile: File): List<AmountResult> {
        return try {
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            val image = InputImage.fromBitmap(bitmap, 0)
            val result = textRecognizer.process(image).await()
            
            Log.d(TAG, "=== EXTRACTING ALL CANDIDATES ===")
            val candidates = extractAmountCandidates(result.text)
            Log.d(TAG, "Found ${candidates.size} valid candidates")
            
            candidates
        } catch (e: Exception) {
            Log.e(TAG, "OCR Error: ${e.message}", e)
            emptyList()
        }
    }
    
    /**
     * Extract amount from receipt image URI
     */
    suspend fun extractAmountFromReceipt(imageUri: Uri): Double? {
        return try {
            val image = InputImage.fromFilePath(context, imageUri)
            val result = textRecognizer.process(image).await()
            
            extractAmountCandidates(result.text).firstOrNull()?.amount
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * UNIVERSAL: Find "TOTAL" in ANY bill format and return amounts
     * Handles:
     * - "Total: 3150" (same line)
     * - "Total: ₹" on one line, "3150" on next line
     * - "Grand Total", "Sub-Total", "Net Total", etc.
     * - Multi-line amounts
     */
    private fun extractAmountCandidates(text: String): List<AmountResult> {
        val lines = text.lines()
        val candidates = mutableListOf<AmountResult>()
        val seenAmounts = mutableSetOf<Double>()
        
        Log.d(TAG, "\n--- Finding TOTAL in ANY bill (${lines.size} lines) ---")
        
        for (i in lines.indices) {
            val line = lines[i]
            val lineLower = line.lowercase()
            
            // Look for ANY "total" keyword variation
            val hasTotalKeyword = lineLower.contains("total") || 
                                 lineLower.contains("amount payable") ||
                                 lineLower.contains("payable") ||
                                 lineLower.contains("balance") ||
                                 lineLower.contains("bill amount")
            
            if (!hasTotalKeyword) {
                continue // Skip lines without total-related keywords
            }
            
            Log.d(TAG, "Line $i has TOTAL keyword: $line")
            
            // Skip tax-only breakdowns (unless they say "Grand/Final/Net Total")
            val isTaxBreakdown = (lineLower.contains("gst") || lineLower.contains("cgst") || 
                                 lineLower.contains("sgst") || lineLower.contains("igst") ||
                                 lineLower.contains("vat") || lineLower.contains("tax")) &&
                                 !(lineLower.contains("grand") || lineLower.contains("final") || 
                                   lineLower.contains("net") || lineLower.contains("bill"))
            
            if (isTaxBreakdown) {
                Log.d(TAG, "  → SKIP (tax breakdown)")
                continue
            }
            
            // Try to extract amount from CURRENT line
            var amountFound = false
            extractAmount(line)?.let { amount ->
                if (amount >= 10.0 && !seenAmounts.contains(amount)) {
                    seenAmounts.add(amount)
                    
                    val confidence = determineConfidence(lineLower)
                    
                    candidates.add(AmountResult(
                        amount = amount,
                        confidence = confidence,
                        sourceLine = line.trim()
                    ))
                    
                    Log.d(TAG, "  → FOUND: ₹$amount ($confidence)")
                    amountFound = true
                }
            }
            
            // If no amount on current line, check next 3 lines
            // This handles: "Total: ₹" on line N, "3150" on line N+1
            if (!amountFound) {
                var foundInNextLines = false
                for (offset in 1..3) {
                    if (i + offset < lines.size && !foundInNextLines) {
                        val nextLine = lines[i + offset]
                        val nextLineLower = nextLine.lowercase()
                        
                        Log.d(TAG, "  → Checking next line ${i+offset}: $nextLine")
                        
                        // Skip if next line also has "total" keyword (it's a different total)
                        if (nextLineLower.contains("total") || 
                            nextLineLower.contains("cgst") || 
                            nextLineLower.contains("sgst")) {
                            Log.d(TAG, "    → Skip (has keyword)")
                            foundInNextLines = true
                            continue
                        }
                        
                        extractAmount(nextLine)?.let { amount ->
                            if (amount >= 10.0 && !seenAmounts.contains(amount)) {
                                seenAmounts.add(amount)
                                
                                val confidence = determineConfidence(lineLower)
                                
                                candidates.add(AmountResult(
                                    amount = amount,
                                    confidence = confidence,
                                    sourceLine = "$line → $nextLine".trim()
                                ))
                                
                                Log.d(TAG, "    → FOUND on next line: ₹$amount ($confidence)")
                                amountFound = true
                                foundInNextLines = true
                            }
                        }
                    }
                }
            }
            
            if (!amountFound) {
                Log.d(TAG, "  → No amount found")
            }
        }
        
        // Also look for standalone large amounts near bottom if no totals found
        if (candidates.isEmpty()) {
            Log.d(TAG, "\n--- No TOTAL keyword found, searching bottom for amounts ---")
            val bottomStart = (lines.size * 0.6).toInt() // Bottom 40%
            
            for (i in bottomStart until lines.size) {
                val line = lines[i]
                val lineLower = line.lowercase()
                
                // Skip tax lines
                if (lineLower.contains("cgst") || lineLower.contains("sgst") || 
                    lineLower.contains("igst") || lineLower.contains("gst") ||
                    lineLower.contains("tax") && !lineLower.contains("total")) {
                    continue
                }
                
                extractAmount(line)?.let { amount ->
                    if (amount >= 100.0 && !seenAmounts.contains(amount)) {
                        seenAmounts.add(amount)
                        
                        candidates.add(AmountResult(
                            amount = amount,
                            confidence = "Low",
                            sourceLine = line.trim()
                        ))
                        
                        Log.d(TAG, "Line $i: ₹$amount (Low) - $line")
                    }
                }
            }
        }
        
        Log.d(TAG, "\n--- Sorting ${candidates.size} candidates ---")
        
        // Sort by confidence, then by amount
        val sorted = candidates.sortedWith(
            compareBy<AmountResult> { 
                when (it.confidence) {
                    "High" -> 0
                    "Medium" -> 1
                    "Low" -> 2
                    else -> 3
                }
            }.thenByDescending { it.amount }
        )
        
        sorted.take(5).forEachIndexed { index, candidate ->
            Log.d(TAG, "#${index + 1}: ₹${candidate.amount} (${candidate.confidence}) - ${candidate.sourceLine.take(50)}")
        }
        
        return sorted.take(5)
    }
    
    /**
     * Determine confidence level based on keywords
     */
    private fun determineConfidence(lineLower: String): String {
        return when {
            // Highest priority - Final totals with all charges
            lineLower.contains("grand total") || 
            lineLower.contains("final total") || 
            lineLower.contains("net total") -> "High"
            
            lineLower.contains("total amount") || 
            lineLower.contains("amount payable") ||
            lineLower.contains("bill amount") ||
            lineLower.contains("total payable") -> "High"
            
            // Total with tax included
            lineLower.contains("total") && (lineLower.contains("incl") || 
                                           lineLower.contains("with") ||
                                           lineLower.contains("₹")) -> "High"
            
            // Generic total or payment
            lineLower.contains("total:") || 
            lineLower.contains("total ₹") ||
            lineLower.contains("payable") -> "Medium"
            
            // Sub-totals are lower priority
            lineLower.contains("sub") -> "Low"
            
            // Any other total
            lineLower.contains("total") -> "Medium"
            
            else -> "Low"
        }
    }
    
    /**
     * Amount result with confidence for user selection
     */
    data class AmountResult(
        val amount: Double,
        val confidence: String,  // "High", "Medium", "Low", "Very Low"
        val sourceLine: String   // The line where amount was found
    )
    
    /**
     * Extract numeric amount from a text line with advanced pattern matching
     * Handles formats:
     * - 1234.56 or 1,234.56
     * - ₹1234.56 or Rs. 1234.56
     * - 462.00 (proper decimal format)
     * 
     * Filters out:
     * - Flight numbers (6E-6924)
     * - Dates (01-Feb-2026)
     * - Ticket/Order numbers
     * - Percentages
     */
    private fun extractAmount(text: String): Double? {
        val originalText = text.uppercase()
        
        // Skip lines that clearly contain non-monetary patterns
        if (shouldSkipLine(originalText)) {
            Log.v(TAG, "  Skipped (non-monetary): $text")
            return null
        }
        
        // Look for monetary amounts with proper formatting
        val amounts = mutableListOf<MoneyAmount>()
        
        // Pattern 1: Currency symbol followed by amount (₹462.00, Rs.462, $50.00)
        val currencyPattern = Regex("""[₹$][\s]?(\d{1,3}(?:,\d{3})*(?:\.\d{2})?)""")
        currencyPattern.findAll(text).forEach { match ->
            match.groupValues.getOrNull(1)?.let { numStr ->
                parseAmount(numStr)?.let { amt ->
                    amounts.add(MoneyAmount(amt, 10)) // High confidence
                }
            }
        }
        
        // Pattern 2: Rs or INR prefix (Rs. 462.00, INR 500)
        val prefixPattern = Regex("""(?:Rs\.?|INR|USD)[\s]?(\d{1,3}(?:,\d{3})*(?:\.\d{2})?)""", RegexOption.IGNORE_CASE)
        prefixPattern.findAll(text).forEach { match ->
            match.groupValues.getOrNull(1)?.let { numStr ->
                parseAmount(numStr)?.let { amt ->
                    amounts.add(MoneyAmount(amt, 9))
                }
            }
        }
        
        // Pattern 3: Standalone decimal amounts (462.00, 1,234.56)
        // Must have exactly 2 decimal places for amounts > 10
        val decimalPattern = Regex("""(?<![A-Z0-9])(\d{1,3}(?:,\d{3})*\.\d{2})(?![A-Z0-9])""")
        decimalPattern.findAll(text).forEach { match ->
            match.groupValues.getOrNull(1)?.let { numStr ->
                parseAmount(numStr)?.let { amt ->
                    // Higher confidence for amounts with 2 decimal places
                    amounts.add(MoneyAmount(amt, 8))
                }
            }
        }
        
        // Pattern 4: Whole numbers that might be amounts (only if > 100 and near keywords)
        if (containsMoneyKeyword(originalText)) {
            val wholePattern = Regex("""(?<![A-Z0-9-])(\d{2,6})(?![A-Z0-9-])""")
            wholePattern.findAll(text).forEach { match ->
                match.groupValues.getOrNull(1)?.let { numStr ->
                    parseAmount(numStr)?.let { amt ->
                        if (amt >= 100) {
                            amounts.add(MoneyAmount(amt, 5)) // Lower confidence
                        }
                    }
                }
            }
        }
        
        // Return highest confidence amount
        return amounts
            .sortedByDescending { it.confidence }
            .firstOrNull()?.amount
    }
    
    /**
     * Check if line should be skipped (contains non-monetary patterns)
     */
    private fun shouldSkipLine(line: String): Boolean {
        // Skip lines with flight/train patterns
        if (Regex("""[A-Z]{1,3}[\s-]*\d{3,4}""").containsMatchIn(line)) return true
        
        // Skip lines with date patterns
        if (Regex("""\d{1,2}[-/]\d{1,2}[-/]\d{2,4}""").containsMatchIn(line)) return true
        if (Regex("""\d{1,2}[-\s][A-Z]{3}[-\s]\d{4}""").containsMatchIn(line)) return true
        
        // Skip lines with ticket/order/PNR patterns
        if (Regex("""(?:TICKET|ORDER|PNR|NUMBER|NO)[:\s#]*[A-Z0-9]{6,}""").containsMatchIn(line)) return true
        
        // Skip lines with GSTIN, PAN patterns
        if (Regex("""(?:GSTIN|PAN|CIN)[:\s]*[A-Z0-9]+""").containsMatchIn(line)) return true
        
        // Skip percentage lines
        if (line.contains("%") && !line.contains("TOTAL")) return true
        
        // Skip SAC/HSN code lines
        if (Regex("""(?:SAC|HSN)[:\s]*\d+""").containsMatchIn(line)) return true
        
        return false
    }
    
    /**
     * Check if line contains money-related keywords
     */
    private fun containsMoneyKeyword(line: String): Boolean {
        val keywords = listOf("TOTAL", "AMOUNT", "PAYABLE", "BALANCE", "PAID", "DUE", "BILL")
        return keywords.any { line.contains(it) }
    }
    
    /**
     * Parse amount string to Double
     */
    private fun parseAmount(numStr: String): Double? {
        return try {
            numStr.replace(",", "").toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Amount with confidence score
     */
    private data class MoneyAmount(
        val amount: Double,
        val confidence: Int
    )
    
    /**
     * Extract full text from receipt (for description)
     */
    suspend fun extractTextFromReceipt(imageFile: File): String? {
        return try {
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            val image = InputImage.fromBitmap(bitmap, 0)
            val result = textRecognizer.process(image).await()
            
            result.text.takeIf { it.isNotBlank() }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Extract full text from receipt URI
     */
    suspend fun extractTextFromReceipt(imageUri: Uri): String? {
        return try {
            val image = InputImage.fromFilePath(context, imageUri)
            val result = textRecognizer.process(image).await()
            
            result.text.takeIf { it.isNotBlank() }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Extract merchant name from receipt (usually first few lines)
     */
    suspend fun extractMerchantName(imageFile: File): String? {
        return try {
            val fullText = extractTextFromReceipt(imageFile)
            fullText?.lines()
                ?.take(3) // Usually merchant name is in first 3 lines
                ?.firstOrNull { line -> 
                    line.trim().length > 3 && !line.matches(Regex(""".*\d{3,}.*"""))
                }
                ?.trim()
        } catch (e: Exception) {
            null
        }
    }
    
    fun cleanup() {
        textRecognizer.close()
    }
}
