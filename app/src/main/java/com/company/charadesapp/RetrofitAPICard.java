package com.company.charadesapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/*GET was not annotated as the URL is dynamic (depends on the phone's language),
so URL annotation was used instead*/
public interface RetrofitAPICard {

    @GET
    Call<List<ModelClassCard>> getModelClass(@Url String url);

}
