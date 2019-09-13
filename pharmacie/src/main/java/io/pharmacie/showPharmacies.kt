package io.pharmacie

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.app.NotificationCompat.EXTRA_PEOPLE

import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog

import java.util.ArrayList

import io.pharmacie.Maps.MapMainActivity
import io.pharmacie.Retrofit.Api
import io.pharmacie.models.pharmacy
import kotlinx.android.synthetic.main.pharmacydetail_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class showPharmacies : AppCompatActivity() {

     lateinit var _myListViewPharmacie: ListView
     var adapter: ArrayAdapter<pharmacy>? = null
     var pharmacies = ArrayList<pharmacy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showpharmacies_layout)

        val decorView = window.decorView
        MainActivity.hideSystemUI(decorView)

        /* BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);*/

        val retrofit = Retrofit.Builder()
            .baseUrl(Api.Base_Url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val dialog = ProgressDialog(this)
       /* dialog.setMessage(resources.getString(R.string.loading))
        dialog.setCancelable(false)
        dialog.setInverseBackgroundForced(false)
        dialog.show()*/
        val api = retrofit.create(Api::class.java)
        val call = api.getPharmacies(FilterVille.commune)
        call.enqueue(object : Callback<List<pharmacy>> {
            override fun onResponse(call: Call<List<pharmacy>>, response: Response<List<pharmacy>>) {

                val pha = response.body()
                if (pha == null) {

                } else
                    for (ph in pha) {
                        pharmacies.add(ph)
                    }
                showPharmacies()
                dialog.hide()
            }

            override fun onFailure(call: Call<List<pharmacy>>, t: Throwable) {
                dialog.hide()
            }
        })
    }

    fun showPharmacies() {
        _myListViewPharmacie = findViewById(R.id.myListViewPharmacy)
        _myListViewPharmacie.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, pharmacies)
        _myListViewPharmacie.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val pharmacydetail_layout = LayoutInflater.from(this)
                .inflate(R.layout.pharmacydetail_layout, null)

           /* val _ownerContent = pharmacydetail_layout.findViewById<TextView>(R.id.ownerContent)
            val _adressContent = pharmacydetail_layout.findViewById<TextView>(R.id.adressContent)
            val _worktimeContent = pharmacydetail_layout.findViewById<TextView>(R.id.worktimeContent)
            val _phoneNumberContent = pharmacydetail_layout.findViewById<TextView>(R.id.phoneNumberContent)
            val _caissConvContent = pharmacydetail_layout.findViewById<TextView>(R.id.caissConvContent)
            val _lienfcbkContent = pharmacydetail_layout.findViewById<TextView>(R.id.lienfcbkContent)
            // TextView _lienMapContent = pharmacydetail_layout.findViewById(R.id.lienMapContent);
            val _datagardContent = pharmacydetail_layout.findViewById<TextView>(R.id.datagardContent)*/

            for (pha in pharmacies)
                if (pha.toString() == (view as TextView).text.toString()) {
                    MainActivity.PreviousClass = showPharmacies::class.java
                    val intent = Intent(applicationContext, detail_pharma::class.java)
                    intent.putExtra("name",pha.nomPrenomPharmacien)
                    intent.putExtra("adr",pha.adresse)
                    intent.putExtra("heure",pha.heure)
                    intent.putExtra("tlf",pha.numeroTelephone)
                    intent.putExtra("caisse",pha.caisseConventionnee)
                    intent.putExtra("fb",pha.facebookUrl)
                    intent.putExtra("garde",pha.dateGarde)
                    startActivity(intent)
                    finish()
                   /* overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
                    ownerContent.text = pha.toString()
                    adressContent.text = pha.adresse
                    worktimeContent.text = pha.heure
                    phoneNumberContent.text = pha.numeroTelephone
                    caissConvContent.text = pha.caisseConventionnee
                    lienfcbkContent.text = pha.facebookUrl
                    // _lienMapContent.setText(getResources().getString(R.string.lienMap));
                    datagardContent.text = pha.dateGarde*/
                }

           /*         /*val dialogFragment = Pharma_detail(data)
                    dialogFragment.allowEnterTransitionOverlap
                    dialogFragment.show(fragment.childFragmentManager, "simple dialog")*/
            MaterialStyledDialog.Builder(this)
                .setIcon(R.drawable.ic_detail)
                .setTitle(R.string.details)
                .setHeaderColor(R.color.blueChosen)
                .withDialogAnimation(true)
                .withIconAnimation(true)
                .setCustomView(pharmacydetail_layout)
                .setNegativeText(resources.getString(R.string.negativeText))
                .onNegative { dialog, which -> dialog.dismiss() }
                .setPositiveText(resources.getString(R.string.positiveText))
                .onPositive { dialog, which ->
                    MapMainActivity.showPharmacy = adressContent.text.toString()
                    MapMainActivity.pharmacyOwnerName = ownerContent.text.toString()
                    MapMainActivity.movingCamera = "SHOWPHARMACY"
                    val intent = Intent(applicationContext, MapMainActivity::class.java)
                    startActivity(intent)
                    finish()
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
                }
                .show()*/
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intentprevious = Intent(applicationContext, FilterVille::class.java)
            startActivity(intentprevious)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }

        if (keyCode == KeyEvent.KEYCODE_HOME) {

        }

        // // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event)

    }
}
