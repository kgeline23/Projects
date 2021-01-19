package com.example.fhictcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class LocalJson extends AppCompatActivity {

    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    String json_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_json);


    }


    // method to convert the json file into a readable string
    public void getJson(){
        InputStream is;
        Scanner scn;
        try {
            is = getAssets().open("json.json");
            scn = new Scanner(is);
            json_string = (new Scanner(is)).useDelimiter("\\z").next();
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }


}