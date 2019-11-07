/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */
package co.bybardo.myapp.infrastructure.manager.image

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import co.bybardo.myapp.infrastructure.manager.auth.AuthenticationManager
import co.bybardo.myapp.infrastructure.networking.RestConstants
import co.bybardo.myapp.ui.extensions.getDrawableCompat
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.schedulers.Schedulers

class GlideImageManager(
    private val authenticationManager: AuthenticationManager
) : ImageManager {

    override fun loadCircleUriImage(
        context: Context,
        uri: Uri,
        imageView: ImageView,
        @DrawableRes placeholder: Int
    ) {
        GlideApp.with(context)
            .load(uri)
            .circleCrop()
            .placeholder(placeholder)
            .into(imageView)
    }

    @SuppressLint("CheckResult")
    override fun loadCircleImage(
        context: Context,
        url: String,
        imageView: ImageView,
        @DrawableRes placeholder: Int,
        authRequired: Boolean
    ) {
        val finalUrl = if (authRequired) {
            getAuthorizedGlideUrl(url)
        } else {
            GlideUrl(url)
        }

        val finalPlaceholder: Drawable = if (imageView.drawable != null) {
            imageView.drawable // Use current image
        } else {
            context.getDrawableCompat(placeholder)
        }

        // To Reload the image even if is the same URL
        val requestOptions = RequestOptions()
        requestOptions.signature(
            ObjectKey(System.currentTimeMillis().toString()))

        GlideApp.with(context)
            .setDefaultRequestOptions(requestOptions)
            .load(finalUrl)
//            .apply(RequestOptions
//                .skipMemoryCacheOf(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE))
            .circleCrop()
            .placeholder(finalPlaceholder)
            .into(imageView)
    }

    override fun loadBitmap(context: Context, url: String): Single<Bitmap> {
        return Single.create(SingleOnSubscribe<Bitmap> { emitter ->
            GlideApp.with(context)
                .asBitmap()
                .load(url)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        emitter.onError(Exception("Error downloading bitmap"))
                        return true
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (resource != null) {
                            emitter.onSuccess(resource)
                        } else {
                            emitter.onError(Exception("ResourceReady but null"))
                        }

                        return true
                    }
                })
                .submit()
        }).subscribeOn(Schedulers.io())
    }

    private fun getAuthorizedGlideUrl(url: String): GlideUrl {
        return GlideUrl(url, LazyHeaders.Builder()
            .addHeader(RestConstants.HEADER_AUTH) {
                RestConstants.HEADER_BEARER + authenticationManager.getToken()
                    .onErrorReturnItem("")
                    .blockingGet()
            }
            .build())
    }
}