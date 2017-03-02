package com.comeonbaby.android.app.commands;

import android.content.Context;
import android.os.Bundle;

import com.comeonbaby.android.app.db.dto.UserDTO;

import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by olegs on 07.02.2017.
 */

public class LoginCommand extends BaseCommand {

    public LoginCommand(Context context, String successAction, String failAction) {
        super(context, successAction, failAction);
    }

    @Override
    protected Bundle perform(Bundle extras) throws Exception {
        UserDTO user = (UserDTO) extras.getSerializable(CommandConstants.EXTRA_SYSTEM_USER);
        Response response = TestRetrofit.getApi().loginUser(user).execute();
        return null;

    }


}
