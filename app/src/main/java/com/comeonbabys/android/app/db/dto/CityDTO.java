/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.comeonbabys.android.app.db.dto;
import android.os.Bundle;
import android.os.Handler;

import com.comeonbabys.android.app.requests.Constants;
import com.comeonbabys.android.app.requests.ExtraConstants;
import com.comeonbabys.android.app.requests.commands.Commands;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CityDTO implements Serializable {
    private final static String CITIES_ARRAY = "cities";
	private static List<CityDTO> listCity;

    private static final long serialVersionUID = 2743523520579446539L;
    private Long id;
    private String name = "";

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Constants.MSG_LIST_CITY_SUCCESS: {
                    Bundle data = msg.getData();
                    JSONObject jsdata = null;
                    if (data.containsKey(ExtraConstants.DATA)) {
                        try {
                            jsdata = new JSONObject(data.getString(ExtraConstants.DATA));
                            JSONArray jsarr = jsdata.getJSONArray(CITIES_ARRAY);
                            listCity = new ArrayList<CityDTO>();
                            for(int i = 0; i < jsarr.length(); i++) {
                                listCity.add(CityDTO.parseFromJson(jsarr.get(i).toString()));
                            }
                            //listCity = parseJsonList(jsdata.toString());
                        } catch (Exception e) {e.printStackTrace();}
                    }
                    break;
                }
                case Constants.MSG_LIST_CITY_FAIL: {
                    break;
                }
            }
        }
    };


	public static void updateCityList() {
        Commands.startGetListCity(handler);
	}

//	//!!!!!!Временная реализация метода, которая просто добавляет список городов из эмулятора
//	public List<CityDTO> getListCity() {
//		listCity = new ArrayList<>();
//		String [] cities = BaseActivity.baseActivity.getResources().getStringArray(R.array.list_city);
//		for(String city : cities) {
//			CityDTO cityDTO = new CityDTO();
//			cityDTO.setId("id-" + city);
//			cityDTO.setName(city);
//			listCity.add(cityDTO);
//		}
//		//TODO !!!!!КОСТЫЛЬ
////		if(listCity == null) {
////			listCity = parseJsonList(ServerEmulator.getListCityJSON());
////		}
//		return listCity;
//	}
	/////////////////////////////////////////////////////////////

    public static List<CityDTO> getListCity() {
        if(listCity == null) {
			listCity = new ArrayList<>();
			updateCityList();
			return listCity;
		} else if (listCity.isEmpty()) {
			updateCityList();
		}
        return listCity;
    }

	public void setListCity(List<CityDTO> listCity) {
		this.listCity = listCity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		if(id != null && name.equals("") && listCity != null) {
			for (CityDTO ct : listCity) {
				if (id == ct.getId()) name = ct.getName();
			}
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//Возвращает новый город, разпарсеный из строки JSON
	public static CityDTO parseFromJson(String data) {
		CityDTO city = new CityDTO();
		try {
			JSONObject jsonObject = new JSONObject(data);
			if (jsonObject.has("name")) {
				city.setName(jsonObject.getString("name"));
			}
			if (jsonObject.has("id")) {
				city.setId(jsonObject.getLong("id"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return city;
	}

	//Возвращает List городов, из массива JSON, используя метод setFromJSON(String data)
	public static List<CityDTO> parseJsonList(String data) {
		try {
			List<CityDTO> list = new ArrayList<CityDTO>();
			JSONObject jsonObject = new JSONObject(data);
			JSONArray jsArray = jsonObject.getJSONArray(CITIES_ARRAY);
			for (int i = 0; i < jsArray.length(); i++) {
				CityDTO food = new CityDTO();
				food.parseFromJson(jsArray.getJSONObject(i).toString());
				list.add(food);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}