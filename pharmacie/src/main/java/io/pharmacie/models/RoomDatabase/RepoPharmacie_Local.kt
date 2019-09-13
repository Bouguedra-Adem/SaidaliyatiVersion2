package io.pharmacie.models.RoomDatabase

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import io.pharmacie.models.pharmacy

class RepoPharmacie_Local(context: Context) {

    private val pharmacieDa:DaoPharmaice?
    private val allPharmacie: LiveData<List<pharmacy>>

    init {
        val db=DataBase_Instance.getInstance(context)


        pharmacieDa=db?.DaoPharmacie()
        this.allPharmacie= pharmacieDa!!.getAllPharmacie()

    }

    fun insertPharmacie(Pharmacie:pharmacy){
        Log.e("insert", "ademmm")
        inserAsynckTasck(pharmacieDa!!).execute(Pharmacie)
    }

    fun deltePharmacie(Pharmacie:pharmacy){
        DeleteAsynckTasck(pharmacieDa!!).execute(Pharmacie)

    }
    fun checkExistPharma(pharma:pharmacy):Boolean?{
        Log.e("heree adem", getAllPharmacie().toString())
        return  getAllPharmacie().value!!.contains(pharma)
    }
    fun getAllPharmacie(): LiveData<List<pharmacy>> {
        return pharmacieDa!!.getAllPharmacie()

    }
    private  class inserAsynckTasck (private val dao: DaoPharmaice): AsyncTask<pharmacy, Void, Void>(){
        override fun doInBackground(vararg params: pharmacy?): Void? {
            dao.insertPharmacie(params[0]!!)
            return null
        }

    }
    private  class getAsynckTasck (private val dao: DaoPharmaice): AsyncTask<pharmacy, Void, Void>(){
        override fun doInBackground(vararg params: pharmacy?): Void? {
            dao.insertPharmacie(params[0]!!)
            return null
        }

    }
    private  class DeleteAsynckTasck (private val dao: DaoPharmaice): AsyncTask<pharmacy, Void, Void>(){
        override fun doInBackground(vararg params: pharmacy?): Void? {
            dao.deltePharmacie(params[0] !!)
            return null

        }

    }


}