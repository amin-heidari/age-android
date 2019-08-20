package com.aminheidari.age.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminheidari.age.R

/**
 * A simple [Fragment] subclass.
 *
 */
class MultipleAgesPromoFragment : BaseFragment() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    companion object {

        @JvmStatic
        fun newInstance() = MultipleAgesPromoFragment()

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
        return inflater.inflate(R.layout.fragment_multiple_ages_promo, container, false)
    }

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



}
