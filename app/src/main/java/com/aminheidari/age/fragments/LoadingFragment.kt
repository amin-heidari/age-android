package com.aminheidari.age.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminheidari.age.BuildConfig
import com.aminheidari.age.R
import com.aminheidari.age.config.RemoteConfigManager
import com.aminheidari.age.models.AppException
import com.aminheidari.age.models.AppWidgetOverride
import com.aminheidari.age.models.RemoteConfig
import com.aminheidari.age.utils.*
import kotlinx.android.synthetic.main.fragment_loading.*
import retrofit2.Call

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

    override fun onDestroy() {
        super.onDestroy()

        configCall?.cancel()
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
                            errorImageView.setImageResource(R.drawable.ic_no_internet)
                            errorTitleTextView.text = "No Internet!"
                            errorDescriptionTextView.text = "It looks like you're not connected to the internet. Please connect and try again!"
                            retryButton.visibility = View.VISIBLE

                            PreferencesUtil.appWidgetOverride = AppWidgetOverride.OpenApp
                        }
                        is AppException.CertificateExpired -> {
                            errorImageView.setImageResource(R.drawable.ic_sad_face)
                            errorTitleTextView.text = "Please upgrade!"
                            errorDescriptionTextView.text = "Please upgrade the application to the latest version in order to continue."
                            retryButton.visibility = View.GONE

                            // A bit of a tricky case, but this would suffice for the widget.
                            // Will simply ask the user to come back to the app to (figure things out) and continue.
                            PreferencesUtil.appWidgetOverride = AppWidgetOverride.OpenApp
                        }
                        else -> {
                            errorImageView.setImageResource(R.drawable.ic_sad_face)
                            errorTitleTextView.text = "Error!"
                            errorDescriptionTextView.text = "An unknown error occurred. Please try again later!"
                            retryButton.visibility = View.VISIBLE

                            PreferencesUtil.appWidgetOverride = AppWidgetOverride.OpenApp
                        }
                    }
                }
                is Either.Success -> {
                    errorLayout.visibility = View.GONE

                    when (result.data.version.compare(BuildConfig.VERSION_NAME)) {
                        RemoteConfig.Version.CompareResult.ForcedUpgrade -> {
                            // The app's version is below the minimum required version.

                            PreferencesUtil.appWidgetOverride = AppWidgetOverride.Upgrade(result.data.storeUrl)

                            showFragment(UpgradeFragment.newInstance(), BackStackBehaviour.Wipe)
                        }
                        RemoteConfig.Version.CompareResult.OptionalUpgrade -> {
                            // The app's version is below the latest version.

                            PreferencesUtil.appWidgetOverride = AppWidgetOverride.None

                            val skippedLatestVersion = PreferencesUtil.skippedLatestVersion
                            if (skippedLatestVersion != null && skippedLatestVersion.compareVersionTo(result.data.version.latest) >= 0) {
                                // User has already skipped to upgrade to that version before.
                                proceedToTheApp()
                            } else {
                                // User has never skipped an upgrade to this version.
                                showFragment(UpgradeFragment.newInstance(), BackStackBehaviour.Wipe)
                            }
                        }
                        RemoteConfig.Version.CompareResult.LatestVersion -> {
                            PreferencesUtil.appWidgetOverride = AppWidgetOverride.None

                            proceedToTheApp()
                        }
                    }

                }
            }
        }
    }

    private fun proceedToTheApp() {
        if (PreferencesUtil.defaultBirthday != null) {
            showFragment(AgeFragment.newInstance(), BackStackBehaviour.Wipe)
        } else {
            showFragment(NewAgeFragment.newInstance(NewAgeFragment.Scenario.NewDefault), BackStackBehaviour.Wipe, TransactionAnimation.PresentBottom)
        }
    }

    // endregion

}
