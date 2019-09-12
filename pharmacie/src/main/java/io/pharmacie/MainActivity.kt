package io.pharmacie

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import butterknife.BindView
import butterknife.ButterKnife
import io.pharmacie.Maps.MapMainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.*

class MainActivity : AppCompatActivity() {


    //everything is fine and the user can make map requests
    //an error occured but we can resolve it
    val isServicesOK: Boolean
        get() {
            Log.d(TAG, "isServicesOK: checking google services version")
            val availabe = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@MainActivity)
            if (availabe == ConnectionResult.SUCCESS) {
                Log.d(TAG, "isServicesOK: Google Play Services is working")
                return true
            } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availabe)) {
                Log.d(TAG, "isServicesOK: an error occured but we can fix it")
                val dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this@MainActivity, availabe, ERROR_DIALOG_REQUEST)
                dialog.show()

            } else {
                Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show()
            }
            return false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        val decorView = window.decorView
        hideSystemUI(decorView)

        card_view_login!!.setOnClickListener {
            // Finish the registration screen and return to the Login activity
            PreviousClass = MainActivity::class.java
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }

       card_view_showbycity!!.setOnClickListener {
            PreviousClass = MainActivity::class.java
            val intent = Intent(applicationContext, FilterVille::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }

        card_view_singup!!.setOnClickListener {
            PreviousClass = MainActivity::class.java
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }

         card_view_oncall!!.setOnClickListener {
            if (isServicesOK) {
                PreviousClass = MainActivity::class.java
                MapMainActivity.movingCamera = "ONCALL"
                val intent = Intent(applicationContext, MapMainActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
            }
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
        private val TAG = "MainActivity"
        private val ERROR_DIALOG_REQUEST = 9001
        lateinit var PreviousClass: Class<*>


        fun hideSystemUI(decorView: View) {
            // Enables regular immersive mode.
            // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
            // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY

            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }
}
