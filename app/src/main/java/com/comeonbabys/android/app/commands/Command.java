package com.comeonbabys.android.app.commands;

import android.os.Bundle;

/**
 * Created by Oleg Shevchenko on 07.02.2017.
 */

public interface Command {
    public void execute(Bundle bundle) throws Exception;
}
