package com.domoticatesis.Routes;

import com.domoticatesis.Entities.ResultHeader;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface createTaskApiService
{
    @POST("task_create")
    @FormUrlEncoded
    Call<ResultHeader> createTask(@Field("name") String name,@Field("action") String action,
                                  @Field("time-task") String timeTask,@Field("area-id") int areaId
                                     ,@Field("appliance-id") int applianceId);

}
