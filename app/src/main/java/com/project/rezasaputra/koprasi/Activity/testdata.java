package com.project.rezasaputra.koprasi.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.project.rezasaputra.koprasi.R;

public class testdata extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView namatxt = (TextView) findViewById(R.id.namaadmin);
        TextView roletxt = (TextView) findViewById(R.id.roleadmin);

        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);

        final String id = pref.getString("id", "");
        final String nama = pref.getString("name", "");
        final String role = pref.getString("roles", "");

        namatxt.setText(nama);
        roletxt.setText(role);
    }
    public void logoutUser() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id","");
        editor.putString("name","");
        editor.putString("roles","");
        editor.putInt("login",0);
        editor.clear();
        editor.commit();
        // pergi ke login activity
        Intent intent = new Intent(testdata.this, Login.class);
        startActivity(intent);
        finish();
    }
}