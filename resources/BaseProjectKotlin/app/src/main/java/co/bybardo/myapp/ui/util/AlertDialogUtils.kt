/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes

object AlertDialogUtils {

    @SuppressLint("ResourceType")
    fun createAlertDialogBuilder(context: Context, @StringRes title: Int, @StringRes description: Int):
        AlertDialog.Builder {
        var alertDialogBuilder = AlertDialog.Builder(context)

        if (title > -1) {
            alertDialogBuilder
                .setTitle(title)
        }

        if (description > -1) {
            alertDialogBuilder
                .setMessage(description)
        }

        return alertDialogBuilder
    }

    fun createBaseAlertDialogBuilder(context: Context) = AlertDialog.Builder(context)
}
