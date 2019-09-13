package io.pharmacie.models.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.pharmacie.models.pharmacy

@Database(entities = [pharmacy::class],version = 1,exportSchema = false)
abstract class DataBase_Instance: RoomDatabase(){
    abstract fun DaoPharmacie():DaoPharmaice

    companion object{
        @Volatile
        var database :DataBase_Instance?=null
        fun getInstance(context: Context): DataBase_Instance? {
            if (database==null){
                synchronized(DataBase_Instance::class.java){
                    if (database==null) {

                        database= Room.databaseBuilder(context.applicationContext,DataBase_Instance::class.java,"OfflineMode").build()
                    }
                }
            }
            return database
        }
    }
}
