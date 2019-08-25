package com.example.imagesearchsample.common

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso

object ImageLoader {
    fun imageUrlToView(url: String, @DrawableRes placeholder: Int?, imageView: AppCompatImageView) {
        if (placeholder == null) {
            Picasso.get().load(Uri.parse("$url")).into(imageView)
        } else {
            Picasso.get().load(Uri.parse("$url")).placeholder(placeholder).into(imageView)
        }
    }

    fun imageUrlToViewAsCircle(url: String, @DrawableRes placeholder: Int?, imageView: AppCompatImageView) {
        if (placeholder == null) {
            Picasso.get().load(Uri.parse("$url")).transform(CircleTransform()).into(imageView)
        } else {
            Picasso.get().load(Uri.parse("$url")).transform(CircleTransform())
                .placeholder(placeholder).into(imageView)
        }
    }

    fun imageUrlToView(@DrawableRes resourceId: Int, imageView: AppCompatImageView) {
        Picasso.get().load(resourceId).into(imageView)
    }

    fun imageUrlToView(localUri: Uri, imageView: AppCompatImageView) {
        Picasso.get().load(localUri).into(imageView)
    }
}