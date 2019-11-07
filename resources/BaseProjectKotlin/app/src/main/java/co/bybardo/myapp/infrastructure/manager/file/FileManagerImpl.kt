/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.file

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.RawRes
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class FileManagerImpl(
    private val context: Context
) : FileManager {
    override fun getStringFromFile(@RawRes jsonResourceID: Int): String {
        var fileAsString = ""
        try {
            val inputStream = context.resources.openRawResource(jsonResourceID)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            fileAsString = String(buffer)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return fileAsString
    }

    override fun uriToImageFile(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val filePath = cursor.getString(columnIndex)
                cursor.close()
                return File(filePath)
            }
            cursor.close()
        }
        return null
    }

    override fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    override fun resizeBitmap(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        var image = image
        var maxWidth = maxWidth
        var maxHeight = maxHeight

        val width = image.width
        val height = image.height

        if (maxHeight > 0 && maxWidth > 0 &&
            (maxHeight < height || maxWidth < width)) {

            if (maxWidth > width) {
                maxWidth = width
            }

            if (maxHeight > height) {
                maxHeight = height
            }

            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap) {
                finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
            } else {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }

            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
            return image
        } else {
            return image
        }
    }

    override fun createFile(bitmap: Bitmap, fileCacheName: String): File {
        val file = File(context.cacheDir, fileCacheName)
        file.createNewFile()

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)

        val bitmapData = bos.toByteArray()

        val fos = FileOutputStream(file)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

        return file
    }
}