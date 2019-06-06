package com.aminheidari.age.utils

import com.aminheidari.age.database.entities.BirthdayEntity
import com.aminheidari.age.models.Birthday

val BirthdayEntity.birthday: Birthday
    get() = Birthday(birth_date, name)