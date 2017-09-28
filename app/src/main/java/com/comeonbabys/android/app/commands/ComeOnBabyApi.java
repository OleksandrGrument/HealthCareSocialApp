package com.comeonbabys.android.app.commands;

import com.comeonbabys.android.app.db.dto.UserDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface ComeOnBabyApi {
    @GET(CommandConstants.SERVER_PATH_LOGIN)
    Call<UserDTO> loginUser(@Query("user") UserDTO user);
}
