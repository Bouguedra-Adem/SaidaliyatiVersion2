package io.pharmacie

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
import butterknife.BindView
import butterknife.ButterKnife
import io.pharmacie.Retrofit.Api
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

            val call = api.loginUser(input_mobile_num!!.text!!.toString(), input_password!!.text!!.toString())
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val user = response.body()


                    if (user == null)
                        Log.d("LoginActivity", "sdlgkjsdlfj")
                    else
                        Log.d("LoginActivity", user.toString())

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
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

    companion object {

        private val REQUEST_SIGNUP = 0
    }
}
