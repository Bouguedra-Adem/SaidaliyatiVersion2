package io.pharmacie;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.pharmacie.Retrofit.Api;
import io.pharmacie.models.User;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.btn_login) Button _btn_Login;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.input_mobile_num) TextInputEditText _input_mobile_num;
    @BindView(R.id.input_password) TextInputEditText _input_pasword;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Api api;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        View decorView = getWindow().getDecorView();
        MainActivity.hideSystemUI(decorView);

      //  BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
       // bottomNav.setOnNavigationItemSelectedListener(navListener);

        _signupLink.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // Start the Signup activity
            MainActivity.PreviousClass = LoginActivity.class;
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    });
        _btn_Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Api.Base_Url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Api api = retrofit.create(Api.class);

                Call<User> call = api.loginUser(_input_mobile_num.getText().toString(),_input_pasword.getText().toString());
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = (User) response.body();


                      if(user == null)
                          Log.d("LoginActivity","sdlgkjsdlfj");

                      else
                          Log.d("LoginActivity",user.toString());

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
