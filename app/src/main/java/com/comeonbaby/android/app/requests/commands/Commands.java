package com.comeonbaby.android.app.requests.commands;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.comeonbaby.android.app.db.dto.CommentDTO;
import com.comeonbaby.android.app.db.dto.CommunityDTO;
import com.comeonbaby.android.app.db.dto.CommunityQADTO;
import com.comeonbaby.android.app.db.dto.NoteDTO;
import com.comeonbaby.android.app.db.dto.ProfileDTO;
import com.comeonbaby.android.app.db.dto.UserDTO;
import com.comeonbaby.android.app.requests.Constants;
import com.comeonbaby.android.app.requests.ExtraConstants;
import com.comeonbaby.android.app.requests.RequestInterface;
import com.comeonbaby.android.app.requests.models.CommunityRequest;
import com.comeonbaby.android.app.requests.models.ImagesUploadRequest;
import com.comeonbaby.android.app.requests.models.NewRequest;
import com.comeonbaby.android.app.requests.models.NewResponse;
import com.comeonbaby.android.app.requests.models.QARequest;
import com.comeonbaby.android.app.requests.models.UpdateAvatarRequest;
import com.comeonbaby.android.app.utils.AppSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Oleg Shevchenko on 15.02.2017.
 */

public class Commands {

    private static final String TAG = "COMM: ";
    private static Retrofit retrofit;
    private static RequestInterface requestInterface;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        requestInterface = retrofit.create(RequestInterface.class);
    }

    //*******************************
    //Логин с помощью социальной сети NEW
    public static void startLoginSocial(final Handler handler, String loginType, String email, Long socialID) {
        final String SUBTAG = TAG + "startLoginSocial()";
        if (loginType == null || socialID == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        UserDTO user = new UserDTO(null, socialID, null, email, loginType);
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.SOCIAL_OPERATION);
        request.setUser(user.toJSON().toString());
        Call<NewResponse> response = requestInterface.loginSocialOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.SOCIAL_OPERATION, Constants.MSG_LOGIN_SOCIAL_SUCCESS, Constants.MSG_LOGIN_SOCIAL_FAIL);
    }

    //*********************
    //Логин с помощью email NEW
    public static void startLoginEmail(final Handler handler, String email, String password) {
        final String SUBTAG = TAG + "startLoginEmail()";
        if (email == null || password == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        UserDTO user = new UserDTO(null, null, password, email, Constants.LOGIN_EMAIL);
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user.toJSON().toString());
        Call<NewResponse> response = requestInterface.loginEmailOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.LOGIN_OPERATION, Constants.MSG_LOGIN_EMAIL_SUCCESS, Constants.MSG_LOGIN_EMAIL_FAIL);
    }

    //Регистрация с помощью email
    public static void startRegisterEmail(final Handler handler, String email, String password, String nickname) {
        final String SUBTAG = TAG + "startRegisterEmail()";
        if (email == null || password == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        UserDTO user = new UserDTO();
        user.setEmail(email);
        user.setPassword(password);
        user.setLoginType(Constants.LOGIN_EMAIL);
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.REGISTER_OPERATION);
        request.setUser(user.toJSON().toString());
        JSONObject data = new JSONObject();
        try {
            data.put(Constants.NICKNAME, nickname);
        } catch (JSONException e) {e.printStackTrace();}
        request.setData(data.toString());
        Call<NewResponse> response = requestInterface.registerUserOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.REGISTER_OPERATION, Constants.MSG_REGISTER_USER_SUCCESS, Constants.MSG_REGISTER_USER_FAIL);
    }

    //Update user password
    public static void startUpdatePassword(final Handler handler, final UserDTO user, String newPassword) {
        final String SUBTAG = TAG + "startUpdatePassword()";
        if (user == null || user.getSystemID() == null || newPassword == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.UPDATE_PASSWORD_OPERATION);
        request.setUser(user.toJSON().toString());
        JSONObject data = new JSONObject();
        try {
            data.put(Constants.NEW_PASSWORD, newPassword);
        } catch (JSONException e) {
        }
        request.setData(data.toString());
        Call<NewResponse> response = requestInterface.updatePasswordOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.UPDATE_PASSWORD_OPERATION, Constants.MSG_PASSWORD_UPDATE_SUCCESS, Constants.MSG_PASSWORD_UPDATE_FAIL);
    }

    //Update user email
    public static void startUpdateEmail(final Handler handler, final UserDTO user, String newEmail) {
        final String SUBTAG = TAG + "startUpdateEmail()";
        if(user == null || user.getSystemID() == null || newEmail == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.UPDATE_EMAIL_OPERATION);
        request.setUser(user.toJSON().toString());
        JSONObject data = new JSONObject();
        try {
            data.put(Constants.NEW_EMAIL, newEmail);
        } catch (JSONException e) {}
        request.setData(data.toString());
        Call<NewResponse> response = requestInterface.updateEmailOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.UPDATE_EMAIL_OPERATION, Constants.MSG_EMAIL_UPDATE_SUCCESS, Constants.MSG_EMAIL_UPDATE_FAIL);
    }

    //Get list cities
    public static void startGetListCity(final Handler handler) {
        final String SUBTAG = TAG + "getListCity()";
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.LIST_CITIES_OPERATION);
        Call<NewResponse> response = requestInterface.getListCityOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.LIST_CITIES_OPERATION, Constants.MSG_LIST_CITY_SUCCESS, Constants.MSG_LIST_CITY_FAIL);
    }

    //Get profile
    public static void getUserProfile(final Handler handler, final UserDTO user) {
        final String SUBTAG = TAG + "getUserProfile()";
        if(user == null || user.getSystemID() == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.GET_PROFILE_OPERATION);
        request.setUser(user.toJSON().toString());
        Call<NewResponse> response = requestInterface.getProfileOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.GET_PROFILE_OPERATION, Constants.MSG_GET_PROFILE_SUCCESS, Constants.MSG_GET_PROFILE_FAIL);
    }

    //Update profile
    public static void updateUserProfile(final Handler handler, final UserDTO user, final ProfileDTO profile) {
        final String SUBTAG = TAG + "updUserProfile()";
        if(user == null || user.getSystemID() == null || profile == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.UPDATE_PROFILE_OPERATION);
        request.setUser(user.toJSON().toString());
        JSONObject jsprofile = null;
        try {
            jsprofile = new JSONObject(profile.toJSON().toString());
        } catch (Exception e) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            e.printStackTrace();
            return;
        }
        request.setData(jsprofile.toString());
        Log.d(SUBTAG, "outProfileJSON: " + jsprofile.toString());
        Call<NewResponse> response = requestInterface.updateProfileOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.UPDATE_PROFILE_OPERATION, Constants.MSG_UPDATE_PROFILE_SUCCESS, Constants.MSG_UPDATE_PROFILE_FAIL);
    }

    //Upload Basic Question
    public static void uploadBasicQuestion(final UserDTO user, final JSONObject jsBasicQuestion) {

        final String SUBTAG = TAG + "uploadBasicQuestion()";

        final NewRequest request = new NewRequest();
        request.setOperation(Constants.UPLOAD_BASIC_QUESTION);
        request.setUser(user.toJSON().toString());
        request.setData(jsBasicQuestion.toString());

        Call<NewResponse> response = requestInterface.uploadBasicQuestion(request);
        executeRequest(response, SUBTAG, Constants.UPLOAD_BASIC_QUESTION);
    }

    // Send to server id key, that server can send to app notification
    public static void sendToServerGCMInformation(final String GoogleCloudMessageId){

        final NewRequest request = new NewRequest();
        request.setOperation("");
        request.setData(GoogleCloudMessageId);
        request.setUser("");

        Call<NewResponse> response = requestInterface.sendToServerGCM(request);
        executeRequest(response , "sendToServerGCMInformation" , "sendToServerGCMInformation");
    }

    //Save note
    public static void saveNoteOperation(final Handler handler, final UserDTO user, final NoteDTO note) {
        final String SUBTAG = TAG + "saveNote()";
        if(user == null || user.getSystemID() == null || note == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        JSONObject jsnote = note.getNoteJSON();
        if(jsnote == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.SAVE_NOTE_OPERATION);
        request.setUser(user.toJSON().toString());
        request.setData(jsnote.toString());
        Log.d(SUBTAG, "outNoteJSON: " + jsnote.toString());
        Call<NewResponse> response = requestInterface.saveNoteOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.SAVE_NOTE_OPERATION, Constants.MSG_SAVE_NOTE_SUCCESS, Constants.MSG_SAVE_NOTE_FAIL);
    }

    //Get user notes
    public static void getNotesOperation(final Handler handler, final UserDTO user, Integer year, Integer month) {
        final String SUBTAG = TAG + "getNotes()";
        if(user == null || user.getSystemID() == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.GET_NOTES_OPERATION);
        request.setUser(user.toJSON().toString());
        JSONObject jsdata = new JSONObject();
        try {
            if(year != null) jsdata.put(Constants.YEAR, year);
            if(month != null) jsdata.put(Constants.MONTH, month);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(jsdata.toString());
        Call<NewResponse> response = requestInterface.getNoresOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.GET_NOTES_OPERATION, Constants.MSG_GET_NOTES_SUCCESS, Constants.MSG_GET_NOTES_FAIL);
    }

    // get guides from server
    public static void getGuideOperation(final Handler handler, final UserDTO user) {
        final String SUBTAG = TAG + "getGuide()";
        if(user == null || user.getSystemID() == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.GET_GUIDE_OPERATION);
        request.setUser(user.toJSON().toString());
//        JSONObject jsdata = new JSONObject();
//        try {
//            if(year != null) jsdata.put(Constants.YEAR, year);
//            if(month != null) jsdata.put(Constants.MONTH, month);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        request.setData(jsdata.toString());
        Call<NewResponse> response = requestInterface.getGuideOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.GET_GUIDE_OPERATION, Constants.MSG_GET_GUIDE_SUCCESS, Constants.MSG_GET_GUIDE_FAIL);
    }


    // get recipes from server
    public static void getRecipeOperation(final Handler handler, final UserDTO user) {
        final String SUBTAG = TAG + "getRecipe()";
        if(user == null || user.getSystemID() == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final NewRequest request = new NewRequest();
        request.setOperation(Constants.GET_RECIPE_OPERATION);
        request.setUser(user.toJSON().toString());
        Log.e("!!!!!!!!!request",request.toString());

        Call<NewResponse> response = requestInterface.getRecipeOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.GET_RECIPE_OPERATION, Constants.MSG_GET_RECIPE_SUCCESS, Constants.MSG_GET_RECIPE_FAIL);
    }


    public static void sendImage(final Handler handler, final List<Bitmap> bitmaps) {
        final String SUBTAG = TAG + "sendImage()";
        Log.d(SUBTAG, "Start method...");
        if(bitmaps == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream stream;
                byte[][] bytes = new byte[bitmaps.size()][];
                for(int i = 0; i < bitmaps.size(); i++) {
                    stream = new ByteArrayOutputStream();
                    bitmaps.get(i).compress(Bitmap.CompressFormat.PNG, 100, stream);
                    bytes[i] = stream.toByteArray();
                    Log.d(SUBTAG, "image " +bitmaps.get(i).getWidth() + "x" + bitmaps.get(i).getWidth() + " added");
                }
                final ImagesUploadRequest request = new ImagesUploadRequest(AppSession.getSession().getSystemUser().getSystemID(), bytes);
                Call<NewResponse> response = requestInterface.putImageToServer(request);
                executeRequest(handler, response, SUBTAG, "", 1, 2);
            }
        });
        thr.setDaemon(true);
        thr.start();
    }

    public static void updateAvatarCommand(final Handler handler, final UserDTO user, final Bitmap bitmap){
        final String SUBTAG = TAG + "updateAvatar()";
        if(user == null || user.getSystemID() == null || bitmap == null) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                //Bitmap resizedBitmap = ImageUtils.getResizedBitmap(bitmap, 200, 200);
                Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                Log.d(SUBTAG, "image " + bitmap.getWidth() + "x" + bitmap.getWidth() + " added");
                final UpdateAvatarRequest request = new UpdateAvatarRequest(Constants.UPDATE_AVATAR_OPERATION, user.toJSON().toString(), bytes);
                Call<NewResponse> response = requestInterface.updateAvatarOperation(request);
                executeRequest(handler, response, SUBTAG, Constants.UPDATE_AVATAR_OPERATION, Constants.MSG_UPDATE_AVATAR_SUCCESS, Constants.MSG_UPDATE_AVATAR_FAIl);
            }
        });
        thr.setDaemon(true);
        thr.start();
    }

    public static void saveComunityRecord(final Handler handler, final CommunityDTO dto, final List<Bitmap> bitmaps) {
        Log.d(TAG, "Start save community record");
        final String SUBTAG = TAG + "saveComunity()";
        if(dto == null || dto.getUser() == null || dto.getUser().getSystemID() == null || dto.getTitle() == null || dto.getContent() == null || dto.getContent_type() == 0) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream stream;
                int numOfImages = bitmaps != null ? bitmaps.size() : 0;
                byte[][] bytes = new byte[numOfImages][];
                for(int i = 0; i < numOfImages; i++) {
                    stream = new ByteArrayOutputStream();
                    bitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    bytes[i] = stream.toByteArray();
                    Log.d(SUBTAG, "image " +bitmaps.get(i).getWidth() + "x" + bitmaps.get(i).getWidth() + " added");
                }
                final CommunityRequest request = new CommunityRequest();
                request.setBitmaps(bytes);
                request.setTitle(dto.getTitle());
                request.setUser(dto.getUser().toJSON().toString());
                request.setContent(dto.getContent());
                request.setOperation(Constants.SAVE_COMUNITY_RECORD_OPERATION);
                request.setType(dto.getContent_type());

                Call<NewResponse> response = requestInterface.saveComunityOperation(request);
                executeRequest(handler, response, SUBTAG, Constants.SAVE_COMUNITY_RECORD_OPERATION, Constants.MSG_SAVE_COMUNITY_SUCCESS, Constants.MSG_SAVE_COMUNITY_FAIL);
            }
        });
        thr.setDaemon(true);
        thr.start();
    }

    public static void sendEditComunityItem(final Handler handler, final CommunityDTO dto) {
        Log.d(TAG, "Start send edit community item");
        final String SUBTAG = TAG + "editComunity()";
        if(dto == null || dto.getUser() == null || dto.getUser().getSystemID() == null || dto.getTitle() == null || dto.getContent() == null || dto.getContent_type() == 0) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final CommunityRequest request = new CommunityRequest();
        request.setTitle(dto.getTitle());
        request.setUser(dto.getUser().toJSON().toString());
        request.setContent(dto.getContent());
        request.setOperation(Constants.EDIT_COMUNITY_RECORD_OPERATION);
        request.setType(dto.getContent_type());
        request.setCommunityID(dto.getId());

        Log.e("sendEditCommunityJSON",request.toString());

        Call<NewResponse> response = requestInterface.editCommunityOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.EDIT_COMUNITY_RECORD_OPERATION, Constants.MSG_EDIT_COMMUNITY_SUCCESS, Constants.MSG_EDIT_COMMUNITY_FAIL);
    }

    public static void getComunityRecords(final Handler handler, int type) {
        final String SUBTAG = TAG + "getComunity()";
        final CommunityRequest request = new CommunityRequest();
        request.setOperation(Constants.GET_COMUNITY_RECORDS_OPERATION);
        request.setUser(AppSession.getSession().getSystemUser().toJSON().toString());
        request.setType(type);
        Call<NewResponse> response = requestInterface.getComunityOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.GET_COMUNITY_RECORDS_OPERATION, Constants.MSG_GET_COMUNITY_SUCCESS, Constants.MSG_GET_COMUNITY_FAIL);
    }

    public static void saveComment(final Handler handler, CommentDTO dto) {
        final String SUBTAG = TAG + "saveComment()";
        if(dto != null || dto.getUserID() != 0 || dto.getComment().isEmpty() || dto.getCommunityID() != 0) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final CommunityRequest request = new CommunityRequest();
        request.setOperation(Constants.SAVE_COMMENT_OPERATION);
        request.setContent(dto.getComment());
        request.setCommunityID(dto.getCommunityID());
        Call<NewResponse> response = requestInterface.saveCommentOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.SAVE_COMMENT_OPERATION, Constants.MSG_SAVE_COMMENT_SUCCESS, Constants.MSG_SAVE_COMMENT_FAIL);
    }

    public static void getComments(final Handler handler, CommunityDTO dto) {
        final String SUBTAG = TAG + "getComments()";
        if(dto != null || dto.getId() != 0) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final CommunityRequest request = new CommunityRequest();
        request.setOperation(Constants.GET_COMMENTS_OPERATION);
        request.setCommunityID(dto.getId());
        Call<NewResponse> response = requestInterface.getCommentsOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.GET_COMMENTS_OPERATION, Constants.MSG_GET_COMMENTS_SUCCESS, Constants.MSG_GET_COMMENTS_FAIL);
    }

    public static void getNotices(final Handler handler) {
        final String SUBTAG = TAG + "getNotices()";
        final CommunityRequest request = new CommunityRequest();
        request.setUser(AppSession.getSession().getSystemUser().toJSON().toString());
        request.setOperation(Constants.GET_NOTICES_OPERATION);

        Call<NewResponse> response = requestInterface.getNoticesOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.GET_NOTICES_OPERATION, Constants.MSG_GET_NOTICES_SUCCESS, Constants.MSG_GET_NOTICES_FAIL);
    }

    public static void deleteCommunityRecord(final Handler handler, CommunityDTO dto) {
        final String SUBTAG = TAG + "deleteCommunity()";
        if(dto == null || dto.getId() == 0) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final CommunityRequest request = new CommunityRequest();
        request.setOperation(Constants.DELETE_COMUNITY_RECORD_OPERATION);
        request.setUser(AppSession.getSession().getSystemUser().toJSON().toString());
        request.setCommunityID(dto.getId());
        Call<NewResponse> response = requestInterface.deleteCommunityOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.DELETE_COMUNITY_RECORD_OPERATION, Constants.MSG_DELETE_COMMUNITY_SUCCESS, Constants.MSG_DELETE_COMMUNITY_FAIL);
    }

    public static void deleteQARecord(final Handler handler, CommunityQADTO dto) {
        final String SUBTAG = TAG + "deleteQARecord()";
        if(dto == null || dto.getId() == 0) {
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final QARequest request = new QARequest();
        request.setOperation(Constants.DELETE_Q_A_RECORD_OPERATION);
        request.setUser(AppSession.getSession().getSystemUser().toJSON().toString());
        request.setId(dto.getId());
        Call<NewResponse> response = requestInterface.deleteQARecord(request);
        executeRequest(handler, response, SUBTAG, Constants.DELETE_Q_A_RECORD_OPERATION, Constants.MSG_GET_COMUNITY_SUCCESS, Constants.MSG_GET_COMUNITY_FAIL);
    }


        public static void deleteUser(final UserDTO user) {
            final String SUBTAG = TAG + "getGuide()";

            if(user == null || user.getSystemID() == null) {
                Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
                return;
            }
            final NewRequest request = new NewRequest();
            request.setOperation(Constants.DELETE_USER_OPERATION);
            request.setUser(user.toJSON().toString());
            Log.e("!!!!!!!!!!!!!deleteUser",request.toString());

            Call<NewResponse> response = requestInterface.deleteUser(request);
            executeRequest(response, SUBTAG, Constants.DELETE_USER_OPERATION);
        }



    public static void getComunityQA(final Handler handler, int type) {
        final String SUBTAG = TAG + "getComunityQA()";
        final CommunityRequest request = new CommunityRequest();
        request.setOperation(Constants.GET_Q_A_OPERATION);
        request.setUser(AppSession.getSession().getSystemUser().toJSON().toString());
        request.setType(type);
        Call<NewResponse> response = requestInterface.getComunityOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.GET_Q_A_OPERATION, Constants.MSG_GET_Q_A_SUCCESS, Constants.MSG_GET_Q_A_FAIL);
    }


    public static void saveQARecord(final Handler handler, final CommunityQADTO dto) {
        Log.d(TAG, "Start save QA record" + dto.toString());
        final String SUBTAG = TAG + "saveComunity()";
        if(dto == null || dto.getTitle() == null || dto.getQuestion_text() == null ) {
            handler.sendEmptyMessage(Constants.MSG_ERROR);
            Log.d(SUBTAG, Constants.ERROR_MESSAGE_NULL_VALUE);
            return;
        }
        final QARequest request = new QARequest();

        request.setTitle(dto.getTitle());
        request.setUser(AppSession.getSession().getSystemUser().toJSON().toString());
        request.setText(dto.getQuestion_text());
        request.setIs_access(dto.isIs_private());
        request.setOperation(Constants.SAVE_QA_OPERATION);

        Log.d(TAG, "!!! request" + request.toString());
        Call<NewResponse> response = requestInterface.saveQAOperation(request);
        executeRequest(handler, response, SUBTAG, Constants.SAVE_QA_OPERATION, Constants.MSG_SAVE_QA_SUCCESS, Constants.MSG_SAVE_QA_FAIL);
    }

    public static void modifyComunityRecord(final Handler handler, CommunityDTO dto) {

    }

    //REQUEST METHOD
    private static void executeRequest(final Handler handler, Call<NewResponse> response,
                                       final String SUBTAG, final String operation,
                                       final int successMsg, final int failMsg) {
        response.enqueue(new Callback<NewResponse>() { // Асинхронный запрос (Отправка без получения ответа в виде информации)
            @Override
            public void onResponse(Call<NewResponse> call, Response<NewResponse> response) {
                Bundle bundle = new Bundle();
                Message msg = new Message();
                NewResponse resp = response.body(); // Создание объекта респонс и заполнение объекта из ретрофита

                if (resp == null) {
                    handler.sendEmptyMessage(Constants.MSG_ERROR);
                    Log.d(SUBTAG, "Retrofit error. Response NULL!");
                    return;
                }
                Log.d(SUBTAG, "Response: operation - " + resp.getOperation() + ", result - " + resp.getResult() + ", message - " + resp.getMessage());

                bundle.putString(ExtraConstants.RESULT, resp.getResult());
                bundle.putString(ExtraConstants.MESSAGE, resp.getMessage());
                bundle.putString(ExtraConstants.OPERATION, resp.getOperation());

                if (resp.getResult() != null && resp.getResult().equals(Constants.SUCCESS)) {
                    if(resp.getUser() != null) bundle.putString(ExtraConstants.USER, resp.getUser());
                    if(resp.getData() != null) bundle.putString(ExtraConstants.DATA, resp.getData());
                    Log.d(SUBTAG, "Response: user - " + resp.getUser() + " data - " + resp.getData());
                    msg.what = successMsg;
                    msg.setData(bundle);
                } else {
                    msg.what = failMsg;
                    msg.setData(bundle);
                }

                if(handler != null) handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NewResponse> call, Throwable t) { // Если ошибка
                Log.d(SUBTAG, "onFailure()");
                Bundle bundle = new Bundle();
                Message msg = new Message();
                msg.what = failMsg;
                bundle.putString(ExtraConstants.RESULT, Constants.FAILURE);
                bundle.putString(ExtraConstants.MESSAGE, Constants.ERROR_MESSAGE_NO_CONNECTION);
                bundle.putString(ExtraConstants.OPERATION, operation);
                msg.setData(bundle);
                if(handler != null) handler.sendMessage(msg);
            }
        });
    }

    private static void executeRequest(Call<NewResponse> response, final String SUBTAG, final String operation){

        response.enqueue(new Callback<NewResponse>() { // Асинхронный запрос (Отправка без получения ответа в виде информации)
            @Override
            public void onResponse(Call<NewResponse> call, Response<NewResponse> response) {
                Log.d(Constants.TAG, SUBTAG + "!!!!!!!!!!!!!!!!!ПЕРЕДАЛО!!!!!!!!!!!");
                NewResponse resp = response.body(); // Создание объекта респонс и заполнение объекта из ретрофита
                if(resp.getResult().equals(Constants.SUCCESS)){
                    Log.d("UPLOAD SUCCESS!!!!!!!!!","!!!!!!!! " + operation + " SUCCESS");
                }
            }

            @Override
            public void onFailure(Call<NewResponse> call, Throwable t) { // Если ошибка
                Log.d(Constants.TAG,"!!!!!!!!!!!!!!!!!!!!!!!!!!!!"); // Ошибка в логи
            }
        });

    }

}
