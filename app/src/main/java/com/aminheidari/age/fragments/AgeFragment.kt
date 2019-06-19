package com.aminheidari.age.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminheidari.age.R
import com.aminheidari.age.calculator.AgeCalculator
import com.aminheidari.age.utils.AppExecutors
import com.aminheidari.age.utils.BackStackBehaviour
import com.aminheidari.age.utils.PreferencesUtil
import com.aminheidari.age.utils.showFragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val birthday = PreferencesUtil.defaultBirthday!!

        ageTextView.text = String.format("%s is: %d-%d-%d", birthday.name,  birthday.birthDate.year, birthday.birthDate.month, birthday.birthDate.day)

        agesButton.setOnClickListener(agesButtonOnClickListener)
    }

    override fun onStart() {
        super.onStart()

        ageCalculator = AgeCalculator(PreferencesUtil.defaultBirthday!!.birthDate)
    }

    override fun onResume() {
        super.onResume()

        refreshAge()
    }

    override fun onStop() {
        super.onStop()

        ageCalculator = null
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

    private fun refreshAge() {
        ageCalculator?.let { calculator ->
            if (isResumed) {
                ageTextView.text = String.format("%10.8f", calculator.currentAge.value)
                Handler().postDelayed({
                    refreshAge()
                }, 10)
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
