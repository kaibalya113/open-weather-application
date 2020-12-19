package com.kaibalya.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText enterCity;
    TextView setData;

    String weatherMain, weatherDescription, cityname, windSpeed;
    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in =  urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }catch(Exception ex){
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
//            Log.i("JSON", s);
            //convert to json object
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Toast.makeText(MainActivity.this, weatherInfo, Toast.LENGTH_LONG).show();
                Log.i("JSON", weatherInfo);

                JSONArray jsonArray = new JSONArray(weatherInfo);
                for (int i=0 ; i< jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    //Log.i("JSON", jsonObject1.getString("main"));
                    weatherMain = jsonObject1.getString("main");
                    weatherDescription = jsonObject1.getString("description");
                   // Log.i("JSON", jsonObject1.getString("description"));

                }
                cityname = jsonObject.getString("name");
               // windSpeed = jsonObject.getString("wind");
                Log.i("cityname", jsonObject.getString("name"));



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        enterCity = findViewById(R.id.entercityName);
        String cityName = enterCity.getText().toString();
        btn = findViewById(R.id.getWeather);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTask task = new DownloadTask();
                task.execute("https://samples.openweathermap.org/data/2.5/weather?q="+enterCity.getText().toString()+"&appid=5fe4578f092799bc5ff1cfe98258600a");

                // set data
                setData= findViewById(R.id.textView4);

                setData.setText(weatherMain+"\n"+weatherDescription +"\n"+cityName);
            }
        });


    }
}