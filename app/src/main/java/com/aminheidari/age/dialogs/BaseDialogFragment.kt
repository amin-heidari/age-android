package com.aminheidari.age.dialogs

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.aminheidari.age.R
import com.aminheidari.age.utils.Logger


abstract class BaseDialogFragment : DialogFragment() {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region API
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        Logger.v(this::class.java.canonicalName, String.format("%d -> onAttach.", hashCode()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.v(this::class.java.canonicalName, String.format("%d -> onCreate.", hashCode()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.v(this::class.java.canonicalName, String.format("%d -> onCreateView.", hashCode()))

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Logger.v(this::class.java.canonicalName, String.format("%d -> onViewCreated.", hashCode()))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        Logger.v(this::class.java.canonicalName, String.format("%d -> onViewStateRestored.", hashCode()))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Logger.v(this::class.java.canonicalName, String.format("%d -> onActivityCreated.", hashCode()))
    }

    override fun onStart() {
        super.onStart()

        Logger.v(this::class.java.canonicalName, String.format("%d -> onStart.", hashCode()))
    }

    override fun onResume() {
        super.onResume()

        Logger.v(this::class.java.canonicalName, String.format("%d -> onResume.", hashCode()))
    }

    override fun onPause() {
        super.onPause()

        Logger.v(this::class.java.canonicalName, String.format("%d -> onPause.", hashCode()))
    }

    override fun onStop() {
        super.onStop()

        Logger.v(this::class.java.canonicalName, String.format("%d -> onStop.", hashCode()))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        Logger.v(this::class.java.canonicalName, String.format("%d -> onDestroyView.", hashCode()))
    }

    override fun onDestroy() {
        super.onDestroy()

        Logger.v(this::class.java.canonicalName, String.format("%d -> onDestroy.", hashCode()))
    }

    override fun onDetach() {
        super.onDetach()

        Logger.v(this::class.java.canonicalName, String.format("%d -> onDetach.", hashCode()))
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
