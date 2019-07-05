package com.aminheidari.age.receivers

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.aminheidari.age.appwidgets.AgeAppWidget
import com.aminheidari.age.utils.Logger

/**
 * The sole purpose of this broadcast receiver, is to trigger an update to all active instances of the
 * `AgeAppWidget` widgets.
 */
class AgeUpdateReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { ctx ->
            val widgetIntent = Intent(ctx, AgeAppWidget::class.java)
            widgetIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            widgetIntent.putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_IDS,
                AppWidgetManager.getInstance(ctx).getAppWidgetIds(
                    ComponentName(ctx, AgeAppWidget::class.java)
                )
            )
            ctx.sendBroadcast(widgetIntent)
        }
    }

}