package com.aminheidari.age.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.vanniktech.rxbilling.RxBilling

const val INPUT = "INPUT"
const val RESULT = "RESULT"

fun Fragment.showFragment(
    fragment: Fragment,
    backStackBehaviour: BackStackBehaviour = BackStackBehaviour.Add,
    transactionAnimation: TransactionAnimation = TransactionAnimation.PushRight
) {
    activity?.showFragment(fragment, backStackBehaviour, transactionAnimation)
}

fun Fragment.popBackstack() {
    activity?.popBackStack()
}

val Fragment.rxBilling: RxBilling?
    get() = activity?.rxBilling

val Fragment.isAtLeastCreated: Boolean
    get() = lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)

val Fragment.isAtLeastStarted: Boolean
    get() = lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

val Fragment.isAtLeastResumed: Boolean
    get() = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)