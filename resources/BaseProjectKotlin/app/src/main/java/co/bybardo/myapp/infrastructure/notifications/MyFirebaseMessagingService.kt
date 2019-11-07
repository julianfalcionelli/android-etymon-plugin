/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.notifications

import co.bybardo.myapp.domain.repository.interfaces.UserRepository
import co.bybardo.myapp.infrastructure.manager.auth.AuthenticationManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject lateinit var authenticationManager: AuthenticationManager
    @Inject lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    // Override handle intent to use our custom Notification Manager
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage == null) {
            return
        }

        // Custom handler ?
        super.onMessageReceived(remoteMessage)
    }

    override fun onNewToken(token: String?) {
        Timber.d("New Firebase Token: %s", token)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        token?.run {
            userRepository.saveCloudMessagingToken(this).blockingGet()
            if (authenticationManager.isLogged()) {
                userRepository.checkDeviceRegistration().blockingGet()
            }
        }

        super.onNewToken(token)
    }

    private fun injectDependencies() {
        (application as DaggerApplication).serviceInjector().inject(this)
    }
}
