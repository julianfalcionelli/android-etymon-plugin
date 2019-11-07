

/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.file

import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.RawRes
import java.io.File

interface FileManager {
    fun getStringFromFile(@RawRes jsonResourceID: Int): String

    fun uriToImageFile(uri: Uri): File?

    fun uriToBitmap(uri: Uri): Bitmap

    fun resizeBitmap(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap

    fun createFile(bitmap: Bitmap, fileCacheName: String): File
}