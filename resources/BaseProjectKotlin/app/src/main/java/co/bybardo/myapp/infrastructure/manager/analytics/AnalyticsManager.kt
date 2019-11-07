/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.analytics

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import co.bybardo.myapp.BuildConfig

import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.NoSuchElementException

class AnalyticsManager(
    context: Context
) {
    companion object {
        const val TAG = "AnalyticsManager"

        // 2 seconds of interval to track same events  - This is to prevent spamming on actions that
        // can be triggered multiple times in very short intervals of time
        const val INTERVAL_MILLIS = 2000
    }

    enum class EventType {
        // Events that we should track every time
        DEFAULT,
        // Events that are only interested to track only just one time
        SINGLE,
        // Events that are only interested to track every X time
        INTERVAL
    }

    private var firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    private val trackedSingleEvents = mutableListOf<String>()
    private val trackedIntervalEvents = mutableMapOf<String, Long>()

    @SuppressLint("CheckResult")
    fun track(event: String, properties: Bundle? = null, eventType: EventType = EventType.DEFAULT) {
        isAnalyticsEnabled()
            .filter {
                when (eventType) {
                    EventType.SINGLE -> return@filter !isSingleEventAlreadyTracked(event)
                    EventType.INTERVAL -> return@filter !isIntervalEventAlreadyTracked(event)
                    else -> return@filter true
                }
            }
            .flatMapCompletable {
                firebaseAnalytics.logEvent(event, properties)

                Timber
                    .tag(TAG)
                    .d("New Event Tracked $event")

                when (eventType) {
                    EventType.SINGLE -> newSingleEvent(event)
                    EventType.INTERVAL -> newIntervalEvent(event)
                }

                Completable.complete()
            }
            .subscribeOn(Schedulers.io())
            .subscribe({
            },
                { error ->
                    if (error is NoSuchElementException) {
                        Timber
                            .tag(TAG)
                            .d("Analytics not enabled")
                    } else {
                        Timber
                            .tag(TAG)
                            .w(error, "Error tracking event.")
                    }
                })
    }

    private fun isSingleEventAlreadyTracked(event: String): Boolean {
        return trackedSingleEvents.contains(event)
    }

    private fun isIntervalEventAlreadyTracked(event: String): Boolean {
        return trackedIntervalEvents.containsKey(event) &&
            System.currentTimeMillis() - trackedIntervalEvents.get(event)!! <= INTERVAL_MILLIS
    }

    private fun newSingleEvent(event: String) {
        if (!trackedSingleEvents.contains(event)) {
            trackedSingleEvents.add(event)
        }
    }

    private fun newIntervalEvent(event: String) {
        trackedIntervalEvents[event] = System.currentTimeMillis()
    }

    /**
     * Emmit an item (True) if Analytics is enabled and
     * if is enabled this try to identify the current user
     * in Segment.
     */
    private fun isAnalyticsEnabled(): Single<Boolean> {
        return Single.just(BuildConfig.ANALYTICS_ENABLED)
    }
}