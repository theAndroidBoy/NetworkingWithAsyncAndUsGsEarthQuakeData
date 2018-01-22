package com.easyapps.networkingWithAsyncAndUsGsEarthQuakeData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.easyapps.networkingWithAsyncAndUsGsEarthQuakeData.utilities.NetworkUtils;
import com.easyapps.networkingWithAsyncAndUsGsEarthQuakeData.utilities.JsonUtils;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String TAG = "flow";
    private TextView mEarthquakeTextView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        Log.i(TAG, "onCreate: ");
        mEarthquakeTextView = findViewById(R.id.tv_earthquake_data);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        loadEarthQuakeData();
    }

    //-------------------------------------------------------------
    private void loadEarthQuakeData() {
        Log.i(TAG, "loadWeatherData: ");
        showEarthQuakeDataView();
        String location = "";
        new FetchWeatherTask().execute(location);
    }

    //-----------------------------------------------------
    private void showEarthQuakeDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mEarthquakeTextView.setVisibility(View.VISIBLE);
    }

    //------------------------------------------------
    private void showErrorMessage() {
        mEarthquakeTextView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    //----------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return true;
    }

    //--------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            mEarthquakeTextView.setText("");
            loadEarthQuakeData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //---------------------------------------------------
    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
//----------------------------
        @Override
        protected String[] doInBackground(String... params) {
            Log.i(TAG, "doInBackground: ");
            if (params.length == 0) {
                return null;
            }

            URL weatherRequestUrl = NetworkUtils.buildUrl();
            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[] simpleJsonWeatherData = JsonUtils
                        .extractEarthquakes(MainActivity.this, jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        //--------------------------------------------------------------------
        @Override
        protected void onPostExecute(String[] weatherData) {
            Log.i(TAG, "onPostExecute: ");
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showEarthQuakeDataView();

                for (String weatherString : weatherData) {
                    mEarthquakeTextView.append((weatherString) + "\n\n\n");
                    Log.i(TAG, "onPostExecute: " + weatherString);
                }
            } else {
                showErrorMessage();
            }
        }
    } //Async task closed
}