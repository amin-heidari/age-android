package com.aminheidari.age.utils

import java.util.*

/**
 * Returns a new `Date` object, which is `seconds` number of seconds shifted from the receiver.
 */
fun Date.addingTimerInterval(seconds: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(seconds, Calendar.SECOND)
    return calendar.time
}
