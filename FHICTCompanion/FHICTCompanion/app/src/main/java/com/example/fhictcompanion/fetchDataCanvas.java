package com.example.fhictcompanion;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class fetchDataCanvas extends AsyncTask <Void, Void, Void>{

    String currToken,s;

    public fetchDataCanvas(String token){
        this.currToken = token;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        //String filename = params[0];
//        String[] result;
//
//        try {
//            URL url = new URL("https://api.fhict.nl/canvas/courses/me");
//
//            HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
//
//            connection.setRequestProperty("Accept","application/json");
//            connection.setRequestProperty("Authorization","Bearer " + currToken);
//
//            connection.connect();
//
//            //create inputStream
//            InputStream is = connection.getInputStream();
//            Scanner scn = new Scanner(is);
//            s = scn.useDelimiter("\\Z").next();
//
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}
