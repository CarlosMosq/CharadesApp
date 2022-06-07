package com.company.charadesapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPICard {

    @GET("CarlosMosq/Game-Cards/main/cards-sample.json")
    Call<List<ModelClassCard>> getModelClass();

}
