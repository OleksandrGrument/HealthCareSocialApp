package com.comeonbaby.android.app.db.dto;

import android.text.TextUtils;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

@Table
public class NoteDTO extends SugarRecord implements Serializable {

	private static final long serialVersionUID = -6751608395666620912L;

	private int year;
	private int month;
	private int day;

	private String bbt;									//Basic body temperature (Double)
	private String recommended_foods;					//Recommended food
	private String has_nut;								//Boolean eat nuts
	private String recommended_nuts;					//Eat recommended nuts
	private String has_tea;								//Boolean drink tea
	private String recommended_teas;					//Drink recommended teas
	private String has_exercise;						//Boolean has exercises
	private String recommended_exercise;				//Recommended exercises
	private String going_to_bed_from;					//Wake up time
	private String going_to_bed_to;						//Time when go to bed (SimpleDateFormat("HH:mm")) 24H
	private String water_intake;						//How much water drinked (Double litres)
	private String hip_bath;							//-Water heating time (minutes)
	private String vitamin;								//Boolean intake vitamins
	private String folate;								//Boolean folic acid
	private String coffee_intake;						//Integer 1 or 2
	private String alcohol_consumption;					//-Number of alcohol glasses (Integer)
	private String smoking;								//Boolean smoking
	private String emotional_state;						//Integer from 0 (very good) to 4 (very bad)
	private String bmi;									//Current body mass index

	//Default constructor
	public NoteDTO() {}

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }

	public String getGoing_to_bed_from() {
		return going_to_bed_from;
	}
	public void setGoing_to_bed_from(String going_to_bed_from) {this.going_to_bed_from = going_to_bed_from;}
	public String getGoing_to_bed_to() {
		return going_to_bed_to;
	}
	public void setGoing_to_bed_to(String going_to_bed_to) {
		this.going_to_bed_to = going_to_bed_to;
	}
	public String isHas_exercise() {
		return has_exercise;
	}
	public void setHas_exercise(String has_exercise) {
		this.has_exercise = has_exercise;
	}
	public String isHas_nut() {
		return has_nut;
	}
	public void setHas_nut(String has_nut) {
		this.has_nut = has_nut;
	}
	public String isHas_tea() {
		return has_tea;
	}
	public void setHas_tea(String has_tea) {
		this.has_tea = has_tea;
	}
	public String getRecommended_teas() {
		return recommended_teas;
	}
	public void setRecommended_teas(String recommended_teas) {this.recommended_teas = recommended_teas;}
	public String getRecommended_foods() {
		return recommended_foods;
	}
	public void setRecommended_foods(String recommended_foods) {this.recommended_foods = recommended_foods;}
	public String getHip_bath() {
		return hip_bath;
	}
	public void setHip_bath(String hip_bath) {
		this.hip_bath = hip_bath;
	}
	public String getRecommended_exercise() {
		return recommended_exercise;
	}
	public void setRecommended_exercise(String recommended_exercise) {this.recommended_exercise = recommended_exercise;}
	public String getAlcohol_consumption() {
		return alcohol_consumption;
	}
	public void setAlcohol_consumption(String alcohol_consumption) {this.alcohol_consumption = alcohol_consumption;}
	public String getWater_intake() {
		return water_intake;
	}
	public void setWater_intake(String water_intake) {
		this.water_intake = water_intake;
	}
	public String getBmi() {
		return bmi;
	}
	public void setBmi(String bmi) {
		this.bmi = bmi;
	}
	public String getVitamin() {
		return vitamin;
	}
	public void setVitamin(String vitamin) {
		this.vitamin = vitamin;
	}
	public String getRecommended_nuts() {
		return recommended_nuts;
	}
	public void setRecommended_nuts(String recommended_nuts) {this.recommended_nuts = recommended_nuts;}
	public String getEmotional_state() {return emotional_state;}
	public void setEmotional_state(String emotional_state) {
		this.emotional_state = emotional_state;
	}
	public String getCoffee_intake() {
		return coffee_intake;
	}
	public void setCoffee_intake(String coffee_intake) {
		this.coffee_intake = coffee_intake;
	}
	public String getSmoking() {
		return smoking;
	}
	public void setSmoking(String smoking) {
		this.smoking = smoking;
	}
	public String getBbt() {
		return bbt;
	}
	public void setBbt(String bbt) {
		this.bbt = bbt;
	}
	public String getFolate() {
		return folate;
	}
	public void setFolate(String folate) {
		this.folate = folate;
	}

	public JSONObject getNoteJSON() {
		JSONObject js = new JSONObject();
        try {
            js.put("year", year);
            js.put("month", month);
            js.put("day", day);
            js.put("bbt", bbt);
            js.put("recommended_foods", recommended_foods);
            js.put("has_nut", has_nut);
            js.put("recommended_nuts", recommended_nuts);
            js.put("has_tea", has_tea);
            js.put("recommended_teas", recommended_teas);
            js.put("has_exercise", has_exercise);
            js.put("recommended_exercise", recommended_exercise);
            js.put("going_to_bed_from", going_to_bed_from);
            js.put("going_to_bed_to", going_to_bed_to);
            js.put("water_intake", water_intake);
            js.put("hip_bath", hip_bath);
            js.put("vitamin", vitamin);
            js.put("folate", folate);
            js.put("coffee_intake", coffee_intake);
            js.put("alcohol_consumption", alcohol_consumption);
            js.put("smoking", smoking);
            js.put("emotional_state", emotional_state);
            js.put("bmi", bmi);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return js;
	}

    private String checkNoJsonData(String str) {
        if(str.isEmpty() || str.equals("null")) {
            return null;
        } else {
            return str;
        }
    }

	//Parse single note from JSON string
	public void parseFromJson(String data) {
		try {
			JSONObject js = new JSONObject(data);
            if (js.has("year") && js.has("month") && js.has("day")) {
                year = js.getInt("year");
                month = js.getInt("month");
                day = js.getInt("day");
            } else {
                Log.d("NoteDTO", "Error parsing from JSON: invalid date");
                return;
            }
            if (js.has("bbt")) bbt = checkNoJsonData(js.getString("bbt"));
            if (js.has("recommended_foods")) recommended_foods = checkNoJsonData(js.getString("recommended_foods"));
            if (js.has("has_nut")) has_nut = checkNoJsonData(js.getString("has_nut"));
            if (js.has("recommended_nuts")) recommended_nuts = checkNoJsonData(js.getString("recommended_nuts"));
            if (js.has("has_tea")) has_tea = checkNoJsonData(js.getString("has_tea"));
			if (js.has("recommended_teas")) recommended_teas = checkNoJsonData(js.getString("recommended_teas"));
            if (js.has("has_exercise")) has_exercise = checkNoJsonData(js.getString("has_exercise"));
            if (js.has("recommended_exercise")) recommended_exercise = checkNoJsonData(js.getString("recommended_exercise"));
            if (js.has("going_to_bed_from") && js.has("going_to_bed_to")) {
                going_to_bed_from = checkNoJsonData(js.getString("going_to_bed_from"));
                going_to_bed_to = checkNoJsonData(js.getString("going_to_bed_to"));
            }
            if (js.has("water_intake")) water_intake = checkNoJsonData(js.getString("water_intake"));
            if (js.has("hip_bath")) hip_bath = checkNoJsonData(js.getString("hip_bath"));
            if (js.has("vitamin")) vitamin = checkNoJsonData(js.getString("vitamin"));
            if (js.has("folate")) folate = checkNoJsonData(js.getString("folate"));
            if (js.has("coffee_intake")) coffee_intake = checkNoJsonData(js.getString("coffee_intake"));
            if (js.has("alcohol_consumption")) alcohol_consumption = checkNoJsonData(js.getString("alcohol_consumption"));
            if (js.has("smoking")) smoking = checkNoJsonData(js.getString("smoking"));
            if (js.has("emotional_state")) emotional_state = checkNoJsonData(js.getString("emotional_state"));
            if (js.has("bmi")) bmi = checkNoJsonData(js.getString("bmi"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

//	public List<NoteDTO> parseListNote(String data) {
//		try {
//			List<NoteDTO> listCategoryDTo = new ArrayList<NoteDTO>();
//			JSONObject jsonObject = new JSONObject(data);
//			JSONArray detail = jsonObject.getJSONArray("objects");
//			if (detail != null && detail.length() > 0) {
//				for (int i = 0; i < detail.length(); i++) {
//					NoteDTO category = new NoteDTO();
//					category.setFromJSON(detail.getJSONObject(i).toString());
//					listCategoryDTo.add(category);
//				}
//				return listCategoryDTo;
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
}
