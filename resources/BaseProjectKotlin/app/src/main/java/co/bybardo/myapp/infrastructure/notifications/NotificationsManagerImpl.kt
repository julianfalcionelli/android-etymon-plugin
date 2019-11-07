/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.notifications

import android.annotation.SuppressLint
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import co.bybardo.myapp.R
import co.bybardo.myapp.ui.activity.main.mvp.MainActivity
import java.util.HashMap
import java.util.UUID
import javax.inject.Inject

class NotificationsManagerImpl @Inject constructor(
    context: Application
) : NotificationsManager {

    companion object {
        // If some Channel Configuration it's changed please update the ChannelId since Android SO
        // always keep the original configuration unless the user uninstall and install the application.
        private const val CHANNEL_ID_DEFAULT = "CHANNEL_ID_DEFAULT"
        private const val CHANNEL_ID_ALERTS = "CHANNEL_ID_ALERTS"
        private const val CHANNEL_ID_MARKETING = "CHANNEL_ID_MARKETING"
        private const val CHANNEL_ID_PUSH_NOTIFICATIONS = "CHANNEL_ID_PUSH_NOTIFICATIONS"
    }

    private val context: Context
    private val notificationManager: NotificationManager

    // This Map is used to save [when] value for notifications to prevent them to switch places
    @SuppressLint("UseSparseArrays")
    private val notificationsIdWhen = HashMap<Int, Long>()

    private val defaultTitle: String
        get() = context.getString(R.string.app_name)

    // TODO Use Vectorized Icon
//    private val notificationIcon: Int
//        get() = R.drawable.ic_launcher_foreground
    private val notificationIcon: Int
        get() = R.mipmap.ic_launcher

    private val defaultPendingIntent: PendingIntent
        get() {
            // TODO
            // val notificationIntent = Intent(context, SplashActivity::class.java)
            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            return PendingIntent.getActivity(context,
                System.currentTimeMillis().toInt(),
                notificationIntent, 0)
        }

    init {
        this.context = context
        notificationManager = this.context.getSystemService(
            NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationsChannels()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationsChannels() {
        createDefaultChannel()
        createAlertsChannel()
        createPushNotificationsChannel()
        createMarketingChannel()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createDefaultChannel() {
        val channel = NotificationChannel(CHANNEL_ID_DEFAULT,
            "Default",
            NotificationManager.IMPORTANCE_DEFAULT)
        channel.setSound(null, null)

        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createAlertsChannel() {
        val channel = NotificationChannel(CHANNEL_ID_ALERTS,
            "Alerts",
            NotificationManager.IMPORTANCE_HIGH)

        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createPushNotificationsChannel() {
        val channel = NotificationChannel(CHANNEL_ID_PUSH_NOTIFICATIONS,
            "Push Notifications",
            NotificationManager.IMPORTANCE_HIGH)

        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createMarketingChannel() {
        val channel = NotificationChannel(CHANNEL_ID_MARKETING,
            "Marketing",
            NotificationManager.IMPORTANCE_DEFAULT)

        notificationManager.createNotificationChannel(channel)
    }

    override fun createNotification(title: String?, description: String?): Notification {
        val notificationBuilder = createBaseNotification(CHANNEL_ID_DEFAULT)

        if (title != null) {
            notificationBuilder.setContentTitle(title)
        }

        if (description != null) {
            notificationBuilder.setContentText(description)
        }

        return notificationBuilder.build()
    }

    @SuppressLint("CheckResult")
    private fun sendNotification(notificationId: Int, notification: Notification) {
        notification.`when` = getWhenFromNotificationId(notificationId)

        // In the future check if user has notifications enable on in-app settings
        notificationManager.notify(notificationId, notification)
    }

    private fun cancelNotification(notificationId: Int) {
        if (notificationsIdWhen.containsKey(notificationId)) {
            notificationsIdWhen.remove(notificationId)
        }

        notificationManager.cancel(notificationId)
    }

    private fun createBaseNotification(channel: String): Notification.Builder {
        val notificationBuilder = Notification.Builder(context)
            .setSmallIcon(notificationIcon)
            // .setColor(context.getColorCompat(R.color.light_green))
            .setPriority(Notification.PRIORITY_MAX)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(channel)
        }

        return notificationBuilder
    }

    private fun createBasicNotification(title: String?, description: String?): Notification {
        val notificationBuilder = createBaseNotification(CHANNEL_ID_PUSH_NOTIFICATIONS)

        if (title != null) {
            notificationBuilder.setContentTitle(title)
        }

        if (description != null) {
            notificationBuilder.setContentText(description)
        }

        notificationBuilder.setContentIntent(defaultPendingIntent)

        return notificationBuilder.build()
    }

    private fun getWhenFromNotificationId(notificationId: Int): Long {
        if (!notificationsIdWhen.containsKey(notificationId)) {
            notificationsIdWhen[notificationId] = System.currentTimeMillis()
        }

        return notificationsIdWhen[notificationId]!!
    }

    private fun getRandomId(): Int {
        return UUID.randomUUID().toString().hashCode()
    }

    override fun newPush(title: String?, description: String?) {
        sendNotification(1,
            createBasicNotification(title ?: defaultTitle, description))
    }

    // TODO
    override fun newCustomNotification(title: String?, description: String?) {
        var notificationBuilder = createBaseNotification(CHANNEL_ID_ALERTS)

        notificationBuilder
            .setContentTitle(title)
            .setContentText(description)
            .setContentIntent(getCustomNotificationPendingIntent())

        sendNotification(
            getRandomId(),
            notificationBuilder.build()
        )
    }

    // TODO Create Custom Handler
    private fun getCustomNotificationPendingIntent(): PendingIntent {
        val intent = null
//        val intent = Intent(context, NotificationActionHandlerReceiver::class.java)
//
//        intent.action = NotificationActionHandlerReceiver.ACTION
//        intent.putExtra(NotificationActionHandlerReceiver.EXTRA, someExtra)

        return PendingIntent.getBroadcast(context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun clearAll() {
        notificationManager.cancelAll()
    }
}