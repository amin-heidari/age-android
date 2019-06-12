package com.aminheidari.age.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.lang.IllegalStateException

class AlertDialogFragment : BaseDialogFragment() {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    companion object {

        private const val TITLE = "TITLE"
        private const val MESSAGE = "MESSAGE"
        private const val NEUTRAL_BUTTON = "NEUTRAL_BUTTON"

        // The unique request code which belongs to this fragment only.
        const val requestCode = 10

        /**
         * https://stackoverflow.com/a/47514643
         * https://stackoverflow.com/a/21032871
         */
        @JvmStatic
        fun showNewInstance(targetFragment: Fragment,
                            title: Int? = null,
                            message: Int? = null,
                            neutralButton: Int? = null)
        {
            val fragment = AlertDialogFragment()

            val args = Bundle()
            title?.let { args.putInt(TITLE, it) }
            message?.let { args.putInt(MESSAGE, it) }
            neutralButton?.let { args.putInt(NEUTRAL_BUTTON, it) }
            fragment.arguments = args

            fragment.setTargetFragment(targetFragment, requestCode)

            // If the back button needs to be blocked.
            // https://stackoverflow.com/a/10171885
            fragment.isCancelable = true

            targetFragment.activity?.let { fragment.show(it.supportFragmentManager, AlertDialogFragment::class.java.canonicalName) }
        }

    }

    // endregion

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region API
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            val builder = AlertDialog.Builder(activity)

            arguments?.let { args ->
                if (args.containsKey(TITLE)) {
                    builder.setTitle(args.getInt(TITLE))
                }
                if (args.containsKey(MESSAGE)) {
                    builder.setMessage(args.getInt(MESSAGE))
                }
                if (args.containsKey(NEUTRAL_BUTTON)) {
                    builder.setNeutralButton(args.getInt(NEUTRAL_BUTTON)) { _, _ ->
                        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
                    }
                }
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null!")

    }

    override fun onStart() {
        super.onStart()

        dialog.setCanceledOnTouchOutside(false)
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
