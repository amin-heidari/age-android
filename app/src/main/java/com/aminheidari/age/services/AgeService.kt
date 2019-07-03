package com.aminheidari.age.services

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.widget.CalendarView
import android.widget.RemoteViews
import com.aminheidari.age.R
import com.aminheidari.age.appwidgets.AgeAppWidget
import com.aminheidari.age.calculator.AgeCalculator
import com.aminheidari.age.utils.PreferencesUtil
import java.util.*

class AgeService : Service() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

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

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        if (ageCalculator == null) {
//            PreferencesUtil.defaultBirthday?.let { birthday ->
//                ageCalculator = AgeCalculator(birthday.birthDate)
//            }
//        }

        val view = RemoteViews(packageName, R.layout.appwidget_age)
        view.setTextViewText(R.id.textView, Random().nextInt().toString())

        AppWidgetManager.getInstance(this).updateAppWidget(ComponentName(this, AgeAppWidget::class.java), view)

        return super.onStartCommand(intent, flags, startId)
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private var ageCalculator: AgeCalculator? = null

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
