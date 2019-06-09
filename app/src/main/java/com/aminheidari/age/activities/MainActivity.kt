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

class MainActivity : AppCompatActivity(), OnFragmentsStackChangedListener {

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

        supportFragmentManager.addOnBackStackChangedListener {
            adjustToolbar()
        }

        setSupportActionBar(toolbar)
        adjustToolbar()

        showFragment(LoadingFragment.newInstance(), BackStackBehaviour.None, TransactionAnimation.None)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    popBackStack()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
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

    private fun adjustToolbar() {
        toolbar.visibility = if (topBaseFragment?.isShowToolbar == true) View.VISIBLE else View.GONE

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
            title = topBaseFragment?.title
        }
    }

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region OnFragmentsStackChangedListener
    // ====================================================================================================

    override fun onFragmentsStackChanged() {
        adjustToolbar()
    }

    // endregion

}
