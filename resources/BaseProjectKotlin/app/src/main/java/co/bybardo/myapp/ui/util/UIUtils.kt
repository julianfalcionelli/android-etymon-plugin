/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.util

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.text.style.TextAppearanceSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

object UIUtils {
    interface OnViewTouchListener {
        fun onViewTouch(view: View, event: MotionEvent)
    }

    fun addOnViewTouchListener(
        view: View,
        listener: OnViewTouchListener,
        exceptions: List<View>?
    ) {
        // Check Exceptions
        if (exceptions != null && !exceptions.isEmpty() && exceptions.contains(view)) {
            return
        }

        // Add listener to RecyclerView Items
        if (view is RecyclerView) {
            view.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
                    listener.onViewTouch(view, event)
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                }
            })

            return
        }

        view.setOnTouchListener { _, event ->
            listener.onViewTouch(view, event)
            false
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                addOnViewTouchListener(innerView, listener, exceptions)
            }
        }
    }

    interface ActionFormListener {
        fun onFormFilled()
        fun onFormUnfilled()
    }

    fun performActionOnFormChange(
        listOfEditTexts: ArrayList<EditText>,
        action: ActionFormListener,
        preCheck: Boolean = false
    ) {
        if (preCheck) {
            if (isFormFilled(listOfEditTexts)) {
                action.onFormFilled()
            } else {
                action.onFormUnfilled()
            }
        }

        listOfEditTexts.forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(text: Editable?) {
                    if (isFormFilled(listOfEditTexts)) {
                        action.onFormFilled()
                    } else {
                        action.onFormUnfilled()
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })
        }
    }

    fun isFormFilled(listOfEditTexts: ArrayList<EditText>): Boolean =
        listOfEditTexts.firstOrNull { editText -> TextUtils.isEmpty(editText.text) } == null

    fun setTextWithMultipleStyles(context: Context, styles: ArrayList<Int>, texts: ArrayList<String>?):
        SpannableString? {
        if (texts == null || styles.size != texts.size) {
            return null
        }

        var allText: String? = null

        for (text in texts) {
            allText = if (allText == null) text else "$allText$text"
        }

        val spannableString = SpannableString(allText)

        var startIndex = 0

        for (i in texts.indices) {
            if (styles[i] > -1) {
                spannableString.setSpan(TextAppearanceSpan(context, styles[i]), startIndex,
                    startIndex + texts[i].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            startIndex += texts[i].length
        }

        return spannableString
    }

    fun setTextWithMultipleListeners(
        context: Context,
        listeners: ArrayList<View.OnClickListener?>,
        texts: ArrayList<String>?,
        styles: ArrayList<Int> = arrayListOf()
    ):
        SpannableString? {
        if (texts == null || listeners.size != texts.size) {
            return null
        }

        var allText: String? = null

        for (text in texts) {
            allText = if (allText == null) text else "$allText$text"
        }

        val spannableString = SpannableString(allText)

        var startIndex = 0

        for (i in texts.indices) {
            if (listeners[i] != null) {
                spannableString.setSpan(object : ClickableSpan() {
                    override fun onClick(view: View?) {
                        listeners[i]!!.onClick(view)
                    }
                }, startIndex,
                    startIndex + texts[i].length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }

            if (styles[i] > -1) {
                spannableString.setSpan(TextAppearanceSpan(context, styles[i]), startIndex,
                    startIndex + texts[i].length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            startIndex += texts[i].length
        }

        return spannableString
    }

    fun removeListLayoutAnimation(recyclerView: RecyclerView) {
        val itemAnimator = recyclerView.itemAnimator as SimpleItemAnimator
        itemAnimator.supportsChangeAnimations = false
        recyclerView.itemAnimator = null
    }

    fun getRootView(activity: Activity): View {
        return activity.window.decorView.findViewById(android.R.id.content)
    }
}