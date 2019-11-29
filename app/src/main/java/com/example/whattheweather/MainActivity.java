package com.example.whattheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView titleBox, resultBox;
    EditText inputBox;
    String city ;

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            URL url;
            String res = "";
            try {
                url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while(data != -1)
                {
                    res += (char)data;
                    data = reader.read();
                }
                return res;
            }catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }
        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            try {
                JSONObject jsonObject =  new JSONObject(res);
                String weatherInfo  = jsonObject.getString("weather");
                Log.i("Sajjan", weatherInfo);
                JSONArray jsonArray = new JSONArray(weatherInfo);
                for(int i = 0 ; i<jsonArray.length(); i++)
                {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    Log.i("Sajjan",jsonPart.getString("main"));
                    Log.i("Sajjan",jsonPart.getString("description"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void findWeather(View view) throws ExecutionException, InterruptedException {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(inputBox.getWindowToken(), 0);

        city = inputBox.getText().toString();
        Toast.makeText(this, "city is " + city, Toast.LENGTH_LONG).show();
        DownloadTask task = new DownloadTask();
//        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=" + city);
//         task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + city);
        String result = task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22").get();
        Log.i("Sajjan", result);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.myimageview);
        titleBox = (TextView) findViewById(R.id.entercitytextview);
        resultBox = (TextView) findViewById(R.id.resultBox);
        inputBox = (EditText) findViewById(R.id.entertext);
//        imageView.setImageResource("/drawable-v24/images1.jpeg");

//        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");
//        task.execute("https://www.internetfags.net/superheroes.php");

    }

}
