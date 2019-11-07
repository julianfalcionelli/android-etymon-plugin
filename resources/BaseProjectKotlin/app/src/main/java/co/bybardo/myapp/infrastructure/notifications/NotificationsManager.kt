/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.notifications

import android.app.Notification

interface NotificationsManager {
    fun clearAll()

    fun newPush(title: String?, description: String? = null)

    fun createNotification(title: String?, description: String? = null): Notification

    fun newCustomNotification(title: String?, description: String?)
}