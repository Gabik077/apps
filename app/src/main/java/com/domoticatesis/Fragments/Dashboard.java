package com.domoticatesis.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.domoticatesis.Entities.ResultHeader;
import com.domoticatesis.Helpers.GpsHelper;
import com.domoticatesis.Helpers.WeatherHelper;
import com.domoticatesis.R;
import com.domoticatesis.Routes.ExpenseApiService;
import com.domoticatesis.Routes.alarmApiService;
import com.domoticatesis.Routes.currentMonthExpenseApiService;
import com.domoticatesis.Routes.humidityApiService;
import com.domoticatesis.Routes.lightsApiService;
import com.domoticatesis.Routes.lockApiService;
import com.domoticatesis.Routes.temperatureApiService;
import com.domoticatesis.Web.APIServices;
import com.intrusoft.scatter.ChartData;
import com.intrusoft.scatter.PieChart;
import com.timqi.sectorprogressview.ColorfulRingProgressView;
import com.timqi.sectorprogressview.SectorProgressView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 3;

    Typeface weatherFont;
    ToggleButton toggleHab,toggleCoc,toggleAlarm,toggleAlarmGas;
    android.support.v7.widget.SwitchCompat switchLock;

    TextView cityField, detailsField, currentTemperatureField,
            humidity_field, pressure_field, weatherIcon, updatedField
            ,temp_value,humid_value,txtactual,txtahorro,txtlimite;

    PieChart pieChart;
    ColorfulRingProgressView int_temp;
    ColorfulRingProgressView int_hum;
    SharedPreferences preferences;
    SharedPreferences preferencesSettings;
    SectorProgressView sectorProgress;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Dashboard() {
    }


    public static Dashboard newInstance() {
        Dashboard fragment = new Dashboard();
        Bundle args = new Bundle();
      //  args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);


        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weathericons-regular-webfont.ttf");
        toggleHab = view.findViewById(R.id.toggleHab);
        toggleCoc = view.findViewById(R.id.toggleCoc);
        toggleAlarm=view.findViewById(R.id.alarmtoggle);
        toggleAlarmGas=view.findViewById(R.id.alarmtoggle_gas);
        switchLock = view.findViewById(R.id.lockSwith);
        cityField = view.findViewById(R.id.city_field);
        updatedField = view.findViewById(R.id.updated_field);
        detailsField = view.findViewById(R.id.details_field);
        currentTemperatureField = view.findViewById(R.id.current_temperature_field);
        humidity_field = view.findViewById(R.id.humidity_field);
        pressure_field = view.findViewById(R.id.pressure_field);
        weatherIcon = view.findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        pieChart = view.findViewById(R.id.pie_chart);
        int_temp = view.findViewById(R.id.int_temp);
        int_hum =  view.findViewById(R.id.int_hum);
        sectorProgress =  view.findViewById(R.id.pie_chart_expense);
        int_temp =  view.findViewById(R.id.int_temp);
        int_hum =  view.findViewById(R.id.int_hum);
        temp_value = view.findViewById(R.id.temp_value);
        humid_value = view.findViewById(R.id.humid_value);
        txtactual = view.findViewById(R.id.txtactual);
        txtahorro = view.findViewById(R.id.txtahorro);
        txtlimite = view.findViewById(R.id.txtlimite);


        preferences = getActivity().getSharedPreferences("lights_state",
               getActivity().getApplicationContext().MODE_PRIVATE);

        preferencesSettings = getActivity().getSharedPreferences("settings",
                getActivity().getApplicationContext().MODE_PRIVATE);

        checkPreferences(preferences);

        addWeatherContent();

        getExpenseByArea();

        getTemperatureService();

        getCurrentExpense();


        toggleHab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {

                SharedPreferences.Editor toEdit;
                toEdit = preferences.edit();


                if (isChecked) {
                    turnLights("light_on_l1", 8);
                    toEdit.putBoolean("light1", true);
                } else {
                    turnLights("light_off_l1", 8);
                    toEdit.putBoolean("light1", false);
                }
                toEdit.commit();
            }
        });

        toggleCoc.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                SharedPreferences.Editor toEdit;
                toEdit = preferences.edit();

                if(isChecked){
                    turnLights("light_on_l2",11);
                    toEdit.putBoolean("light2", true);
                }else{
                    turnLights("light_off_l2",11);
                    toEdit.putBoolean("light2", false);
                }
                toEdit.commit();
            }
        }) ;

        toggleAlarm.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                SharedPreferences.Editor toEdit;
                toEdit = preferences.edit();

                if(isChecked){
                    setAlarm("alarm_mov_on");

                    toEdit.putBoolean("alarm", true);
                }else{
                    setAlarm("alarm_mov_off");

                    toEdit.putBoolean("alarm", false);
                }
                toEdit.commit();
            }
        }) ;
        toggleAlarmGas.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                SharedPreferences.Editor toEdit;
                toEdit = preferences.edit();

                if(isChecked){
                    setAlarm("alarm_gas_on");
                    toEdit.putBoolean("alarm_gas", true);
                }else{
                    setAlarm("alarm_gas_off");
                    toEdit.putBoolean("alarm_gas", false);
                }
                toEdit.commit();
            }
        }) ;

        switchLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor toEdit;
                toEdit = preferences.edit();

                if(isChecked){
                     setLock("lock");
                    toEdit.putBoolean("lock", true);
                }else{
                    setLock("unlock");
                    toEdit.putBoolean("lock", false);
                }
                toEdit.commit();
            }
        });


        return view;
    }

    public void addWeatherContent(){

        WeatherHelper.placeIdTask asyncTask =new WeatherHelper.placeIdTask(new WeatherHelper.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                humidity_field.setText("Humedad: "+weather_humidity);
                pressure_field.setText("Presión: "+weather_pressure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

            }
        });

        GpsHelper gps = new GpsHelper(getActivity());
        if(gps.canGetLocation()){
            asyncTask.execute(Double.toString(gps.getLatitude()), Double.toString(gps.getLongitude()));
        }else{
            asyncTask.execute("-25.3667", "-57.6"); //  asyncTask.execute("Latitude", "Longitude")
        }



    }


public void addChartConsume (List<ChartData> data,String total){
    Log.d("CHART",data.get(0).getDisplayText());
    pieChart.setChartData(data);
    pieChart.setAboutChart("Total: "+total+" Gs");
}



    public void turnLights(String action,int appliance_id) {
        Call<ResultHeader> call;
       OkHttpClient okhttpClient;
        okhttpClient = new OkHttpClient();
        lightsApiService service_api = APIServices.getApiLightsInstance(okhttpClient);

        call = service_api.turnLights(action,appliance_id);
        call.enqueue(new Callback<ResultHeader>() {


            @Override
            public void onResponse(Call<ResultHeader> call, Response<ResultHeader> response) {
                ResultHeader result=response.body();
                if(response.isSuccessful()){

                        Log.d("STATUS LIGHTS",result.getMsg());

                }else{
                    Log.d("RESULT LIGHTS","ERROR RESPONSE "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResultHeader> call, Throwable t) {
                Log.d("onFailure",t.getMessage());
            }
        });

    }

    public void setAlarm(String action) {
        Call<ResultHeader> call;
        OkHttpClient okhttpClient;
        okhttpClient = new OkHttpClient();
        alarmApiService service_api = APIServices.getApiAlarmInstance(okhttpClient);

        call = service_api.setAlarm(action);
        call.enqueue(new Callback<ResultHeader>() {


            @Override
            public void onResponse(Call<ResultHeader> call, Response<ResultHeader> response) {
                ResultHeader result=response.body();
                if(response.isSuccessful()){

                    Log.d("ALARM",result.getMsg());

                }else{
                    Log.d("RESULT ALARM","ERROR RESPONSE "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResultHeader> call, Throwable t) {
                Log.d("onFailure",t.getMessage());
            }
        });

    }
    public void setLock(String action) {
        Call<ResultHeader> call;
        OkHttpClient okhttpClient;
        okhttpClient = new OkHttpClient();
        lockApiService service_api = APIServices.getApiLockInstance(okhttpClient);

        call = service_api.setLock(action);
        call.enqueue(new Callback<ResultHeader>() {


            @Override
            public void onResponse(Call<ResultHeader> call, Response<ResultHeader> response) {
                ResultHeader result=response.body();
                if(response.isSuccessful()){

                    Log.d("LOCK",result.getMsg());

                }else{
                    Log.d("RESULT LOCK","ERROR RESPONSE "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResultHeader> call, Throwable t) {
                Log.d("onFailure",t.getMessage());
            }
        });

    }
    public void getTemperatureService() {
        Call<ResultHeader> call;
        OkHttpClient okhttpClient;
        okhttpClient = new OkHttpClient();
        temperatureApiService service_api = APIServices.getApiTemperatureInstance(okhttpClient);

        call = service_api.getTemperature();
        call.enqueue(new Callback<ResultHeader>() {


            @Override
            public void onResponse(Call<ResultHeader> call, Response<ResultHeader> response) {
                ResultHeader result=response.body();
                if(response.isSuccessful()){
                    try {
                        Log.d("TEMPERATURE", result.getMsg() + "-" + result.getTemperature().getValue());
                        temp_value.setText(result.getTemperature().getValue().trim().concat("°C"));
                        int_temp.setPercent(Float.parseFloat(result.getTemperature().getValue()));

                        getHumidity();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Log.d("RESULT TEMPERATURE","ERROR RESPONSE "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResultHeader> call, Throwable t) {
                if(t!=null){
                    Log.d("onFailure",t.getMessage());
                }

            }
        });

    }
    public void getHumidity() {
        Call<ResultHeader> call;
        OkHttpClient okhttpClient;
        okhttpClient = new OkHttpClient();
        humidityApiService service_api = APIServices.getApiHumidityInstance(okhttpClient);

        call = service_api.getHumidity();
        call.enqueue(new Callback<ResultHeader>() {


            @Override
            public void onResponse(Call<ResultHeader> call, Response<ResultHeader> response) {
                ResultHeader result=response.body();
                if(response.isSuccessful()){

                    Log.d("HUMIDITY",result.getMsg()+"-"+result.getHumidity().getValue());

                    humid_value.setText(result.getHumidity().getValue().trim().concat("%"));
                    int_hum.setPercent(Float.parseFloat(result.getHumidity().getValue()));

                }else{
                    Log.d("RESULT HUMIDITY","ERROR RESPONSE "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResultHeader> call, Throwable t) {
                Log.d("onFailure",t.getMessage());
            }
        });

    }
    public void getExpenseByArea() {
        Call<ResultHeader> call;
        OkHttpClient okhttpClient;
        okhttpClient = new OkHttpClient();
        ExpenseApiService service_api = APIServices.getApiAreaExpenseInstance(okhttpClient);

        call = service_api.getExpenseByArea();
        call.enqueue(new Callback<ResultHeader>() {


            @Override
            public void onResponse(Call<ResultHeader> call, Response<ResultHeader> response) {
                ResultHeader result=response.body();
                if(response.isSuccessful()){

                    List<ChartData> data = new ArrayList<>();
                    Double total=0.0;


                    for (int i = 0; i < result.getExpense_list().size(); i++) {
                        total=total+result.getExpense_list().get(i).getExpense();
                    }

                    Log.d("TOTAL",""+total);
                    for (int i = 0; i < result.getExpense_list().size(); i++) {
                        Log.d("TOTAL area",""+result.getExpense_list().get(i).getExpense().floatValue()/total.floatValue()*100);
                        Log.d("EXPENSE",""+result.getExpense_list().get(i).getExpense());
                        data.add(new ChartData(result.getExpense_list().get(i).getArea_name()+" "+result.getExpense_list().get(i).getExpense().intValue()+" Gs.", (result.getExpense_list().get(i).getExpense().floatValue()/total.floatValue()*100)-0.1f));

                    }

                   if(!data.isEmpty()){
                       addChartConsume(data,total.intValue()+"");
                   }

                }else{
                    Log.d("RESULT AREA EXPENSE","ERROR RESPONSE "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResultHeader> call, Throwable t) {
                if(t!=null){
                    Log.d("onFailure_areas",t.getMessage());
                }

            }
        });

    }

    public void getCurrentExpense() {
        Call<ResultHeader> call;
        OkHttpClient okhttpClient;
        okhttpClient = new OkHttpClient();
        currentMonthExpenseApiService service_api = APIServices.getApiCurrentExpenseInstance(okhttpClient);

        call = service_api.getCurrentMonthExpense();
        call.enqueue(new Callback<ResultHeader>() {


            @Override
            public void onResponse(Call<ResultHeader> call, Response<ResultHeader> response) {
                ResultHeader result=response.body();
                if(response.isSuccessful()){


                    double total=result.getTotalExpense();

                    String l= preferencesSettings.getString("limit", "");
                    if(l!=null){
                        double limit= 150000.0;
                        try{
                             limit =  Integer.parseInt(l);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }

                        NumberFormat formatter = NumberFormat.getInstance(new Locale("es"));
                        if(limit!=0.0){


                            if(total>limit){
                                total=limit;
                                double diff= total-limit;
                                txtahorro.setText("• Limite de consumo excedido por: "+formatter.format((int)diff)+" Gs.");
                            }else{
                                double diff= limit-total;

                                txtahorro.setText("• Estas ahorrando: "+formatter.format((int)diff)+" Gs.");
                            }

                        }


                        sectorProgress.setPercent((float) (total/limit)*100 );

                        txtlimite.setText("Límite: "+formatter.format((int)limit)+" Gs.");
                        txtactual.setText("Consumo actual: "+formatter.format((int)total)+" Gs.");


                        Log.d("RESULT TOTAL limit",""+limit);
                    }



                }else{
                    Log.d("RESULT TOTAL EXPENSE","ERROR RESPONSE "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResultHeader> call, Throwable t) {
                if(t!=null){
                    Log.d("onFailure TOTAL EXPENSE",t.getMessage());
                }

            }
        });

    }
private void checkPreferences(SharedPreferences preferences){
        if (preferences!=null) {
            boolean light1 = preferences.getBoolean("light1", false);
            boolean light2 = preferences.getBoolean("light2", false);

            boolean alarm = preferences.getBoolean("alarm", false);
            boolean lock = preferences.getBoolean("lock", false);

            if(light1){
                toggleHab.setChecked(true);
            }else{
                toggleHab.setChecked(false);
            }
            if(light2){
                toggleCoc.setChecked(true);
            }else{
                toggleCoc.setChecked(false);
            }

            if(alarm){
                toggleAlarm.setChecked(true);
            }else{
                toggleAlarm.setChecked(false);
            }
            if(lock){
                switchLock.setChecked(true);
            }else{
                switchLock.setChecked(false);
            }
        }
    }




}
