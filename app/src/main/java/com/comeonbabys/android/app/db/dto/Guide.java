package com.comeonbabys.android.app.db.dto;


import java.util.ArrayList;



public class Guide {
    public static ArrayList<Guide> guide = new ArrayList();
    private int cartoonId;
    private String date;
    private String name;
    private String url_image;
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {

        return url;
    }

    public Guide() {}

    public Guide(String paramString, int paramInt)
    {
        this.name = paramString;
        this.cartoonId = paramInt;
    }

    public Guide(String paramString1, String paramString2, String paramString3, String paramString4, String url)
    {
        this.name = paramString1;
        this.url_image = paramString2;
        this.date = paramString3;
        this.url_image = paramString4;
        this.url = url;
    }

    public int getCartoonId()
    {
        return this.cartoonId;
    }

    public String getDate()
    {
        return this.date;
    }

    public String getName()
    {
        return this.name;
    }

    public String getUrl_image()
    {
        return this.url_image;
    }

    public String toString()
    {
        StringBuilder localStringBuilder = new StringBuilder("Guide{");
        localStringBuilder.append("cartoonId=").append(this.cartoonId);
        localStringBuilder.append(", name='").append(this.name).append('\'');
        localStringBuilder.append('}');
        return localStringBuilder.toString();
    }
}