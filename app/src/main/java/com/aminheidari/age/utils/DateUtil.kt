package com.aminheidari.age.utils

import java.util.*

/**
 * Returns a new `Date` object, which is `seconds` number of seconds shifted from the receiver.
 */
fun Date.addingTimerInterval(seconds: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.SECOND, seconds)
    return calendar.time
}

/**
 * Returns a new `Date` object, which is `years` number of years shifted from the receiver.
 */
fun Date.addingYears(years: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.YEAR, years)
    return calendar.time
}
