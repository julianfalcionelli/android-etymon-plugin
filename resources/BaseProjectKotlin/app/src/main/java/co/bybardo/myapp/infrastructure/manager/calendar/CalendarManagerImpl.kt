/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.infrastructure.manager.calendar

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime

class CalendarManagerImpl : CalendarManager {

    override fun addToCalendar(
        context: Context,
        eventName: String,
        startDate: OffsetDateTime,
        duration: Duration,
        address: String,
        rule: String?
    ) {
        val startDateMillis = startDate.toInstant().toEpochMilli()
        val endDateMillis = startDateMillis.plus(duration.toMillis())

        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDateMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDateMillis)
            .apply { rule?.let { putExtra(CalendarContract.Events.RRULE, it) } }
            .putExtra(CalendarContract.Events.TITLE, eventName)
            .putExtra(CalendarContract.Events.EVENT_LOCATION, address)
            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)

        context.startActivity(intent)
    }
}