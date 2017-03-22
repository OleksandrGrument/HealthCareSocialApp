package com.comeonbabys.android.app.db.dto;

import java.util.ArrayList;

/**
 * Created by Home on 23.02.2017.
 */

public class Recipe {
    private String name;
    private String url_icon;
    private String content;
    private String date;
    public static ArrayList<Recipe> recipes = new ArrayList<>();

    public Recipe(String name, String url_icon, String content, String date) {
        this.content = content;
        this.name = name;
        this.url_icon = url_icon;
        this.date = date;
    }

    public Recipe(){
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getUrl_icon() {
        return url_icon;
    }

    public String getDate() {
        return date;
    }


    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", url_icon='" + url_icon + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
