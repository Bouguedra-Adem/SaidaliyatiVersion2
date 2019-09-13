package io.pharmacie.Retrofit

import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class repo {
    var retrofitInterface = Api.createAvatar()
   // var apiService = Api.create()
    fun uploadFile(file: File) {



        val requestFile = RequestBody.create(
            MediaType.parse("image/*"),
            file
        )
        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile)
        // add another part within the multipart request
        val descriptionString = "hello, this is description speaking"
        val description = RequestBody.create(
            okhttp3.MultipartBody.FORM, descriptionString)
        // finally, execute the request
        val call = retrofitInterface.upload(body)
        call.enqueue(object: Callback<ResponseUpload> {
            override fun onResponse(call: Call<ResponseUpload>, response:retrofit2.Response<ResponseUpload>) {

            }
            override fun onFailure(call: Call<ResponseUpload>, t:Throwable) {
            }
        })
    }




    }



