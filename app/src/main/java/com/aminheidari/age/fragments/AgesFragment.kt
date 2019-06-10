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

        private const val ITEM_MY_AGE = 0
        private const val ITEM_AGE = 1
        private const val ITEM_ADD_AGE = 2

        @JvmStatic
        fun newInstance() = AgesFragment()

    }

    inner class Adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount(): Int {
            val birthdays = DatabaseManager.birthdays.value
            return if (birthdays == null) {
                2
            } else {
                birthdays.size + 2
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when {
                position == 0 -> ITEM_MY_AGE
                position < itemCount-1 -> ITEM_AGE
                else -> ITEM_ADD_AGE
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ITEM_MY_AGE -> MyAgeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_age, parent, false))
                ITEM_AGE -> AgeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_age, parent, false))
                ITEM_ADD_AGE -> AddAgeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_add_age, parent, false))
                else -> { throw IllegalStateException("viewType not supported!") }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is MyAgeViewHolder) {
                PreferencesUtil.defaultBirthday?.let { bd ->
                    holder.bind(bd)
                }
            } else if (holder is AgeViewHolder) {
                DatabaseManager.birthdays.value?.let { birthdays ->
                    holder.bind(birthdays[position-1].birthday)
                }
            }
        }

        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            super.onViewAttachedToWindow(holder)
        }

        override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
            super.onViewDetachedFromWindow(holder)
        }

    }

    inner class MyAgeViewHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener, ItemBinder<Birthday> {

        override fun bind(item: Birthday) { }

        override fun onClick(p0: View?) { }

    }

    inner class AgeViewHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener, ItemBinder<Birthday> {

        override fun bind(item: Birthday) { }

        override fun onClick(p0: View?) { }

    }

    inner class AddAgeViewHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener {

        override fun onClick(p0: View?) { }

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

        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = AgesAdapter(this)

        DatabaseManager.birthdays.observe(this, birthdaysObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        DatabaseManager.birthdays.removeObservers(this)
    }

    override fun onStart() {
        super.onStart()

        (recyclerView.adapter as? AgesAdapter)?.items = listOf(AgesAdapter.Item.AddAge)
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val birthdaysObserver = Observer<List<BirthdayEntity>> { birthdays ->
        recyclerView.adapter?.notifyDataSetChanged()
    }

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

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
                showFragment(NewAgeFragment.newInstance(NewAgeFragment.Scenario.EditDefault), BackStackBehaviour.Add, TransactionAnimation.PresentBottom)
            }
            is AgesAdapter.Item.Age -> {
                showFragment(NewAgeFragment.newInstance(NewAgeFragment.Scenario.EditEntity(item.birthdayEntity)), BackStackBehaviour.Add, TransactionAnimation.PresentBottom)
            }
            is AgesAdapter.Item.AddAge -> {
                showFragment(NewAgeFragment.newInstance(NewAgeFragment.Scenario.NewEntity), BackStackBehaviour.Add, TransactionAnimation.PresentBottom)
            }
        }
    }

    // endregion

}
