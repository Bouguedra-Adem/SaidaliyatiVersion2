package io.pharmacie

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.ArrayList
import io.pharmacie.Retrofit.Api
import io.pharmacie.models.Commune
import io.pharmacie.models.RoomDatabase.DataBase_Instance
import io.pharmacie.models.RoomDatabase.RepoPharmacie_Local
import io.pharmacie.models.pharmacy
import kotlinx.android.synthetic.main.activity_ville.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Observer


class FilterVille : AppCompatActivity() {
    lateinit var _myListView: ListView
     lateinit var _mySpinner: Spinner
    lateinit var _modeoffline: ListView

      var communes = ArrayList<String>()
    var liscommune=ArrayList<Commune>()
    var array=ArrayList<String>()

    //List<Commune> ;

      var wilaya = arrayOf("TOUS LES WILAYA", "Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Bejaia", "Biskra", "Bechar", "Blida", "Bouira", "Tamanrasset", "Tebéssa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saida", "Skikda", "Sidi Bel Abbès", "Annaba", "Guelma", "Constantine", "Mèdéa", "Mostaganem", "Msila", "Mascara", "Ouargla", "Oran", "El Bayadh", "Illizi", "Bourdj Bou Arreridj", "Boumerdès", "Tarf", "Tindouf", "Tissemsilt", "El Oued", "Khenschla", "Souk Ahras", "Tipaza", "Mila", "Ain Defla", "Naama", "Ain Tèmouchent", "Ghardaia", "Relizane")
     lateinit var adapter:ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choosecity_layout)

        val decorView = window.decorView
        MainActivity.hideSystemUI(decorView)
        if (verifyAvailableNetwork(AppCompatActivity())) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Api.Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create<Api>(Api::class.java!!)

            /* val dialog = ProgressDialog(this@FilterVille)
        dialog.setMessage(resources.getString(R.string.loading))
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()*/

            val call = api.communes()
            call.enqueue(object : Callback<List<Commune>> {
                override fun onResponse(call: Call<List<Commune>>, response: Response<List<Commune>>) {
                    val coms = response.body()
                    liscommune = coms as ArrayList<Commune>
                    for (c in coms!!) {

                        Log.e("MUSTAPHADEBBIH", c.codeWilaya.toString())

                        communes.add(c.nomCommune.toString())

                    }
                    initializeView()
                    //dialog.hide()
                }

                override fun onFailure(call: Call<List<Commune>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    //dialog.hide()
                }
            })
        }else{
            mySpinner.setVisibility(View.GONE)
            //myListView.setVisibility(View.GONE)
            // modeoffline!!.setVisibility(View.VISIBLE)
            Log.e("dataromlist", "herrrrrrrrrrrrre")
            val RepoLocal= RepoPharmacie_Local(this.applicationContext)
            RepoLocal.getAllPharmacie().observe(this, androidx.lifecycle.Observer{

                    val list=it
                    var nomPharma=ArrayList<String>()
                    for (c in list!!) {

                        Log.e("MUSTAPHIH", c.nomPrenomPharmacien.toString())
                        nomPharma.add(c.nomPrenomPharmacien)


                    }
                   // _modeoffline = findViewById(R.id.modeoffline!!)
                    _myListView = findViewById(R.id.myListView)
                    _myListView!!.adapter = ArrayAdapter(this@FilterVille, android.R.layout.simple_list_item_1, nomPharma)

                

            })

        }
    }

    private fun initializeView() {
        _mySpinner = findViewById(R.id.mySpinner)
        _mySpinner.adapter = ArrayAdapter(this@FilterVille, android.R.layout.simple_list_item_1, wilaya)
        _myListView = findViewById(R.id.myListView)


        Log.e("mustaphaamine",array.toString())
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
        var coms = ArrayList<String>()
        if (wilayacode == 0) {
            adapter = ArrayAdapter(this@FilterVille, android.R.layout.simple_list_item_1, communes)
        }else {
            Log.e("wilaya",wilayacode.toString())
            Log.e("listcommun",liscommune.toString())
            for (commune in liscommune) {
                Log.e("listcommun",commune.nomCommune)
                Log.e("listcommuncode",commune.codeWilaya.toString())
                if (commune.codeWilaya == wilayacode) {
                    Log.e("listcommun",commune.nomCommune)
                    coms.add(commune.nomCommune.toString())
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
    fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean{
        val connectivityManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
    companion object {

        private val REQUEST_SIGNUP = 0
        lateinit var commune: String


    }
}
