package com.aminheidari.age

import android.app.Application

class App: Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

}

// ====================================================================================================
// region Constants/Types
// ====================================================================================================

// endregion

// ====================================================================================================
// region Static
// ====================================================================================================

// endregion

// ====================================================================================================
// region API
// ====================================================================================================

// endregion

// ====================================================================================================
// region Life Cycle
// ====================================================================================================

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