package com.aminheidari.age.fragments

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
import com.aminheidari.age.models.Birthday
import com.aminheidari.age.utils.PreferencesUtil
import com.aminheidari.age.utils.addingTimerInterval
import com.aminheidari.age.utils.addingYears
import kotlinx.android.synthetic.main.fragment_new_age.*
import java.io.Serializable
import java.lang.IllegalStateException
import java.util.*

class NewAgeFragment : BaseFragment() {

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    companion object {

        private const val SCENARIO = "SCENARIO"

        @JvmStatic
        fun newInstance(scenario: Scenario): NewAgeFragment {
            val fragment = NewAgeFragment()
            val args = Bundle()
            args.putSerializable(SCENARIO, scenario)
            fragment.arguments = args
            return fragment
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
                editingBirthDate = Calendar.getInstance().time.addingYears(-RemoteConfigManager.remoteConfig.ageSpecs.defaultAge)
                nameEditText.requestFocus()
            }
            is Scenario.EditDefault -> {
                deleteButton.visibility = View.GONE
                val birthday = PreferencesUtil.defaultBirthday!!
                editingBirthDate = birthday.birthDate
                nameEditText.setText(birthday.name)
            }
            is Scenario.NewEntity -> {
                deleteButton.visibility = View.GONE
                editingBirthDate = Calendar.getInstance().time.addingYears(-RemoteConfigManager.remoteConfig.ageSpecs.defaultAge)
                nameEditText.requestFocus()
            }
            is Scenario.EditEntity -> {
                deleteButton.visibility = View.VISIBLE
                editingBirthDate = currentScenario.birthdayEntity.birth_date
                nameEditText.setText(currentScenario.birthdayEntity.name)
            }
        }

        nameEditText.addTextChangedListener(nameTextWatcher)

        proceedButton.setOnClickListener(proceedButtonOnClickListener)
        deleteButton.setOnClickListener(deleteButtonOnClickListener)
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val scenario: Scenario by lazy {
        val args = arguments ?: throw IllegalStateException("Arguments should not be null!")
        args.getSerializable(SCENARIO) as Scenario
    }

    private var editingBirthDate: Date? = null

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
