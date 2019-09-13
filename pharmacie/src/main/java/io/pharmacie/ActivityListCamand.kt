package io.pharmacie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import io.pharmacie.Retrofit.Api
import io.pharmacie.models.Camand
import io.pharmacie.models.Commune
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class ActivityListCamand : AppCompatActivity() {
    lateinit var _myListView: ListView
    lateinit var adapter: ArrayAdapter<String>
    var camands = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_camand)
        val retrofit = Retrofit.Builder()
            .baseUrl(Api.Base_Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create<Api>(Api::class.java!!)
        val call = api.getCmdUserEmail("fa_bouguedra@esi.dz")
        call.enqueue(object : Callback<List<Camand>> {


            override fun onResponse(call: Call<List<Camand>>, response: Response<List<Camand>>) {
                val camand = response.body()
                for (c in camand!!) {
                    Log.e("MUSTAPHADEBBIH",c.nomCmd)

                    camands.add(c.nomCmd.toString())
                }
                initializeView()

            }

            override fun onFailure(call: Call<List<Camand>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()

            }
        })
    }
    private fun initializeView() {
        _myListView.adapter = ArrayAdapter(this@ActivityListCamand, android.R.layout.simple_list_item_1, camands)


    }
}
