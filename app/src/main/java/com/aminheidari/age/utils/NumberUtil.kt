package com.aminheidari.age.utils

import android.content.res.Resources
import kotlin.math.round
import kotlin.math.roundToInt

private val Float.dpToPxFloat: Float
    get() = this * Resources.getSystem().displayMetrics.density

private val Float.pxToDpFloat: Float
    get() = this / Resources.getSystem().displayMetrics.density

//val Int.dpToPx: Int
//    get() = this.toFloat().dpToPx.roundToInt()

//val Int.pxToDp: Int
//    get() = this.toFloat().pxToDp.roundToInt()

val Float.dpToPx: Int
    get() = this.dpToPxFloat.roundToInt()

//val Float.pxToDp: Int
//    get() = this.pxToDp.roundToInt()

//val Int.dpToPx: Float
//    get() = this.toFloat().dpToPx

val Int.pxToDp: Float
    get() = this.toFloat().pxToDpFloat