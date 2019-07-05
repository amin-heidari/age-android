package com.aminheidari.age.appwidgets

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.View
import android.widget.RemoteViews
import com.aminheidari.age.R
import com.aminheidari.age.activities.MainActivity
import com.aminheidari.age.calculator.AgeCalculator
import com.aminheidari.age.receivers.AgeUpdateReceiver
import com.aminheidari.age.utils.PreferencesUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class AgeAppWidget : AppWidgetProvider() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        const val AUTO_UPDATE_REQUEST_ID = 30
        const val MANUAL_UPDATE_REQUEST_ID = 40

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
        val currentAge: AgeCalculator.Age? = if (PreferencesUtil.defaultBirthday != null) AgeCalculator(PreferencesUtil.defaultBirthday!!.birthDate).currentAge else null

        // Evaluate the refreshed time stamp.
        val refreshed = SimpleDateFormat.getTimeInstance().format(Calendar.getInstance().time)

        // Pending intents.
        val manualUpdatePendingIntent = PendingIntent.getBroadcast(context, MANUAL_UPDATE_REQUEST_ID, Intent(context, AgeUpdateReceiver::class.java), 0)
        val appPendingIntent = PendingIntent.getActivity(context, MANUAL_UPDATE_REQUEST_ID, Intent(context, MainActivity::class.java), 0)

        // Update all widgets.
        appWidgetIds.forEach { widgetId ->
            val remoteViews = RemoteViews(context.packageName, R.layout.appwidget_age)

            remoteViews.setOnClickPendingIntent(R.id.rootLayout, appPendingIntent)

            if (currentAge != null) {
                remoteViews.setTextViewText(R.id.ageTextView, String.format("%.8f", currentAge.value))

                remoteViews.setViewVisibility(R.id.refreshedTextView, View.VISIBLE)
                remoteViews.setTextViewText(R.id.refreshedTextView, String.format("Last updated on: %s", refreshed))

                remoteViews.setViewVisibility(R.id.refreshButton, View.VISIBLE)
                remoteViews.setOnClickPendingIntent(R.id.refreshButton, manualUpdatePendingIntent)
            } else {
                remoteViews.setTextViewText(R.id.ageTextView, "TBD")

                remoteViews.setViewVisibility(R.id.refreshedTextView, View.GONE)
                remoteViews.setViewVisibility(R.id.refreshButton, View.GONE)
            }
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        // Set up a repeating pending intent to `AgeUpdateReceiver` to keep updating the widget instances.
        val pendingIntent = PendingIntent.getBroadcast(context, AUTO_UPDATE_REQUEST_ID, Intent(context, AgeUpdateReceiver::class.java), 0)
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
            .setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime(), 60000, pendingIntent)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)

        // Cancel the repeating alarm.
        val pendingIntent = PendingIntent.getBroadcast(context, AUTO_UPDATE_REQUEST_ID, Intent(context, AgeUpdateReceiver::class.java), 0)
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

