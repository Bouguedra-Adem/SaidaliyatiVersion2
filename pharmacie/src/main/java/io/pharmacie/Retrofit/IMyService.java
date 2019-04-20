package io.pharmacie.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> sendSMS(@Field("accountSid") String accountSid,
                               @Field("authToken") String authToken,
                               @Field("fromPhoneNumber") String fromPhoneNumber,
                               @Field("password") String password,
                               @Field("lastname") String Lastname,
                               @Field("firstname") String Firstname,
                               @Field("email") String email,
                               @Field("PhoneNumber") String PhoneNumber,
                               @Field("SSN") String SSN);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("PhoneNumber") String PhoneNumber,
                                 @Field("password") String password);
}
