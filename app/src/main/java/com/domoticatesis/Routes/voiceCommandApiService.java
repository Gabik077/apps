package com.domoticatesis.Routes;

import com.domoticatesis.Entities.ResultHeader;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface voiceCommandApiService {
    @POST("voiceCommand")
    @FormUrlEncoded
    Call<ResultHeader> sendVoice(@Field("voice") String voice,@Field("pid") int pid);

}
