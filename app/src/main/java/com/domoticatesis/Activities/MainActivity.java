package com.domoticatesis.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.domoticatesis.Entities.ResultHeader;
import com.domoticatesis.Fragments.Dashboard;
import com.domoticatesis.Fragments.Settings;
import com.domoticatesis.Fragments.Tasks;
import com.domoticatesis.R;
import com.domoticatesis.Routes.voiceCommandApiService;
import com.domoticatesis.Web.APIServices;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        preferences =getSharedPreferences("process",
                getApplicationContext().MODE_PRIVATE);

        //lockDrawer(drawer);

        getSupportActionBar().setTitle("Dashboard");
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, Dashboard.newInstance() )
                .commit();

    }

    public void lockDrawer(DrawerLayout navigationView){

        navigationView.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        navigationView.setScrimColor(getResources().getColor(R.color.colorTransparent));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_voice) {
            promptSpeechInput();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            getSupportActionBar().setTitle("Dashboard");
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, Dashboard.newInstance() )
                    .commit();
        } else if (id == R.id.nav_manage) {
            getSupportActionBar().setTitle("Configuración");
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, Settings.newInstance() )
                    .commit();

        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_tasks) {

            getSupportActionBar().setTitle("Programación de tareas");
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, Tasks.newInstance() )
                    .commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    public static String limpiarAcentos(String cadena) {
        String limpio =null;
        if (cadena !=null) {
            String valor = cadena;
            valor = valor.toUpperCase();
            // Normalizar texto para eliminar acentos, dieresis, cedillas y tildes
            limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
            // Quitar caracteres no ASCII excepto la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
            limpio = limpio.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores
            limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC);
        }
        return limpio;
    }
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                   // txtSpeechInput.setText(result.get(0));
                    Log.d("VOICE",result.get(0));
                    if(result.get(0)!=null && !result.get(0).equals("")){

                        sendVoiceText(limpiarAcentos(result.get(0)).toLowerCase(),preferences.getInt("pid", 0));
                    }

                }
                break;
            }

        }
    }

    public void sendVoiceText(String voice,int pid) {
        Call<ResultHeader> call;
        OkHttpClient okhttpClient;
        okhttpClient = new OkHttpClient();
        voiceCommandApiService service_api = APIServices.getApiVoiceCommandApiServiceInstance(okhttpClient);

        call = service_api.sendVoice(voice,pid);
        call.enqueue(new Callback<ResultHeader>() {


            @Override
            public void onResponse(Call<ResultHeader> call, Response<ResultHeader> response) {
                ResultHeader result = response.body();
                if (response.isSuccessful()) {

                    SharedPreferences.Editor toEdit;
                    toEdit = preferences.edit();
                    toEdit.putInt("pid", result.getPid());
                    toEdit.commit();
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
