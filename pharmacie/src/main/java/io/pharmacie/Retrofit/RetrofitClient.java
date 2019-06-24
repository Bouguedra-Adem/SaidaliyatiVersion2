package io.pharmacie.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstance() {
        if(instance==null)

            instance = new Retrofit.Builder()
                    .baseUrl(Api.Base_Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return instance;
    }
}
