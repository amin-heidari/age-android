package com.aminheidari.age.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import com.aminheidari.age.R
import com.aminheidari.age.config.RemoteConfigManager
import com.aminheidari.age.database.DatabaseManager
import com.aminheidari.age.database.entities.BirthdayEntity
import com.aminheidari.age.dialogs.DatePickerDialogFragment
import com.aminheidari.age.models.BirthDate
import com.aminheidari.age.models.Birthday
import com.aminheidari.age.utils.*
import kotlinx.android.synthetic.main.fragment_new_age.*
import java.io.Serializable
import java.util.*

class NewAgeFragment : BaseFragment() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        private const val SCENARIO = "SCENARIO"

        val REQUEST_CODE: Int by lazy { NewAgeFragment::class.hashCode() }

        @JvmStatic
        fun newInstance(scenario: Scenario, targetFragment: BaseFragment? = null): NewAgeFragment {
            val fragment = NewAgeFragment()

            val args = Bundle()
            args.putSerializable(SCENARIO, scenario)
            fragment.arguments = args

            targetFragment?.let { target ->
                fragment.setTargetFragment(target, REQUEST_CODE)
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

    sealed class Result: Serializable {
        // When the default age is modified (Note that adding the new age happens only once so no reason to have a `Result` case for it).
        object UpdatedDefault: Result()
        // Covers the addition/removal/edit of the other ages.
        object ModifiedEntities: Result()
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
        return inflater.inflate(R.layout.fragment_new_age, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameEditText.customSelectionActionModeCallback = object: ActionMode.Callback {
            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean = false
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean = false
            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean = false
            override fun onDestroyActionMode(p0: ActionMode?) { }
        }

        when (val currentScenario = scenario) {
            is Scenario.NewDefault -> {
                deleteButton.visibility = View.GONE
                editingBirthDate = evaluateDefaultBirthDate()
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
                    (data.getSerializableExtra(DatePickerDialogFragment.PICKED_BIRTH_DATE) as? BirthDate)?.let { result ->
                        editingBirthDate = result
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
        proceedButton.isEnabled = nameEditText.text.isNotEmpty()
    }

    private fun finishWithResult(result: Result) {
        targetFragment?.onActivityResult(
            REQUEST_CODE,
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(RESULT, result)
            })
        popBackstack()
    }

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    private val dateTextViewOnClickListener = View.OnClickListener {
        editingBirthDate?.let { bd ->
            DatePickerDialogFragment.showNewInstance(this, bd)
        }
    }

    private val proceedButtonOnClickListener = View.OnClickListener {
        when (val currentScenario = scenario) {
            is Scenario.NewDefault -> {
                PreferencesUtil.defaultBirthday = Birthday(editingBirthDate!!, nameEditText.text.toString())

                showFragment(AgeFragment.newInstance(), BackStackBehaviour.Wipe)
            }
            is Scenario.EditDefault -> {
                PreferencesUtil.defaultBirthday = Birthday(editingBirthDate!!, nameEditText.text.toString())

                finishWithResult(Result.UpdatedDefault)
            }
            is Scenario.NewEntity -> {
                // TODO: Make the db update.

                finishWithResult(Result.ModifiedEntities)
            }
            is Scenario.EditEntity -> {
                // TODO: Show loading state on UI.
                DatabaseManager.deleteBirthday(currentScenario.birthdayEntity) {
                    finishWithResult(Result.ModifiedEntities)
                }
            }
        }
    }

    private val deleteButtonOnClickListener = View.OnClickListener {
        // TODO: Make the db update.

        finishWithResult(Result.ModifiedEntities)
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
