package com.example.user.mapapplication;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 6/21/2017.
 */
public class RestManager {

    private String path = "http://iranhis.com";
    RetrofitService retrofitService;

    public RetrofitService getRetrofitService()
    {
        if ( retrofitService == null )
        {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors

// add logging as last interceptor
            httpClient.addInterceptor(logging);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(path)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            retrofitService = retrofit.create(RetrofitService.class);
        }
        return retrofitService;
    }
}