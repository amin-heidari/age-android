package com.aminheidari.age.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminheidari.age.R

class UpgradeFragment : BaseFragment() {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    companion object {

        @JvmStatic
        fun newInstance() = UpgradeFragment()

    }

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upgrade, container, false)
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

}
