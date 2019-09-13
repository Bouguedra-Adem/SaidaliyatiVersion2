package io.pharmacie.Maps

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

import io.pharmacie.MainActivity
import io.pharmacie.R
import io.pharmacie.Retrofit.Api
import io.pharmacie.Retrofit.RetrofitClient
import io.pharmacie.models.pharmacy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import com.google.android.gms.tasks.OnCompleteListener as OnCompleteListener1

class MapMainActivity : AppCompatActivity(), OnMapReadyCallback {

   private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
     private var mLocationPermissionsGranted: Boolean? = false
     private var mMap: GoogleMap? = null
     internal var pharmaciesGarde = ArrayList<pharmacy>()

    private val ZOOM:Float  = 12f
    private lateinit var lastLocation: Location


    override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.map_layout)


         val decorView = window.decorView
         MainActivity.hideSystemUI(decorView)


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
         getLocationPermission()
         //getPharmacyGard()
         //getMyCurrentLocation()
     }

     override fun onMapReady(googleMap: GoogleMap) {
         Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show()


         mMap = googleMap


        if (mLocationPermissionsGranted!!) {
             if (ActivityCompat.checkSelfPermission(
                     this,
                     Manifest.permission.ACCESS_FINE_LOCATION
                 ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                     this,
                     Manifest.permission.ACCESS_COARSE_LOCATION
                 ) != PackageManager.PERMISSION_GRANTED
             ) {
                 return
             }
             mMap!!.isMyLocationEnabled = true
             when (movingCamera) {
                 "ONCALL" ->
                     //closestAddress();
                     getPharmacyGard()
                 "SHOWPHARMACY" -> {

                     try {

                         showPharmacy()


                     } catch (e: IOException) {
                         e.printStackTrace()
                     }

                 }
             }
             //mMap.getUiSettings().setMyLocationButtonEnabled(false);
         }
     }

     @Throws(IOException::class)
     private fun showPharmacy() {
         val geocoder = Geocoder(this@MapMainActivity)
         var list: List<Address> = ArrayList()
         val closectPHarmacy: Address? = null
         try {

             val dialog = ProgressDialog(this@MapMainActivity)
             dialog.setMessage(resources.getString(R.string.loading))
             dialog.setCancelable(false)
             dialog.setInverseBackgroundForced(false)
             dialog.show()

             list = geocoder.getFromLocationName(showPharmacy, 1)
             if (list.size > 0) {
                 val adr = list[0]
                 Log.d(TAG, adr.toString())
                 moveCamera(LatLng(adr.latitude, adr.longitude), DEFAULT_ZOOM, pharmacyOwnerName!!)
                 dialog.hide()
             }
         } catch (e: IOException) {

         }

     }

    private fun getMyCurrentLocation(){


        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){


            mMap?.isMyLocationEnabled = true
            mFusedLocationProviderClient?.lastLocation?.addOnSuccessListener(this) { location ->
                // Got last known location. In some rare situations this can be null.
                // 3
                if (location != null) {


                    lastLocation = location
                 val currentLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                    moveCamera(currentLatLng,ZOOM,"My location")
                   // mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, ZOOM))
                }
            }
        }
    }



    private fun getPharmacyGard() {

         val c = Calendar.getInstance().time

         val df = SimpleDateFormat("dd-MM-yyyy")

         val formattedDate = df.format(c)

         //val formattedDateprim = "06-05-2019"



         val dialog = ProgressDialog(this@MapMainActivity)
         dialog.setMessage(resources.getString(R.string.loading))
         dialog.setCancelable(false)
         dialog.setInverseBackgroundForced(false)
         dialog.show()

         val retrofit = RetrofitClient.getInstance()
         val api = retrofit!!.create(Api::class.java)
         val call = api.getPharmaciesGarde(formattedDate)
         call.enqueue(object : Callback<List<pharmacy>> {
             override fun onResponse(call: Call<List<pharmacy>>, response: Response<List<pharmacy>>) {

                 val pha = response.body()
                 if (pha == null) {
                     Log.d(TAG, "NO PHARMACY IN ON CALL")
                 } else {
                     Log.d(TAG, "THERE ARE PHARMACIES ON CALL 1111111 $formattedDate")

                     for (ph in pha) {
                         pharmaciesGarde.add(ph)
                     }
                     if (pharmaciesGarde.size == 0) {
                         Log.d(TAG, "THERE ARE PHARMACIES ON CALL 22222222" + pharmaciesGarde.size)
                         Toast.makeText(this@MapMainActivity, "NO PHARMACY IN ON CALL", Toast.LENGTH_SHORT).show()
                     } else {
                         var i = 0
                         for (ph in pharmaciesGarde) {
                             adresses[i] = ph.adresse
                             Log.d(TAG, "ADRESS  :" + adresses[i])
                             i++
                         }
                         Log.d(TAG, "THERE ARE PHARMACIES ON CALL 33333333")

                         closestAddress()
                         dialog.hide()
                     }
                 }
             }

             override fun onFailure(call: Call<List<pharmacy>>, t: Throwable) {}
         })

     }

    private fun closestAddress() {
         val geocoder = Geocoder(this@MapMainActivity)

         val c = Calendar.getInstance().time

         val df = SimpleDateFormat("dd-MM-yyyy")

         val formattedDate = df.format(c)

         Log.d(TAG, "the date of today  :$formattedDate")

         //mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap?.isMyLocationEnabled = true
            mFusedLocationProviderClient?.lastLocation?.addOnSuccessListener(this) { location ->
                // Got last known location. In some rare situations this can be null.
                // 3
                if (location != null) {
                    Log.d(TAG, "GET MY CURRENT LOCATION INSIDE")

                    val currentLocation = location
                    Log.d(TAG, "onComplete : found location!")
                    // String[] pharmacyAddresses = {"91 BOULEVARD KRIM BELKACEM ALGER CENTRE","18 RUE MELHOUSE ALGER CENTRE","18 RUE MELHOUSE ALGER CENTRE",
                    //     "33 RUE DIDOUCHE MOURAD ALGER CENTRE","37 AVENUE DES TROIS FRERES MADANI EL MADANIA","CITE DIAR SAADA"};
                    var distance = 0.0
                    var distanceCaculated: Double
                    var closectPHarmacy: Address? = null
                    var list: List<Address> = ArrayList()
                    Log.d(TAG, "THIS IS CLOSENT PHARMACY" + adresses[0])
                    for (adress in adresses) {
                        try {
                            if (adress != null) {
                                list = geocoder.getFromLocationName(adress, 1)
                                if (list.size > 0) {
                                    val adr = list[0]
                                    if (adr == null || currentLocation == null) {
                                        if (adr == null)
                                            Log.d(TAG, "adr null")
                                        else
                                            Log.d(TAG, "currentlocation null")
                                    } else {
                                        distanceCaculated = CalculationByDistance(
                                            LatLng(adr.latitude, adr.longitude),
                                            LatLng(currentLocation.latitude, currentLocation.longitude)
                                        )
                                        if (distance == 0.0 || distance > distanceCaculated) {
                                            distance = distanceCaculated
                                            Log.d(TAG, adress + "this is its destance " + distance)
                                            closectPHarmacy = adr
                                        }
                                    }
                                }
                            }
                        } catch (e: IOException) {
                            Log.d(TAG, "Address: $adress  $e")
                        }

                    }
                    if (closectPHarmacy != null)
                        moveCamera(
                            LatLng(closectPHarmacy.latitude, closectPHarmacy.longitude),
                            DEFAULT_ZOOM,
                            "this is the closest pharmacy"
                        )
                    else
                        Log.d(TAG, "adr is null")
                }
            }
        }
     }

    fun CalculationByDistance(StartP: LatLng, EndP: LatLng): Double {
         val Radius = 6371// radius of earth in Km
         val lat1 = StartP.latitude
         val lat2 = EndP.latitude
         val lon1 = StartP.longitude
         val lon2 = EndP.longitude
         val dLat = Math.toRadians(lat2 - lat1)
         val dLon = Math.toRadians(lon2 - lon1)
         val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + (Math.cos(Math.toRadians(lat1))
                 * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                 * Math.sin(dLon / 2))
         val c = 2 * Math.asin(Math.sqrt(a))
         val valueResult = Radius * c
         val km = valueResult / 1
         val newFormat = DecimalFormat("####")
         val kmInDec = Integer.valueOf(newFormat.format(km))
         val meter = valueResult % 1000
         val meterInDec = Integer.valueOf(newFormat.format(meter))
         Log.i(
             "Radius Value", "" + valueResult + "   KM  " + kmInDec
                     + " Meter   " + meterInDec
         )

         return Radius * c
     }

    private fun initMap() {
         Toast.makeText(this@MapMainActivity, "Map is ready", Toast.LENGTH_SHORT)
         val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
         mapFragment!!.getMapAsync(this@MapMainActivity)
     }

     private fun moveCamera(latlng: LatLng, zoom: Float, title: String) {
         Log.d(TAG, "moveCamera: moving the camera ot: 1st")
         mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom))
         val options = MarkerOptions()
           .position(latlng)
           .title(title)
            mMap!!.addMarker(options)
       if (title != resources.getString(R.string.myLocation)) {
             val options = MarkerOptions()
                 .position(latlng)
                 .title(title)
             mMap!!.addMarker(options)
         }
     }

     private fun getLocationPermission() {
         val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
         Log.d(TAG, "OUTSIDE IF")
         if (ContextCompat.checkSelfPermission(this.applicationContext,FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
             Log.d(TAG, "HERE GETLOCATIONPERMISSION  IS GOOD")
             if (ContextCompat.checkSelfPermission(this.applicationContext,COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                 mLocationPermissionsGranted = true
                 Log.d(TAG, "TEST:1")
                 initMap()
             } else {
                 Log.d(TAG, "TEST:2")
                 ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
             }
         }else {
             Log.d(TAG, "TEST:3")
             ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
             if (ContextCompat.checkSelfPermission(this.applicationContext,FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                 Log.d(TAG, "HERE GETLOCATIONPERMISSION  IS GOOD")
                 if (ContextCompat.checkSelfPermission(this.applicationContext,COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                     mLocationPermissionsGranted = true
                     Log.d(TAG, "TEST:1")
                     initMap()
                 } else {
                     Log.d(TAG, "TEST:2")
                     ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
                 }
             }
         }
     }

     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
         mLocationPermissionsGranted = false
         when (requestCode) {
             LOCATION_PERMISSION_REQUEST_CODE -> {
                 if (grantResults.size > 0) {
                     var i = 0
                     while (i > grantResults.size) {
                         if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                             mLocationPermissionsGranted = false
                             return
                         }
                         i++
                     }
                     mLocationPermissionsGranted = true
                     initMap()
                 }

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
         private val TAG = "MapMainActivity"
         private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
         private val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
         private val LOCATION_PERMISSION_REQUEST_CODE = 1234
         private val DEFAULT_ZOOM = 15f
         var movingCamera: String? = null
         var showPharmacy: String? = null
         var pharmacyOwnerName: String? = null
         private val adresses = arrayOfNulls<String>(100)
     }
}
