package com.comeonbaby.android.app.commands;

import com.comeonbaby.android.app.db.dto.UserDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by olegs on 07.02.2017.
 */

public interface ComeOnBabyApi {
    @GET(CommandConstants.SERVER_PATH_LOGIN)
    Call<UserDTO> loginUser(@Query("user") UserDTO user);
}
