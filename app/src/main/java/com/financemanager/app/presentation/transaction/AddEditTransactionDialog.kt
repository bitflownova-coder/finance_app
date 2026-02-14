package com.financemanager.app.presentation.transaction

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.financemanager.app.R
import com.financemanager.app.databinding.DialogAddEditTransactionBinding
import com.financemanager.app.domain.model.*
import com.financemanager.app.presentation.budget.BudgetViewModel
import com.financemanager.app.presentation.profile.ProfileViewModel
import com.financemanager.app.presentation.splitbill.SplitBillViewModel
import com.financemanager.app.util.ImageStorageHelper
import com.financemanager.app.util.ReceiptOcrHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * Dialog for adding or editing a transaction
 */
@AndroidEntryPoint
class AddEditTransactionDialog : DialogFragment() {
    
    private var _binding: DialogAddEditTransactionBinding? = null
    private val binding get() = _binding!!
    
    private val profileViewModel: ProfileViewModel by viewModels()
    private val budgetViewModel: BudgetViewModel by viewModels()
    private val splitBillViewModel: SplitBillViewModel by viewModels()
    
    @Inject
    lateinit var imageStorageHelper: ImageStorageHelper
    
    @Inject
    lateinit var receiptOcrHelper: ReceiptOcrHelper
    
    private var transaction: Transaction? = null
    private var accounts: List<BankAccount> = emptyList()
    private var budgets: List<Budget> = emptyList()
    var onTransactionSaved: ((Transaction) -> Unit)? = null
    
    // Split bill data
    private val participants = mutableListOf<ParticipantData>()
    private var splitType = SplitType.EQUAL
    
    // Receipt data
    private var receiptImagePath: String? = null
    private var tempCameraFile: File? = null
    private var pendingCameraUri: Uri? = null
    
    // Permission launcher for camera
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Camera Permission Required")
                .setMessage("Camera permission is needed to take photos of receipts. Please grant the permission in app settings.")
                .setPositiveButton("OK", null)
                .show()
        }
    }
    
    // Image pickers
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { handleGalleryImage(it) }
    }
    
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraFile != null) {
            handleCameraImage(tempCameraFile!!)
        } else {
            tempCameraFile?.delete()
            tempCameraFile = null
        }
    }
    
    data class ParticipantData(
        var name: String = "",
        var phoneNumber: String = "",
        var shareAmount: Double = 0.0
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transaction = arguments?.getParcelable(ARG_TRANSACTION)
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddEditTransactionBinding.inflate(layoutInflater)
        
        setupTransactionTypes()
        setupCategories()
        setupSplitBill()
        setupClickListeners()
        
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (transaction == null) "Add Transaction" else "Edit Transaction")
            .setView(binding.root)
            .create()
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Load current state immediately
        accounts = profileViewModel.uiState.value.accounts
        budgets = budgetViewModel.uiState.value.budgets
        setupAccounts()
        setupBudgets()
        populateFields()
        
        // Continue observing for updates
        observeAccounts()
        observeBudgets()
        setupCategoryListener()
    }
    
    private fun observeAccounts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.uiState.collect { state ->
                    accounts = state.accounts
                    setupAccounts()
                }
            }
        }
    }
    
    private fun observeBudgets() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                budgetViewModel.uiState.collect { state ->
                    budgets = state.budgets
                    setupBudgets()
                }
            }
        }
    }
    
    private fun setupTransactionTypes() {
        val types = TransactionType.values().map { it.name }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            types
        )
        binding.actTransactionType.setAdapter(adapter)
        binding.actTransactionType.setText(TransactionType.DEBIT.name, false)
    }
    
    private fun setupCategories() {
        val categories = TransactionCategory.values().map { "${it.icon} ${it.displayName}" }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categories
        )
        binding.actCategory.setAdapter(adapter)
        binding.actCategory.setText(categories[0], false)
    }
    
    private fun setupCategoryListener() {
        binding.actCategory.setOnItemClickListener { _, _, position, _ ->
            val category = TransactionCategory.values()[position]
            filterBudgetsByCategory(category)
        }
    }
    
    private fun setupSplitBill() {
        // Setup split type dropdown
        val splitTypes = SplitType.values().map { it.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() } }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            splitTypes
        )
        binding.actSplitType.setAdapter(adapter)
        binding.actSplitType.setText(splitTypes[0], false)
        
        // Split bill toggle
        binding.switchSplitBill.setOnCheckedChangeListener { _, isChecked ->
            binding.layoutSplitDetails.isVisible = isChecked
            if (isChecked && participants.isEmpty()) {
                // Add current user as first participant
                addParticipant("You (paid)", "", 0.0)
                recalculateSplits()
            }
        }
        
        // Add participant button
        binding.btnAddParticipant.setOnClickListener {
            showAddParticipantDialog()
        }
        
        // Split type selection
        binding.actSplitType.setOnItemClickListener { _, _, position, _ ->
            splitType = SplitType.values()[position]
            recalculateSplits()
        }
        
        // Amount changes should recalculate splits
        binding.etAmount.setOnFocusChangeListener { _, _ ->
            if (binding.switchSplitBill.isChecked) {
                recalculateSplits()
            }
        }
    }
    
    private fun showAddParticipantDialog() {
        val nameInput = EditText(requireContext()).apply {
            hint = "Name"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        }
        val phoneInput = EditText(requireContext()).apply {
            hint = "Phone (optional)"
            inputType = InputType.TYPE_CLASS_PHONE
        }
        val amountInput = EditText(requireContext()).apply {
            hint = if (splitType == SplitType.PERCENTAGE) "Percentage %" else "Amount ₹"
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            isEnabled = splitType != SplitType.EQUAL
        }
        
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 20, 50, 20)
            addView(nameInput)
            addView(phoneInput)
            if (splitType != SplitType.EQUAL) {
                addView(amountInput)
            }
        }
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Participant")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString()
                val phone = phoneInput.text.toString()
                val amount = if (splitType != SplitType.EQUAL) {
                    amountInput.text.toString().toDoubleOrNull() ?: 0.0
                } else {
                    0.0
                }
                
                if (name.isNotBlank()) {
                    addParticipant(name, phone, amount)
                    recalculateSplits()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun addParticipant(name: String, phone: String, amount: Double) {
        participants.add(ParticipantData(name, phone, amount))
        updateParticipantsList()
    }
    
    private fun updateParticipantsList() {
        binding.layoutParticipants.removeAllViews()
        
        participants.forEachIndexed { index, participant ->
            val itemView = layoutInflater.inflate(android.R.layout.simple_list_item_2, binding.layoutParticipants, false)
            val text1 = itemView.findViewById<android.widget.TextView>(android.R.id.text1)
            val text2 = itemView.findViewById<android.widget.TextView>(android.R.id.text2)
            
            text1.text = participant.name
            text2.text = "₹${String.format("%.2f", participant.shareAmount)}${if (participant.phoneNumber.isNotBlank()) " • ${participant.phoneNumber}" else ""}"
            
            itemView.setOnLongClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Remove Participant?")
                    .setMessage("Remove ${participant.name} from this split?")
                    .setPositiveButton("Remove") { _, _ ->
                        participants.removeAt(index)
                        updateParticipantsList()
                        recalculateSplits()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                true
            }
            
            binding.layoutParticipants.addView(itemView)
        }
    }
    
    private fun recalculateSplits() {
        if (participants.isEmpty()) return
        
        val totalAmount = binding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
        if (totalAmount <= 0) return
        
        when (splitType) {
            SplitType.EQUAL -> {
                val share = totalAmount / participants.size
                participants.forEach { it.shareAmount = share }
            }
            SplitType.CUSTOM -> {
                // Keep custom amounts as entered
                // No recalculation needed
            }
            SplitType.PERCENTAGE -> {
                val totalPercentage = participants.sumOf { it.shareAmount }
                if (totalPercentage > 0) {
                    participants.forEach { 
                        it.shareAmount = (it.shareAmount / 100) * totalAmount 
                    }
                }
            }
        }
        
        updateParticipantsList()
    }
    
    private fun setupBudgets() {
        // Initial setup with all budgets
        updateBudgetDropdown(budgets)
    }
    
    private fun filterBudgetsByCategory(category: TransactionCategory) {
        val filteredBudgets = budgets.filter { it.category?.name == category.name }
        updateBudgetDropdown(filteredBudgets)
    }
    
    private fun updateBudgetDropdown(budgetsToShow: List<Budget>) {
        if (budgetsToShow.isEmpty()) {
            binding.tilBudget.hint = "Budget (No budget for this category)"
            binding.actBudget.setText("", false)
            binding.actBudget.isEnabled = false
            return
        }
        
        binding.tilBudget.hint = "Budget (Optional)"
        binding.actBudget.isEnabled = true
        
        val budgetNames = budgetsToShow.map { budget ->
            val categoryIcon = budget.category?.icon ?: "💰"
            val categoryName = budget.category?.displayName ?: "General"
            "$categoryIcon $categoryName - ₹${budget.totalBudget} (₹${budget.spentAmount} spent)"
        }
        
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            budgetNames
        )
        binding.actBudget.setAdapter(adapter)
        
        if (budgetNames.isNotEmpty()) {
            binding.actBudget.setText(budgetNames[0], false)
        }
    }
    
    private fun setupAccounts() {
        if (accounts.isEmpty()) {
            binding.tilAccount.error = "Please add an account first"
            binding.btnSave.isEnabled = false
            return
        }
        
        // Clear error and enable save button when accounts are available
        binding.tilAccount.error = null
        binding.btnSave.isEnabled = true
        
        val accountNames = accounts.map { "${it.bankName} - ${it.accountType.name}" }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            accountNames
        )
        binding.actAccount.setAdapter(adapter)
        
        if (accountNames.isNotEmpty()) {
            binding.actAccount.setText(accountNames[0], false)
        }
    }
    
    private fun populateFields() {
        transaction?.let {
            binding.etAmount.setText(it.amount.toString())
            binding.actTransactionType.setText(it.transactionType.name, false)
            binding.actCategory.setText("${it.category.icon} ${it.category.displayName}", false)
            binding.etDescription.setText(it.description)
            
            // Set account
            val accountIndex = accounts.indexOfFirst { acc -> acc.accountId == it.accountId }
            if (accountIndex >= 0) {
                val accountName = "${accounts[accountIndex].bankName} - ${accounts[accountIndex].accountType.name}"
                binding.actAccount.setText(accountName, false)
            }
            
            // Load receipt if exists
            it.receiptPath?.let { path ->
                receiptImagePath = path
                if (File(path).exists()) {
                    displayReceipt(path)
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        
        binding.btnSave.setOnClickListener {
            if (validateFields()) {
                saveTransaction()
            }
        }
        
        // Receipt attachment buttons
        binding.btnAttachCamera.setOnClickListener {
            launchCamera()
        }
        
        binding.btnAttachGallery.setOnClickListener {
            launchGallery()
        }
        
        binding.btnRemoveReceipt.setOnClickListener {
            removeReceipt()
        }
        
        binding.cardReceiptPreview.setOnClickListener {
            receiptImagePath?.let { path ->
                viewFullReceipt(path)
            }
        }
    }
    
    private fun validateFields(): Boolean {
        var isValid = true
        
        // Validate amount (optional if receipt is attached)
        if (binding.etAmount.text.isNullOrBlank()) {
            if (receiptImagePath == null) {
                binding.tilAmount.error = "Amount is required"
                isValid = false
            } else {
                binding.tilAmount.error = null
            }
        } else {
            try {
                val amount = binding.etAmount.text.toString().toDouble()
                if (amount <= 0) {
                    binding.tilAmount.error = "Amount must be greater than 0"
                    isValid = false
                } else {
                    binding.tilAmount.error = null
                }
            } catch (e: Exception) {
                binding.tilAmount.error = "Invalid amount"
                isValid = false
            }
        }
        
        // Validate description (optional for split bills and receipts)
        if (binding.etDescription.text.isNullOrBlank() && !binding.switchSplitBill.isChecked && receiptImagePath == null) {
            binding.tilDescription.error = "Description is required"
            isValid = false
        } else {
            binding.tilDescription.error = null
        }
        
        // Validate account selection
        if (accounts.isEmpty()) {
            binding.tilAccount.error = "No accounts available"
            isValid = false
        }
        
        // Validate split bill if enabled
        if (!validateSplitBill()) {
            isValid = false
        }
        
        return isValid
    }
    
    private fun saveTransaction() {
        // Auto-fill amount if empty and receipt is attached
        val amount = binding.etAmount.text.toString().toDoubleOrNull() ?: run {
            if (receiptImagePath != null) 0.01 else 0.0
        }
        val typeText = binding.actTransactionType.text.toString()
        val transactionType = TransactionType.fromString(typeText)
        
        // Extract category from text with icon
        val categoryText = binding.actCategory.text.toString()
        val categoryDisplayName = categoryText.substringAfter(" ").trim()
        val category = TransactionCategory.values().find { 
            it.displayName == categoryDisplayName 
        } ?: TransactionCategory.OTHER
        
        // Use provided description or auto-fill for split bills/receipts
        val description = binding.etDescription.text.toString().ifBlank {
            when {
                binding.switchSplitBill.isChecked -> "Split bill transaction"
                receiptImagePath != null -> "Receipt transaction"
                else -> ""
            }
        }
        
        // Get selected account
        val accountText = binding.actAccount.text.toString()
        val selectedAccountIndex = accounts.indexOfFirst { 
            "${it.bankName} - ${it.accountType.name}" == accountText 
        }
        val accountId = if (selectedAccountIndex >= 0) {
            accounts[selectedAccountIndex].accountId
        } else {
            accounts.firstOrNull()?.accountId ?: 0
        }
        
        val savedTransaction = Transaction(
            transactionId = transaction?.transactionId ?: 0,
            userId = transaction?.userId ?: 0, // Will be set in ViewModel
            accountId = accountId,
            amount = amount,
            transactionType = transactionType,
            category = category,
            description = description,
            receiptPath = receiptImagePath,
            timestamp = transaction?.timestamp ?: System.currentTimeMillis(),
            createdAt = transaction?.createdAt ?: System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        onTransactionSaved?.invoke(savedTransaction)
        
        // Create split bill if enabled
        if (binding.switchSplitBill.isChecked && participants.isNotEmpty()) {
            createSplitBill(savedTransaction, amount, description)
        }
        
        dismiss()
    }
    
    private fun createSplitBill(transaction: Transaction, amount: Double, description: String) {
        lifecycleScope.launch {
            try {
                val splitBill = SplitBill(
                    transactionId = transaction.transactionId,
                    userId = transaction.userId,
                    totalAmount = amount,
                    description = description,
                    splitType = splitType,
                    participants = participants.map { participant ->
                        Participant(
                            splitId = 0,
                            name = participant.name,
                            phoneNumber = participant.phoneNumber.takeIf { it.isNotBlank() },
                            shareAmount = participant.shareAmount,
                            isPaid = participant.name.contains("You", ignoreCase = true)
                        )
                    }
                )
                
                splitBillViewModel.onEvent(
                    com.financemanager.app.presentation.splitbill.SplitBillEvent.AddSplitBill(
                        transactionId = transaction.transactionId,
                        userId = transaction.userId,
                        totalAmount = amount,
                        description = description,
                        splitType = splitType,
                        participants = splitBill.participants
                    )
                )
            } catch (e: Exception) {
                // Log error or show toast
            }
        }
    }
    
    private fun validateSplitBill(): Boolean {
        if (!binding.switchSplitBill.isChecked) return true
        
        if (participants.isEmpty()) {
            Snackbar.make(binding.root, "Add at least one participant", Snackbar.LENGTH_SHORT).show()
            return false
        }
        
        // Validate based on split type
        val totalAmount = binding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
        when (splitType) {
            SplitType.CUSTOM -> {
                val totalShares = participants.sumOf { it.shareAmount }
                if (Math.abs(totalShares - totalAmount) > 0.01) {
                    Snackbar.make(binding.root, "Split amounts must equal total amount", Snackbar.LENGTH_SHORT).show()
                    return false
                }
            }
            SplitType.PERCENTAGE -> {
                val totalPercentage = participants.sumOf { it.shareAmount }
                if (Math.abs(totalPercentage - 100.0) > 0.01) {
                    Snackbar.make(binding.root, "Percentages must add up to 100%", Snackbar.LENGTH_SHORT).show()
                    return false
                }
            }
            SplitType.EQUAL -> {
                // No validation needed, amounts are calculated automatically
            }
        }
        
        return true
    }
    
    private fun launchCamera() {
        // Check if we need to request permission (Android 6.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted, open camera
                    openCamera()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                    // Show explanation before requesting
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Camera Permission Needed")
                        .setMessage("This app needs camera access to take photos of receipts.")
                        .setPositiveButton("Grant") { _, _ ->
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                else -> {
                    // Request permission directly
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        } else {
            // No runtime permission needed for Android < 6.0
            openCamera()
        }
    }
    
    private fun openCamera() {
        try {
            val (file, uri) = imageStorageHelper.createImageFile()
            tempCameraFile = file
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        } catch (e: Exception) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Camera Error")
                .setMessage("Unable to open camera: ${e.message}")
                .setPositiveButton("OK", null)
                .show()
        }
    }
    
    private fun launchGallery() {
        try {
            galleryLauncher.launch("image/*")
        } catch (e: Exception) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Gallery Error")
                .setMessage("Unable to open gallery: ${e.message}")
                .setPositiveButton("OK", null)
                .show()
        }
    }
    
    private fun handleCameraImage(imageFile: File) {
        try {
            if (imageFile.exists()) {
                receiptImagePath = imageFile.absolutePath
                displayReceipt(imageFile.absolutePath)
                processReceiptWithOcr(imageFile)
            }
        } catch (e: Exception) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Save Error")
                .setMessage("Unable to save receipt: ${e.message}")
                .setPositiveButton("OK", null)
                .show()
        }
    }
    
    private fun handleGalleryImage(uri: Uri) {
        try {
            val savedPath = imageStorageHelper.saveImageFromUri(uri)
            if (savedPath != null) {
                receiptImagePath = savedPath
                displayReceipt(savedPath)
                processReceiptWithOcr(File(savedPath))
            } else {
                throw Exception("Failed to save image")
            }
        } catch (e: Exception) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Save Error")
                .setMessage("Unable to save receipt: ${e.message}")
                .setPositiveButton("OK", null)
                .show()
        }
    }
    
    private fun displayReceipt(imagePath: String) {
        binding.apply {
            // Show preview card, hide attach buttons
            cardReceiptPreview.isVisible = true
            layoutAttachButtons.isVisible = false
            
            // Load and display thumbnail (safe size-aware loading)
            try {
                // Load bitmap at appropriate size (prevents canvas error)
                // 52dp ImageView needs ~256px bitmap max (accounting for density)
                val bitmap = imageStorageHelper.loadBitmapAtDisplaySize(imagePath, 256, 256)
                if (bitmap != null) {
                    ivReceiptThumbnail.setImageBitmap(bitmap)
                    tvReceiptName.text = "Receipt attached"
                } else {
                    tvReceiptName.text = "Receipt (unable to preview)"
                }
            } catch (e: Exception) {
                tvReceiptName.text = "Receipt (unable to preview)"
            }
        }
    }
    
    private fun processReceiptWithOcr(imageFile: File) {
        lifecycleScope.launch {
            try {
                // Check if binding is still valid
                if (_binding == null) return@launch
                
                // Show processing hint
                binding.tilAmount.helperText = "🔍 Scanning receipt (Multi-layer detection)..."
                
                // Extract amount using multi-layer OCR
                val detectedAmount = receiptOcrHelper.extractAmountFromReceipt(imageFile)
                
                if (detectedAmount != null && detectedAmount >= 10.0) {
                    // Check binding again before UI updates
                    if (_binding == null) return@launch
                    
                    // Auto-fill amount field
                    binding.etAmount.setText(String.format("%.2f", detectedAmount))
                    binding.tilAmount.helperText = "✓ Auto-detected: ₹${String.format("%.2f", detectedAmount)}"
                    
                    // Optionally extract merchant name for description
                    val merchantName = receiptOcrHelper.extractMerchantName(imageFile)
                    if (merchantName != null && binding.etDescription.text.isNullOrBlank()) {
                        binding.etDescription.setText(merchantName)
                    }
                    
                    // Get alternative candidates for user confirmation
                    val candidates = receiptOcrHelper.extractAmountWithCandidates(imageFile)
                    
                    // Show success notification with options
                    if (candidates.size > 1) {
                        // Multiple candidates - show selection dialog
                        showAmountConfirmationDialog(detectedAmount, candidates, imageFile)
                    } else {
                        // Single candidate - show edit option
                        Snackbar.make(
                            binding.root,
                            "✓ Amount ₹${String.format("%.2f", detectedAmount)} detected",
                            Snackbar.LENGTH_LONG
                        ).setAction("Edit") {
                            binding.etAmount.requestFocus()
                            binding.etAmount.selectAll()
                        }.show()
                    }
                } else {
                    // Check binding before showing candidates
                    if (_binding == null) return@launch
                    
                    // No amount detected - try to show candidates
                    val candidates = receiptOcrHelper.extractAmountWithCandidates(imageFile)
                    if (candidates.isNotEmpty()) {
                        binding.tilAmount.helperText = "⚠️ Auto-detection uncertain. Please select:"
                        showAmountSelectionDialog(candidates)
                    } else {
                        binding.tilAmount.helperText = "⚠️ No amount detected. Please enter manually."
                        Snackbar.make(
                            binding.root,
                            "Unable to detect amount from receipt",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("AddEditTransaction", "OCR failed", e)
                // Check binding before showing error
                if (_binding == null) return@launch
                
                binding.tilAmount.helperText = "❌ OCR failed. Please enter amount manually."
                Snackbar.make(
                    binding.root,
                    "Error scanning receipt: ${e.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    /**
     * Show confirmation dialog with detected amount and alternatives
     */
    private fun showAmountConfirmationDialog(
        detectedAmount: Double,
        candidates: List<com.financemanager.app.util.ReceiptOcrHelper.AmountResult>,
        imageFile: File
    ) {
        val items = mutableListOf<String>()
        val amounts = mutableListOf<Double>()
        
        // Add detected amount first
        items.add("✓ ₹${String.format("%.2f", detectedAmount)} (Recommended)")
        amounts.add(detectedAmount)
        
        // Add other candidates
        candidates.filter { it.amount != detectedAmount }.take(4).forEach { candidate ->
            items.add("₹${String.format("%.2f", candidate.amount)} (${candidate.confidence} confidence)")
            amounts.add(candidate.amount)
        }
        
        // Add manual entry option
        items.add("✏️ Enter manually")
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm Amount")
            .setMessage("Select the correct total amount from receipt:")
            .setItems(items.toTypedArray()) { dialog, which ->
                if (which < amounts.size) {
                    // User selected an amount
                    binding.etAmount.setText(String.format("%.2f", amounts[which]))
                    binding.tilAmount.helperText = "✓ Amount confirmed by user"
                } else {
                    // User chose manual entry
                    binding.etAmount.requestFocus()
                    binding.tilAmount.helperText = "Please enter amount manually"
                }
                dialog.dismiss()
            }
            .setNegativeButton("Keep ₹${String.format("%.2f", detectedAmount)}") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    /**
     * Show selection dialog when no amount was auto-detected
     */
    private fun showAmountSelectionDialog(
        candidates: List<com.financemanager.app.util.ReceiptOcrHelper.AmountResult>
    ) {
        val items = candidates.take(5).map { candidate ->
            "₹${String.format("%.2f", candidate.amount)} (${candidate.confidence} confidence) - ${candidate.sourceLine.take(40)}"
        }.toTypedArray()
        
        val amounts = candidates.take(5).map { it.amount }
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Select Amount")
            .setMessage("Auto-detection uncertain. Please select amount:")
            .setItems(items) { dialog, which ->
                binding.etAmount.setText(String.format("%.2f", amounts[which]))
                binding.tilAmount.helperText = "✓ Amount selected by user"
                dialog.dismiss()
            }
            .setNegativeButton("Enter Manually") { dialog, _ ->
                binding.etAmount.requestFocus()
                dialog.dismiss()
            }
            .show()
    }
    
    private fun removeReceipt() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Remove Receipt?")
            .setMessage("Are you sure you want to remove this receipt?")
            .setPositiveButton("Remove") { _, _ ->
                receiptImagePath?.let { path ->
                    try {
                        File(path).delete()
                    } catch (e: Exception) {
                        // Log error but continue
                    }
                }
                receiptImagePath = null
                binding.cardReceiptPreview.isVisible = false
                binding.layoutAttachButtons.isVisible = true
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun viewFullReceipt(imagePath: String) {
        // Open image in full screen viewer or external app
        try {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                File(imagePath)
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(intent)
        } catch (e: Exception) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Unable to Open")
                .setMessage("Could not open receipt image")
                .setPositiveButton("OK", null)
                .show()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        receiptOcrHelper.cleanup()
        _binding = null
    }
    
    companion object {
        private const val ARG_TRANSACTION = "transaction"
        
        fun newInstance(transaction: Transaction?): AddEditTransactionDialog {
            return AddEditTransactionDialog().apply {
                arguments = bundleOf(ARG_TRANSACTION to transaction)
            }
        }
    }
}
