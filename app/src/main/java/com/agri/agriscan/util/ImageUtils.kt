package com.agri.agriscan.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.media.ExifInterface
import com.agri.agriscan.util.Constants
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import android.util.Base64

object ImageUtils {

    /**
     * Resize image to maximum dimensions while maintaining aspect ratio
     */
    fun resizeImage(
        context: Context,
        uri: Uri,
        maxWidth: Int = Constants.MAX_IMAGE_WIDTH,
        maxHeight: Int = Constants.MAX_IMAGE_HEIGHT
    ): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            var width = options.outWidth
            var height = options.outHeight
            var scale = 1

            while (width / 2 >= maxWidth && height / 2 >= maxHeight) {
                width /= 2
                height /= 2
                scale *= 2
            }

            val finalOptions = BitmapFactory.Options().apply {
                inSampleSize = scale
            }

            val finalInputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(finalInputStream, null, finalOptions)
            finalInputStream?.close()

            // Fix orientation
            bitmap?.let { fixOrientation(context, uri, it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Fix image orientation based on EXIF data
     */
    private fun fixOrientation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val exif = inputStream?.let { ExifInterface(it) }
            inputStream?.close()

            val orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            ) ?: ExifInterface.ORIENTATION_NORMAL

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flipBitmap(bitmap, horizontal = true)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> flipBitmap(bitmap, horizontal = false)
                else -> bitmap
            }
        } catch (e: Exception) {
            e.printStackTrace()
            bitmap
        }
    }

    /**
     * Rotate bitmap by given degrees
     */
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * Flip bitmap horizontally or vertically
     */
    private fun flipBitmap(bitmap: Bitmap, horizontal: Boolean): Bitmap {
        val matrix = Matrix().apply {
            if (horizontal) {
                postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            } else {
                postScale(1f, -1f, bitmap.width / 2f, bitmap.height / 2f)
            }
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * Compress bitmap to JPEG with quality
     */
    fun compressBitmap(
        bitmap: Bitmap,
        quality: Int = Constants.IMAGE_QUALITY
    ): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }

    /**
     * Save bitmap to file
     */
    fun saveBitmapToFile(
        context: Context,
        bitmap: Bitmap,
        filename: String
    ): File? {
        return try {
            val file = File(context.cacheDir, filename)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, Constants.IMAGE_QUALITY, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Convert bitmap to Base64 string
     */
    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArray = compressBitmap(bitmap)
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    /**
     * Get file size in MB
     */
    fun getFileSizeInMB(file: File): Double {
        return file.length().toDouble() / (1024 * 1024)
    }

    /**
     * Delete temporary files
     */
    fun deleteTempFiles(context: Context) {
        try {
            val cacheDir = context.cacheDir
            cacheDir.listFiles()?.forEach { file ->
                if (file.name.startsWith("temp_") || file.name.startsWith("disease_temp_")) {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Create a scaled bitmap for thumbnail
     */
    fun createThumbnail(
        context: Context,
        uri: Uri,
        size: Int = 200
    ): Bitmap? {
        return resizeImage(context, uri, size, size)
    }
}