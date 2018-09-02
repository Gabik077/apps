package com.domoticatesis.Routes;

import com.domoticatesis.Entities.ResultHeader;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by gabriel_cabrera on 20/4/18.
 */

public interface ExpenseApiService {
    @GET("get_monthly_expense_by_area")
    Call<ResultHeader> getExpenseByArea();

}
