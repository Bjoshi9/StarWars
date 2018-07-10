package com.brijesh.starwars.data.remote;

import com.brijesh.starwars.BuildConfig;
import com.brijesh.starwars.data.model.ApiError;
import com.brijesh.starwars.data.model.HttpError;
import com.brijesh.starwars.data.model.People;
import com.brijesh.starwars.data.model.PeopleResponse;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StarWarsApiClient {
    private final EventBus mEventBus;
    private StarWarsService starWarsService;

    public StarWarsApiClient(EventBus eventBus) {
        mEventBus = eventBus;
        setStarWarsService();
    }

    private void setStarWarsService() {
        if (starWarsService == null) {
            OkHttpClient okHttpClient;
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(2, TimeUnit.MINUTES)
                        .readTimeout(2, TimeUnit.MINUTES)
                        .addInterceptor(httpLoggingInterceptor)
                        .writeTimeout(2, TimeUnit.MINUTES)
                        .build();
            } else {
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(2, TimeUnit.MINUTES)
                        .readTimeout(2, TimeUnit.MINUTES)
                        .writeTimeout(2, TimeUnit.MINUTES)
                        .build();
            }
            starWarsService = new Retrofit.Builder()
                    .baseUrl("https://swapi.co/api/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(StarWarsService.class);
        }
    }

    public void getPeopleList(int pageNo) {
        Call<PeopleResponse> callBack = starWarsService.getPeopleList(pageNo);
        callBack.enqueue(new Callback<PeopleResponse>() {
            @Override
            public void onResponse(@NonNull Call<PeopleResponse> call, @NonNull Response<PeopleResponse> response) {
                if (response.isSuccessful()) {
                    mEventBus.post(response.body());
                } else {
                    mEventBus.post(new HttpError(response.code(), ApiConstants.UrlCodes.PEOPLE_LIST));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PeopleResponse> call, @NonNull Throwable t) {
                mEventBus.post(new ApiError(ApiConstants.UrlCodes.PEOPLE_LIST));
            }
        });
    }

    public void getPeopleDetail(String url) {
        Call<People> callBack = starWarsService.getPeopleDetail(url);
        callBack.enqueue(new Callback<People>() {
            @Override
            public void onResponse(@NonNull Call<People> call, @NonNull Response<People> response) {
                if (response.isSuccessful()) {
                    mEventBus.post(response.body());
                } else {
                    mEventBus.post(new HttpError(response.code(), ApiConstants.UrlCodes.PEOPLE_DETAIL));
                }
            }

            @Override
            public void onFailure(@NonNull Call<People> call, @NonNull Throwable t) {
                mEventBus.post(new ApiError(ApiConstants.UrlCodes.PEOPLE_DETAIL));
            }
        });
    }
}
