package io.pharmacie;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.pharmacie.Retrofit.IMyService;
import io.pharmacie.Retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.btn_login) Button _btn_Login;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.input_mobile_num) TextInputEditText _input_mobile_num;
    @BindView(R.id.input_password) TextInputEditText _input_pasword;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyservice;

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
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyservice = retrofitClient.create(IMyService.class);


        _signupLink.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // Start the Signup activity
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    });
        _btn_Login.setOnClickListener(new View.OnClickListener(){
            @Override
               public void onClick(View v){
                loginUser(_input_mobile_num.getText().toString(),
                        _input_pasword.getText().toString());
               }
        });

    }

    private void loginUser(String phoneNumber, String password ) {

        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this,"Phone Number cannot be null or empty",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password cannot be null or empty",Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(iMyservice.loginUser(phoneNumber,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception{
                        Toast.makeText(LoginActivity.this,""+response,Toast.LENGTH_SHORT).show();
                    }
                }));
    }

}
