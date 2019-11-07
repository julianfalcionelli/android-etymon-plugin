/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.application

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDex
import co.bybardo.myapp.BuildConfig
import co.bybardo.myapp.R
import co.bybardo.myapp.infrastructure.manager.logging.CrashlyticsReportingTree
import co.bybardo.myapp.infrastructure.notifications.FirebaseUtils
import com.facebook.stetho.Stetho
import com.google.android.libraries.places.api.Places
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import com.uber.rxdogtag.RxDogTag
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MyAppApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        // ETYMON-ALFA-LEAK-DETECTION
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        // ETYMON-OMEGA-LEAK-DETECTION

        initializeLogs()
        AndroidThreeTen.init(this)
        initializeRx()

        // ETYMON-ALFA-GOOGLE-MAPS
        // Initialize Places.
        Places.initialize(applicationContext, getString(R.string.GOOGLE_MAPS_API_KEY))
        // ETYMON-OMEGA-GOOGLE-MAPS

        // ETYMON-ALFA-FIREBASE-MESSAGING
        logFCMToken()
        // ETYMON-OMEGA-FIREBASE-MESSAGING
    }

    private fun initializeRx() {
        // RxDog improves the readability of Rx Stacktraces
        RxDogTag.install()

        // Ignore Rx Undeliverable Exceptions
        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                return@setErrorHandler
            }

            Timber.w(e, "Undeliverable exception received, not sure what to do")
        }
    }

    private fun initializeLogs() {
        initializeTimber()
        // ETYMON-ALFA-STETHO
        initializeStetho()
        // ETYMON-OMEGA-STETHO
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            // Show logs in logcat
            Timber.plant(Timber.DebugTree())
        }

        // ETYMON-ALFA-CRASHLYTICS
        if (BuildConfig.LOG_TO_CRASHLYTICS) {
            // Show logs and crashes in crashlytics
            Timber.plant(CrashlyticsReportingTree())
        }
        // ETYMON-OMEGA-CRASHLYTICS
    }

    // ETYMON-ALFA-STETHO
    private fun initializeStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
    // ETYMON-OMEGA-STETHO

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    // ETYMON-ALFA-FIREBASE-MESSAGING
    // This is only for test purpose.
    @SuppressLint("CheckResult")
    private fun logFCMToken() {
        Completable.create { e ->
            try {
                Timber.d("Firebase Push Notifications Token: " +
                    FirebaseUtils.getPushNotificationsToken(this))
            } catch (error: Exception) {
                e.onError(error)
            }

            e.onComplete()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {})
    }
    // ETYMON-OMEGA-FIREBASE-MESSAGING
}