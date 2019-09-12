package io.pharmacie.Maps;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.pharmacie.MainActivity;
import io.pharmacie.R;
import io.pharmacie.Retrofit.Api;
import io.pharmacie.Retrofit.RetrofitClient;
import io.pharmacie.models.pharmacy;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapMainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapMainActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    public static String movingCamera;
    public static String showPharmacy;
    public static String pharmacyOwnerName;
    ArrayList<pharmacy> pharmaciesGarde = new ArrayList<>();
    private static String adresses[] = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        Log.d(TAG,"HERE IS GOOD");

        View decorView = getWindow().getDecorView();
        MainActivity.hideSystemUI(decorView);

        getLocationPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"HERE IS GOOD");
        mMap = googleMap;
        if (mLocationPermissionsGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            switch(movingCamera) {
                case "ONCALL":
                    //closestAddress();
                    getPharmacyGard();
                    break;
                case "SHOWPHARMACY":
                    Log.d(TAG,"SHOWPHARMACY");
                    try {
                        Log.d(TAG,"SHOWPHARMACY success");
                        showPharmacy();
                        Log.d(TAG,"SHOWPHARMACY success 222");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:

            }
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void showPharmacy() throws IOException {
        Geocoder geocoder = new Geocoder(MapMainActivity.this);
        List<Address> list = new ArrayList<>();
        Address closectPHarmacy = null;
        try {
            Log.d(TAG,"adress found");
            ProgressDialog dialog=new ProgressDialog(MapMainActivity.this);
            dialog.setMessage(getResources().getString(R.string.loading));
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog.show();

            list = geocoder.getFromLocationName(showPharmacy,1);
            if(list.size()>0) {
                Address adr = list.get(0);
                Log.d(TAG,adr.toString());
                moveCamera(new LatLng(adr.getLatitude(),adr.getLongitude()),DEFAULT_ZOOM,pharmacyOwnerName);
                dialog.hide();
            }
        } catch (IOException e){
            Log.d(TAG,"IOException: "+  e);
        }
    }


    private void getPharmacyGard(){

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        String formattedDate = df.format(c);

        String formattedDateprim = "06-05-2019";

        ProgressDialog dialog=new ProgressDialog(MapMainActivity.this);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        Retrofit retrofit = RetrofitClient.getInstance();
        Api api = retrofit.create(Api.class);
        Call<List<pharmacy>> call = api.getPharmaciesGarde(formattedDateprim);
        call.enqueue(new Callback<List<pharmacy>>() {
            @Override
            public void onResponse(Call<List<pharmacy>> call, Response<List<pharmacy>> response) {

                List<pharmacy> pha = response.body();
                if(pha == null){
                     Log.d(TAG,"NO PHARMACY IN ON CALL");
                }else {
                    Log.d(TAG,"THERE ARE PHARMACIES ON CALL 1111111 " +formattedDate);

                    for (pharmacy ph : pha) {
                        pharmaciesGarde.add(ph);
                    }
                    if(pharmaciesGarde.size()==0) {
                        Log.d(TAG, "THERE ARE PHARMACIES ON CALL 22222222" + pharmaciesGarde.size());
                        Toast.makeText(MapMainActivity.this, "NO PHARMACY IN ON CALL", Toast.LENGTH_SHORT).show();
                    }else {
                        int i = 0;
                        for (pharmacy ph : pharmaciesGarde) {
                            adresses[i] = ph.getAdresse();
                            Log.d(TAG, "ADRESS  :" + adresses[i]);
                            i++;
                        }
                        Log.d(TAG, "THERE ARE PHARMACIES ON CALL 33333333");

                        closestAddress();
                       dialog.hide();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<pharmacy>> call, Throwable t) {
            }
        });

    }

    private void closestAddress(){
        Geocoder geocoder = new Geocoder(MapMainActivity.this);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        String formattedDate = df.format(c);

        Log.d(TAG,"the date of today  :" + formattedDate);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"onComplete : found location!");
                           // String[] pharmacyAddresses = {"91 BOULEVARD KRIM BELKACEM ALGER CENTRE","18 RUE MELHOUSE ALGER CENTRE","18 RUE MELHOUSE ALGER CENTRE",
                               //     "33 RUE DIDOUCHE MOURAD ALGER CENTRE","37 AVENUE DES TROIS FRERES MADANI EL MADANIA","CITE DIAR SAADA"};
                            Location currentLocation = (Location) task.getResult();
                            double distance = 0.0f;
                            double distanceCaculated;
                            Address closectPHarmacy = null;
                            List<Address> list = new ArrayList<>();
                            Log.d(TAG,"THIS IS CLOSENT PHARMACY"+ adresses[0]);
                            for(String adress:adresses){
                                try {
                                    if(adress!=null) {
                                        list = geocoder.getFromLocationName(adress, 1);
                                        if (list.size() > 0) {
                                            Address adr = list.get(0);
                                            if (adr == null || currentLocation == null) {
                                                if (adr == null) Log.d(TAG, "adr null");
                                                else Log.d(TAG, "currentlocation null");
                                            } else {
                                                distanceCaculated = CalculationByDistance(new LatLng(adr.getLatitude(), adr.getLongitude()), new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                                                if (distance == 0.0f || distance > distanceCaculated) {
                                                    distance = distanceCaculated;
                                                    Log.d(TAG, adress + "this is its destance " + distance);
                                                    closectPHarmacy = adr;
                                                }
                                            }
                                        }
                                    }
                                } catch (IOException e) {
                                    Log.d(TAG,"Address: " +adress + "  " +  e);
                                }
                            }
                            if(closectPHarmacy != null)
                                moveCamera(new LatLng(closectPHarmacy.getLatitude(),closectPHarmacy.getLongitude()),DEFAULT_ZOOM,"this is the closest pharmacy");
                            else Log.d(TAG, "adr is null");
                        }else{
                            Log.d(TAG,"onComplete current location is null");
                            Toast.makeText(MapMainActivity.this,"unable to get current location ",Toast.LENGTH_SHORT);
                        }
                    }
                });
            }

        }catch(SecurityException e){
            Log.e(TAG,"getDevicesLocation SecurityException: "+ e);
        }
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    private void initMap(){
        Toast.makeText(MapMainActivity.this,"Map is ready",Toast.LENGTH_SHORT);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapMainActivity.this);
    }

    private void getDevicesLocation(){
        Log.d(TAG,"getDeviceLocation : getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"onComplete : found location!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFAULT_ZOOM, getResources().getString(R.string.myLocation));
                        }else{
                            Log.d(TAG,"onComplete current location is null");
                            Toast.makeText(MapMainActivity.this,"unable to get current location ",Toast.LENGTH_SHORT);
                        }
                    }
                });
            }

        }catch(SecurityException e){
            Log.e(TAG,"getDevicesLocation SecurityException: "+ e);
        }
    }

    private void moveCamera(LatLng latlng,float zoom,String title){
        Log.d(TAG,"moveCamera: moving the camera ot: 1st");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
        if(!title.equals(getResources().getString(R.string.myLocation))){
            MarkerOptions options = new MarkerOptions()
                    .position(latlng)
                    .title(title);
            mMap.addMarker(options);
        }
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
     if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "HERE GETLOCATIONPERMISSION  IS GOOD");
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length >0) {
                    for(int i = 0; i > grantResults.length ; i++){
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    initMap();
                }

            }
        }
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
