package com.easyapps.networkingWithAsyncAndUsGsEarthQuakeData.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class JsonUtils {

    public static String[] extractEarthquakes(Context context, String jsonResponse) {

        String[] parsedWeatherData = null;
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
            parsedWeatherData = new String[earthquakeArray.length()];

            for (int i = 0; i < earthquakeArray.length(); i++) {   //Json parsing

                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                Log.i("value", "extractEarthquakes: " + location);
                parsedWeatherData[i] = "Earthquake " +
                        GeneralDateUtils.getFriendlyDateString(context, time, true) +
                        " , \n" + location + " ," +
                        "\n Magnitude - " + magnitude;
            }

        } catch (JSONException e) {

            Log.e("ParsingMethods", "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of earthquakes
        return parsedWeatherData;
    }
}