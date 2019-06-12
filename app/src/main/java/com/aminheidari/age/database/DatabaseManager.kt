package com.aminheidari.age.database

import androidx.lifecycle.LiveData
import androidx.room.Room
import com.aminheidari.age.App
import com.aminheidari.age.database.entities.BirthdayEntity
import com.aminheidari.age.models.Birthday
import com.aminheidari.age.utils.Completion
import com.aminheidari.age.utils.AppExecutors
import com.aminheidari.age.utils.Either
import com.aminheidari.age.utils.Finish
import java.util.*

object DatabaseManager {

    // ====================================================================================================
    // region Constants/Types
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Static
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region API
    // ====================================================================================================

    fun addBirthday(birthday: Birthday, finish: Finish<Unit>) {
        AppExecutors.worker.execute {
            database.birthdayDao().insert(BirthdayEntity(
                Calendar.getInstance().time,
                birthday.birthDate.year,
                birthday.birthDate.month,
                birthday.birthDate.day,
                birthday.name
            ))
            AppExecutors.main.execute {
                finish(Unit)
            }
        }
    }

    fun updateBirthday(birthdayEntity: BirthdayEntity, birthday: Birthday, finish: Finish<Unit>) {
        AppExecutors.worker.execute {
            database.birthdayDao().update(BirthdayEntity(
                birthdayEntity.created,
                birthday.birthDate.year,
                birthday.birthDate.month,
                birthday.birthDate.day,
                birthday.name
            ))
            AppExecutors.main.execute {
                finish(Unit)
            }
        }
    }

    fun deleteBirthday(birthdayEntity: BirthdayEntity, finish: Finish<Unit>) {
        AppExecutors.worker.execute {
            database.birthdayDao().delete(birthdayEntity)
            AppExecutors.main.execute {
                finish(Unit)
            }
        }
    }

    val birthdays: LiveData<List<BirthdayEntity>> by lazy {
        database.birthdayDao().getAll()
    }

    // endregion

    // ====================================================================================================
    // region Life Cycle
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Properties
    // ====================================================================================================

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(App.instance.applicationContext, AppDatabase::class.java, "age-db").build()
    }

    // endregion

    // ====================================================================================================
    // region Methods
    // ====================================================================================================

    // endregion

    // ====================================================================================================
    // region Actions
    // ====================================================================================================

    // endregion

}