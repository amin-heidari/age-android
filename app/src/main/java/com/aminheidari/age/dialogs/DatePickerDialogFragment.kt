package com.aminheidari.age.dialogs

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aminheidari.age.R
import com.aminheidari.age.fragments.BaseFragment
import com.aminheidari.age.fragments.NewAgeFragment
import com.aminheidari.age.models.BirthDate
import java.io.Serializable
import com.aminheidari.age.utils.Logger

class DatePickerDialogFragment : BaseDialogFragment() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        val requestCode: Int by lazy { DatePickerDialogFragment::class.hashCode() }

        private const val INITIAL_BIRTH_DATE = "INITIAL_BIRTH_DATE"
        const val PICKED_BIRTH_DATE = "PICKED_BIRTH_DATE"

        @JvmStatic
        fun showNewInstance(targetFragment: BaseFragment,
                            initialBirthDate: BirthDate
        ) {
            val fragment = DatePickerDialogFragment()

            val args = Bundle()
            args.putSerializable(INITIAL_BIRTH_DATE, initialBirthDate)
            fragment.arguments = args

            fragment.setTargetFragment(targetFragment, requestCode)

            // If the back button needs to be blocked.
            // https://stackoverflow.com/a/10171885
            fragment.isCancelable = false

            targetFragment.activity?.let { fragment.show(it.supportFragmentManager, AlertDialogFragment::class.java.canonicalName) }
        }

    }

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(context!!, onDateSetListener, initialBirthDate.year, initialBirthDate.month, initialBirthDate.day)
    }

    override fun onStart() {
        super.onStart()

        dialog.setCanceledOnTouchOutside(false)
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val initialBirthDate: BirthDate by lazy { arguments!!.getSerializable(INITIAL_BIRTH_DATE) as BirthDate }

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    private val onDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
//        Logger.d("lalala", String.format("Picked %d, %d, %d", year, month, day))
        targetFragment?.let { target ->
            target.onActivityResult(requestCode, Activity.RESULT_OK, Intent().apply { putExtra(PICKED_BIRTH_DATE, BirthDate(year, month, day)) })
        }
    }

    // endregion


}
