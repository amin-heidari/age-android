package com.aminheidari.age.appwidgets

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.RemoteViews
import com.aminheidari.age.R
import com.aminheidari.age.calculator.AgeCalculator
import com.aminheidari.age.receivers.AgeUpdateReceiver
import com.aminheidari.age.utils.Logger
import com.aminheidari.age.utils.PreferencesUtil
import java.util.*
import kotlin.math.absoluteValue

/**
 * Implementation of App Widget functionality.
 */
class AgeAppWidget : AppWidgetProvider() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        const val UPDATE_REQUEST_ID = 30
    }

    // endregion

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region API
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        // Evaluate the current age.
        var currentAge: AgeCalculator.Age? = null
        PreferencesUtil.defaultBirthday?.let { birthday ->
            currentAge = AgeCalculator(birthday.birthDate).currentAge
        }

        // Update all widgets.
        appWidgetIds.forEach { widgetId ->
            val remoteViews = RemoteViews(context.packageName, R.layout.appwidget_age)
            remoteViews.setTextViewText(R.id.textView, if (currentAge != null) String.format("%.8f", currentAge!!.value) else "TBD")
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        // Set up a repeating pending intent to `AgeUpdateReceiver` to keep updating the widget instances.
        val pendingIntent = PendingIntent.getBroadcast(context, UPDATE_REQUEST_ID, Intent(context, AgeUpdateReceiver::class.java), 0)
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
            .setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime(), 60000, pendingIntent)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)

        // Cancel the repeating alarm.
        val pendingIntent = PendingIntent.getBroadcast(context, UPDATE_REQUEST_ID, Intent(context, AgeUpdateReceiver::class.java), 0)
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pendingIntent)
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    // endregion

}

