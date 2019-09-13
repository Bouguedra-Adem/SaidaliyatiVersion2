package io.pharmacie.models.RoomDatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import io.pharmacie.models.pharmacy
@Dao
interface DaoPharmaice {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPharmacie(Pharmacie: pharmacy)
    @Delete
    fun deltePharmacie(Pharmacie:pharmacy)

    @Query("Select * from pharmacy ")
    fun getAllPharmacie (): LiveData<List<pharmacy>>

}