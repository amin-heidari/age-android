package com.aminheidari.age.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.DatePicker
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
import kotlin.properties.Delegates

class NewAgeFragment : BaseFragment() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        private const val SCENARIO = "SCENARIO"

        const val REQUEST_CODE: Int = 10

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
                titleTextView.text = "Enter your Age"
            }
            is Scenario.EditDefault -> {
                deleteButton.visibility = View.GONE
                val birthday = PreferencesUtil.defaultBirthday!!
                editingBirthDate = birthday.birthDate
                nameEditText.setText(birthday.name)
                titleTextView.text = "Your Age"
            }
            is Scenario.NewEntity -> {
                deleteButton.visibility = View.GONE
                editingBirthDate = evaluateDefaultBirthDate()
                titleTextView.text = "New Age"
            }
            is Scenario.EditEntity -> {
                deleteButton.visibility = View.VISIBLE
                editingBirthDate = currentScenario.birthdayEntity.birthday.birthDate
                nameEditText.setText(currentScenario.birthdayEntity.name)
                titleTextView.text = "Edit Age"
            }
        }

        dateEditText.setOnClickListener(dateTextViewOnClickListener)

        nameEditText.addTextChangedListener(nameTextWatcher)

        proceedButton.setOnClickListener(proceedButtonOnClickListener)
        deleteButton.setOnClickListener(deleteButtonOnClickListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            DatePickerDialogFragment.REQUEST_CODE -> {
                editingBirthDate = data?.getSerializableExtra(RESULT) as? BirthDate
            }
            else -> Unit
        }
    }

    override fun onStart() {
        super.onStart()

        updateProceedButton()
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
                dateEditText.setText(String.format("%d - %d - %d", value.year, value.month, value.day))
            } else {
                dateEditText.text = null
            }
        }

    private var isProcessing: Boolean by Delegates.observable(false) { _, _, newValue ->
        if (newValue) {
            nameEditText.isEnabled = false
            dateEditText.isEnabled = false
            proceedButton.isEnabled = false
            deleteButton.isEnabled = false
            progressBar.visibility = View.VISIBLE
        } else {
            nameEditText.isEnabled = true
            dateEditText.isEnabled = true
            proceedButton.isEnabled = true
            deleteButton.isEnabled = true
            progressBar.visibility = View.GONE
        }
    }

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    override fun handleBackPressed(listener: OnNavigateAwayListener): Boolean {
        return isProcessing
    }

    private fun updateProceedButton() {
        proceedButton.isEnabled = nameEditText.text.toString().isNotEmpty()
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
            val todayBirthDate = Calendar.getInstance().time.birthDate
            DatePickerDialogFragment.showNewInstance(
                this,
                DatePickerDialogFragment.Input(
                    bd,
                    todayBirthDate,
                    BirthDate(todayBirthDate.year - RemoteConfigManager.remoteConfig.ageSpecs.maxAge, todayBirthDate.month, todayBirthDate.day)
                )
            )
        }
    }

    private val proceedButtonOnClickListener = View.OnClickListener {
        val birthday = Birthday(editingBirthDate!!, nameEditText.text.toString())

        when (val currentScenario = scenario) {
            is Scenario.NewDefault -> {
                PreferencesUtil.defaultBirthday = birthday

                showFragment(AgeFragment.newInstance(), BackStackBehaviour.Wipe)
            }
            is Scenario.EditDefault -> {
                PreferencesUtil.defaultBirthday = birthday

                finishWithResult(Result.UpdatedDefault)
            }
            is Scenario.NewEntity -> {
                isProcessing = true
                DatabaseManager.addBirthday(birthday) {
                    finishWithResult(Result.ModifiedEntities)
                }
            }
            is Scenario.EditEntity -> {
                isProcessing = true
                DatabaseManager.updateBirthday(currentScenario.birthdayEntity, birthday) {
                    finishWithResult(Result.ModifiedEntities)
                }
            }
        }
    }

    private val deleteButtonOnClickListener = View.OnClickListener {
        (scenario as? Scenario.EditEntity)?.let { editEntityScenario ->
            isProcessing = true
            DatabaseManager.deleteBirthday(editEntityScenario.birthdayEntity) {
                finishWithResult(Result.ModifiedEntities)
            }
        }
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
