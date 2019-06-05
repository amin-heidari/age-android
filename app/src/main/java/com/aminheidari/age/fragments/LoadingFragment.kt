package com.aminheidari.age.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminheidari.age.R
import com.aminheidari.age.config.RemoteConfigManager
import com.aminheidari.age.models.Either
import com.aminheidari.age.models.RemoteConfig
import com.aminheidari.age.utils.BackStackBehaviour
import com.aminheidari.age.utils.Logger
import com.aminheidari.age.utils.TransactionAnimation
import com.aminheidari.age.utils.showFragment
import kotlinx.android.synthetic.main.fragment_loading.*
import retrofit2.Call
import java.util.*
import kotlin.math.absoluteValue

class LoadingFragment : BaseFragment() {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    companion object {

        @JvmStatic
        fun newInstance() = LoadingFragment()

    }

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configCall = RemoteConfigManager.fetchConfig { result ->
            when (result) {
                is Either.Failure -> {
                    Logger.d(result.exception.toString())
                }
                is Either.Success -> {
                    Logger.d("Yeeay!")
                    Logger.d("lalala", this@LoadingFragment.view.toString())
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private var bgColor = ""

    private var configCall: Call<RemoteConfig>? = null

    // endregion

}
