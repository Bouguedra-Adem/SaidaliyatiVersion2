package io.pharmacie

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.pharmacie.Retrofit.Api
import io.pharmacie.Retrofit.ResponseMessage
import kotlinx.android.synthetic.main.activity_confirmation.*
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)


        val decorView = window.decorView
        MainActivity.hideSystemUI(decorView)

        //  BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        // bottomNav.setOnNavigationItemSelectedListener(navListener);

        link_Anuller!!.setOnClickListener {
            // Start the Signup activity
           // MainActivity.PreviousClass = LoginActivity::class.java
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivityForResult(intent, ConfirmationActivity.REQUEST_SIGNUP)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }

            btn_Confirm!!.setOnClickListener {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Api.Base_Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(Api::class.java)
                val pref = getSharedPreferences("ahlamfile", Context.MODE_PRIVATE)



                if (input_password_confirmation!!.text!!.toString() == input_password_user!!.text!!.toString()) {

                    val emailUser = pref.getString("email", "NONE")


                    // BEGIN
                    val call = api.ConfirmationCode(
                        emailUser,
                        Code_input!!.text!!.toString(),
                        input_password_confirmation!!.text!!.toString()
                    )
                    call.enqueue(object : Callback<ResponseMessage> {
                        override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                            val repense = response.body()
                            val result = repense?.result
                            if (result!!) {
                                val pref = getSharedPreferences("ahlamfile", Context.MODE_PRIVATE)
                                with(pref.edit()) {
                                    putBoolean("connected", true).commit()
                                    putString("email", emailUser).commit()
                                    putString("pwd", input_password_confirmation!!.text!!.toString()).commit()
                                }
                                Toast.makeText(applicationContext, repense!!.message, Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Il y a une erreur dans l'email ou le mot de passe",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }

                        override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Le mot de passe et le confirmation ne sont pas compatible",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // END




        }






    }
    companion object {
        private val REQUEST_SIGNUP = 0
    }
}
