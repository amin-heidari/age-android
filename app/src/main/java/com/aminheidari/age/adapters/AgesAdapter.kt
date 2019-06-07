package com.aminheidari.age.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aminheidari.age.R
import com.aminheidari.age.database.DatabaseManager
import com.aminheidari.age.database.entities.BirthdayEntity
import com.aminheidari.age.fragments.AgesFragment
import com.aminheidari.age.models.Birthday
import com.aminheidari.age.utils.ItemBinder
import com.aminheidari.age.utils.OnItemSelectListener
import com.aminheidari.age.utils.PreferencesUtil
import com.aminheidari.age.utils.birthday
import kotlin.properties.Delegates

class AgesAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    companion object {
        private const val MY_AGE = 0
        private const val AGE = 1
        private const val ADD_AGE = 2
    }

    sealed class Item {
        data class MyAge(val birthday: Birthday): Item()
        data class Age(val birthdayEntity: BirthdayEntity): Item()
        object AddAge: Item()

        val viewType: Int
            get() {
                return when (this) {
                    is MyAge -> MY_AGE
                    is Age -> AGE
                    is AddAge -> ADD_AGE
                }
            }
    }

    inner class MyAgeViewHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener, ItemBinder<Birthday> {

        override fun bind(item: Birthday) { }

        override fun onClick(p0: View?) { }

    }

    inner class AgeViewHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener, ItemBinder<BirthdayEntity> {

        override fun bind(item: BirthdayEntity) { }

        override fun onClick(p0: View?) { }

    }

    inner class AddAgeViewHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener {

        override fun onClick(p0: View?) {
            listener?.onSelected(Item.AddAge)
        }

    }

    private data class ItemsDiffCallback(val oldItems: List<Item>, val newItems: List<Item>): DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = compareItems(oldItems[oldItemPosition], newItems[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = compareContent(oldItems[oldItemPosition], newItems[newItemPosition])

        private fun compareItems(oldItem: Item, newItem: Item): Boolean {
            return when (oldItem) {
                is Item.MyAge -> newItem is Item.MyAge
                is Item.Age -> newItem is Item.Age
                is Item.AddAge -> newItem is Item.AddAge
            }
        }

        private fun compareContent(oldItem: Item, newItem: Item): Boolean {
            return when (oldItem) {
                is Item.MyAge -> {
                    when (newItem) {
                        is Item.MyAge -> (oldItem.birthday == newItem.birthday)
                        else -> false
                    }
                }
                is Item.Age -> {
                    when (newItem) {
                        is Item.Age -> (oldItem.birthdayEntity == newItem.birthdayEntity)
                        else -> false
                    }
                }
                else -> true
            }
        }

    }

    // endregion

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region API
    // ====================================================================================================

    var listener: OnItemSelectListener<Item>? = null

    var items: List<Item> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        DiffUtil.calculateDiff(ItemsDiffCallback(oldValue, newValue)).dispatchUpdatesTo(this)
    }

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region RecyclerView.Adapter
    // ====================================================================================================

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MY_AGE -> MyAgeViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_age,
                    parent,
                    false
                )
            )
            AGE -> AgeViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_age,
                    parent,
                    false
                )
            )
            ADD_AGE -> AddAgeViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_add_age,
                    parent,
                    false
                )
            )
            else -> { throw IllegalStateException("viewType not supported!") }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (item) {
            is Item.MyAge -> {
                (holder as? MyAgeViewHolder)?.bind(item.birthday)
            }
            is Item.Age -> {
                (holder as? AgeViewHolder)?.bind(item.birthdayEntity)
            }
            else -> Unit
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
    }

    // endregion

}