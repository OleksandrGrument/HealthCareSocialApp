package com.comeonbabys.android.app.requests;


import com.comeonbabys.android.app.requests.models.CommunityRequest;
import com.comeonbabys.android.app.requests.models.ImagesUploadRequest;
import com.comeonbabys.android.app.requests.models.NewRequest;
import com.comeonbabys.android.app.requests.models.NewResponse;
import com.comeonbabys.android.app.requests.models.QARequest;
import com.comeonbabys.android.app.requests.models.UpdateAvatarRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("users-app")
    Call<NewResponse> updateEmailOperation(@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> updatePasswordOperation(@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> registerUserOperation(@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> loginEmailOperation(@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> loginSocialOperation(@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> getProfileOperation(@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> updateProfileOperation(@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> uploadBasicQuestion(@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> saveNoteOperation(@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> getNoresOperation(@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> getGuideOperation(@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> getRecipeOperation(@Body NewRequest request);

    @POST("cities")
    Call<NewResponse> getListCityOperation(@Body NewRequest request);

    @POST("putimages")
    Call<NewResponse> putImageToServer(@Body ImagesUploadRequest request);

    @POST("avatar")
    Call<NewResponse> updateAvatarOperation(@Body UpdateAvatarRequest request);

    @POST("community")
    Call<NewResponse> saveComunityOperation (@Body CommunityRequest request);

    @POST("community")
    Call<NewResponse> getComunityOperation (@Body CommunityRequest request);

    @POST("community")
    Call<NewResponse> saveCommentOperation (@Body CommunityRequest request);

    @POST("community")
    Call<NewResponse> getCommentsOperation (@Body CommunityRequest request);

    @POST("community")
    Call<NewResponse> getNoticesOperation (@Body CommunityRequest request);

    @POST("community")
    Call<NewResponse> deleteCommunityOperation (@Body CommunityRequest request);

    @POST("community")
    Call<NewResponse> editCommunityOperation (@Body CommunityRequest request);

    @POST("community-qa")
    Call<NewResponse> saveQAOperation (@Body QARequest request);

    @POST("community-qa")
    Call<NewResponse> deleteQARecord (@Body QARequest request);

    @POST("google-cloud-id")
    Call<NewResponse> sendToServerGCM (@Body NewRequest request);

    @POST("users-app")
    Call<NewResponse> deleteUser (@Body NewRequest request);

    @POST("community")
    Call<NewResponse> addLike (@Body CommunityRequest request);

    @POST("community")
    Call<NewResponse> deleteLike (@Body CommunityRequest request);

    @POST("community")
    Call<NewResponse> isUserLiked (@Body CommunityRequest request);

}

