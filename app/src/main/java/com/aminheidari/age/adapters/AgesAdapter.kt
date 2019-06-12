package com.aminheidari.age.adapters

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aminheidari.age.R
import com.aminheidari.age.calculator.AgeCalculator
import com.aminheidari.age.database.entities.BirthdayEntity
import com.aminheidari.age.models.Birthday
import com.aminheidari.age.utils.ItemBinder
import com.aminheidari.age.utils.Logger
import com.aminheidari.age.utils.OnItemSelectedListener
import com.aminheidari.age.utils.birthday
import kotlin.properties.Delegates

class AgesAdapter(val onItemSelectedListener: OnItemSelectedListener<Item>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    inner class MyAgeViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener, ItemBinder<Birthday>, AgeRefresher {
        private val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        private val birthdayTextView: TextView = view.findViewById(R.id.birthdayTextView)
        private val defaultTextView: TextView = view.findViewById(R.id.defaultTextView)

        private var ageCalculator: AgeCalculator? = null

        init {
            view.setOnClickListener(this)
        }

        override fun bind(item: Birthday) {
            nameTextView.text = item.name
            birthdayTextView.text = String.format("%d - %d - %d", item.birthDate.year, item.birthDate.month, item.birthDate.day)
            defaultTextView.visibility = View.VISIBLE

            ageCalculator = AgeCalculator(item.birthDate)
        }

        override fun onClick(view: View?) {
            onItemSelectedListener.onItemSelected(items[adapterPosition])
        }

        private var _isRefreshingAge: Boolean = false
        override var isRefreshingAge: Boolean
            get() = _isRefreshingAge
            set(value) {
                _isRefreshingAge = value

                if (value) {
                    refreshAge()
                }
            }

        private fun refreshAge() {
            if (isRefreshingAge && isRecyclerViewVisible) {
                birthdayTextView.text = ageCalculator?.currentAge?.full
                Handler().postDelayed({
                    refreshAge()
                }, 1)
            }
        }

    }

    inner class AgeViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener, ItemBinder<BirthdayEntity> {
        private val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        private val birthdayTextView: TextView = view.findViewById(R.id.birthdayTextView)
        private val defaultTextView: TextView = view.findViewById(R.id.defaultTextView)

        init {
            view.setOnClickListener(this)
        }

        override fun bind(item: BirthdayEntity) {
            val birthday = item.birthday
            nameTextView.text = birthday.name
            birthdayTextView.text = String.format("%d - %d - %d", birthday.birthDate.year, birthday.birthDate.month, birthday.birthDate.day)
            defaultTextView.visibility = View.GONE
        }

        override fun onClick(view: View?) {
            onItemSelectedListener.onItemSelected(items[adapterPosition])
        }

    }

    inner class AddAgeViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            onItemSelectedListener.onItemSelected(items[adapterPosition])
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

    private interface AgeRefresher {
        var isRefreshingAge: Boolean
    }

    // endregion

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region API
    // ====================================================================================================

    var items: List<Item> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        DiffUtil.calculateDiff(ItemsDiffCallback(oldValue, newValue)).dispatchUpdatesTo(this)
    }

    /**
     * Looks like I have no choice but to make this adapter a little bit life cycle aware.
     */
    var isRecyclerViewVisible: Boolean = false

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)

//        Logger.d("lalala", String.format("onViewAttachedToWindow -> %d", holder.hashCode()))

        (holder as? AgeRefresher)?.isRefreshingAge = true
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)

//        Logger.d("lalala", String.format("onViewDetachedFromWindow -> %d", holder.hashCode()))

        (holder as? AgeRefresher)?.isRefreshingAge = false
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

//        isAttachedToRecyclerView = true

//        Logger.d("lalala", String.format("onAttachedToRecyclerView -> %d", this.hashCode()))
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

//        Logger.d("lalala", String.format("onDetachedFromRecyclerView -> %d", this.hashCode()))
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)

//        Logger.d("lalala", String.format("onViewRecycled -> %d", holder.hashCode()))
    }

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

    // endregion

}