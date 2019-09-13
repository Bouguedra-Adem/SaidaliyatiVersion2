package io.pharmacie

import android.content.Context
import android.os.Bundle

import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import butterknife.BindView
import butterknife.ButterKnife
import io.pharmacie.Retrofit.Api
import io.pharmacie.Retrofit.ResponseMessage
import io.pharmacie.models.User
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {


    var api: Api? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        val decorView = window.decorView
        MainActivity.hideSystemUI(decorView)

        //  BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        // bottomNav.setOnNavigationItemSelectedListener(navListener);

        link_signup!!.setOnClickListener {
            // Start the Signup activity
            MainActivity.PreviousClass = LoginActivity::class.java
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }

        btn_login!!.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl(Api.Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(Api::class.java)

            val call = api.loginUser(input_email!!.text!!.toString(), input_password!!.text!!.toString())
            call.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    val repense = response.body()
                    val result = repense?.result
                    if (result!!) {
                       val pref = getSharedPreferences("ahlamfile", Context.MODE_PRIVATE)
                       with(pref.edit()){
                           putBoolean("connected", true).commit()
                           putString("email", input_email!!.text!!.toString()).commit()
                           putString("pwd", input_password!!.text!!.toString()).commit()
                       }
                        Toast.makeText(applicationContext,"Vous étes Connecté", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
                    }
                    else{
                        Toast.makeText(applicationContext,"Il y a une erreur dans l'email ou le mot de passe", Toast.LENGTH_SHORT).show()

                    }
                }
                override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
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
    }
}
