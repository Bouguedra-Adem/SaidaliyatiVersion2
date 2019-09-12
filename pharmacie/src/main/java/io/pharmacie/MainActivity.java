package io.pharmacie;

import android.app.Dialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.pharmacie.Maps.MapMainActivity;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.card_view_login)  CardView _link_Login;
    @BindView(R.id.card_view_showbycity)  CardView _link_showbycity;
    @BindView(R.id.card_view_singup)  CardView _link_singup;
    @BindView(R.id.card_view_oncall)  CardView _link_oncall;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    public static Class PreviousClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        View decorView = getWindow().getDecorView();
        hideSystemUI(decorView);

            _link_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                PreviousClass = MainActivity.class;
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _link_showbycity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviousClass = MainActivity.class;
                Intent intent = new Intent(getApplicationContext(),FilterVille.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _link_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviousClass = MainActivity.class;
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

             _link_oncall.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(isServicesOK()) {
                         PreviousClass = MainActivity.class;
                         MapMainActivity.movingCamera = "ONCALL";
                         Intent intent = new Intent(getApplicationContext(), MapMainActivity.class);
                         startActivity(intent);
                         finish();
                         overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                     }
                 }
             });

    }

    public boolean isServicesOK(){
        Log.d(TAG,"isServicesOK: checking google services version");
        int availabe = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(availabe== ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG,"isServicesOK: Google Play Services is working");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(availabe)){
            //an error occured but we can resolve it
            Log.d(TAG,"isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,availabe,ERROR_DIALOG_REQUEST);
            dialog.show();

        }else{
            Toast.makeText(this,"You can't make map requests",Toast.LENGTH_SHORT).show();
        }
        return false;
    }



    public static void hideSystemUI(View decorView) {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intentprevious = new Intent(getApplicationContext(),MainActivity.PreviousClass);
            startActivity(intentprevious);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        if (keyCode == KeyEvent.KEYCODE_HOME)
        {

        }

        // // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);

    }
}
