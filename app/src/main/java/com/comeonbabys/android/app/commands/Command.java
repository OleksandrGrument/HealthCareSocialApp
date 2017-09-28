package com.comeonbabys.android.app.commands;

import android.os.Bundle;



public interface Command {
    public void execute(Bundle bundle) throws Exception;
}
