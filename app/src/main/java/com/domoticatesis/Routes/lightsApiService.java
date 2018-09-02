package com.domoticatesis.Routes;

import com.domoticatesis.Entities.ResultHeader;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by gabriel_cabrera on 18/4/18.
 */

public interface lightsApiService {
    @GET("arduino/lights")
    Call<ResultHeader> turnLights(@Query("action") String action,@Query("appliance_id") int appliance_id);
}
