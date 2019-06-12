package com.aminheidari.age.calculator

import com.aminheidari.age.models.BirthDate
import com.aminheidari.age.utils.Logger
import java.math.BigDecimal
import java.util.*

class AgeCalculator(private val birthDate: BirthDate) {

    data class Age(val value: BigDecimal, val full: String, val rational: String)

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

            if (counter.rem(100) == 0) {
                Logger.d("lalala", this.hashCode().toString() +  " -> Calculated!!!" + counter.toString())
            }

            return Age(BigDecimal.ONE, counter.toString(), "234512341234")
        }

}