package com.aminheidari.age.calculator

import com.aminheidari.age.models.BirthDate
import com.aminheidari.age.utils.Logger
import java.math.BigDecimal
import java.util.*

class AgeCalculator(private val birthDate: BirthDate) {

    data class Age(val full: Int, val rational: Double) {
        val value: Double
            get() = full.toDouble() + rational
    }

    val currentAge: Age
        get() {
            // Evaluate the last birthday, as well as the next birthday.
            val now = Calendar.getInstance()

            // Set to the midnight local time.
            val thisYearsBirthday = Calendar.getInstance()
            thisYearsBirthday.set(Calendar.MONTH, birthDate.month)
            thisYearsBirthday.set(Calendar.DAY_OF_MONTH, birthDate.day)
            thisYearsBirthday.set(Calendar.HOUR_OF_DAY, 0)
            thisYearsBirthday.set(Calendar.MINUTE, 0)
            thisYearsBirthday.set(Calendar.SECOND, 0)
            thisYearsBirthday.set(Calendar.MILLISECOND, 0)

            val lastBirthday: Calendar
            val nextBirthday: Calendar

            if (now > thisYearsBirthday) { // We are past this year's birthday.
                lastBirthday = thisYearsBirthday.clone() as Calendar

                nextBirthday = lastBirthday.clone() as Calendar
                nextBirthday.set(Calendar.YEAR, lastBirthday.get(Calendar.YEAR) + 1)
            } else { // The birthday is still ahead of us this year.
                nextBirthday = thisYearsBirthday.clone() as Calendar

                lastBirthday = nextBirthday.clone() as Calendar
                lastBirthday.set(Calendar.YEAR, nextBirthday.get(Calendar.YEAR) - 1)
            }

            val fullYears: Int = lastBirthday.get(Calendar.YEAR) - birthDate.year
            val milisLastToNextBday = nextBirthday.time.time - lastBirthday.time.time
            val milisLastBdayToNow = now.time.time - lastBirthday.time.time
            val yearFraction: Double = milisLastBdayToNow.toDouble() / milisLastToNextBday.toDouble()

            return Age(fullYears, yearFraction)
        }

}