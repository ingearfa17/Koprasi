package com.project.rezasaputra.koprasi.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.rezasaputra.koprasi.Activity.helper.AppConfig;
import com.project.rezasaputra.koprasi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FormKelurahan extends AppCompatActivity {
    private static final String TAG = Login.class.getSimpleName();
    SharedPreferences pref;
    Spinner spinner;
    String URL = "https://koperasi.digitalfatih.com/apigw/koperasi/getkoperasibyid/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_kelurahan);
        spinner=(Spinner)findViewById(R.id.namakoprasi);

        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        final String kecamatan = pref.getString("id_kec", "");
        final String kelurahan = pref.getString("id_kel", "");

        if (!kecamatan.isEmpty() && !kelurahan.isEmpty()) {
            // login user
            loadSpinnerData(kecamatan, kelurahan);
        } else {
            // jika inputan kosong tampilkan pesan
            Toast.makeText(getApplicationContext(),
                    "Data Tidak Ada", Toast.LENGTH_LONG)
                    .show();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                koperasi_profile koperasi = (koperasi_profile) adapterView.getSelectedItem();
                SharedPreferences idkop = getApplicationContext().getSharedPreferences("koperasi", 0);// 0 - for private mode
                SharedPreferences.Editor editor = idkop.edit();
                editor.putString("id_kop", koperasi.getId());
                editor.commit();

                Toast.makeText(FormKelurahan.this, "Id Koperasi: "+idkop.getString("id_kop", null)+",  Nama Koperasi : "+koperasi.getName(), Toast.LENGTH_SHORT).show();

                String id = koperasi.getId();
                String nama = koperasi.getName();
                String badanhukum = koperasi.getBadanhukum();
                String alamat = koperasi.getAlamat();
                String tlp = koperasi.getTlp();

                TextView badanhktxt = (TextView) findViewById(R.id.badanhukumKp);
                TextView alamattxt = (TextView) findViewById(R.id.alamatKp);
                TextView tlptxt = (TextView) findViewById(R.id.tlpKp);
                TextView namatxt  = (TextView) findViewById(R.id.namaKp);

                badanhktxt.setText(badanhukum);
                alamattxt.setText(alamat);
                tlptxt.setText(tlp);
                namatxt.setText(nama);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }
    private void loadSpinnerData(final String kecamatan, final String kelurahan) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

            StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.URL_KOPRASI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<koperasi_profile> koperasiList = new ArrayList<>();
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getInt("success")==1){
                        JSONArray jsonArray=jsonObject.getJSONArray("name");
                        koperasiList.add(new koperasi_profile("", "-Pilih Koperasi-", "-sample-", "-sample", "-sample"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            koperasiList.add(new koperasi_profile(jsonObject1.getString("id_koperasi"), jsonObject1.getString("nm_koperasi"), jsonObject1.getString("no_badan_hukum"), jsonObject1.getString("alamat_kantor"),jsonObject1.getString("no_tlp")));
                        }
                        ArrayAdapter<koperasi_profile> adapter = new ArrayAdapter<koperasi_profile>(FormKelurahan.this, android.R.layout.simple_spinner_dropdown_item, koperasiList);
                        spinner.setAdapter(adapter);
                    }
                    ArrayAdapter<koperasi_profile> adapter = new ArrayAdapter<koperasi_profile>(FormKelurahan.this, android.R.layout.simple_spinner_dropdown_item, koperasiList);
                    spinner.setAdapter(adapter);
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){@Override
        public Map<String, String> getParams(){
            Map<String, String> params = new HashMap<>();
            params.put("id_kec", kecamatan);
            params.put("id_kel", kelurahan);
            return params;
        }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }
    /*This function is for contekan*/
    private void setDataKoperasi() {
        ArrayList<koperasi_profile> koperasiList = new ArrayList<>();
        //Add countries

        koperasiList.add(new koperasi_profile("1", "India", "sample", "sample", "sammple"));
        koperasiList.add(new koperasi_profile("2", "USA", "sample", "",""));
        koperasiList.add(new koperasi_profile("3", "China","","",""));
        koperasiList.add(new koperasi_profile("4", "UK","","",""));

        //fill data in spinner
        ArrayAdapter<koperasi_profile> adapter = new ArrayAdapter<koperasi_profile>(FormKelurahan.this, android.R.layout.simple_spinner_dropdown_item, koperasiList);
        spinner.setAdapter(adapter);
        //spinner.setSelection(adapter.getPosition(myItem));//Optional to set the selected item.
    }

    public void search(View view) {
        Intent intent = new Intent(FormKelurahan.this, Form1Kelurahan.class);
        startActivity(intent);
    }


}