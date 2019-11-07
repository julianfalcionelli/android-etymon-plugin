/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.extensions

import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.animation.AlphaAnimation
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat

fun View.hide() {
    visibility = GONE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun View.show() {
    visibility = VISIBLE
}

fun View.isVisible() = visibility == VISIBLE

fun View.isGone() = visibility == GONE

fun View.isInvisible() = visibility == INVISIBLE

fun View.fadeIn() {
    val animation = AlphaAnimation(0.0f, 1.0f)
    animation.duration = 500

    startAnimation(animation)
}

const val DRAWABLE_LEFT = 0
const val DRAWABLE_TOP = 1
const val DRAWABLE_RIGHT = 2
const val DRAWABLE_BOTTOM = 3

fun EditText.setDrawableRightClickListener(listener: View.OnClickListener) {
    setOnTouchListener(View.OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= right - compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                // your action here
                listener.onClick(v)
                return@OnTouchListener true
            }
        }
        false
    })
}

fun EditText.setInputTypePassword() {
    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    setSelection(text.length)
}

fun EditText.setInputTypeText() {
    inputType = InputType.TYPE_CLASS_TEXT
    setSelection(text.length)
}

fun TextView.setDrawableRight(@DrawableRes drawableRes: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRes, 0)
}

fun EditText.setFont(@FontRes fontRes: Int) {
    typeface = ResourcesCompat.getFont(context, fontRes)
}

fun View.setBackgroundColorRes(@ColorRes color: Int) {
    setBackgroundColor(context.getColorCompat(color))
}
