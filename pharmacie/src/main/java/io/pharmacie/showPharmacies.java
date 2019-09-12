package io.pharmacie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.util.ArrayList;
import java.util.List;

import io.pharmacie.Maps.MapMainActivity;
import io.pharmacie.Retrofit.Api;
import io.pharmacie.models.pharmacy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class showPharmacies  extends AppCompatActivity {

    ListView _myListViewPharmacie;
    ArrayAdapter<pharmacy> adapter;
    ArrayList<pharmacy> pharmacies= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpharmacies_layout);

        View decorView = getWindow().getDecorView();
        MainActivity.hideSystemUI(decorView);

       /* BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProgressDialog dialog=new ProgressDialog(showPharmacies.this);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        Api api = retrofit.create(Api.class);
        Call<List<pharmacy>> call = api.getPharmacies(FilterVille.commune);
        call.enqueue(new Callback<List<pharmacy>>() {
            @Override
            public void onResponse(Call<List<pharmacy>> call, Response<List<pharmacy>> response) {

                List<pharmacy> pha = response.body();
                if(pha == null){

                }else
                    for(pharmacy ph : pha){
                        pharmacies.add(ph);
                    }
                showPharmacies();
                    dialog.hide();
            }

            @Override
            public void onFailure(Call<List<pharmacy>> call, Throwable t) {
                dialog.hide();
            }
        });
    }

    public void showPharmacies(){
        _myListViewPharmacie = findViewById(R.id.myListViewPharmacy);
        _myListViewPharmacie.setAdapter(new ArrayAdapter<>(showPharmacies.this,android.R.layout.simple_list_item_1,pharmacies));
        _myListViewPharmacie.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                final  View pharmacydetail_layout = LayoutInflater.from(showPharmacies.this)
                        .inflate(R.layout.pharmacydetail_layout,null);

                TextView _ownerContent = pharmacydetail_layout.findViewById(R.id.ownerContent);
                TextView _adressContent = pharmacydetail_layout.findViewById(R.id.adressContent);
                TextView _worktimeContent = pharmacydetail_layout.findViewById(R.id.worktimeContent);
                TextView _phoneNumberContent = pharmacydetail_layout.findViewById(R.id.phoneNumberContent);
                TextView _caissConvContent = pharmacydetail_layout.findViewById(R.id.caissConvContent);
                TextView _lienfcbkContent = pharmacydetail_layout.findViewById(R.id.lienfcbkContent);
                // TextView _lienMapContent = pharmacydetail_layout.findViewById(R.id.lienMapContent);
                TextView _datagardContent = pharmacydetail_layout.findViewById(R.id.datagardContent);

                for(pharmacy pha: pharmacies)
                    if(pha.toString().equals(((TextView) view).getText().toString())){
                        _ownerContent.setText(pha.toString());
                        _adressContent.setText(pha.getAdresse());
                        _worktimeContent.setText(pha.getHeure());
                        _phoneNumberContent.setText(pha.getNumeroTelephone());
                        _caissConvContent.setText(pha.getCaisseConventionnee());
                        _lienfcbkContent.setText(pha.getFacebookUrl());
                        // _lienMapContent.setText(getResources().getString(R.string.lienMap));
                        _datagardContent.setText(pha.getDateGarde());
                    }

                new MaterialStyledDialog.Builder(showPharmacies.this)
                        .setIcon(R.drawable.ic_detail)
                        .setTitle(R.string.details)
                        .setHeaderColor(R.color.blueChosen)
                        .withDialogAnimation(true)
                        .withIconAnimation(true)
                        .setCustomView(pharmacydetail_layout)
                        .setNegativeText(getResources().getString(R.string.negativeText))
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).setPositiveText(getResources().getString(R.string.positiveText))
                        .onPositive((new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MapMainActivity.showPharmacy = _adressContent.getText().toString();
                                MapMainActivity.pharmacyOwnerName = _ownerContent.getText().toString();
                                MapMainActivity.movingCamera = "SHOWPHARMACY";
                                Intent intent = new Intent(getApplicationContext(), MapMainActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }
                        }))
                        .show();
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
