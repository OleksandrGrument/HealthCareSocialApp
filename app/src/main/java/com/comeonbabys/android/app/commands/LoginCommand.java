package com.comeonbabys.android.app.commands;

import android.content.Context;
import android.os.Bundle;

import com.comeonbabys.android.app.db.dto.UserDTO;

import retrofit2.Response;


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
