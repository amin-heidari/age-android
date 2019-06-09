package com.aminheidari.age.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aminheidari.age.R
import com.aminheidari.age.config.RemoteConfigManager
import com.aminheidari.age.fragments.LoadingFragment
import com.aminheidari.age.models.RemoteConfig
import com.aminheidari.age.utils.BackStackBehaviour
import com.aminheidari.age.utils.TransactionAnimation
import com.aminheidari.age.utils.showFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        showFragment(LoadingFragment.newInstance(), BackStackBehaviour.None, TransactionAnimation.None)
    }

    // endregion

}
