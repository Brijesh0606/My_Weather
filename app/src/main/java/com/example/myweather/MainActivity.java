package com.example.myweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView city_name,city_temp,feel_like,min_temp,max_temp,pressure,humidity,visibility;
    EditText et_cityname;
    ImageView condition_img,search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city_name = findViewById(R.id.city_name);
        city_temp = findViewById(R.id.city_temp);
        feel_like = findViewById(R.id.feel_like);
        min_temp  = findViewById(R.id.min_temp);
        max_temp  = findViewById(R.id.max_temp);
        pressure  = findViewById(R.id.pressure);
        humidity  = findViewById(R.id.humidity);
        visibility= findViewById(R.id.visibility);

        et_cityname = findViewById(R.id.et_cityname);

        condition_img = findViewById(R.id.condition_img);
        search        = findViewById(R.id.search);

        search.setOnClickListener(v -> {
            String cityname = et_cityname.getText().toString();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            getdata(cityname);
        });
    }

    private void getdata(String cityname) {
        String apikey = "306e0bbe1b64bc3d166cbee09123fb1c";
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+cityname+"&appid="+apikey;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        @SuppressLint("SetTextI18n") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject object = response.getJSONObject("main");
                JSONArray object1 = response.getJSONArray("weather");
                JSONObject weatherObject = object1.getJSONObject(0);

                String temperature = object.getString("temp");
                double temp = Double.parseDouble(temperature)-273.15;
                city_temp.setText(Double.toString(temp).substring(0,5)+" °C");

                String feellike = object.getString("feels_like");
                double feeltemp = Double.parseDouble(feellike)-273.15;
                feel_like.setText(Double.toString(feeltemp).substring(0,5)+" °C");

                String mintemp = object.getString("temp_min");
                double tempmin = Double.parseDouble(mintemp)-273.15;
                min_temp.setText(Double.toString(tempmin).substring(0,5)+" °C");

                String maxtemp = object.getString("temp_max");
                double tempmax = Double.parseDouble(maxtemp)-273.15;
                max_temp.setText(Double.toString(tempmax).substring(0,5)+" °C");

                String pressurevalue = object.getString("pressure");
                pressure.setText(pressurevalue+" mbar");

                String humidityvalue = object.getString("humidity");
                humidity.setText(humidityvalue+" %");

                String visibilityvalue = response.getString("visibility");
                visibility.setText(visibilityvalue+" m");

                String cityname1 = response.getString("name");
                city_name.setText("Weather of "+cityname1);

                String conditions = weatherObject.getString("main");

                switch (conditions) {
                    case "Rain":
                        condition_img.setBackgroundResource(R.drawable.raining);
                        break;
                    case "Clouds":
                        condition_img.setBackgroundResource(R.drawable.clouds);
                        break;
                    case "Snow":
                        condition_img.setBackgroundResource(R.drawable.snow);
                        break;
                    case "Clear":
                        condition_img.setBackgroundResource(R.drawable.sun);
                        break;
                    case "Dust":
                        condition_img.setBackgroundResource(R.drawable.sandstorm);
                        break;
                    case "Mist":
                        condition_img.setBackgroundResource(R.drawable.mist);
                        break;
                    case "Haze":
                        condition_img.setBackgroundResource(R.drawable.fog);
                        break;
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> Toast.makeText(MainActivity.this, "Please Check City Name!!", Toast.LENGTH_SHORT).show());
        queue.add(request);
    }
}