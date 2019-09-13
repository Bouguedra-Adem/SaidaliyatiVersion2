package io.pharmacie

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import io.pharmacie.Maps.MapMainActivity
import kotlinx.android.synthetic.main.activity_detail_pharma.*
class detail_pharma : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pharma)
        val pref = getSharedPreferences("ahlamfile", Context.MODE_PRIVATE)
        val con = pref.getBoolean("connected", false)
        if (!con) {
           cmd.setVisibility(View.GONE)
        }
        Log.e("data",intent.getStringExtra("adr"))
        ownerContent.text = intent.getStringExtra("name")
        adressContent.text = intent.getStringExtra("adr")
        worktimeContent.text = intent.getStringExtra("heure")
        phoneNumberContent.text =intent.getStringExtra("tlf")
        caissConvContent.text =intent.getStringExtra("caisse")
        lienfcbkContent.text = intent.getStringExtra("fb")
        datagardContent.text = intent.getStringExtra("tlf")
        map.setOnClickListener {
            MapMainActivity.showPharmacy = adressContent.text.toString()
            MapMainActivity.pharmacyOwnerName = ownerContent.text.toString()
            MapMainActivity.movingCamera = "SHOWPHARMACY"
            val intent = Intent(applicationContext, MapMainActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
        cmd.setOnClickListener {

            val intent = Intent(applicationContext, ActivityComand::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intentprevious = Intent(applicationContext,MainActivity.PreviousClass)
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
