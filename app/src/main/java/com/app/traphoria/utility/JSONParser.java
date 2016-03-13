package com.app.traphoria.utility;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;


public class JSONParser {
    private static final String TAG = "JSONParser";
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {
    }

    public String getJSONFromUrl(String urlString) {

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = connection.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            json = sb.toString();
            is.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return json;

    }



}
