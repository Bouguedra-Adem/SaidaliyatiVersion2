package io.pharmacie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import io.pharmacie.Retrofit.Api;
import io.pharmacie.models.Commune;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.pharmacie.R.string.loading;


public class FilterVille extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;
    ListView _myListView;
    Spinner _mySpinner;
    ArrayList<Commune> communes = new ArrayList<>();
    public static String commune;
    //List<Commune> ;
    String[] wilaya = {"All","Adrar","Chlef","Laghouat","Oum El Bouaghi","Batna","Bejaia","Biskra","Bechar","Blida","Bouira","Tamanrasset",
            "Tebéssa","Tlemcen","Tiaret","Tizi Ouzou","Alger","Djelfa","Jijel","Sétif","Saida","Skikda","Sidi Bel Abbès","Annaba","Guelma","Constantine"
            ,"Mèdéa","Mostaganem","Msila","Mascara","Ouargla","Oran","El Bayadh","Illizi","Bourdj Bou Arreridj","Boumerdès","Tarf","Tindouf","Tissemsilt","El Oued"
            ,"Khenschla","Souk Ahras","Tipaza","Mila","Ain Defla","Naama","Ain Tèmouchent","Ghardaia","Relizane"};
    ArrayAdapter<Commune> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosecity_layout);

        View decorView = getWindow().getDecorView();
        MainActivity.hideSystemUI(decorView);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        ProgressDialog dialog=new ProgressDialog(FilterVille.this);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        Call<List<Commune>> call = api.getCommunes();
        call.enqueue(new Callback<List<Commune>>() {
            @Override
            public void onResponse(Call<List<Commune>> call, Response<List<Commune>> response) {
                List<Commune> coms = response.body();

                for(Commune c : coms){
                    communes.add(c);
                }
                initializeView();
                dialog.hide();
            }

            @Override
            public void onFailure(Call<List<Commune>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                dialog.hide();
            }
        });
    }

    private void initializeView(){
        _mySpinner = findViewById(R.id.mySpinner);
        _mySpinner.setAdapter(new ArrayAdapter<>(FilterVille.this,android.R.layout.simple_list_item_1,wilaya));
        _myListView = findViewById(R.id.myListView);
        _myListView.setAdapter(new ArrayAdapter<>(FilterVille.this,android.R.layout.simple_list_item_1,communes));
        _mySpinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0 && position < wilaya.length){
                    getSelectectWilayaData(position);
                }else{
                    Toast.makeText(FilterVille.this,"Selected Wilaya Doesn't exist",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                commune = ((TextView) view).getText().toString().replaceAll("\\s", "%20");
                MainActivity.PreviousClass = FilterVille.class;
                Intent intent = new Intent(getApplicationContext(),showPharmacies.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getSelectectWilayaData(int wilayacode) {
        ArrayList<Commune> coms = new ArrayList<>();
        if(wilayacode == 0){
            adapter= new ArrayAdapter<>(FilterVille.this,android.R.layout.simple_list_item_1,communes);
        }else{
            for(Commune commune:communes){
                if(commune.getWiyalaCode() == wilayacode){
                    coms.add(commune);
                }
            }
            adapter= new ArrayAdapter<>(FilterVille.this,android.R.layout.simple_list_item_1,coms);
        }
        _myListView.setAdapter(adapter);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intentprevious = new Intent(getApplicationContext(),MainActivity.class);
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
