package com.financemanager.app.util

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import android.graphics.Typeface
import com.financemanager.app.domain.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Utility class for generating PDF reports
 */
class PdfGenerator @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val currencyFormat = NumberFormat.getCurrencyInstance()
    
    private companion object {
        const val PAGE_WIDTH = 595 // A4 width in points
        const val PAGE_HEIGHT = 842 // A4 height in points
        const val MARGIN = 40
        const val LINE_HEIGHT = 20
        const val TITLE_SIZE = 24f
        const val HEADING_SIZE = 18f
        const val NORMAL_SIZE = 14f
        const val SMALL_SIZE = 12f
    }
    
    /**
     * Generate transaction summary PDF
     */
    fun generateTransactionSummaryPdf(
        summary: TransactionSummary,
        transactions: List<Transaction>,
        fileName: String
    ): File {
        val pdfDocument = PdfDocument()
        var pageNumber = 1
        
        // Page 1: Summary
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber++).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        var yPosition = drawHeader(canvas, "Transaction Summary", summary.dateRange)
        
        // Summary statistics
        yPosition = drawSummaryStats(canvas, summary, yPosition)
        
        // Category breakdown
        yPosition = drawCategoryBreakdown(canvas, summary.categoryBreakdown, yPosition)
        
        pdfDocument.finishPage(page)
        
        // Page 2+: Transaction details
        if (transactions.isNotEmpty()) {
            pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber++).create()
            page = pdfDocument.startPage(pageInfo)
            canvas = page.canvas
            yPosition = drawTransactionList(canvas, transactions, MARGIN + 60)
            pdfDocument.finishPage(page)
        }
        
        // Save to file
        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).use { outputStream ->
            pdfDocument.writeTo(outputStream)
        }
        pdfDocument.close()
        
        return file
    }
    
    /**
     * Generate budget comparison PDF
     */
    fun generateBudgetComparisonPdf(
        budgetComparisons: List<BudgetComparison>,
        dateRange: DateRange,
        fileName: String
    ): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        
        var yPosition = drawHeader(canvas, "Budget vs Actual Report", dateRange)
        yPosition = drawBudgetComparisons(canvas, budgetComparisons, yPosition)
        
        pdfDocument.finishPage(page)
        
        // Save to file
        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).use { outputStream ->
            pdfDocument.writeTo(outputStream)
        }
        pdfDocument.close()
        
        return file
    }
    
    private fun drawHeader(canvas: android.graphics.Canvas, title: String, dateRange: DateRange): Int {
        val paint = Paint().apply {
            textSize = TITLE_SIZE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        
        var yPosition = MARGIN + 40
        canvas.drawText(title, MARGIN.toFloat(), yPosition.toFloat(), paint)
        
        paint.textSize = NORMAL_SIZE
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        yPosition += LINE_HEIGHT + 10
        
        val dateRangeText = "${dateFormat.format(Date(dateRange.startDate))} - ${dateFormat.format(Date(dateRange.endDate))}"
        canvas.drawText(dateRangeText, MARGIN.toFloat(), yPosition.toFloat(), paint)
        
        // Draw line
        yPosition += LINE_HEIGHT
        paint.strokeWidth = 2f
        canvas.drawLine(
            MARGIN.toFloat(),
            yPosition.toFloat(),
            (PAGE_WIDTH - MARGIN).toFloat(),
            yPosition.toFloat(),
            paint
        )
        
        return yPosition + LINE_HEIGHT
    }
    
    private fun drawSummaryStats(canvas: android.graphics.Canvas, summary: TransactionSummary, startY: Int): Int {
        val paint = Paint().apply {
            textSize = HEADING_SIZE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        
        var yPosition = startY
        canvas.drawText("Summary", MARGIN.toFloat(), yPosition.toFloat(), paint)
        yPosition += LINE_HEIGHT + 10
        
        paint.textSize = NORMAL_SIZE
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        
        val summaryItems = listOf(
            "Total Income" to currencyFormat.format(summary.totalIncome),
            "Total Expenses" to currencyFormat.format(summary.totalExpenses),
            "Net Amount" to currencyFormat.format(summary.netAmount),
            "Transaction Count" to summary.transactionCount.toString(),
            "Average Transaction" to currencyFormat.format(summary.averageTransaction)
        )
        
        for ((label, value) in summaryItems) {
            canvas.drawText("$label:", MARGIN.toFloat(), yPosition.toFloat(), paint)
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(value, (MARGIN + 200).toFloat(), yPosition.toFloat(), paint)
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            yPosition += LINE_HEIGHT
        }
        
        return yPosition + LINE_HEIGHT
    }
    
    private fun drawCategoryBreakdown(
        canvas: android.graphics.Canvas,
        categoryBreakdown: Map<TransactionCategory, CategorySummary>,
        startY: Int
    ): Int {
        val paint = Paint().apply {
            textSize = HEADING_SIZE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        
        var yPosition = startY
        canvas.drawText("Category Breakdown", MARGIN.toFloat(), yPosition.toFloat(), paint)
        yPosition += LINE_HEIGHT + 10
        
        paint.textSize = SMALL_SIZE
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        
        // Table header
        canvas.drawText("Category", MARGIN.toFloat(), yPosition.toFloat(), paint)
        canvas.drawText("Amount", (MARGIN + 180).toFloat(), yPosition.toFloat(), paint)
        canvas.drawText("Count", (MARGIN + 320).toFloat(), yPosition.toFloat(), paint)
        canvas.drawText("%", (MARGIN + 420).toFloat(), yPosition.toFloat(), paint)
        yPosition += LINE_HEIGHT
        
        // Draw line
        paint.strokeWidth = 1f
        canvas.drawLine(
            MARGIN.toFloat(),
            yPosition.toFloat(),
            (PAGE_WIDTH - MARGIN).toFloat(),
            yPosition.toFloat(),
            paint
        )
        yPosition += 5
        
        // Table rows
        for ((_, categorySummary) in categoryBreakdown) {
            yPosition += LINE_HEIGHT
            canvas.drawText(categorySummary.category.displayName, MARGIN.toFloat(), yPosition.toFloat(), paint)
            canvas.drawText(
                currencyFormat.format(categorySummary.totalAmount),
                (MARGIN + 180).toFloat(),
                yPosition.toFloat(),
                paint
            )
            canvas.drawText(
                categorySummary.transactionCount.toString(),
                (MARGIN + 320).toFloat(),
                yPosition.toFloat(),
                paint
            )
            canvas.drawText(
                String.format("%.1f%%", categorySummary.percentage),
                (MARGIN + 420).toFloat(),
                yPosition.toFloat(),
                paint
            )
        }
        
        return yPosition + LINE_HEIGHT
    }
    
    private fun drawTransactionList(
        canvas: android.graphics.Canvas,
        transactions: List<Transaction>,
        startY: Int
    ): Int {
        val paint = Paint().apply {
            textSize = HEADING_SIZE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        
        var yPosition = startY
        canvas.drawText("Transaction Details", MARGIN.toFloat(), yPosition.toFloat(), paint)
        yPosition += LINE_HEIGHT + 10
        
        paint.textSize = SMALL_SIZE
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        
        // Table header
        canvas.drawText("Date", MARGIN.toFloat(), yPosition.toFloat(), paint)
        canvas.drawText("Description", (MARGIN + 100).toFloat(), yPosition.toFloat(), paint)
        canvas.drawText("Category", (MARGIN + 260).toFloat(), yPosition.toFloat(), paint)
        canvas.drawText("Amount", (MARGIN + 400).toFloat(), yPosition.toFloat(), paint)
        yPosition += LINE_HEIGHT
        
        // Draw line
        paint.strokeWidth = 1f
        canvas.drawLine(
            MARGIN.toFloat(),
            yPosition.toFloat(),
            (PAGE_WIDTH - MARGIN).toFloat(),
            yPosition.toFloat(),
            paint
        )
        yPosition += 5
        
        // Transaction rows (limit to first 20)
        for (transaction in transactions.take(20)) {
            yPosition += LINE_HEIGHT
            
            val dateText = SimpleDateFormat("MM/dd", Locale.getDefault()).format(Date(transaction.timestamp))
            canvas.drawText(dateText, MARGIN.toFloat(), yPosition.toFloat(), paint)
            
            val descriptionText = if (transaction.description.length > 15) {
                transaction.description.take(12) + "..."
            } else {
                transaction.description
            }
            canvas.drawText(descriptionText, (MARGIN + 100).toFloat(), yPosition.toFloat(), paint)
            
            canvas.drawText(transaction.category.displayName, (MARGIN + 260).toFloat(), yPosition.toFloat(), paint)
            
            val amountText = if (transaction.transactionType == TransactionType.DEBIT) {
                "-${currencyFormat.format(transaction.amount)}"
            } else {
                "+${currencyFormat.format(transaction.amount)}"
            }
            canvas.drawText(amountText, (MARGIN + 400).toFloat(), yPosition.toFloat(), paint)
        }
        
        if (transactions.size > 20) {
            yPosition += LINE_HEIGHT + 10
            paint.textSize = SMALL_SIZE
            canvas.drawText(
                "... and ${transactions.size - 20} more transactions",
                MARGIN.toFloat(),
                yPosition.toFloat(),
                paint
            )
        }
        
        return yPosition
    }
    
    private fun drawBudgetComparisons(
        canvas: android.graphics.Canvas,
        budgetComparisons: List<BudgetComparison>,
        startY: Int
    ): Int {
        val paint = Paint().apply {
            textSize = SMALL_SIZE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
        }
        
        var yPosition = startY
        
        // Table header
        canvas.drawText("Budget Name", MARGIN.toFloat(), yPosition.toFloat(), paint)
        canvas.drawText("Budgeted", (MARGIN + 150).toFloat(), yPosition.toFloat(), paint)
        canvas.drawText("Actual", (MARGIN + 270).toFloat(), yPosition.toFloat(), paint)
        canvas.drawText("Difference", (MARGIN + 370).toFloat(), yPosition.toFloat(), paint)
        yPosition += LINE_HEIGHT
        
        // Draw line
        paint.strokeWidth = 1f
        canvas.drawLine(
            MARGIN.toFloat(),
            yPosition.toFloat(),
            (PAGE_WIDTH - MARGIN).toFloat(),
            yPosition.toFloat(),
            paint
        )
        yPosition += 5
        
        // Budget rows
        for (comparison in budgetComparisons) {
            yPosition += LINE_HEIGHT
            
            canvas.drawText(comparison.budget.budgetName, MARGIN.toFloat(), yPosition.toFloat(), paint)
            canvas.drawText(
                currencyFormat.format(comparison.budget.totalBudget),
                (MARGIN + 150).toFloat(),
                yPosition.toFloat(),
                paint
            )
            canvas.drawText(
                currencyFormat.format(comparison.actualSpent),
                (MARGIN + 270).toFloat(),
                yPosition.toFloat(),
                paint
            )
            
            val differenceText = if (comparison.difference >= 0) {
                "+${currencyFormat.format(comparison.difference)}"
            } else {
                currencyFormat.format(comparison.difference)
            }
            canvas.drawText(differenceText, (MARGIN + 370).toFloat(), yPosition.toFloat(), paint)
        }
        
        return yPosition
    }
}
