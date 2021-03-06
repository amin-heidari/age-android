package com.aminheidari.age.utils

import com.aminheidari.age.database.entities.BirthdayEntity
import com.aminheidari.age.models.BirthDate
import com.aminheidari.age.models.Birthday

val BirthdayEntity.birthday: Birthday
    get() = Birthday(BirthDate(year, month, day), name)