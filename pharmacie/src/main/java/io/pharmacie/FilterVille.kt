package io.pharmacie

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.util.ArrayList
import io.pharmacie.Retrofit.Api
import io.pharmacie.models.Commune
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FilterVille : AppCompatActivity() {
    lateinit var _myListView: ListView
     lateinit var _mySpinner: Spinner
      var communes = ArrayList<Commune>()
    //List<Commune> ;
      var wilaya = arrayOf("All", "Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Bejaia", "Biskra", "Bechar", "Blida", "Bouira", "Tamanrasset", "Tebéssa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saida", "Skikda", "Sidi Bel Abbès", "Annaba", "Guelma", "Constantine", "Mèdéa", "Mostaganem", "Msila", "Mascara", "Ouargla", "Oran", "El Bayadh", "Illizi", "Bourdj Bou Arreridj", "Boumerdès", "Tarf", "Tindouf", "Tissemsilt", "El Oued", "Khenschla", "Souk Ahras", "Tipaza", "Mila", "Ain Defla", "Naama", "Ain Tèmouchent", "Ghardaia", "Relizane")
     lateinit var adapter:ArrayAdapter<Commune>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choosecity_layout)

        val decorView = window.decorView
        MainActivity.hideSystemUI(decorView)
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val api = retrofit.create<Api>(Api::class.java!!)

        val dialog = ProgressDialog(this@FilterVille)
        dialog.setMessage(resources.getString(R.string.loading))
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()

        val call = api.communes
        call.enqueue(object : Callback<List<Commune>> {
            override fun onResponse(call: Call<List<Commune>>, response: Response<List<Commune>>) {
                val coms = response.body()

                for (c in coms!!) {
                    communes.add(c)
                }
                initializeView()
                dialog.hide()
            }

            override fun onFailure(call: Call<List<Commune>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                dialog.hide()
            }
        })
    }

    private fun initializeView() {
        _mySpinner = findViewById(R.id.mySpinner)
        _mySpinner.adapter = ArrayAdapter(this@FilterVille, android.R.layout.simple_list_item_1, wilaya)
        _myListView = findViewById(R.id.myListView)
        _myListView.adapter = ArrayAdapter(this@FilterVille, android.R.layout.simple_list_item_1, communes)
        _mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (position >= 0 && position < wilaya.size) {
                    getSelectectWilayaData(position)
                } else {
                    Toast.makeText(this@FilterVille, "Selected Wilaya Doesn't exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        _myListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            commune = (view as TextView).text.toString().replace("\\s".toRegex(), "%20")
            MainActivity.PreviousClass = FilterVille::class.java
            val intent = Intent(applicationContext, showPharmacies::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getSelectectWilayaData(wilayacode: Int) {
        val coms = ArrayList<Commune>()
        if (wilayacode == 0) {
            adapter = ArrayAdapter(this@FilterVille, android.R.layout.simple_list_item_1, communes)
        } else {
            for (commune in communes) {
                if (commune.wiyalaCode == wilayacode) {
                    coms.add(commune)
                }
            }
            adapter = ArrayAdapter(this@FilterVille, android.R.layout.simple_list_item_1, coms)
        }
        _myListView.adapter = adapter
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intentprevious = Intent(applicationContext, MainActivity::class.java)
            startActivity(intentprevious)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }

        if (keyCode == KeyEvent.KEYCODE_HOME) {

        }

        // // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event)

    }

    companion object {

        private val REQUEST_SIGNUP = 0
        lateinit var commune: String
    }
}
