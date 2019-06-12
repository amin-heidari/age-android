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

    private var counter = 0

    private val calendar: Calendar by lazy {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, birthDate.year)
        cal.set(Calendar.MONTH, birthDate.month)
        cal.set(Calendar.DAY_OF_MONTH, birthDate.day)
        cal
    }

    val currentAge: Age
        get() {

            counter++

            val diff = Calendar.getInstance().time.time - calendar.time.time

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

            if (now.compareTo(thisYearsBirthday) > 0) { // We are past this year's birthday.
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

            if (counter.rem(100) == 0) {
                Logger.d("lalala", this.hashCode().toString() +  " -> Calculated!!!" + counter.toString())
            }

            return Age(fullYears, yearFraction)
        }

}