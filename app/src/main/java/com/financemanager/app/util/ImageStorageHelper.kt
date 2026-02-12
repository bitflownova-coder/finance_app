package com.financemanager.app.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper for managing receipt images
 */
@Singleton
class ImageStorageHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val receiptsDir: File
        get() {
            val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "receipts")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return dir
        }
    
    /**
     * Create a temporary file for camera capture
     */
    fun createImageFile(): Pair<File, Uri> {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "RECEIPT_$timeStamp.jpg"
        val imageFile = File(receiptsDir, imageFileName)
        
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )
        
        return Pair(imageFile, uri)
    }
    
    /**
     * Save image from URI to app storage
     */
    fun saveImageFromUri(sourceUri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(sourceUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            
            saveImage(bitmap)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Save bitmap to app storage
     */
    fun saveImage(bitmap: Bitmap): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "RECEIPT_$timeStamp.jpg"
        val imageFile = File(receiptsDir, imageFileName)
        
        FileOutputStream(imageFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        
        return imageFile.absolutePath
    }
    
    /**
     * Create thumbnail from image
     */
    fun createThumbnail(imagePath: String, maxSize: Int = 200): String? {
        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(imagePath, options)
            
            options.inSampleSize = calculateInSampleSize(options, maxSize, maxSize)
            options.inJustDecodeBounds = false
            
            val bitmap = BitmapFactory.decodeFile(imagePath, options)
            
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val thumbnailFileName = "THUMB_$timeStamp.jpg"
            val thumbnailFile = File(receiptsDir, thumbnailFileName)
            
            FileOutputStream(thumbnailFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            }
            
            thumbnailFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Calculate sample size for downscaling
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height, width) = options.outHeight to options.outWidth
        var inSampleSize = 1
        
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        
        return inSampleSize
    }
    
    /**
     * Delete image file
     */
    fun deleteImage(imagePath: String): Boolean {
        return try {
            val file = File(imagePath)
            file.delete()
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get URI from file path
     */
    fun getUriFromPath(filePath: String): Uri {
        val file = File(filePath)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }
}
