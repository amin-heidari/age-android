package com.aminheidari.age.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aminheidari.age.R
import com.aminheidari.age.fragments.NewAgeFragment
import com.aminheidari.age.utils.BackStackBehaviour
import com.aminheidari.age.utils.TransactionAnimation
import com.aminheidari.age.utils.showFragment

class NewAgeActivity : AppCompatActivity() {

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

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_age)

        showFragment(NewAgeFragment.newInstance(NewAgeFragment.Scenario.NewEntity), BackStackBehaviour.None, TransactionAnimation.None)
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
