package io.pharmacie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.pharmacie.Retrofit.Api;
import io.pharmacie.Retrofit.RetrofitClient;
import io.pharmacie.models.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.link_login) TextView _loginLink;
    @BindView(R.id.input_firstname)TextInputEditText _input_firstname;
    @BindView(R.id.input_lastname) TextInputEditText _input_lastname;
    @BindView(R.id.input_email) TextInputEditText _input_email;
    @BindView(R.id.input_mobile) TextInputEditText _input_mobile;
    @BindView(R.id.input_NSS) TextInputEditText _input_NSS;
    @BindView(R.id.btn_signup) Button _signupButton;

    final String  accountSid= "AC611f2652a8f2262f662a0308bb9f7cd5";
    final String authToken ="7cd41eb1ae681c57c89c0c5178f135de";
    final String fromPhoneNumber = "+12012318756";
    final int numbterLetterPwd = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        View decorView = getWindow().getDecorView();
        MainActivity.hideSystemUI(decorView);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(_input_lastname.getText().toString(),_input_firstname.getText().toString(),_input_email.getText().toString()
                        ,_input_mobile.getText().toString(),_input_NSS.getText().toString(),generatePwd(6));

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(Api.Base_Url)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();

                Api client = retrofit.create(Api.class);
                Call<User> call = client.createAccount(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        Toast.makeText(SignupActivity.this,"something went great :)",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                       Toast.makeText(SignupActivity.this,"something went wrong :(",Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });


        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                MainActivity.PreviousClass = SignupActivity.class;
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public String generatePwd(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String pass = "";
        for(int x=0;x<length;x++)
        {
            int i = (int)Math.floor(Math.random() * 62);
            pass += chars.charAt(i);
        }
        System.out.println(pass);
        return pass;
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