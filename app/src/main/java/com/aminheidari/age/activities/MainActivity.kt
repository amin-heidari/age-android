package com.aminheidari.age.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.aminheidari.age.R
import com.aminheidari.age.fragments.BaseFragment
import com.aminheidari.age.fragments.LoadingFragment
import com.aminheidari.age.utils.*
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

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragment(LoadingFragment.newInstance(), BackStackBehaviour.None, TransactionAnimation.None)
    }

    override fun onBackPressed() {
        super.onBackPressed()

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

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val topBaseFragment: BaseFragment?
        get() = supportFragmentManager.fragments.lastOrNull { it is BaseFragment } as? BaseFragment

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
