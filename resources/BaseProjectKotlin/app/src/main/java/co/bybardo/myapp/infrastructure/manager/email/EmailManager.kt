/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.email

import android.app.Activity
import android.content.Intent
import android.net.Uri
import co.bybardo.myapp.R

class EmailManager() {

    fun sendEmail(activity: Activity, to: String, subject: String) {
        val buffer = StringBuffer()

        buffer.append("mailto:")
            .append(to)
            .append("?subject=")
            .append(subject)
            .append("&body=")

        val uriString = buffer.toString().replace(" ", "%20")

        activity.startActivity(
            Intent.createChooser(Intent(Intent.ACTION_SENDTO, Uri.parse(uriString)),
                activity.getString(R.string.app_name)))
    }
}
