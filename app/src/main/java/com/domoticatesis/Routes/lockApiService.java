package com.domoticatesis.Routes;

import com.domoticatesis.Entities.ResultHeader;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gabriel_cabrera on 19/4/18.
 */

public interface lockApiService {
    @GET("arduino/lock")
    Call<ResultHeader> setLock(@Query("action") String action);

}
