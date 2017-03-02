package com.comeonbaby.android.app.server;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.comeonbaby.android.app.db.dto.CommunityDTO;
import com.comeonbaby.android.app.db.dto.CommunityQADTO;
import com.comeonbaby.android.app.db.dto.ImageCommunityDTO;
import com.comeonbaby.android.app.db.dto.UserDTO;
import com.comeonbaby.android.app.utils.AppSession;
import com.comeonbaby.android.app.utils.ConstsCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ServerEmulator {

    private static final String LOGTAG = "ServerEmulator";
    private static final String IMAGES_PATH = Environment.getExternalStorageDirectory() + "/DCIM/";  //путь сохранения картинок

    //Список записей комьюнити
    private static List<CommunityDTO> listCommunity  = new ArrayList<>();

    //Список записей вопросов
    private static List<CommunityQADTO> listQA = new ArrayList<>();

    public static List<CommunityDTO> getListCommunity() {return listCommunity;}
    public static List<CommunityQADTO> getListQA() {return listQA;}

    //Добавить новую запись в комьюнити
    public static long addNewCommunityRecord(CommunityDTO dto, List<Bitmap> listImage) {
        if(dto == null) return -1;
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.YEAR) + "-" +
                (cal.get(Calendar.MONTH) < 9 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + "-" +
                (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + cal.get(Calendar.DAY_OF_MONTH) : cal.get(Calendar.DAY_OF_MONTH));
        dto.setDate_created(date);
        dto.setId(listCommunity.size() + 1);
        dto.setUser(AppSession.getSession().getSystemUser());
        dto.setListImage(new ArrayList<ImageCommunityDTO>());
        for(Bitmap bipmap : listImage) {
            ImageCommunityDTO imgCom = new ImageCommunityDTO();
            //imgCom.setCommunity();
            imgCom.setId(listImage.size() + 1);
            imgCom.setImage(saveNewImage(bipmap).getPath());
            dto.getListImage().add(imgCom);
        }
        listCommunity.add(dto);
        return dto.getId();
    }

    //Сохраняет обьект Bitmap в формате PNG в MediaStore
    //Возвращает Uri сохраненного файла .png
    private static Uri saveNewImage(Bitmap inImage) {
        String imgName = UUID.randomUUID().toString() + ".png";                 //рандомное имя файла
        File file = new File(IMAGES_PATH, imgName);
        try {
            if (!file.exists()) file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file, false);
            inImage.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
        Log.d(LOGTAG, "New image path = " + file.getAbsolutePath());
        return Uri.fromFile(file);
    }

    //Добавить новую запись вопроса
    public static Long addNewQuestionRecord(CommunityQADTO dto) {
        if(dto == null) return -1l;
        dto.setId(Long.valueOf(listQA.size() + 1));
        dto.setUser(AppSession.getSession().getSystemUser());
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.YEAR) + "-" +
                (cal.get(Calendar.MONTH) < 9 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + "-" +
                (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + cal.get(Calendar.DAY_OF_MONTH) : cal.get(Calendar.DAY_OF_MONTH));
        dto.setQuestion_date(date);

        //***********TEST ANSWER***********
        dto.setIs_answered(true);
        dto.setAnswer_text("Test answer generated on server emulator.\n" + " Bla bla bla bla bla......");
        dto.setAnswer_date(date);
        dto.setUserAnswer(new UserDTO());
        //*********************************

        listQA.add(dto);
        return dto.getId();
    }

    //Удалить вопрос
    public static boolean removeQuestionRecord(Long id) {
        for(int i = 0; i < listQA.size(); i++) {
            if(listQA.get(i).getId() == id) {
                listQA.remove(i);
                return true;
            }
        }
        return false;
    }

    //************TEST**************
    //Добавить записи на сервер
    static {
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.YEAR) + "-" +
                (cal.get(Calendar.MONTH) < 9 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + "-" +
                (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + cal.get(Calendar.DAY_OF_MONTH) : cal.get(Calendar.DAY_OF_MONTH));

        UserDTO testUser = new UserDTO();
        testUser.setSystemID(54658L);
        testUser.getProfileDTO().setNickname("Oleg Shevchenko");

        //EVENT
        CommunityDTO dto = new CommunityDTO();
        dto.setUser(testUser);
        dto.setId(listQA.size() + 1);
        dto.setContent_type(ConstsCore.EVENT_TYPE);
        dto.setTitle("Test Event Title");
        dto.setContent("Test EVENT content generated on server emulator: bla bla bla bla bla bla bla bla bla bla bla bla..." +
                "<hr>" +
                "<a href=\"http://google.com\"> Google </a>");
        dto.setDate_created(date);
        dto.setId(listQA.size() + 1);
        listCommunity.add(dto);

        //RECIPE
        dto = new CommunityDTO();
        dto.setId(listQA.size() + 1);
        dto.setUser(testUser);
        dto.setContent_type(ConstsCore.RECIPE_TYPE);
        dto.setTitle("Test Recipe Title");
        dto.setContent("Test RECIPE generated on server emulator: bla bla bla bla bla bla bla bla bla bla bla bla..." );
        dto.setDate_created(date);
        dto.setLike_count(25);
        listCommunity.add(dto);

        //MY STORY
        dto = new CommunityDTO();
        dto.setId(listQA.size() + 1);
        dto.setUser(testUser);
        dto.setContent_type(ConstsCore.SUCCESS_TYPE);
        dto.setTitle("Test Story Title");
        dto.setContent("Test MY STORY generated on server emulator: bla bla bla bla bla bla bla bla bla bla bla bla..." );
        dto.setDate_created(date);
        dto.setLike_count(34);
        listCommunity.add(dto);

        //HUSBAND STORY
        dto = new CommunityDTO();
        dto.setId(listQA.size() + 1);
        dto.setUser(testUser);
        dto.setContent_type(ConstsCore.HUSBAND_TYPE);
        dto.setTitle("Test Husband Story Title");
        dto.setContent("Test HUSBAND STORY generated on server emulator: bla bla bla bla bla bla bla bla bla bla bla bla..." );
        dto.setDate_created(date);
        dto.setLike_count(16);
        listCommunity.add(dto);
    }

    //Метод возвращает строку JSON с массивом городов
    public static String getListCityJSON() {
        JSONArray jsArray = new JSONArray();
        JSONObject seulJSON = new JSONObject();
        JSONObject pusanJSON = new JSONObject();
        String finalJsonStr = "";
        try {
            seulJSON.put("name", "Seul"); seulJSON.put("id", "seul_id"); seulJSON.put("resource_uri", "seul_uri");
            pusanJSON.put("name", "Pusan"); pusanJSON.put("id", "pusan_id"); pusanJSON.put("resource_uri", "pusan_uri");
            jsArray.put(0, seulJSON);
            jsArray.put(1, pusanJSON);
            finalJsonStr = new JSONObject().put("objects", jsArray).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return finalJsonStr;
    }
}
