package com.aminheidari.age.appwidgets

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import android.view.View
import android.widget.RemoteViews
import com.aminheidari.age.R
import com.aminheidari.age.activities.MainActivity
import com.aminheidari.age.calculator.AgeCalculator
import com.aminheidari.age.models.AppWidgetOverride
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

    companion object { }

    // endregion

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    private sealed class Content {
        data class NoDefaultAge(
            val appPendingIntent: PendingIntent
        ): Content()
        data class RealtimeAge(
            val age: AgeCalculator.Age,
            val refreshed: String,
            val manualRefreshPendingIntent: PendingIntent,
            val appPendingIntent: PendingIntent
        ): Content()
        data class UpgradeOverride(
            val storePendingIntent: PendingIntent
        ): Content()
        data class ConnectionOverride(
            val appPendingIntent: PendingIntent
        ): Content()
    }

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

        val content = evaluateContent(context)

        // Update all widgets.
        appWidgetIds.forEach { widgetId ->
            val remoteViews = RemoteViews(context.packageName, R.layout.appwidget_age)

            when (content) {
                is Content.NoDefaultAge -> {
                    remoteViews.setOnClickPendingIntent(R.id.rootLayout, content.appPendingIntent)

                    remoteViews.setTextViewText(R.id.ageTextView, "No age!")

                    remoteViews.setViewVisibility(R.id.refreshedTextView, View.GONE)
                    remoteViews.setViewVisibility(R.id.refreshButton, View.GONE)
                }
                is Content.RealtimeAge -> {
                    remoteViews.setOnClickPendingIntent(R.id.rootLayout, content.appPendingIntent)

                    remoteViews.setTextViewText(R.id.ageTextView, String.format("%.8f", content.age.value))

                    remoteViews.setViewVisibility(R.id.refreshedTextView, View.VISIBLE)
                    remoteViews.setTextViewText(R.id.refreshedTextView, String.format("Last updated on: %s", content.refreshed))

                    remoteViews.setViewVisibility(R.id.refreshButton, View.VISIBLE)
                    remoteViews.setOnClickPendingIntent(R.id.refreshButton, content.manualRefreshPendingIntent)
                }
                is Content.ConnectionOverride -> {
                    remoteViews.setOnClickPendingIntent(R.id.rootLayout, content.appPendingIntent)

                    remoteViews.setTextViewText(R.id.ageTextView, "Connect!")

                    remoteViews.setViewVisibility(R.id.refreshedTextView, View.GONE)
                    remoteViews.setViewVisibility(R.id.refreshButton, View.GONE)
                }
                is Content.UpgradeOverride -> {
                    remoteViews.setOnClickPendingIntent(R.id.rootLayout, content.storePendingIntent)

                    remoteViews.setTextViewText(R.id.ageTextView, "Upgrade!")

                    remoteViews.setViewVisibility(R.id.refreshedTextView, View.GONE)
                    remoteViews.setViewVisibility(R.id.refreshButton, View.GONE)
                }
            }

            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        // Set up a repeating pending intent to `AgeUpdateReceiver` to keep updating the widget instances.
        val pendingIntent = PendingIntent.getBroadcast(context, 0, Intent(context, AgeUpdateReceiver::class.java), 0)
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
            .setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime(), 60000, pendingIntent)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)

        // Cancel the repeating alarm.
        val pendingIntent = PendingIntent.getBroadcast(context, 0, Intent(context, AgeUpdateReceiver::class.java), 0)
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

    private fun evaluateContent(context: Context): Content {
        return when (val override = PreferencesUtil.appWidgetOverride) {
            is AppWidgetOverride.Upgrade -> {
                Content.UpgradeOverride(
                    PendingIntent.getActivity(context, 0, Intent(Intent.ACTION_VIEW, Uri.parse(override.storeUrl)), 0)
                )
            }
            is AppWidgetOverride.OpenApp -> {
                Content.ConnectionOverride(
                    PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)
                )
            }
            is AppWidgetOverride.None -> {
                if (PreferencesUtil.defaultBirthday != null) {
                    Content.RealtimeAge(
                        AgeCalculator(PreferencesUtil.defaultBirthday!!.birthDate).currentAge,
                        SimpleDateFormat.getTimeInstance().format(Calendar.getInstance().time),
                        PendingIntent.getBroadcast(context, 0, Intent(context, AgeUpdateReceiver::class.java), 0),
                        PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)
                    )
                } else {
                    Content.NoDefaultAge(
                        PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)
                    )
                }
            }
        }
    }

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    // endregion

}

