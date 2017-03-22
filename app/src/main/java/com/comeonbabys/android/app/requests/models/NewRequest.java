package com.comeonbabys.android.app.requests.models;

import com.comeonbabys.android.app.requests.Constants;

/**
 * Created by olegs on 16.02.2017.
 */

public class NewRequest {
    private final String secret = Constants.SECRET;
    private String operation;
    private String user;
    private String data;

    public NewRequest() {}

    public NewRequest(String operation, String user, String data) {
        this.operation = operation;
        this.user = user;
        this.data = data;
    }

    public void setOperation(String operation) {this.operation = operation;}
    public void setUser(String user) {this.user = user;}
    public void setData(String data) {this.data = data;}

    @Override
    public String toString() {
        return "NewRequest{" +
                "secret='" + secret + '\'' +
                ", operation='" + operation + '\'' +
                ", user='" + user + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
