package com.domoticatesis.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.domoticatesis.Entities.ResultHeader;
import com.domoticatesis.R;
import com.domoticatesis.Routes.createTaskApiService;
import com.domoticatesis.Web.APIServices;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Tasks extends Fragment {

    EditText edit_hour,edit_nametask;
    Spinner sp_zones,sp_types;
    Button btn_save;
    int current_zone=1;
    String current_type;
    int current_appliance;

    public Tasks() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Tasks newInstance() {
        Tasks fragment = new Tasks();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        sp_zones = view.findViewById(R.id.sp_zone);
        sp_types = view.findViewById(R.id.sp_type);
        edit_hour = view.findViewById(R.id.edit_hour);
        btn_save = view.findViewById(R.id.btn_save_task);
        edit_nametask = (EditText) view.findViewById(R.id.edit_nametask);

        final List<String> zones_list = new ArrayList<>();
        zones_list.add("Cocina");
        zones_list.add("Habitación");



        final List<String> type_list = new ArrayList<>();
        type_list.add("Encendido");
        type_list.add("Apagado");


        ArrayAdapter<String> spinner_adapter_zone = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_spinner_item, zones_list);

        spinner_adapter_zone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_zones.setAdapter(spinner_adapter_zone);

        sp_zones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


                if(pos==0){
                    current_zone=1;//cocina
                    current_appliance = 8;
                }else{
                    current_zone=  3;//Habitación
                    current_appliance = 11;
                }



            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ArrayAdapter<String> spinner_adapter_type = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_spinner_item, type_list);

        spinner_adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_types.setAdapter(spinner_adapter_type);

        sp_types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


                if(pos==0){
                    current_type=  "task_light_on";
                }else{
                    current_type=  "task_light_off";
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit_nametask.getText().toString().equals("") && !edit_hour.getText().toString().equals("")){
                    getCurrentExpense();
                }else{
                    Toast.makeText(getActivity(),"Ingresar todos los campos",
                            Toast.LENGTH_LONG).show();
                }
    }
});

        return view;
    }

    public void getCurrentExpense() {
        Call<ResultHeader> call;
        OkHttpClient okhttpClient;
        okhttpClient = new OkHttpClient();
        createTaskApiService service_api = APIServices.getApiCreateServiceInstance(okhttpClient);

        call = service_api.createTask(edit_nametask.getText().toString(),current_type,edit_hour.getText().toString(),current_zone,current_appliance);
        call.enqueue(new Callback<ResultHeader>() {


            @Override
            public void onResponse(Call<ResultHeader> call, Response<ResultHeader> response) {
                ResultHeader result=response.body();
                if(response.isSuccessful()){


                    int status=result.getStatus();
                    if(status==1){
                        Toast.makeText(getActivity(),"Tarea Creada",
                                Toast.LENGTH_LONG).show();

                        edit_nametask.setText("");
                        edit_hour.setText("");
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
}
