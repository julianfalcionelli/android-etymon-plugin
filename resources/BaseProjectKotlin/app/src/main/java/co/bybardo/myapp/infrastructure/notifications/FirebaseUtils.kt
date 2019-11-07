/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.notifications

import android.content.Context
import co.bybardo.myapp.R
import com.google.firebase.iid.FirebaseInstanceId
import java.io.IOException

object FirebaseUtils {

    fun cleanupFirebaseInstance() {
        // Resets Instance ID and revokes all tokens.
        FirebaseInstanceId.getInstance().deleteInstanceId()
    }

    @Throws(IOException::class)
    fun getPushNotificationsToken(context: Context): String {
        return FirebaseInstanceId.getInstance().getToken(
            context.getString(R.string.firebase_sender_id), "FCM")
    }
}