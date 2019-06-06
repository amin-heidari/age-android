package com.aminheidari.age.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
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
    Fade, // Simple fading.
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

        // This is also an option for transitions, but seems limited:
        // https://stackoverflow.com/a/37076187
        // https://developer.android.com/reference/android/support/v4/app/FragmentTransaction.html#settransition
        // https://stackoverflow.com/a/11695582

        when (transactionAnimation) {
            TransactionAnimation.None -> { transaction.setCustomAnimations(0 ,0) }
            TransactionAnimation.Fade -> { transaction.setCustomAnimations(R.anim.fragment_anim_none, R.anim.fragment_fade_out, R.anim.fragment_anim_none, R.anim.fragment_fade_out) }
            TransactionAnimation.PushRight -> { transaction.setCustomAnimations(R.anim.fragment_new_in_right, R.anim.fragment_old_out_left, R.anim.fragment_old_in_left, R.anim.fragment_new_out_right) }
            TransactionAnimation.PresentBottom -> { transaction.setCustomAnimations(R.anim.fragment_new_in_bottom, R.anim.fragment_old_out_top, R.anim.fragment_old_in_top, R.anim.fragment_new_out_bottom) }
        }

        transaction.replace(R.id.fragmentContainer, fragment)

        if (backStackBehaviour == BackStackBehaviour.Add) {
            transaction.addToBackStack(fragment::class.java.canonicalName)
        }

        transaction.commit()
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

