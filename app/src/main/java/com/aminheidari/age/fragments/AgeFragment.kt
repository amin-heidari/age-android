package com.aminheidari.age.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminheidari.age.R
import com.aminheidari.age.calculator.AgeCalculator
import com.aminheidari.age.constants.Constants
import com.aminheidari.age.utils.*
import com.vanniktech.rxbilling.RxBilling
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_age.*

class AgeFragment : BaseFragment() {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    companion object {

        @JvmStatic
        fun newInstance() = AgeFragment()

    }

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
        return inflater.inflate(R.layout.fragment_age, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val birthday = PreferencesUtil.defaultBirthday!!

        ageTextView.text = String.format("%s is: %d-%d-%d", birthday.name,  birthday.birthDate.year, birthday.birthDate.month, birthday.birthDate.day)

        agesButton.setOnClickListener(agesButtonOnClickListener)
    }

    override fun onStart() {
        super.onStart()

        ageCalculator = AgeCalculator(PreferencesUtil.defaultBirthday!!.birthDate)

        // If the app is already purchased, then don't do anything.
        // Note that for a regular app we'll always set up store connections and utils. But here we have a single non-consumable IAP so making it simpler.
        if (PreferencesUtil.multipleAgesPurchaseToken == null) {
            // Update UI.
            agesButton.visibility = View.VISIBLE
        } else {
            // Otherwise, query the status of the in app purchase and proceed accordingly.
            val isBillingForInAppSupported = rxBilling?.isBillingForInAppSupported
            if (isBillingForInAppSupported != null) {
                isBillingForInAppSupportedDisposable = isBillingForInAppSupported.subscribe({
                    // According to the docs, if it completes (i.e. if we're here), then the in app billing is supported.
                    if (isAtLeastStarted) {
                        // Dispose it.
                        isBillingForInAppSupportedDisposable?.dispose()

                        val queryInAppPurchases = rxBilling?.queryInAppPurchases(Constants.Billing.multipleAgesId)
                        if (queryInAppPurchases != null) {
                            queryInAppPurchasesDisposable = queryInAppPurchases.subscribe({ inventoryInApp ->
                                // Individual items.
                                if (isAtLeastStarted) {
                                    // Check if it's our desired in app purchase.
                                    if (inventoryInApp.sku() == Constants.Billing.multipleAgesId) {
                                        // We don't need further updates from this point on since we have a single in app purchase at the moment.
                                        queryInAppPurchasesDisposable?.dispose()

                                        // Update UI.
                                        agesButton.visibility = View.VISIBLE
                                    }
                                }
                            }, {
                                // Possible Error handling.
                            }, {
                                // Completed.
                                // Debugging shows that it's disposed at this point. But it doesn't hurt.
                                queryInAppPurchasesDisposable?.dispose()
                            })
                        } else {
                            // Possible error handling logic.
                        }
                    }
                }, {
                    // Possible Error handling.
                })
            } else {
                // Possible error handling logic.
            }
        }
    }

    override fun onResume() {
        super.onResume()

        refreshAge()
    }

    override fun onStop() {
        super.onStop()

        ageCalculator = null

        isBillingForInAppSupportedDisposable?.dispose()
        queryInAppPurchasesDisposable?.dispose()
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private var ageCalculator: AgeCalculator? = null

    private var isBillingForInAppSupportedDisposable: Disposable? = null
    private var queryInAppPurchasesDisposable: Disposable? = null

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    private fun refreshAge() {
        ageCalculator?.let { calculator ->
            if (isResumed) {
                ageTextView.text = String.format("%10.8f", calculator.currentAge.value)
                Handler().postDelayed({
                    refreshAge()
                }, Constants.AgeCalculation.refreshInterval)
            }
        }
    }

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    private val agesButtonOnClickListener = View.OnClickListener {
        showFragment(AgesFragment.newInstance(), BackStackBehaviour.Add)
    }

    // endregion

}
