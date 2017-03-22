package com.comeonbabys.android.app.commands;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by olegs on 07.02.2017.
 */

public class TestRetrofit {
    private static ComeOnBabyApi comeOnBabyApi;
    private Retrofit retrofit;

    public TestRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("127.0.0.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        comeOnBabyApi = retrofit.create(ComeOnBabyApi.class);
    }

    public static ComeOnBabyApi getApi() {return comeOnBabyApi;}
}
