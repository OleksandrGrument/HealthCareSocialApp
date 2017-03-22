package com.comeonbabys.android.app.requests.models;

/**
 * Created by olegs on 23.02.2017.
 */

public class UpdateAvatarRequest {
    private String operation;
    private String user;
    private byte[] bitmap;

    public UpdateAvatarRequest(String operation, String user, byte[] bitmap) {
        this.operation = operation;
        this.user = user;
        this.bitmap = bitmap;
    }

    public String getOperation() {return operation;}
    public void setOperation(String operation) {this.operation = operation;}

    public String getUser() {return user;}
    public void setUser(String user) {this.user = user;}

    public byte[] getBitmap() {return bitmap;}
    public void setBitmap(byte[] bitmap) {this.bitmap = bitmap;}
}
