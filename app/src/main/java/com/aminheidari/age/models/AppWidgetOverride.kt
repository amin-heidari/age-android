package com.aminheidari.age.models

import java.io.Serializable

/**
 * Used by the containing app if it needs to override the behaviour of the widget.
 */
sealed class AppWidgetOverride: Serializable {

    /**
     * The containing app does not want to override the behaviour of the widget in any way.
     * The widget should continue it's normal behaviour.
     */
    object None: AppWidgetOverride()

    /**
     * The containing app needs the widget to open the app to figure things out and continue.
     * In the meantime, the widget should obey this.
     */
    object OpenApp: AppWidgetOverride()

    /**
     * The containing app has found out that there's an upgrade available on the store.
     * The widget needs to upgrade the app using the give `storeUrl`, and open the app to settle things and proceed.
     */
    data class Upgrade(val storeUrl: String): AppWidgetOverride()
}