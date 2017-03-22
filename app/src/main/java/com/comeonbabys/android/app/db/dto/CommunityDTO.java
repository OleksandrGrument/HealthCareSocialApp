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
public class CommunityDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2529449337056238119L;
	private long id;								//id на сервере
	private String content;							//Пользовательский текст
	private int content_type;  						//тип: ConstsCore.SUCCESS_TYPE, ConstsCore.RECIPE_TYPE, ConstsCore.HUSBAND_TYPE
	private String title;							//Пользовательский заголовок
	private String date_created;					//дата создания
	private List<ImageCommunityDTO> listImage;		//список изображений
	private UserDTO user;							//пользователь, создавший
	private int like_count;							//счетчик лайков
	private boolean like;							//лайкал ли текущий пользователь

	public boolean isLike() {
		return like;
	}
	public void setLike(boolean like) {
		this.like = like;
	}

	public int getLike_count() {
		return like_count;
	}
	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}

	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}

	public List<ImageCommunityDTO> getListImage() {
		return listImage;
	}
	public void setListImage(List<ImageCommunityDTO> listImage) {
		this.listImage = listImage;
	}

	public String getDate_created() {
		return date_created;
	}
	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public int getContent_type() {
		return content_type;
	}
	public void setContent_type(int content_type) {
		this.content_type = content_type;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param string
	 */

	private final static String BLOGID = "blog_id";
	private final static String USERID = "user_id";
    private final static String USERNICKNAME = "nickname";
    private final static String USERAVATAR = "avatar";
	private final static String BLOGTYPE = "type";
	private final static String BLOGTITLE = "title";
	private final static String BLOGTEXT = "text";
	private final static String BLOGIMAGES = "images";
	private final static String BLOGDATE = "date";
	private final static String LIKECOUNT = "likes";
	private final static String ISLIKED = "isliked";

	public void parseFromJson(String data) {
		try {
			JSONObject json = new JSONObject(data);
			if (json.has(BLOGID)) id = json.getLong(BLOGID);
			if (json.has(BLOGTEXT)) content = json.getString(BLOGTEXT);
			if (json.has(BLOGTYPE)) content_type = json.getInt(BLOGTYPE);
			if (json.has(BLOGTITLE)) title = json.getString(BLOGTITLE);
			if (json.has(BLOGDATE)) date_created = json.getString(BLOGDATE);
			if (json.has(LIKECOUNT)) like_count = json.getInt(LIKECOUNT);
			if (json.has(ISLIKED)) like = json.getBoolean(ISLIKED);

			user = new UserDTO();
			if (json.has(USERID)) user.setSystemID(json.getLong(USERID));
			if (json.has(USERAVATAR))  user.getProfileDTO().setAvatar(json.getString(USERAVATAR));
            if (json.has(USERNICKNAME)) user.getProfileDTO().setNickname(json.getString(USERNICKNAME));

			if (json.has(BLOGIMAGES)) {
				ImageCommunityDTO imgDto = new ImageCommunityDTO();
				listImage = imgDto.parseListImags(json.getString(BLOGIMAGES));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static List<CommunityDTO> parseListCommunity(JSONArray detail) {
		try {
			List<CommunityDTO> listCategoryDTo = new ArrayList<CommunityDTO>();
			if (detail != null && detail.length() > 0) {
				for (int i = 0; i < detail.length(); i++) {
					CommunityDTO category = new CommunityDTO();
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

	@Override
	public String toString() {
		return "CommunityDTO{" +
				"id=" + id +
				", content='" + content + '\'' +
				", content_type=" + content_type +
				", title='" + title + '\'' +
				", date_created='" + date_created + '\'' +
				", listImage=" + listImage +
				", user=" + user +
				", like_count=" + like_count +
				", like=" + like +
				'}';
	}
}
