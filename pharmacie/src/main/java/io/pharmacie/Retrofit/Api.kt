package io.pharmacie.Retrofit

import java.util.concurrent.TimeUnit

import io.pharmacie.models.Camand
import io.pharmacie.models.Commune
import io.pharmacie.models.User
import io.pharmacie.models.pharmacy
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface Api {

    @get:GET("/communes")
    val communes: Call<List<Commune>>
    //String Base_Url = "http://10.0.2.2:3000/";

    @GET("/login/{useremail}/{pwd}")
    fun loginUser(
        @Path(value = "useremail") email: String,
        @Path(value = "pwd") pwd: String
    ): Call<User>

    @GET("/pharmacies/{commune}")
    fun getPharmacies(@Path(value = "commune", encoded = true) commune: String): Call<List<pharmacy>>

    @GET("/pharmaciegarde/{dateGarde}")
    fun getPharmaciesGarde(@Path(value = "dateGarde", encoded = true) dateGarde: String): Call<List<pharmacy>>

    @POST("/users")
    fun createAccount(@Body user: User): Call<User>

    @Multipart
    @POST("/upload-avatar")
    fun upload(@Part file: MultipartBody.Part): Call<ResponseUpload>

    @POST("/command")
    fun createCommand(@Body cmd: Camand): Call<ResponseMessage>

    @GET(" /command/userEmail/{userEmail}")
    fun getCmdUserEmail(@Path(value = "userEmail") userEmail: String): Call<List<Camand>>

    @GET("/command/nomCmd/{nomCmd}")
    fun getCmdNon(@Path(value = "nomCmd") nomCmd: String): Call<Camand>

    companion object {

        val Base_Url = "https://tranquil-escarpment-61375.herokuapp.com/"

        fun createAvatar(): Api {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://ancient-harbor-80131.herokuapp.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(Api::class.java)
        }
    }

} /*package io.pharmacie.Retrofit;

import java.util.List;
import io.pharmacie.models.Commune;
import io.pharmacie.models.User;
import io.pharmacie.models.pharmacy;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    String Base_Url = "https://ancient-harbor-80131.herokuapp.com/";
    //String Base_Url = "http://10.0.2.2:3000/";

    @GET("/users/{phone}/{pwd}")
    Call<User> loginUser(@Path(value="phone") String phone,
                              @Path(value="pwd") String pwd);

    @GET("/communes")
    Call<List<Commune>> getCommunes();

    @GET("/pharmacies/{commune}")
    Call<List<pharmacy>> getPharmacies(@Path(value="commune",encoded = true) String commune);

    @GET("/pharmaciegarde/{dateGarde}")
    Call<List<pharmacy>> getPharmaciesGarde(@Path(value="dateGarde",encoded = true) String dateGarde);


    @POST("/users")
    Call<User> createAccount(@Body User user);



}*/

