package com.aminheidari.age.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aminheidari.age.R
import com.aminheidari.age.fragments.BaseFragment
import com.aminheidari.age.fragments.NewAgeFragment
import java.io.Serializable
import com.aminheidari.age.utils.Logger

class DatePickerDialogFragment : BaseDialogFragment() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        val requestCode: Int by lazy { DatePickerDialogFragment::class.hashCode() }

        private const val INPUT = "INPUT"
        const val RESULT = "RESULT"

        @JvmStatic
        fun showNewInstance(targetFragment: BaseFragment,
                            input: Input)
        {
            val fragment = DatePickerDialogFragment()

            val args = Bundle()
            args.putSerializable(INPUT, input)
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
    // region Constants/Types
    // ====================================================================================================

    data class Input(val year: Int, val month: Int, val day: Int): Serializable

    data class Result(val year: Int, val month: Int, val day: Int): Serializable

    // endregion

    // ====================================================================================================
    // region API
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(context!!, onDateSetListener, input.year, input.month, input.day)
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    override val allowBackgroundDismiss: Boolean
        get() = false

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
        Logger.d("lalala", String.format("Picked %d, %d, %d", year, month, day))
    }

    // endregion


}
