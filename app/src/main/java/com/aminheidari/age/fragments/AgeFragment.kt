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
        // Otherwise, query the status of the in app purchase and proceed accordingly.
        isBillingForInAppSupportedDisposable = rxBilling?.isBillingForInAppSupported?.subscribe {
            if (isVisible) {
                isBillingForInAppSupportedDisposable?.dispose()

                queryInAppPurchasesDisposable = rxBilling?.queryInAppPurchases(Constants.Billing.multipleAgesId)?.subscribe { inventoryInApp ->
                    if (isVisible) {
                        // We can safely dispose here since only a single in app purchase we polled above.
                        queryInAppPurchasesDisposable?.dispose()

                        if (inventoryInApp.sku() == Constants.Billing.multipleAgesId) {
                            agesButton.visibility = View.VISIBLE
                        }
                    }
                }
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
