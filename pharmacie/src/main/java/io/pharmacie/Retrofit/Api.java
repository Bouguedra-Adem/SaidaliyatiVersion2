
package io.pharmacie.Retrofit;

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

    String Base_Url = "https://tranquil-escarpment-61375.herokuapp.com/";
    //String Base_Url = "http://10.0.2.2:3000/";

    @GET("/login/{useremail}/{pwd}")
    Call<User> loginUser(@Path(value="useremail") String email,
                         @Path(value="pwd") String pwd);

    @GET("/communes")
    Call<List<Commune>> getCommunes();

    @GET("/pharmacies/{commune}")
    Call<List<pharmacy>> getPharmacies(@Path(value="commune",encoded = true) String commune);

    @GET("/pharmaciegarde/{dateGarde}")
    Call<List<pharmacy>> getPharmaciesGarde(@Path(value="dateGarde",encoded = true) String dateGarde);

    @POST("/users")
    Call<User> createAccount(@Body User user);
}/*package io.pharmacie.Retrofit;

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

