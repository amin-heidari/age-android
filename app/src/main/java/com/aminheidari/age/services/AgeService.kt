package com.aminheidari.age.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.aminheidari.age.calculator.AgeCalculator
import com.aminheidari.age.utils.PreferencesUtil

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
        if (ageCalculator == null) {
            PreferencesUtil.defaultBirthday?.let { birthday ->
                ageCalculator = AgeCalculator(birthday.birthDate)
            }
        }

        

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
