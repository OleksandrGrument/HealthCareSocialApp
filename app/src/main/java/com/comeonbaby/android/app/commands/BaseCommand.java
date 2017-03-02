package com.comeonbaby.android.app.commands;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Oleg Shevchenko on 07.02.2017.
 */

public abstract class BaseCommand implements Command {
    protected final Context context;
    protected final String successAction;
    protected final String failAction;

    public BaseCommand(Context context, String successAction, String failAction) {
        this.context = context;
        this.successAction = successAction;
        this.failAction = failAction;
    }

    public void execute(Bundle bundle) throws Exception {
        Bundle result;
        try {
            result = perform(bundle);
            sendResult(result, successAction);
        } catch (Exception exc) {
            exc.printStackTrace();
            result = new Bundle();
            result.putSerializable(CommandConstants.EXTRA_EXCEPTION, exc);
            result.putString(CommandConstants.EXTRA_COMMAND_ACTION, failAction);
            sendResult(result, failAction);
            throw exc;
        }
    }

    protected void sendResult(Bundle result, String action) {
        Intent intent = new Intent(action);
        if (null != result) {
            intent.putExtras(result);
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    protected abstract Bundle perform(Bundle extras) throws Exception;
}
