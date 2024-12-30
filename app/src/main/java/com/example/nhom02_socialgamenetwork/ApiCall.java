package com.example.nhom02_socialgamenetwork;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {
    public static final String base_url = "https://b028-14-173-22-191.ngrok-free.app";
    private static Retrofit retrofit = null;

    public static Retrofit getClient (){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
