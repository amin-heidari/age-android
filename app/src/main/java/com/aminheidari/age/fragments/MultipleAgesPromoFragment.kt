package com.aminheidari.age.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminheidari.age.R
import com.aminheidari.age.constants.Constants
import com.aminheidari.age.dialogs.AlertDialogFragment
import com.aminheidari.age.utils.PreferencesUtil
import com.aminheidari.age.utils.isAtLeastCreated
import com.aminheidari.age.utils.popBackstack
import com.aminheidari.age.utils.rxBilling
import com.vanniktech.rxbilling.PurchasedInApp
import com.vanniktech.rxbilling.RxBilling
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_multiple_ages_promo.*
import kotlin.properties.Delegates

/**
 * MultipleAgesPromoFragment.
 */
class MultipleAgesPromoFragment : BaseFragment() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        @JvmStatic
        fun newInstance() = MultipleAgesPromoFragment()

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_ages_promo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buyButton.setOnClickListener(buyButtonOnClickListener)
        restoreButton.setOnClickListener(restoreButtonOnClickListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            AlertDialogFragment.REQUEST_CODE -> {
                popBackstack()
            }
            else -> Unit
        }
    }

    override fun handleBackPressed(listener: OnNavigateAwayListener): Boolean {
        // Don't allow going back if purchase in progress.
        if (isProcessing) { return true }
        return super.handleBackPressed(listener)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Doing this as late as possible in an attempt to capture the status of the purchase even if the user backgrounds the app.
        purchaseDisposable?.dispose()
        queryInAppPurchasesDisposable?.dispose()
        purchasedInAppsDisposable?.dispose()
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private var purchaseDisposable: Disposable? = null
    private var queryInAppPurchasesDisposable: Disposable? = null
    private var purchasedInAppsDisposable: Disposable? = null

    private var isProcessing: Boolean by Delegates.observable(false, { _, _, newValue ->
        if (newValue) {
            progressBar.visibility = View.VISIBLE
            actionsLayout.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            actionsLayout.visibility = View.VISIBLE
        }
    })

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    /**
     * Right now only showing a generic error message.
     * Can update and enhance this later.
     */
    private fun presentError() {
        /*
        Now here,
        If we wanna let the user stay on the page after we show error
        Then I'll need to reset `isProcessing`

        Otherwise, all good (once isPrcessing is set, it's set for good).
         */

        AlertDialogFragment.showNewInstance(
            this,
            AlertDialogFragment.Input.Informational(R.string.error, R.string.error_description, R.string.ok)
        )
    }

    private fun successWrapUp() {
        popBackstack()

        AlertDialogFragment.showNewInstance(
            this,
            AlertDialogFragment.Input.Informational(R.string.congrats, R.string.purchase_success, R.string.ok)
        )
    }

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    private val buyButtonOnClickListener = View.OnClickListener {
        isProcessing = true

        val currentRxBilling = rxBilling
        if (currentRxBilling != null) {
            queryInAppPurchasesDisposable = currentRxBilling.queryInAppPurchases(Constants.Billing.multipleAgesId).subscribe({ inventoryInApp ->
                // Individual items.
                if (!isDetached) {
                    // Check if it's our desired in app purchase.
                    if (inventoryInApp.sku() == Constants.Billing.multipleAgesId) {
                        purchaseDisposable = currentRxBilling.purchase(inventoryInApp, "").subscribe({ purchaseResponse ->
                            // Not sure if this is needed for a `Single`, just doing it to be safe.
                            purchaseDisposable?.dispose()

                            when (purchaseResponse.purchaseState()) {
                                RxBilling.BillingResponse.OK -> {
                                    PreferencesUtil.multipleAgesPurchaseToken = purchaseResponse.purchaseToken()

                                    successWrapUp()
                                }
                                else -> {
                                    presentError()
                                }
                            }
                        }, {
                            presentError()
                        })

                        // We don't need further updates from this point on since we have a single in app purchase at the moment.
                        queryInAppPurchasesDisposable?.dispose()
                    }
                }
            }, {
                // Error.
                presentError()
            }, {
                // Completed.
                // Debugging shows that it's disposed at this point. But it doesn't hurt.
                queryInAppPurchasesDisposable?.dispose()
            })
        } else {
            presentError()
        }
    }

    private val restoreButtonOnClickListener = View.OnClickListener {
        isProcessing = true

        var multipleAgesPurhcasedInApp: PurchasedInApp? = null

        val currentRxBilling = rxBilling
        if (currentRxBilling != null) {
            purchasedInAppsDisposable = currentRxBilling.purchasedInApps.subscribe({ purchasedInApp ->
                // Individual items.
                if (isAtLeastCreated) {
                    // Check if it's our desired in app purchase.
                    if (purchasedInApp.productId() == Constants.Billing.multipleAgesId) {
                        multipleAgesPurhcasedInApp = purchasedInApp

                        when (purchasedInApp.purchaseState()) {
                            RxBilling.BillingResponse.OK -> {
                                // Update the persistence.
                                PreferencesUtil.multipleAgesPurchaseToken = purchasedInApp.purchaseToken()
                            }
                            else -> {
                                presentError()
                            }
                        }

                        // We don't need further updates from this point on since we have a single in app purchase at the moment.
                        purchasedInAppsDisposable?.dispose()
                    }
                }
            }, {
                // Error.
                presentError()
            }, {
                // Completed.
                // Debugging shows that it's disposed at this point. But it doesn't hurt.
                purchasedInAppsDisposable?.dispose()

                // If at this point the `multipleAgesPurhcasedInApp` is still not set,
                // then that means either nothing was emitted above, or it means our desired IAP wasn't emitted.
                // Therefore that's also a restore error.
                if (multipleAgesPurhcasedInApp == null) {
                    presentError()
                }
            })
        } else {
            presentError()
        }
    }

    // endregion



}
