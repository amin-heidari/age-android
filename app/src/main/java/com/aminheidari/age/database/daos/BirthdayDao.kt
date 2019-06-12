package com.aminheidari.age.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aminheidari.age.database.entities.BirthdayEntity

@Dao
interface BirthdayDao {
    @Query("SELECT * FROM BirthdayEntity ORDER BY created DESC")
    fun getAll(): LiveData<List<BirthdayEntity>>

    @Insert
    fun insert(birthdayEntity: BirthdayEntity)

    @Update
    fun update(birthdayEntity: BirthdayEntity)

    @Delete
    fun delete(birthdayEntity: BirthdayEntity)
}