package com.aminheidari.age.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminheidari.age.BuildConfig
import com.aminheidari.age.R
import com.aminheidari.age.config.RemoteConfigManager
import com.aminheidari.age.dialogs.AlertDialogFragment
import com.aminheidari.age.models.RemoteConfig
import com.aminheidari.age.utils.*
import kotlinx.android.synthetic.main.fragment_upgrade.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (RemoteConfigManager.remoteConfig.version.compare(BuildConfig.VERSION_NAME)) {
            RemoteConfig.Version.CompareResult.ForcedUpgrade -> {
                upgradeTitleTextView.text = "Please upgrade to latest version."
                upgradeDescriptionTextView.text = "You need to upgrade to the latest version of the application in order to continue using the app."
                skipButton.visibility = View.GONE
            }
            RemoteConfig.Version.CompareResult.OptionalUpgrade -> {
                upgradeTitleTextView.text = "There's a new version available :)"
                upgradeDescriptionTextView.text = "There’s a newer, cooler version of the application available on the store."
                skipButton.visibility = View.VISIBLE
            }
            else -> {
                Logger.wtf("Not supported!")
            }
        }

        upgradeButton.setOnClickListener(upgradeButtonOnClickListener)
        skipButton.setOnClickListener(skipButtonOnClickListener)
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

    private val upgradeButtonOnClickListener = View.OnClickListener {
        activity?.let { activity ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(RemoteConfigManager.remoteConfig.storeUrl))
            if (intent.resolveActivity(activity.packageManager) != null) {
                startActivity(intent)
            } else {
                AlertDialogFragment.showNewInstance(
                    this,
                    AlertDialogFragment.Input.Informational(R.string.sorry, R.string.error, R.string.ok)
                )
            }
        }
    }

    private val skipButtonOnClickListener = View.OnClickListener {
        assert(RemoteConfigManager.remoteConfig.version.compare(BuildConfig.VERSION_NAME) != RemoteConfig.Version.CompareResult.ForcedUpgrade)

        PreferencesUtil.skippedLatestVersion = RemoteConfigManager.remoteConfig.version.latest

        if (PreferencesUtil.defaultBirthday != null) {
            showFragment(AgeFragment.newInstance(), BackStackBehaviour.Wipe)
        } else {
            showFragment(NewAgeFragment.newInstance(NewAgeFragment.Scenario.NewDefault), BackStackBehaviour.Wipe, TransactionAnimation.PresentBottom)
        }
    }

    // endregion
}
