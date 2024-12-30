package com.example.nhom02_socialgamenetwork;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CallApiRetrofit {
    @POST("predict")
    Call<String> predictToxicity(@Body String text);
}
