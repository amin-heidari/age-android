package com.aminheidari.age.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminheidari.age.BuildConfig
import com.aminheidari.age.R
import com.aminheidari.age.config.RemoteConfigManager
import com.aminheidari.age.models.AppException
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        synchronized(this) {
            if (configCall == null) {
                loadConfig()
            }
        }

        retryButton.setOnClickListener {
            // Not really necessary, but wouldn't hurt.
            configCall?.cancel()

            loadConfig()
        }
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private var configCall: Call<RemoteConfig>? = null

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    private fun loadConfig() {
        progressBar.visibility = View.VISIBLE
        errorLayout.visibility = View.GONE

        configCall = RemoteConfigManager.fetchConfig { result ->
            progressBar.visibility = View.GONE

            when (result) {
                is Either.Failure -> {
                    errorLayout.visibility = View.VISIBLE

                    when (result.exception) {
                        is AppException.Connection -> {
                            errorTitleTextView.text = "No Internet!"
                            errorDescriptionTextView.text = "It looks like you're not connected to the internet. Please connect and try again!"
                            retryButton.visibility = View.VISIBLE
                        }
                        is AppException.CertificateExpired -> {
                            errorTitleTextView.text = "Please upgrade!"
                            errorDescriptionTextView.text = "Please upgrade the application from the app store!"
                            retryButton.visibility = View.GONE
                        }
                        else -> {
                            errorTitleTextView.text = "Error!"
                            errorDescriptionTextView.text = "An error occured, please try again!"
                            retryButton.visibility = View.VISIBLE
                        }
                    }
                }
                is Either.Success -> {
                    errorLayout.visibility = View.GONE

                    when (result.data.version.compare(BuildConfig.VERSION_NAME)) {
                        RemoteConfig.Version.CompareResult.ForcedUpgrade -> {
                            
                        }
                        RemoteConfig.Version.CompareResult.OptionalUpgrade -> {

                        }
                        RemoteConfig.Version.CompareResult.LatestVersion -> {
                            proceedToTheApp()
                        }
                    }

                }
            }
        }
    }

    private fun proceedToTheApp() {

    }

    // endregion

}
