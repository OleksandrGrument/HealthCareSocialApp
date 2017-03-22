package com.comeonbabys.android.app.db.dto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1134743269115278436L;

    ProfileDTO profileDTO = new ProfileDTO();
    private Long systemID;
    private Long socialID;
    private String password;
    private String email;
    private String loginType;

    public UserDTO(Long systemID, Long socialID, String password, String email, String loginType) {
        this.systemID = systemID;
        this.socialID = socialID;
        this.password = password;
        this.email = email;
        this.loginType = loginType;
    }

    public UserDTO() {
    }

    public ProfileDTO getProfileDTO() {
        return profileDTO;
    }
    public void setProfileDTO(ProfileDTO profileDTO) {
        this.profileDTO = profileDTO;
    }

    public Long getSystemID() {return systemID;}
    public void setSystemID(Long systemID) {this.systemID = systemID;}

    public Long getSocialID() {return socialID;}
    public void setSocialID(Long socialID) {this.socialID = socialID;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getEmail() {return email;}
    public void setEmail(String email) {
        this.email = email;
    }

    public String  getLoginType() {
        return loginType;
    }
    public void setLoginType(String loginType) {this.loginType = loginType;}

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", systemID);
            json.put("socialID", socialID);
            json.put("password", password);
            json.put("email", email);
            json.put("loginType", loginType);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    public void setFromJSON(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("id")) systemID = jsonObject.getLong("id");
            if (jsonObject.has("socialID")) socialID = jsonObject.getLong("socialID");
            if (jsonObject.has("password")) password = jsonObject.getString("password");
            if (jsonObject.has("email")) email = jsonObject.getString("email");
            if (jsonObject.has("loginType")) loginType = jsonObject.getString("loginType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<UserDTO> listFromJSON(String data) {
        List<UserDTO> list = new ArrayList<UserDTO>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsArray = jsonObject.getJSONArray("usersarray");
            for (int i = 0; i < jsArray.length(); i++) {
                UserDTO userDto = new UserDTO();
                userDto.setFromJSON(jsArray.get(i).toString());
                list.add(userDto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "systemID=" + systemID +
                ", socialID=" + socialID +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", loginType=" + loginType +
                '}';
    }

    //    /**
//     * @author PvTai
//     * @description this is the comparator to sort by ascending the name
//     */
//    public static Comparator<UserDTO> ItemNameAscComparator = new Comparator<UserDTO>() {
//        public int compare(UserDTO item1, UserDTO item2) {
//            String itemName1 = ((UserDTO) item1).getFirstName();
//            String itemName2 = ((UserDTO) item2).getFirstName();
//
//            // ascending order
//            return itemName1.compareToIgnoreCase(itemName2);
//        }
//
//    };
//
//    /**
//     * @author PvTai
//     * @description this is the comparator to sort by descending the name
//     */
//    public static Comparator<UserDTO> ItemNameDescComparator = new Comparator<UserDTO>() {
//        public int compare(UserDTO item1, UserDTO item2) {
//            String itemName1 = ((UserDTO) item1).getFirstName();
//            String itemName2 = ((UserDTO) item2).getFirstName();
//
//            // descending order
//            return itemName2.compareToIgnoreCase(itemName1);
//        }
//
//    };
}