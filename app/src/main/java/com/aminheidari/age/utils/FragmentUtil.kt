package com.aminheidari.age.utils

import androidx.fragment.app.Fragment

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