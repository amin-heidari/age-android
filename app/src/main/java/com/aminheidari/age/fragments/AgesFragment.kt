package com.aminheidari.age.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminheidari.age.R
import com.aminheidari.age.adapters.AgesAdapter
import com.aminheidari.age.database.DatabaseManager
import com.aminheidari.age.database.entities.BirthdayEntity
import com.aminheidari.age.models.Birthday
import com.aminheidari.age.utils.*
import kotlinx.android.synthetic.main.fragment_ages.*
import java.lang.IllegalStateException

class AgesFragment : BaseFragment(), OnItemSelectedListener<AgesAdapter.Item> {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    companion object {

        @JvmStatic
        fun newInstance() = AgesFragment()

    }

    // endregion

    // ====================================================================================================
    // region API
    // ====================================================================================================

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        synchronized(this) {
            if (recyclerView.adapter == null) {
                recyclerView.layoutManager = LinearLayoutManager(view.context)
                recyclerView.adapter = AgesAdapter(this)
            }
        }

        DatabaseManager.birthdays.observe(this, birthdaysObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        DatabaseManager.birthdays.removeObservers(this)
    }

    override fun onStart() {
        super.onStart()

        (recyclerView.adapter as? AgesAdapter)?.apply {
            items = adapterItems
            isRecyclerViewVisible = true
        }
    }

    override fun onStop() {
        super.onStop()

        (recyclerView.adapter as? AgesAdapter)?.apply {
            isRecyclerViewVisible = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            NewAgeFragment.REQUEST_CODE -> {
                (data?.getSerializableExtra(RESULT) as? NewAgeFragment.Result)?.let { result ->
                    // Of course I could have created yet another live data for the default birthday,
                    // with a fancy mediator live data to combine that with the entities.
                    // But not worth my time.
                    // Besides, I'm sure the SharedPreferences is pretty efficient in not doing IO when
                    // the value has not changed so we shouldn't miss even a single screen refresh cycle.

                    // It's all fancy and amazing that you have created this, but cannot make UI update here (See issue AGE-42),
                    // and can only update properties and values.

                    when (result) {
                        is NewAgeFragment.Result.UpdatedDefault -> {
                            refreshAdapterItems()
                        }
                        else -> Unit // Because we'll get a hit on `birthdaysObserver` below.
                    }
                }
            }
        }
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val birthdaysObserver = Observer<List<BirthdayEntity>> { _ ->
        refreshAdapterItems()
    }

    private var adapterItems: List<AgesAdapter.Item> = listOf(AgesAdapter.Item.AddAge)

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    private fun refreshAdapterItems() {
        adapterItems = evaluateAdapterItems()

        // This is to make sure there is no race condition between `birthdaysObserver` and `onStart`.
        if (isVisible) {
            (recyclerView.adapter as? AgesAdapter)?.items = adapterItems
        }
    }

    private fun evaluateAdapterItems(): List<AgesAdapter.Item> {
        val list = mutableListOf<AgesAdapter.Item>(AgesAdapter.Item.MyAge(PreferencesUtil.defaultBirthday!!))

        DatabaseManager.birthdays.value?.let { birthdays ->
            list.addAll(birthdays.map { AgesAdapter.Item.Age(it) })
        }

        list.add(AgesAdapter.Item.AddAge)

        return list
    }

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region OnItemSelectedListener<AgesAdapter.Item>
    // ====================================================================================================

    override fun onItemSelected(item: AgesAdapter.Item) {
        when(item) {
            is AgesAdapter.Item.MyAge -> {
                showFragment(NewAgeFragment.newInstance(NewAgeFragment.Scenario.EditDefault, this), BackStackBehaviour.Add, TransactionAnimation.PresentBottom)
            }
            is AgesAdapter.Item.Age -> {
                showFragment(NewAgeFragment.newInstance(NewAgeFragment.Scenario.EditEntity(item.birthdayEntity), this), BackStackBehaviour.Add, TransactionAnimation.PresentBottom)
            }
            is AgesAdapter.Item.AddAge -> {
                showFragment(NewAgeFragment.newInstance(NewAgeFragment.Scenario.NewEntity, this), BackStackBehaviour.Add, TransactionAnimation.PresentBottom)
            }
        }
    }

    // endregion

}
