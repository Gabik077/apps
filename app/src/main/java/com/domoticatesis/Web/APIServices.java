package com.domoticatesis.Web;

import com.domoticatesis.Routes.ExpenseApiService;
import com.domoticatesis.Routes.alarmApiService;
import com.domoticatesis.Routes.createTaskApiService;
import com.domoticatesis.Routes.currentMonthExpenseApiService;
import com.domoticatesis.Routes.humidityApiService;
import com.domoticatesis.Routes.lightsApiService;
import com.domoticatesis.Routes.lockApiService;
import com.domoticatesis.Routes.temperatureApiService;
import com.domoticatesis.Routes.voiceCommandApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gabriel_cabrera on 18/4/18.
 */

public class APIServices {

   private static String SERVER = "http://192.168.0.205:5000";

   private static Retrofit retrofit;

   private static Retrofit startRetrofitInstance(OkHttpClient okhttpClient){

       okhttpClient = new OkHttpClient.Builder()
               .connectTimeout(5, TimeUnit.SECONDS)
               .writeTimeout(30, TimeUnit.SECONDS)
               .readTimeout(10, TimeUnit.SECONDS)
               .build();

         retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient)
                .build();

        return retrofit;
    }

    public static lightsApiService getApiLightsInstance
            (OkHttpClient okhttpClient) {

        if(retrofit==null){
            startRetrofitInstance(okhttpClient);
        }

        return retrofit.create(lightsApiService.class);
    }

    public static alarmApiService getApiAlarmInstance
            (OkHttpClient okhttpClient) {

        if(retrofit==null){
            startRetrofitInstance(okhttpClient);
        }

        return retrofit.create(alarmApiService.class);
    }

    public static lockApiService getApiLockInstance
            (OkHttpClient okhttpClient) {

        if(retrofit==null){
            startRetrofitInstance(okhttpClient);
        }

        return retrofit.create(lockApiService.class);
    }
    public static temperatureApiService getApiTemperatureInstance
            (OkHttpClient okhttpClient) {

        if(retrofit==null){
            startRetrofitInstance(okhttpClient);
        }

        return retrofit.create(temperatureApiService.class);
    }
    public static humidityApiService getApiHumidityInstance
            (OkHttpClient okhttpClient) {

        if(retrofit==null){
            startRetrofitInstance(okhttpClient);
        }

        return retrofit.create(humidityApiService.class);
    }
    public static ExpenseApiService getApiAreaExpenseInstance
            (OkHttpClient okhttpClient) {

        if(retrofit==null){
            startRetrofitInstance(okhttpClient);
        }

        return retrofit.create(ExpenseApiService.class);
    }
    public static currentMonthExpenseApiService getApiCurrentExpenseInstance
            (OkHttpClient okhttpClient) {

        if(retrofit==null){
            startRetrofitInstance(okhttpClient);
        }

        return retrofit.create(currentMonthExpenseApiService.class);
    }


    public static createTaskApiService getApiCreateServiceInstance
            (OkHttpClient okhttpClient) {

        if(retrofit==null){
            startRetrofitInstance(okhttpClient);
        }

        return retrofit.create(createTaskApiService.class);
    }

    public static voiceCommandApiService getApiVoiceCommandApiServiceInstance
            (OkHttpClient okhttpClient) {

        if(retrofit==null){
            startRetrofitInstance(okhttpClient);
        }

        return retrofit.create(voiceCommandApiService.class);
    }
}
