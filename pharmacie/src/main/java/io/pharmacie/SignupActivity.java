package io.pharmacie;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyservice = retrofitClient.create(IMyService.class);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = generatePwd(numbterLetterPwd);
                sendSms(accountSid,authToken,fromPhoneNumber,password,_input_lastname.getText().toString(),
                        _input_firstname.getText().toString(),_input_email.getText().toString(),
                        _input_mobile.getText().toString(),_input_NSS.getText().toString());
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }






    private void sendSms(String accountSid, String authToken, String fromPhoneNumber,
                         String password,String Lastname,String Firstname,String email,
                         String PhoneNumber,String SSN) {
        if(TextUtils.isEmpty(PhoneNumber)){
            Toast.makeText(this,"Pnone number  cannot be null or empty",Toast.LENGTH_SHORT).show();
            return;
        }
        compositeDisposable.add(iMyservice.sendSMS(accountSid,authToken,fromPhoneNumber,
                password,Lastname,Firstname,email,
                PhoneNumber,SSN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception{
                        Toast.makeText(SignupActivity.this,""+response,Toast.LENGTH_SHORT).show();
                    }
                }));
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
}