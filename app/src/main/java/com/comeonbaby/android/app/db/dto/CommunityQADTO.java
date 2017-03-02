/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.comeonbaby.android.app.db.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author PvTai Nov 7, 2014 10:44:40 AM
 */
public class CommunityQADTO implements Serializable {


	private static final long serialVersionUID = -5205814598680261223L;
	private UserDTO user;
	private UserDTO userAnswer;
	private String answer_date;
	private String question_date;
	private String description;
	private boolean is_answered;
	private boolean is_private;
	private String title;
	private String question_text;
	private String answer_text;
	private Long id;

	public CommunityQADTO(){
		Calendar cal = Calendar.getInstance();
		String date = cal.get(Calendar.YEAR) + "-" +
				(cal.get(Calendar.MONTH) < 9 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + "-" +
				(cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + cal.get(Calendar.DAY_OF_MONTH) : cal.get(Calendar.DAY_OF_MONTH));
		question_date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion_text() {
		return question_text;
	}

	public void setQuestion_text(String question_text) {
		this.question_text = question_text;
	}

	public String getAnswer_text() {
		return answer_text;
	}

	public void setAnswer_text(String answer_text) {
		this.answer_text = answer_text;
	}

	public UserDTO getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(UserDTO userAnswer) {
		this.userAnswer = userAnswer;
	}


	public String getAnswer_date() {
		return answer_date;
	}

	public void setAnswer_date(String answer_date) {
		this.answer_date = answer_date;
	}

	public String getQuestion_date() {
		return question_date;
	}

	public void setQuestion_date(String question_date) {
		this.question_date = question_date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isIs_answered() {
		return is_answered;
	}

	public void setIs_answered(boolean is_answered) {
		this.is_answered = is_answered;
	}

	public boolean isIs_private() {
		return is_private;
	}

	public void setIs_private(boolean is_private) {
		this.is_private = is_private;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
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

	private final static String Q_AID = "id";
	private final static String USERID = "id_user";
	private final static String Q_ATITLE = "title";
	private final static String Q_ATEXT = "text";
	private final static String Q_AANSWER = "answer";
	private final static String Q_A_QUESTION_DATE = "question_date";
	private final static String Q_A_ANSWER_DATE = "answer_date";
	private final static String Q_A_ISACCESS = "is_access";
	private final static String Q_A_ISANSWER = "is_answer";
	private final static String USER_NICKNAME = "nickname";
	private final static String USER_AVATAR = "avatar";



	public void parseFromJson(String data) {
		try {
			JSONObject jsonObject = new JSONObject(data);
			if (jsonObject.has(Q_AID)) id = jsonObject.getLong(Q_AID);
			if (jsonObject.has(Q_ATITLE)) title = jsonObject.getString(Q_ATITLE);
			if (jsonObject.has(Q_ATEXT)) question_text = jsonObject.getString(Q_ATEXT);
			if (jsonObject.has(Q_AANSWER)) answer_text = jsonObject.getString(Q_AANSWER);
			if (jsonObject.has(Q_A_QUESTION_DATE)) question_date = jsonObject.getString(Q_A_QUESTION_DATE);
			if (jsonObject.has(Q_A_ANSWER_DATE)) answer_date = jsonObject.getString(Q_A_ANSWER_DATE);
			if (jsonObject.has(Q_A_ISACCESS)) {
				String access = jsonObject.getString(Q_A_ISACCESS);
				if (access.equals("true")) {
					is_private = true;
				} else {
					is_private = false;
				}
			}
			if (jsonObject.has(Q_A_ISANSWER)) {
				String access = jsonObject.getString(Q_A_ISANSWER);
				if (access.equals("true")) {
					is_answered = true;
				} else {
					is_answered = false;
				}
			}

			if (jsonObject.has(USERID)) {
				user = new UserDTO();
				ProfileDTO pf= new ProfileDTO();
				pf.setNickname(jsonObject.getString(USER_NICKNAME));
				pf.setAvatar(jsonObject.getString(USER_AVATAR));
				user.setProfileDTO(pf);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public List<CommunityQADTO> parseListCommunity(JSONArray detail) {
		try {
			List<CommunityQADTO> listCategoryDTo = new ArrayList<>();
			if (detail != null && detail.length() > 0) {
				for (int i = 0; i < detail.length(); i++) {
					CommunityQADTO category = new CommunityQADTO();
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
