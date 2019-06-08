package com.aminheidari.age.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminheidari.age.R
import com.aminheidari.age.config.RemoteConfigManager
import com.aminheidari.age.database.entities.BirthdayEntity
import com.aminheidari.age.dialogs.DatePickerDialogFragment
import com.aminheidari.age.models.Birthday
import com.aminheidari.age.utils.PreferencesUtil
import com.aminheidari.age.utils.addingTimerInterval
import com.aminheidari.age.utils.addingYears
import kotlinx.android.synthetic.main.fragment_new_age.*
import kotlinx.android.synthetic.main.fragment_new_age.view.*
import java.io.Serializable
import java.lang.IllegalStateException
import java.security.SecureRandom
import java.util.*

class NewAgeFragment : BaseFragment() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        private const val SCENARIO = "SCENARIO"

        val requestCode: Int by lazy { NewAgeFragment::class.hashCode() }

        @JvmStatic
        fun newInstance(scenario: Scenario, targetFragment: BaseFragment? = null): NewAgeFragment {
            val fragment = NewAgeFragment()

            val args = Bundle()
            args.putSerializable(SCENARIO, scenario)
            fragment.arguments = args

            targetFragment?.let { target ->
                fragment.setTargetFragment(target, requestCode)
            }

            return fragment
        }

        private fun evaluateDefaultBirthDate(): BirthDate {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -RemoteConfigManager.remoteConfig.ageSpecs.defaultAge)
            return BirthDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }

        private val DatePickerDialogFragment.Result.birthDate: BirthDate
            get() = BirthDate(year, month, day)
    }

    // endregion

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    sealed class Scenario: Serializable {
        object NewDefault: Scenario()
        object EditDefault: Scenario()
        object NewEntity: Scenario()
        data class EditEntity(val birthdayEntity: BirthdayEntity): Scenario()
    }

    data class BirthDate(val year: Int, val month: Int, val day: Int): Serializable

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_age, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (val currentScenario = scenario) {
            is Scenario.NewDefault -> {
                deleteButton.visibility = View.GONE
                editingBirthDate = evaluateDefaultBirthDate()
                nameEditText.requestFocus()
            }
            is Scenario.EditDefault -> {
                deleteButton.visibility = View.GONE
//                val birthday = PreferencesUtil.defaultBirthday!!
//                editingBirthDate = birthday.birthDate
//                nameEditText.setText(birthday.name)
            }
            is Scenario.NewEntity -> {
                deleteButton.visibility = View.GONE
//                editingBirthDate = Calendar.getInstance().time.addingYears(-RemoteConfigManager.remoteConfig.ageSpecs.defaultAge)
//                nameEditText.requestFocus()
            }
            is Scenario.EditEntity -> {
                deleteButton.visibility = View.VISIBLE
//                editingBirthDate = currentScenario.birthdayEntity.birth_date
//                nameEditText.setText(currentScenario.birthdayEntity.name)
            }
        }

        dateTextView.setOnClickListener(dateTextViewOnClickListener)

        nameEditText.addTextChangedListener(nameTextWatcher)

        proceedButton.setOnClickListener(proceedButtonOnClickListener)
        deleteButton.setOnClickListener(deleteButtonOnClickListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            DatePickerDialogFragment.requestCode -> {
                data?.let { data ->
                    (data.getSerializableExtra(DatePickerDialogFragment.RESULT) as? DatePickerDialogFragment.Result)?.let { result ->
                        editingBirthDate = result.birthDate
                    }
                }
            }
            else -> Unit
        }
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val scenario: Scenario by lazy {
        arguments!!.getSerializable(SCENARIO) as Scenario
    }

    private var _editingBirthDate: BirthDate? = null
    private var editingBirthDate: BirthDate?
        get() = _editingBirthDate
        set(value) {
            _editingBirthDate = value
            if (value != null) {
                dateTextView.text = String.format("%d - %d - %d", value.year, value.month, value.day)
            } else {
                dateTextView.text = null
            }
        }

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    private fun updateProceedButton() {
        proceedButton.isEnabled = !nameEditText.text.isEmpty()
    }

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    private val dateTextViewOnClickListener = View.OnClickListener {
        editingBirthDate?.let { bd ->
            DatePickerDialogFragment.showNewInstance(this, DatePickerDialogFragment.Input(bd.year, bd.month, bd.day))
        }
    }

    private val proceedButtonOnClickListener = View.OnClickListener {
        when (val currentScenario = scenario) {
            is Scenario.NewDefault -> {
//                PreferencesUtil.defaultBirthday = Birthday(editingBirthDate)
            }
            is Scenario.EditDefault -> {
                throw NotImplementedError()
            }
            is Scenario.NewEntity -> {
                throw NotImplementedError()
            }
            is Scenario.EditEntity -> {
                throw NotImplementedError()
            }
        }
    }

    private val deleteButtonOnClickListener = View.OnClickListener {

    }

    private val nameTextWatcher = object: TextWatcher {

        override fun afterTextChanged(p0: Editable?) {
            updateProceedButton()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

    }

    // endregion

}
