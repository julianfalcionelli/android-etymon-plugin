/*
 * Created by Julián Falcionelli on 2019.
 * Copyright © 2019 Bardo (bybardo.co). All rights reserved.
 * Happy Coding !
 */

package co.bybardo.myapp.ui.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import co.bybardo.myapp.R

fun FragmentManager.addFragment(
    container: Int,
    fragment: Fragment,
    tag: String,
    addToBackStack: Boolean = false,
    commitAllowingStateLoss: Boolean = false
) {
    var fragmentTransaction = beginTransaction()
        .replace(container, fragment, tag)

    if (addToBackStack) {
        fragmentTransaction.addToBackStack(null)
    }

    if (commitAllowingStateLoss) {
        fragmentTransaction.commitAllowingStateLoss()
    } else {
        fragmentTransaction.commit()
    }
}

fun FragmentManager.addFragmentWithSlideInOutAnimations(
    container: Int,
    fragment: Fragment,
    tag: String,
    addToBackStack: Boolean = false,
    commitAllowingStateLoss: Boolean = false
) {
    var fragmentTransaction = beginTransaction()

    fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left,
        R.anim.slide_in_from_left, R.anim.slide_out_to_right)
        .replace(container, fragment, tag)

    if (addToBackStack) {
        fragmentTransaction.addToBackStack(null) // Need Backstack Tag
    }

    if (commitAllowingStateLoss) {
        fragmentTransaction.commitAllowingStateLoss()
    } else {
        fragmentTransaction.commit()
    }
}

fun FragmentManager.removeFragment(
    fragment: Fragment
) {
    var fragmentTransaction = beginTransaction()
        .remove(fragment)

    fragmentTransaction.commitAllowingStateLoss()
}
