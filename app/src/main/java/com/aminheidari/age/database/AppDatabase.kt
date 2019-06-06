package com.aminheidari.age.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aminheidari.age.database.daos.BirthdayDao
import com.aminheidari.age.database.entities.BirthdayEntity

@Database(entities = [BirthdayEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun birthdayDao(): BirthdayDao
}