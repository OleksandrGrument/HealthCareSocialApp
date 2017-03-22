/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.comeonbabys.android.app.db.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author PvTai Nov 7, 2014 10:44:40 AM
 */
public class ComeOnGuideDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6733408141287937950L;
	private int id;
	private String content;
	private String title;
	private String resource_uri;
	private String date_created;
	private String date_modified;
	private String url;
	private String thumbnail;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getResource_uri() {
		return resource_uri;
	}

	public void setResource_uri(String resource_uri) {
		this.resource_uri = resource_uri;
	}

	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	public String getDate_modified() {
		return date_modified;
	}

	public void setDate_modified(String date_modified) {
		this.date_modified = date_modified;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * @param string
	 */
	public void parseFromJson(String data) {
		try {
			JSONObject jsonObject = new JSONObject(data);
			if (jsonObject.has("id")) {
				id = jsonObject.getInt("id");
			}
			if (jsonObject.has("content")) {
				content = jsonObject.getString("content");
			}
			if (jsonObject.has("resource_uri")) {
				resource_uri = jsonObject.getString("resource_uri");
			}
			if (jsonObject.has("title")) {
				title = jsonObject.getString("title");
			}
			if (jsonObject.has("created")) {
				date_created = jsonObject.getString("created");
				if (date_created.contains("T")) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
					try {
						Date d = format.parse(date_created);
						date_created = format1.format(d);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			if (jsonObject.has("modified")) {
				date_modified = jsonObject.getString("modified");
				if (date_modified.contains("T")) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
					try {
						Date d = format.parse(date_modified);
						date_modified = format1.format(d);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			if (jsonObject.has("url")) {
				url = jsonObject.getString("url");
			}
			if (jsonObject.has("thumbnail")) {
				thumbnail = jsonObject.getString("thumbnail");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param string
	 */
	public List<ComeOnGuideDTO> parseListComeOnGuide(String data) {
		try {
			List<ComeOnGuideDTO> listCategoryDTo = new ArrayList<ComeOnGuideDTO>();
			JSONObject jsonObject = new JSONObject(data);
			JSONArray detail = jsonObject.getJSONArray("objects");
			if (detail != null && detail.length() > 0) {
				for (int i = 0; i < detail.length(); i++) {
					ComeOnGuideDTO category = new ComeOnGuideDTO();
					category.parseFromJson(detail.getJSONObject(i).toString());
					listCategoryDTo.add(category);
				}
				return listCategoryDTo;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
