package com.aminheidari.age.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Lifecycle
import com.aminheidari.age.R
import com.aminheidari.age.constants.Constants
import com.aminheidari.age.fragments.BaseFragment
import com.aminheidari.age.fragments.LoadingFragment
import com.aminheidari.age.models.AppWidgetOverride
import com.aminheidari.age.utils.*
import com.vanniktech.rxbilling.RxBilling
import com.vanniktech.rxbilling.google.play.library.RxBillingGooglePlayLibrary
import io.reactivex.disposables.Disposable
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

    override fun onStart() {
        super.onStart()

        // If we're not sure about the state of the in app purchase.
        // Then setup the store utilities.
        // Note that for a regular app we'll always do it. But here we have a single non-consumable IAP so making it simpler.
        if (PreferencesUtil.multipleAgesPurchaseToken == null) {
            val purchasedInApps = rxBilling?.purchasedInApps
            if (purchasedInApps != null) {
                purchasedInAppsDisposable = purchasedInApps.subscribe({ purchasedInApp ->
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                        if (purchasedInApp.productId() == Constants.Billing.multipleAgesId) {
                            // We don't need further updates from this point on since we have a single in app purchase at the moment.
                            purchasedInAppsDisposable?.dispose()

                            when (purchasedInApp.purchaseState()) {
                                RxBilling.BillingResponse.OK -> {
                                    // Update the persistence.
                                    PreferencesUtil.multipleAgesPurchaseToken = purchasedInApp.purchaseToken()
                                }
                                else -> Unit
                            }
                        }
                    }
                }, {
                    // Possible Error handling.
                }, {
                    // Completed.
                    // Debugging shows that it's disposed at this point. But it doesn't hurt.
                    purchasedInAppsDisposable?.dispose()
                })
            } else {
                // Possible error handling logic.
            }
        }
    }

    override fun onStop() {
        super.onStop()

        purchasedInAppsDisposable?.dispose()
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
        _rxBilling = null
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val topBaseFragment: BaseFragment?
        get() = supportFragmentManager.fragments.lastOrNull { it is BaseFragment } as? BaseFragment

    private var _rxBilling: RxBilling? = null

    private var purchasedInAppsDisposable: Disposable? = null

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
