package com.domoticatesis.Fragments;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.domoticatesis.R;


public class Settings extends Fragment {

    // TODO: Rename and change types of parameters

    EditText editport1,editport2,editlimit;
    Button btn_save;
    SharedPreferences preferences;

    public Settings() {
        // Required empty public constructor
    }

    public static Settings newInstance() {
        Settings fragment = new Settings();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.fragment_settings, container, false);

       editport1=view.findViewById(R.id.edit_port1);
       editport2=view.findViewById(R.id.editport2);
       editlimit=view.findViewById(R.id.edit_limit);
       btn_save=view.findViewById(R.id.btn_save_setting);

        preferences = getActivity().getSharedPreferences("settings",
                getActivity().getApplicationContext().MODE_PRIVATE);


        editport1.setText(preferences.getString("port1",""));
        editport2.setText(preferences.getString("port2",""));
        editlimit.setText(preferences.getString("limit",""));



    btn_save.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SharedPreferences.Editor toEdit;
            toEdit = preferences.edit();
            toEdit.putString("port1", editport1.getText().toString());
            toEdit.putString("port2", editport2.getText().toString());
            toEdit.putString("limit", editlimit.getText().toString());
            toEdit.commit();

            Toast.makeText(getActivity(),"Configuración Guardada",
                    Toast.LENGTH_LONG).show();
        }
    });

        return view;
    }

}
