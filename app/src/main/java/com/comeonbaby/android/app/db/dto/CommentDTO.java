/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.comeonbaby.android.app.db.dto;

import android.util.Log;

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
public class CommentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5522596989870728918L;
	private long id;
	private String comment;
	private long communityID;
	private long userID;
	private String nickname;
	private String avatar;
	private String date_created;

	public String getNickname() {return nickname;}
	public void setNickname(String nickname) {this.nickname = nickname;}
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	public String getComment() {return comment;}
	public void setComment(String comment) {this.comment = comment;}
	public long getCommunityID() {return communityID;}
	public void setCommunityID(long communityID) {this.communityID = communityID;}
	public long getUserID() {return userID;}
	public void setUserID(long userID) {this.userID = userID;}
	public String getAvatar() {return avatar;}
	public void setAvatar(String avatar) {this.avatar = avatar;}
	public String getDate_created() {return date_created;}
	public void setDate_created(String date_created) {this.date_created = date_created;}

    private final static String COMMID = "comm_id";
    private final static String BLOGID = "blog_id";
    private final static String USERID = "user_id";
    private final static String USERNICKNAME = "nickname";
    private final static String USERAVATAR = "avatar";
    private final static String BLOGTEXT = "text";
    private final static String BLOGDATE = "date";

	public void parseFromJson(JSONObject json) {
        try {
            if (json.has(COMMID)) id = json.getLong(COMMID);
            if (json.has(BLOGTEXT)) comment = json.getString(BLOGTEXT);
            if (json.has(BLOGDATE)) communityID = json.getLong(BLOGID);
            if (json.has(USERID)) userID = json.getLong(USERID);
            if (json.has(USERAVATAR))  avatar = json.getString(USERAVATAR);
            if (json.has(USERNICKNAME)) nickname = json.getString(USERNICKNAME);
            if (json.has(BLOGDATE)) date_created = json.getString(BLOGDATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}

    public void parseFromJson(String data) {
        try {
            JSONObject json = new JSONObject(data);
            parseFromJson(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<CommentDTO> parseListComments(JSONArray detail) {
        try {
            List<CommentDTO> listComm = new ArrayList<CommentDTO>();
            if (detail != null && detail.length() > 0) {
                for (int i = 0; i < detail.length(); i++) {
                    CommentDTO comm = new CommentDTO();
                    comm.parseFromJson(detail.getJSONObject(i));
                    listComm.add(comm);
                }
                return listComm;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", communityID=" + communityID +
                ", userID=" + userID +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", date_created='" + date_created + '\'' +
                '}';
    }
}
