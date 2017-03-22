/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.comeonbabys.android.app.db.dto;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;


/**
 * @author PvTai Nov 7, 2014 10:44:40 AM
 */
@SuppressWarnings("serial")
public class ProfileDTO implements Serializable {

    //используемые данные
    private int id;
    private boolean is_agreement;
    private boolean is_finish_question;
    private double weight;
    private double height;
    private boolean gender;
    private int menstrual_cycle;
    private int red_days = 7;
    private int birth_year;
    private String last_cycle = "";
    private String address = "";
    private String nickname = "";
    private String avatar = "";
    private CityDTO city;
    private double bmi;

    public void parseFromJson(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("id")) id = jsonObject.getInt("id");
            if (jsonObject.has("is_agreement")) is_agreement = jsonObject.getBoolean("is_agreement");
            if (jsonObject.has("is_finish_question")) is_finish_question = jsonObject.getBoolean("is_finish_question");
            if (jsonObject.has("weight")) weight = jsonObject.getDouble("weight");
            if (jsonObject.has("height")) height = jsonObject.getDouble("height");
            if (jsonObject.has("gender")) gender = jsonObject.getBoolean("gender");
            if (jsonObject.has("menstrual_cycle")) menstrual_cycle = jsonObject.getInt("menstrual_cycle");
            if (jsonObject.has("red_days")) red_days = jsonObject.getInt("red_days");
            if (jsonObject.has("birth_year")) birth_year = jsonObject.getInt("birth_year");
            if (jsonObject.has("address")) {
                address = jsonObject.getString("address");
                if(address.equals("null")) address = "";
            }
            if (jsonObject.has("last_cycle")) {
                last_cycle = jsonObject.getString("last_cycle");
                if(last_cycle.equals("null")) last_cycle = "";
            }
            if (jsonObject.has("nickname")) {
                nickname = jsonObject.getString("nickname");
                if (nickname.equals("null")) nickname = "";
            }
            if (jsonObject.has("avatar")) {
                avatar = jsonObject.getString("avatar");
                if (avatar.equals("null")) avatar = "";
            }
            if (jsonObject.has("city")) {
                Long cityID = jsonObject.getLong("city");
                city = new CityDTO();
                city.setId(cityID);
            }
            bmi = calculateBMI();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("is_agreement", is_agreement);
            json.put("is_finish_question", is_finish_question);
            json.put("weight", weight);
            json.put("height", height);
            json.put("gender", gender);
            json.put("menstrual_cycle", menstrual_cycle);
            json.put("red_days", red_days);
            json.put("birth_year", birth_year);
            json.put("address", address);
            json.put("last_cycle", last_cycle);
            json.put("nickname", nickname);
            json.put("avatar", avatar);
            json.put("city", city == null ? null : city.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public boolean isGender() {
        return gender;
    }
    public void setGender(boolean gender) {this.gender = gender;}

    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
        bmi = calculateBMI();
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
        bmi = calculateBMI();
    }
    public boolean isIs_agreement() {
        return is_agreement;
    }
    public void setIs_agreement(boolean is_agreement) {
        this.is_agreement = is_agreement;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {this.address = address;}

    public String getLast_cycle() {
        return last_cycle;
    }
    public void setLast_cycle(String last_cycle) {this.last_cycle = last_cycle;}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getMenstrual_cycle() {
        return menstrual_cycle;
    }
    public void setMenstrual_cycle(int menstrual_cycle) {this.menstrual_cycle = menstrual_cycle;}

    public int getBirthYear() {
        return birth_year;
    }
    public void setBirthday(int birthday) {;this.birth_year = birthday;}

    public double getBmi() {
        return bmi;
    }
    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public boolean isIs_finish_question() {
        return is_finish_question;
    }
    public void setIs_finish_question(boolean is_finish_question) {this.is_finish_question = is_finish_question;}

    public CityDTO getCity() {
        return city;
    }
    public void setCity(CityDTO city) {this.city = city;}

    public int getRed_days() {
        return red_days;
    }
    public void setRed_days(int red_days) {this.red_days = red_days;}

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {this.nickname = nickname;}

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    //Используется при изменении роста или веса
    //55kg, 160cm, BMI = 55 ÷ (1.60 × 1.60) =21.5
    private double calculateBMI() {
        double bmi = 0.0;
        if (height != 0.0 && weight != 0.0) {
            bmi = ((double) Math.round(weight / ((height * height) / 100000))) / 10;
        }
        return bmi;
    }

    //Generated to test
    @Override
    public String toString() {
        return "ProfileDTO{" +
                "weight=" + weight +
                ", gender=" + gender +
                ", height=" + height +
                ", bmi=" + bmi +
                ", is_agreement=" + is_agreement +
                ", address='" + address + '\'' +
                ", last_cycle='" + last_cycle + '\'' +
                ", city=" + city +
                ", nickname='" + nickname + '\'' +
                ", id=" + id +
                ", menstrual_cycle=" + menstrual_cycle +
                ", birth_year=" + birth_year +
                ", avatar='" + avatar + '\'' +
                ", red_days=" + red_days +
                ", is_finish_question=" + is_finish_question +
                '}';
    }
}
