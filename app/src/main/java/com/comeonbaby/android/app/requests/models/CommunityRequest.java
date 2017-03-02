package com.comeonbaby.android.app.requests.models;

/**
 * Created by olegs on 23.02.2017.
 */

public class CommunityRequest {
    private String operation;
    private String user;
    private String title;
    private String content;
    private String data;
    private int type;
    private long communityID;
    private byte[][] bitmaps;

    public String getOperation() {return operation;}
    public void setOperation(String operation) {this.operation = operation;}
    public String getUser() {return user;}
    public void setUser(String user) {this.user = user;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}
    public String getData() {return data;}
    public void setData(String data) {this.data = data;}
    public int getType() {return type;}
    public void setType(int type) {this.type = type;}
    public long getCommunityID() {return communityID;}
    public void setCommunityID(long communityID) {this.communityID = communityID;}
    public byte[][] getBitmaps() {return bitmaps;}
    public void setBitmaps(byte[][] bitmaps) {this.bitmaps = bitmaps;}
}