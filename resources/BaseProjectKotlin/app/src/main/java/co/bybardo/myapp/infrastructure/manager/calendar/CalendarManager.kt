/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.calendar

import android.content.Context
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime

interface CalendarManager {
    fun addToCalendar(
        context: Context,
        eventName: String,
        startDate: OffsetDateTime,
        duration: Duration,
        address: String,
        rule: String? = null
    )
}