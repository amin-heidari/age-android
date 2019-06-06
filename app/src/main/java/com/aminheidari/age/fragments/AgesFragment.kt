package com.aminheidari.age.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminheidari.age.R
import com.aminheidari.age.models.Birthday
import com.aminheidari.age.utils.ItemBinder
import kotlinx.android.synthetic.main.fragment_ages.*
import java.lang.IllegalStateException
import java.lang.UnsupportedOperationException

class AgesFragment : BaseFragment() {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    companion object {

        private const val TMP_COUNT = 30

        private const val ITEM_AGE = 0
        private const val ITEM_ADD_AGE = 1

        @JvmStatic
        fun newInstance() = AgesFragment()

    }

    internal class Adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount() = TMP_COUNT + 1

        override fun getItemViewType(position: Int): Int {
            return if (position < TMP_COUNT) ITEM_AGE else ITEM_ADD_AGE
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                ITEM_AGE -> AgeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_age, parent, false))
                ITEM_ADD_AGE -> AddAgeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_add_age, parent, false))
                else -> { throw IllegalStateException("viewType not supported!") }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//            (holder as? ItemBinder<Any>)?.bind()
        }

        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            super.onViewAttachedToWindow(holder)
        }

        override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
            super.onViewDetachedFromWindow(holder)
        }

    }

    internal class AgeViewHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener, ItemBinder<Birthday> {

        override fun bind(item: Birthday) { }

        override fun onClick(p0: View?) { }

    }

    internal class AddAgeViewHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener, ItemBinder<Birthday> {

        override fun bind(item: Birthday) { }

        override fun onClick(p0: View?) { }

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
        return inflater.inflate(R.layout.fragment_ages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = Adapter()
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
    // region Actions
    // ====================================================================================================

    // endregion

}
