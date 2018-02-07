package com.free.ahmed.twitterintcore;

import android.app.Application;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ahmed on 2/7/2018.
 */

public class TwitterApplication {
    /*@Override
    public void onCreate() {
        super.onCreate();
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(getCacheDir(), cacheSize);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).build();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://api.twitter.com/").
                client(okHttpClient).addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
    }*/
}
