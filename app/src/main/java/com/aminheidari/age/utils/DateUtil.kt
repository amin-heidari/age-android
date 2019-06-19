package com.aminheidari.age.utils

import com.aminheidari.age.models.BirthDate
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

val Date.birthDate: BirthDate
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return BirthDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }