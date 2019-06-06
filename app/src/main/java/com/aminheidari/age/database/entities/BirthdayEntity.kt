package com.aminheidari.age.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.aminheidari.age.database.converters.DateConverter
import java.util.*

@Entity
@TypeConverters(DateConverter::class)
data class BirthdayEntity(
    @PrimaryKey val created: Date,
    @ColumnInfo(name = "birth_date") val birthDate: Date,
    val name: String
)