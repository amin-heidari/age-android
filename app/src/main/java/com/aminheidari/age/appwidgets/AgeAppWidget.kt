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
import com.aminheidari.age.services.AgeService
import com.aminheidari.age.utils.Logger
import com.aminheidari.age.utils.PreferencesUtil

/**
 * Implementation of App Widget functionality.
 */
class AgeAppWidget : AppWidgetProvider() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        /*
        const val ACTION_REFRESH_AGE = "ACTION_REFRESH_AGE"

        private var calculator: AgeCalculator? = null

        private fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.appwidget_age)

            val calc = calculator
            if (calc != null) {
                views.setTextViewText(R.id.textView, String.format("%10.8f", calc.currentAge.value))
            } else {
                views.setTextViewText(R.id.textView, "Set Up!")
            }

            val intent = Intent(context, AgeAppWidget::class.java)
            intent.setAction(ACTION_REFRESH_AGE)

            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            views.setOnClickPendingIntent(R.id.rootLayout, pendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }*/
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

        Logger.d(hashCode().toString() + " onUpdate")

        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AgeService::class.java)
//        val intent = Intent(context, AgeAppWidget::class.java)
//        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, AgeAppWidget::class.java)))

        if (repeatingIntent == null) {
            repeatingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
//            repeatingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }

        alarmManager?.setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime(), 60000, repeatingIntent)
//        alarmManager?.setExact(AlarmManager.RTC, 60000, repeatingIntent)
    }


    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // Enter relevant functionality for when the first widget is created
        Logger.d(hashCode().toString() + " onEnabled")
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)

        // Enter relevant functionality for when the last widget is disabled
        Logger.d(hashCode().toString() + " onDisabled")

        repeatingIntent?.let { intent ->
            alarmManager?.cancel(repeatingIntent)
        }
//        calculator = null
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)

        Logger.d(hashCode().toString() + " onAppWidgetOptionsChanged")
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)

        Logger.d(hashCode().toString() + " onDeleted")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)

        Logger.d(hashCode().toString() + " onRestored")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (context == null) { return }
        if (intent == null) { return }

        /*
        when (intent.action) {
            ACTION_REFRESH_AGE -> {
                val views = RemoteViews(context.packageName, R.layout.appwidget_age)

                val calc = calculator
                if (calc != null) {
                    views.setTextViewText(R.id.textView, String.format("%10.8f", calc.currentAge.value))
                } else {
                    views.setTextViewText(R.id.textView, "Set Up!")
                }

                val appWidget = ComponentName(context, AgeAppWidget::class.java)
                val appWidgetManager = AppWidgetManager.getInstance(context)

                appWidgetManager.updateAppWidget(appWidget, views)
            }
        }*/

    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    var alarmManager: AlarmManager? = null
    var repeatingIntent: PendingIntent? = null

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

