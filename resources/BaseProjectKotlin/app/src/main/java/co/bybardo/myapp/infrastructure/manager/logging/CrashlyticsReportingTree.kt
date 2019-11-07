/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.logging

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashlyticsReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // These logs will appear in Firebase Console
        if (priority == Log.ERROR) {
            if (t != null) {
                Crashlytics.logException(t)
            } else {
                Crashlytics.logException(Exception(message))
            }
            return
        }

        Crashlytics.log("$tag: $message")
    }
}