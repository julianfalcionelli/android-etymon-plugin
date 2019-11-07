/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import io.reactivex.Single

interface ImageManager {
    fun loadCircleUriImage(
        context: Context,
        uri: Uri,
        imageView: ImageView,
        @DrawableRes placeholder: Int
    )

    fun loadCircleImage(
        context: Context,
        url: String,
        imageView: ImageView,
        @DrawableRes placeholder: Int,
        authRequired: Boolean = false
    )

    fun loadBitmap(context: Context, url: String): Single<Bitmap>
}
