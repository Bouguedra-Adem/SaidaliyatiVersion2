package io.pharmacie

import android.content.Intent

import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import io.pharmacie.Retrofit.Api
import io.pharmacie.models.User
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignupActivity : AppCompatActivity() {




    internal val accountSid = "AC611f2652a8f2262f662a0308bb9f7cd5"
    internal val authToken = "7cd41eb1ae681c57c89c0c5178f135de"
    internal val fromPhoneNumber = "+12012318756"
    internal val numbterLetterPwd = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        ButterKnife.bind(this)

        val decorView = window.decorView
        MainActivity.hideSystemUI(decorView)

        btn_signup!!.setOnClickListener {
            val user = User(
                input_lastname!!.text!!.toString(),
                input_firstname!!.text!!.toString(),
                input_email!!.text!!.toString(),
                input_mobile!!.text!!.toString(),
                input_NSS!!.text!!.toString(),
                generatePwd(6)
            )

            val builder = Retrofit.Builder()
                .baseUrl(Api.Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
            val retrofit = builder.build()

            val client = retrofit.create(Api::class.java)
            val call = client.createAccount(user)
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {

                    Toast.makeText(this@SignupActivity, "something went great :)", Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@SignupActivity, "something went wrong :(", Toast.LENGTH_SHORT).show()
                }
            })
        }


        link_login!!.setOnClickListener {
            // Finish the registration screen and return to the Login activity
            MainActivity.PreviousClass = SignupActivity::class.java
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }

    fun generatePwd(length: Int): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        var pass = ""
        for (x in 0 until length) {
            val i = Math.floor(Math.random() * 62).toInt()
            pass += chars[i]
        }
        println(pass)
        return pass
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intentprevious = Intent(applicationContext, MainActivity.PreviousClass)
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