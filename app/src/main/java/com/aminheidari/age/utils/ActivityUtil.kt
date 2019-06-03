package com.aminheidari.age.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import com.aminheidari.age.R

/**
 * The way
 */
enum class BackStackBehaviour {
    None, // Does not affect the backstack.
    Add, // Adds the txn to the backstack.
    Wipe // Wipes the entire backstack and installs this txn at the root.
}

enum class TransactionAnimation {
    None, // No animation.
    PushRight, // Push the new fragment from right side.
    PresentBottom, // Present the new fragment from bottom.
}

fun Activity.showFragment(
    fragment: Fragment,
    backStackBehaviour: BackStackBehaviour = BackStackBehaviour.Add,
    transactionAnimation: TransactionAnimation = TransactionAnimation.PushRight
) {
    if (this is AppCompatActivity) {
        if (backStackBehaviour == BackStackBehaviour.Wipe) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        val transaction = supportFragmentManager.beginTransaction()
        when (transactionAnimation) {
            TransactionAnimation.None -> { transaction.setCustomAnimations(R.anim.fragment_enter_right, R.anim.fragment_exit_left, R.anim.fragment_enter_left, R.anim.fragment_exit_right) }
            TransactionAnimation.PushRight -> { transaction.setCustomAnimations(R.anim.fragment_enter_right, R.anim.fragment_exit_left, R.anim.fragment_enter_left, R.anim.fragment_exit_right) }
            TransactionAnimation.PresentBottom -> { transaction.setCustomAnimations(R.anim.fragment_enter_right, R.anim.fragment_exit_left, R.anim.fragment_enter_left, R.anim.fragment_exit_right) }
        }

//        if (animate) { transaction.setCustomAnimations(R.anim.fragment_enter_right, R.anim.fragment_exit_left, R.anim.fragment_enter_left, R.anim.fragment_exit_right) }

//        transaction.replace(R.id.fragmentContainer, fragment)
//        if (addToBackStack) { transaction.addToBackStack(fragment::class.java.canonicalName) }
//        transaction.commit()
    }
}

fun Activity.containsInBackStack(name: String): Boolean {
    if (this is AppCompatActivity) {
        for (ind in 0 until supportFragmentManager.backStackEntryCount) {
            if (supportFragmentManager.getBackStackEntryAt(ind).name.equals(name)) { return true }
        }
        return false
    }
    return false
}

