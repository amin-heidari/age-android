package com.aminheidari.age.dialogs

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aminheidari.age.R
import com.aminheidari.age.config.RemoteConfigManager
import com.aminheidari.age.constants.Constants
import com.aminheidari.age.fragments.BaseFragment
import com.aminheidari.age.fragments.NewAgeFragment
import com.aminheidari.age.models.BirthDate
import com.aminheidari.age.utils.INPUT
import java.io.Serializable
import com.aminheidari.age.utils.Logger
import com.aminheidari.age.utils.RESULT
import com.aminheidari.age.utils.addingYears
import java.util.*

class DatePickerDialogFragment : BaseDialogFragment() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        const val REQUEST_CODE: Int = 20

        @JvmStatic
        fun showNewInstance(targetFragment: BaseFragment,
                            input: Input
        ) {
            val fragment = DatePickerDialogFragment()

            val args = Bundle()
            args.putSerializable(INPUT, input)
            fragment.arguments = args

            fragment.setTargetFragment(targetFragment, REQUEST_CODE)

            targetFragment.activity?.let { fragment.show(it.supportFragmentManager, AlertDialogFragment::class.java.canonicalName) }
        }

    }

    // endregion

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    data class Input(
        val initialDate: BirthDate,
        val minDate: BirthDate,
        val maxDate: BirthDate
    ): Serializable

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

        // If the back button needs to be blocked.
        // https://stackoverflow.com/a/10171885
        isCancelable = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = DatePickerDialog(context!!, onDateSetListener, input.initialDate.year, input.initialDate.month, input.initialDate.day)
        dialog.datePicker.maxDate = input.minDate.date.time
        dialog.datePicker.minDate = input.maxDate.date.time
        return dialog
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

    private val onDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        targetFragment?.let { target ->
            target.onActivityResult(REQUEST_CODE, Activity.RESULT_OK, Intent().apply { putExtra(RESULT, BirthDate(year, month, day)) })
        }
    }

    // endregion


}
