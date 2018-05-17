package com.project.rezasaputra.koprasi.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.rezasaputra.koprasi.Activity.helper.AppConfig;
import com.project.rezasaputra.koprasi.Activity.helper.AppController;
import com.project.rezasaputra.koprasi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Philipus on 20/02/2016.
 */
public class Login extends Activity {
    private static final String TAG = Login.class.getSimpleName();
    private Button btnLogin;
    private TextView btnLinkToRegister,skip,reset;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("data", MODE_PRIVATE);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (TextView) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // ngecek apakah user udah login atau belum
        pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        final int login = pref.getInt("login", 0);
        final int roles = pref.getInt("id_roles", 0);
        if (login == 1) {
            // kalau user ternyata udah login langsung di lempar ke main activity tanpa harus login terlebih dahulu
            if (roles == 1) {
                Intent intent = new Intent(Login.this, MainKecamatan.class);
                startActivity(intent);
            }else if (roles == 21) {
                Intent intent = new Intent(Login.this, MainKelurahan.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(Login.this, MainPemkod.class);
                startActivity(intent);
            }
            finish();
        }

        // ketika login button di klik
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // ngecek apakah inputannya kosong atau tidak
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // jika inputan kosong tampilkan pesan
                    Toast.makeText(getApplicationContext(),
                            "Jangan kosongkan email dan password!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link ke register activity
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Login.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void checkLogin(final String email, final String password) {
        // Tag biasanya digunakan ketika ingin membatalkan request volley
        String tag_string_req = "req_login";
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // ngecek node error dari api
                    if (!error) {
                        // user berhasil login
                        String user_id = jObj.getString("user_id");
                        String user_name = jObj.getString("user_name");
                        String full_name = jObj.getString("full_name");
                        String roles_name = jObj.getString("roles_name");
                        String kec_id = jObj.getString("id_kec");
                        String kel_id = jObj.getString("id_kel");
                        Integer roles_id = jObj.getInt("roles_id");


                        // buat session user yang sudah login yang menyimpan id,nama,full name, roles id, roles name
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("user_id", user_id);
                        editor.putString("user_name", user_name);
                        editor.putString("name", full_name);
                        editor.putString("id_kel", kel_id);
                        editor.putString("id_kec", kec_id);
                        editor.putInt("id_roles", roles_id);
                        editor.putString("roles", roles_name);
                        editor.putInt("login", 1);
                        editor.commit();

                        //jika sudah masuk ke mainactivity
                    pref = getSharedPreferences("data", Context.MODE_PRIVATE);
                    final int roles = pref.getInt("id_roles", 0);

                    if (roles == 1) {
                            Intent intent = new Intent(Login.this,
                                    MainKecamatan.class);
                            startActivity(intent);
                        }else if (roles == 21){
                            Intent intent = new Intent(Login.this,
                                    MainKelurahan.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(Login.this,
                                    MainMenu.class);
                            startActivity(intent);
                        }

                        finish();
                    } else {
                        //terjadi error dan tampilkan pesan error dari API
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                //cek error timeout, noconnection dan network error
                if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(),
                            "Please Check Your Connection",
                            Toast.LENGTH_SHORT).show();}
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // kirim parameter ke server
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        // menggunakan fungsi volley adrequest yang kita taro di appcontroller
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    //untuk menampilkan loading dialog
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}