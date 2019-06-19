package com.aminheidari.age.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.aminheidari.age.utils.INPUT
import com.aminheidari.age.utils.RESULT
import java.io.Serializable
import java.lang.IllegalStateException

class AlertDialogFragment : BaseDialogFragment() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        // The unique request code which belongs to this fragment only.
        const val REQUEST_CODE: Int = 30

        /**
         * https://stackoverflow.com/a/47514643
         * https://stackoverflow.com/a/21032871
         */
        @JvmStatic
        fun showNewInstance(targetFragment: Fragment,
                            input: Input)
        {
            val fragment = AlertDialogFragment()

            val args = Bundle()
            args.putSerializable(INPUT, input)
            fragment.arguments = args

            fragment.setTargetFragment(targetFragment, REQUEST_CODE)

            // If the back button needs to be blocked.
            // https://stackoverflow.com/a/10171885
            fragment.isCancelable = true

            targetFragment.activity?.let { fragment.show(it.supportFragmentManager, AlertDialogFragment::class.java.canonicalName) }
        }

    }

    // endregion

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    sealed class Input: Serializable {
        /**
         * Just to give user some information.
         */
        data class Informational(val title: Int?, val message: Int, val neutralButton: Int): Input()

        /**
         * To confirm and proceed with an action.
         */
        data class Proceed(val title: Int?, val message: Int, val positiveButton: Int, val negativeButton: Int): Input()
    }

    sealed class Result: Serializable {
        object Neutral: Result()
        object Negative: Result()
        object Positive: Result()
    }

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

            when (val inputVal = input) {
                is Input.Informational -> {
                    inputVal.title?.let {
                        builder.setTitle(it)
                    }
                    builder.setMessage(inputVal.message)
                    builder.setNeutralButton(inputVal.neutralButton) { _, _ ->
                        targetFragment?.onActivityResult(
                            REQUEST_CODE,
                            Activity.RESULT_OK,
                            Intent().apply {
                                putExtra(RESULT, Result.Neutral)
                            })
                    }
                }
                is Input.Proceed -> {
                    inputVal.title?.let {
                        builder.setTitle(it)
                    }
                    builder.setMessage(inputVal.message)
                    builder.setPositiveButton(inputVal.positiveButton) { _, _ ->
                        targetFragment?.onActivityResult(
                            REQUEST_CODE,
                            Activity.RESULT_OK,
                            Intent().apply {
                                putExtra(RESULT, Result.Positive)
                            }
                        )
                    }
                    builder.setNegativeButton(inputVal.negativeButton) { _, _ ->
                        targetFragment?.onActivityResult(
                            REQUEST_CODE,
                            Activity.RESULT_OK,
                            Intent().apply {
                                putExtra(RESULT, Result.Negative)
                            }
                        )
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

    private val input: Input by lazy { arguments!!.getSerializable(INPUT) as Input }

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
