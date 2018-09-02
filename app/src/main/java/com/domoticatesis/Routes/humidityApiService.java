package com.domoticatesis.Routes;

import com.domoticatesis.Entities.ResultHeader;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by gabriel_cabrera on 20/4/18.
 */

public interface humidityApiService {
    @GET("arduino/humidity")
    Call<ResultHeader> getHumidity();
}
