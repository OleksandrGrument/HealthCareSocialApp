package com.comeonbaby.android.app.requests.models;

/**
 * Created by olegs on 22.02.2017.
 */

public class ImagesUploadRequest {
    private Long userid;
    private byte[][] bitmaps;

    public ImagesUploadRequest(Long userid, byte[][] bitmaps) {
        this.userid = userid;
        this.bitmaps = bitmaps;
    }

    public Long getUserid() {return userid;}
    public void setUserid(Long userid) {this.userid = userid;}
    public byte[][] getBitmaps() {return bitmaps;}
    public void setBitmaps(byte[][] bitmaps) {this.bitmaps = bitmaps;}
}
