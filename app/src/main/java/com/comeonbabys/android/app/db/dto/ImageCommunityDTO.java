/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.comeonbabys.android.app.db.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PvTai Nov 7, 2014 10:44:40 AM
 */
public class ImageCommunityDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3221948263935966161L;
	private int id;
	private String image;
	private String resource_uri;
	private String community;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getResource_uri() {
		return resource_uri;
	}

	public void setResource_uri(String resource_uri) {
		this.resource_uri = resource_uri;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public void parseFromJson(String data) {
		try {
			JSONObject jsonObject = new JSONObject(data);
			if (jsonObject.has("image")) {
				image = jsonObject.getString("image");
			}
			if (jsonObject.has("id")) {
				id = jsonObject.getInt("id");
			}
			if (jsonObject.has("resource_uri")) {
				resource_uri = jsonObject.getString("resource_uri");
			}
			if (jsonObject.has("community")) {
				community = jsonObject.getString("community");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author PvTai
	 * @param data
	 * @return
	 * @description parse list food
	 */
	public List<ImageCommunityDTO> parseListImages(String data) {
		try {
			List<ImageCommunityDTO> list = new ArrayList<ImageCommunityDTO>();
			JSONArray jsArray = new JSONArray(data);
			for (int i = 0; i < jsArray.length(); i++) {
				ImageCommunityDTO img = new ImageCommunityDTO();
				img.parseFromJson(jsArray.getJSONObject(i).toString());
				list.add(img);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ImageCommunityDTO> parseListImags(String data) {
		try {
			List<ImageCommunityDTO> list = new ArrayList<ImageCommunityDTO>();
			String[] images = data.split("<>");
			for (String img : images) {
				ImageCommunityDTO imgComm = new ImageCommunityDTO();
				//imgComm.setId(1);
				imgComm.setImage(img);
				list.add(imgComm);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}