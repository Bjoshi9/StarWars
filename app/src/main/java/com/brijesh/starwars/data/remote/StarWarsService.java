package com.brijesh.starwars.data.remote;

import com.brijesh.starwars.data.model.People;
import com.brijesh.starwars.data.model.PeopleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface StarWarsService {
    @GET("people")
    Call<PeopleResponse> getPeopleList(@Query("page") int pageNo);

    @GET
    Call<People> getPeopleDetail(@Url String url);
}
