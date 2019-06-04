package com.aminheidari.age.fragments

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aminheidari.age.R
import com.aminheidari.age.utils.BackStackBehaviour
import com.aminheidari.age.utils.Logger
import com.aminheidari.age.utils.TransactionAnimation
import com.aminheidari.age.utils.showFragment
import kotlinx.android.synthetic.main.fragment_loading.*
import java.util.*
import kotlin.math.absoluteValue

class LoadingFragment : BaseFragment() {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    companion object {

        @JvmStatic
        fun newInstance() = LoadingFragment()

        val random = Random()

    }

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        when (random.nextInt().absoluteValue.rem(8)) {
            0 -> bgColor = "#FF82AB"
            1 -> bgColor = "#9B30FF"
            2 -> bgColor = "#1E90FF"
            3 -> bgColor = "#00F5FF"
            4 -> bgColor = "#54FF9F"
            5 -> bgColor = "#FFD700"
            6 -> bgColor = "#FF8000"
            7 -> bgColor = "#FF3030"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.v(this::class.java.canonicalName, String.format("%d -> onCreateView.", hashCode()))
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundColor(Color.parseColor(bgColor))

        actionButton.setOnClickListener {
//            activity!!.showFragment(newInstance())
//            if (random.nextBoolean()) {
//
//            } else {
                activity!!.showFragment(newInstance(), BackStackBehaviour.Add, TransactionAnimation.PresentBottom)
//            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private var bgColor = ""

    // endregion

}
