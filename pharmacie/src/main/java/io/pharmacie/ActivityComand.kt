package io.pharmacie

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

import io.pharmacie.Retrofit.Api
import io.pharmacie.Retrofit.ResponseMessage
import io.pharmacie.Retrofit.repo
import io.pharmacie.models.Camand
import io.pharmacie.models.Commune
import kotlinx.android.synthetic.main.activity_comand.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class ActivityComand : AppCompatActivity() {
    private val btn: Button? = null
    private var imageview: ImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private val TAG = "MUSTAPHATESTCAMERA"
    internal var MY_PERMISSIONS_REQUEST_CODE = 123
    var repoCommand: repo = repo()
     var retrofitInterface= Api.createAvatar()


    var bitmapglobal:Bitmap ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comand)
        checkPermission()
        imageview = image as ImageView
        importeimg.setOnClickListener {
            choosePhotoFromGallary()
        }
        send.setOnClickListener {
            //alert()

            val file: File = bitmapToFile(bitmapglobal!!)
            val retrofit = Retrofit.Builder()
                .baseUrl(Api.Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create<Api>(Api::class.java!!)
            val response = repoCommand.uploadFile(file)
            Log.e("RESPONSE", response.toString())
            Log.e("MUSTAPHAPATH", bitmapToFile(bitmapglobal!!).absolutePath)
            // repoCommand.downloadImage(imageview!!,bitmap)
            //imageview!!.setImageBitmap(bitmapglobal)
            val pref = this!!.getSharedPreferences("ahlamfile", Context.MODE_PRIVATE)
            val emailLoc = pref.getString("email", "")
            var dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
            var month = Calendar.getInstance().get(Calendar.MONTH).toString()
            var year = Calendar.getInstance().get(Calendar.YEAR).toString()
            if (month.length == 1) month = "0$month"
            if (dayOfMonth.length == 1) dayOfMonth = "0$dayOfMonth"
            val date = "$dayOfMonth-$month-$year"
            Log.e("adem", "ademmm")
            val cmd = Camand(titredemande!!.text.toString(), emailLoc!!, "En attente", file.name, date.toString())
            val creatRespense = api.createCommand(cmd)
            creatRespense.enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                    val coms = response.body()
                    Log.e("responecomand", response.toString())

                }

                override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    //dialog.hide()
                }
            })

        }
    }

    fun choosePhotoFromGallary() {

        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(galleryIntent, GALLERY)
    }

    fun takePhotoFromCamera() {
        Log.e(TAG,"takePhotoFromCamera")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (ContextCompat.checkSelfPermission(
                this!! as Context,Manifest.permission.CAMERA
            )
            == PackageManager.PERMISSION_GRANTED
        )
            startActivityForResult(intent, CAMERA)
        else Toast.makeText(this!!.applicationContext, "Vous devez autoriser l'application à utilisé la cam", Toast.LENGTH_SHORT).show()

    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data.data

                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this!!.contentResolver, contentURI)
                    imageview!!.setImageBitmap(bitmap)
                    bitmapglobal = bitmap
                    // bitmapglobal  = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, contentURI)
                    //imageview!!.setImageBitmap(bitmapglobal)
                    //ADDED
                    // val path = saveImage(bitmap)
                    // Log.e("MUSTAPHAPATH","this is working here")
                    //  Log.e("MUSTAPHAPATH",path



                    Toast.makeText(this!!.applicationContext, "Image Saved!", Toast.LENGTH_SHORT).show()
                    /*val file:File = bitmapToFile(bitmap)
                    val response = repoCommand.uploadFile(file)
                    Log.e("RESPONSE",response.toString())
                    Log.e("MUSTAPHAPATH",bitmapToFile(bitmap).absolutePath)
                   // repoCommand.downloadImage(imageview!!,bitmap)
                    imageview!!.setImageBitmabitmapp()*/
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this!!.applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }

        }
        else if (requestCode == CAMERA)
        {
            Log.e(TAG,"REQUEST CAMERA CODE UNTILI HERE IT WORKS")

            if(data != null) {
                val thumbnail = data.extras!!.get("data") as Bitmap
                imageview!!.setImageBitmap(thumbnail)
                // saveImage(thumbnail)
                Toast.makeText(this!!.applicationContext, "Image Saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bitmapToFile(bitmap:Bitmap): File {
        // Get the context wrapper
        val wrapper = ContextWrapper(this!!.applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file,"${UUID.randomUUID()}.jpg")

        try{
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e:IOException){
            e.printStackTrace()
        }

        return file
        // Return the saved bitmap uri
        //return Uri.parse(file.absolutePath)
    }


    fun saveImage(myBitmap: Bitmap):String {
        /*Log.e(TAG,"SAVEIMAGE IN ")

           val bytes = ByteArrayOutputStream()
           myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
           Log.e("TESTPATH","TEST1")
           val wallpaperDirectory = File(
               Environment().toString() + IMAGE_DIRECTORY)
           // have the object build the directory structure, if needed.
           Log.d(TAG,wallpaperDirectory.toString())
           Log.e("TESTPATH","TEST2")
           if (!wallpaperDirectory.exists())
           {
               Log.e("TESTPATH","WALLPAPER EXISTS")
               Log.d(TAG,"mkdirs wallpaperdirectory")
               wallpaperDirectory.mkdirs()
           }
           try
           {
               Log.d(TAG,wallpaperDirectory.toString())
               val f = File(wallpaperDirectory, ((Calendar.getInstance()
                   .getTimeInMillis()).toString() + ".jpg"))
               f.createNewFile()
               Log.e("TESTPATH","TEST3")
               val fo = FileOutputStream(f)
               fo.write(bytes.toByteArray())
               MediaScannerConnection.scanFile(activity!!,
                   arrayOf(f.getPath()),
                   arrayOf("image/jpeg"), null)
               fo.close()
               Log.d(TAG, "File Saved::--->" + f.getAbsolutePath())
               Log.e("TESTPATH",f.getAbsolutePath())
               return f.getAbsolutePath()
           }
           catch (e1: IOException) {
               Log.e("TESTPATH","TEST4")
               Log.e("TESTPATH",e1.toString())
               e1.printStackTrace()
           }*/
        return ""
    }


    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
    }

    protected fun checkPermission() {
        if (((ContextCompat.checkSelfPermission(this!!, Manifest.permission.CAMERA)
                    + ContextCompat.checkSelfPermission(
                this!!, Manifest.permission.READ_EXTERNAL_STORAGE)) !== PackageManager.PERMISSION_GRANTED))
        {
            // Do something, when permissions not granted
            if ((ActivityCompat.shouldShowRequestPermissionRationale(
                    this!!, Manifest.permission.CAMERA)
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                    this!!, Manifest.permission.READ_EXTERNAL_STORAGE)))
            {
                // If we should give explanation of requested permissions
                // Show an alert dialog here with request explanation
                val builder = android.app.AlertDialog.Builder(this!!)
                builder.setMessage(("Camera, Read Contacts and Write External" + " Storage permissions are required to do the task."))
                builder.setTitle("Please grant those permissions")
                builder.setPositiveButton("OK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface:DialogInterface, i:Int) {
                        ActivityCompat.requestPermissions(
                            this@ActivityComand!!,
                            arrayOf<String>(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_CODE
                        )
                    }
                })
                builder.setNeutralButton("Cancel", null)
                val dialog = builder.create()
                dialog.show()
            }
            else
            {
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                    this!!,
                    arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_CODE
                )
            }
        }
        else
        {
            // Do something, when permissions are already granted
            Toast.makeText(this!!.applicationContext, "Permissions already granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode:Int, permissions:Array<String>, grantResults:IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CODE -> {
                // When request is cancelled, the results array are empty
                if (((grantResults.size > 0) /*&& (((grantResults[0]
                            + grantResults[2]) == PackageManager.PERMISSION_GRANTED))*/))
                {
                    // Permissions are granted
                    Toast.makeText(this!!.applicationContext, "Permissions granted.", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    // Permissions are denied
                    Toast.makeText(this!!.applicationContext, "Permissions denied.", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
    fun alert (){
        val builder = androidx.appcompat.app.AlertDialog.Builder(this.applicationContext)
        builder.setTitle("Demande Saidaliyati")
        builder.setMessage("Votre demande a été envoyer avec succée ,Nous esaiyons de vous repondre dés que possible")
        builder.setPositiveButton("Continue", null);
        val dialog = builder.create()
        dialog.show()
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intentprevious = Intent(applicationContext,MainActivity.PreviousClass)
            startActivity(intentprevious)
            finish()
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }

        if (keyCode == KeyEvent.KEYCODE_HOME) {

        }

        // // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event)

    }

}
