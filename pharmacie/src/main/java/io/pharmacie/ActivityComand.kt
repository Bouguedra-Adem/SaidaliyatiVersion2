package io.pharmacie

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.ArrayList

import io.pharmacie.Retrofit.Api
import io.pharmacie.Retrofit.Api.Companion.createAvatar


class ActivityComand : AppCompatActivity() {
    private val btn: Button? = null
    private val imageview: ImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private val TAG = "MUSTAPHATESTCAMERA"
    internal var MY_PERMISSIONS_REQUEST_CODE = 123
     var retrofitInterface= Api.createAvatar()


    internal var bitmapglobal: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comand)
    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "\${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format

            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file
        // Return the saved bitmap uri
        //return Uri.parse(file.absolutePath)
    }

    private fun choosePhotoFromGallary() {

        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(galleryIntent, GALLERY)
    }

    protected fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(
                        this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Do something, when permissions not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // If we should give explanation of requested permissions
                // Show an alert dialog here with request explanation
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Camera, Read Contacts and Write External" + " Storage permissions are required to do the task.")
                builder.setTitle("Please grant those permissions")
                val `object`: DialogInterface.OnClickListener

                builder.setNeutralButton("Cancel", null)
                val dialog = builder.create()
                dialog.show()
            }

        }

    }
}
