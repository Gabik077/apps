package com.domoticatesis.Routes;

import com.domoticatesis.Entities.ResultHeader;

import retrofit2.Call;
import retrofit2.http.GET;

public interface currentMonthExpenseApiService {

    @GET("getCurrentMonthExpense")
    Call<ResultHeader> getCurrentMonthExpense();

}
