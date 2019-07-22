package com.aminheidari.age.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.aminheidari.age.R
import com.aminheidari.age.fragments.BaseFragment
import com.aminheidari.age.fragments.LoadingFragment
import com.aminheidari.age.models.AppWidgetOverride
import com.aminheidari.age.utils.*
import com.vanniktech.rxbilling.RxBilling
import com.vanniktech.rxbilling.google.play.library.RxBillingGooglePlayLibrary
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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

    val rxBilling: RxBilling?
        get() = _rxBilling

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragment(LoadingFragment.newInstance(), BackStackBehaviour.None, TransactionAnimation.None)

        _rxBilling = RxBillingGooglePlayLibrary(this)
    }

    override fun onBackPressed() {
        val top = topBaseFragment

        val proceed = { super.onBackPressed() }

        if (top == null) {
            proceed()
        } else {
            val handled = top.handleBackPressed(object: BaseFragment.OnNavigateAwayListener {
                override fun onNavigateAway(proceed: Boolean) {
                    if (proceed) {
                        proceed()
                    }
                }
            })
            if (!handled) {
                proceed()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _rxBilling?.destroy()
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val topBaseFragment: BaseFragment?
        get() = supportFragmentManager.fragments.lastOrNull { it is BaseFragment } as? BaseFragment

    private var _rxBilling: RxBilling? = null

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region OnFragmentsStackChangedListener
    // ====================================================================================================

    // endregion

}
