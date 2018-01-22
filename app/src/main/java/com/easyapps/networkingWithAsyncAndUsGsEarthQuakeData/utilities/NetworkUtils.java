package com.easyapps.networkingWithAsyncAndUsGsEarthQuakeData.utilities;

import android.net.Uri;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public final class NetworkUtils {

    final static String QUERY_FORMAT_PARAM = "format";
    final static String QUERY_EVENT_TYPE_PARAM = "eventtype";
    final static String QUERY_ORDER_BY_PARAM = "orderby";
    final static String QUERY_LIMIT_PARAM = "limit";

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String EarthQuake_ENDPOINT_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/";
    private static final String format_geojson = "geojson";
    private static final String eventType_earthQuake= "earthquake";
    private static final String orderBy_time= "time";
    private static final String limit_10= "10";
    private static final String METHOD_NAME_QUERY = "query";
//-------------------------------------
    public static URL buildUrl() {
        Uri builtUri = Uri.parse(EarthQuake_ENDPOINT_URL).buildUpon()
                .appendPath(METHOD_NAME_QUERY)
                .appendQueryParameter(QUERY_FORMAT_PARAM, format_geojson)
                .appendQueryParameter(QUERY_EVENT_TYPE_PARAM, eventType_earthQuake)
                .appendQueryParameter(QUERY_ORDER_BY_PARAM, orderBy_time)
                .appendQueryParameter(QUERY_LIMIT_PARAM, limit_10)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built URI " + url);

        return url;
    }

    //------------------------
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            return readFromStream(in);
        } finally {
            urlConnection.disconnect();
        }
    }

    //---------------------------
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    //-----------------------------
}