package com.blovvme.makingrestcalls;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blovvme.makingrestcalls.model.WeatherData;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Log Response";
    public static final String BASE_URL = "http://samples.openweathermap.org/data/2.5/forecast?zip=94040&appid=b1b15e88fa797225412429c1c50c122a1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

     String resultResponse = " ";
    WeatherData weatherData;

    public void makingRestCalls(View view) throws IOException {


        Log.d(TAG, "makingRestCalls: ");
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(BASE_URL)
                .build();


        switch (view.getId()){


            case R.id.btnHttp:
                Intent intent = new Intent(this, HttpIntentService.class);
                startService(intent);


                break;
            //synchronous call
            case R.id.btnOkHttp:

                new Thread(new Runnable() {


                    @Override
                    public void run() {

                        try {
                            String result = client.newCall(request).execute().body().string();
                            Log.d(TAG, "run: " + result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
                Toast.makeText(this, "Check your logs",Toast.LENGTH_SHORT).show();
                break;
            //asynchronous call
            case R.id.btnOkHttpAssync:

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {


                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        resultResponse = response.body().string();
                        Gson gson = new Gson();
                        weatherData = gson.fromJson(resultResponse, WeatherData.class);
                        Log.d(TAG, "onResponse: " + weatherData.getCity().getName());


                        //Pass this to the recyclerview
                        //weatherData.getList();

                    }
                });



                break;

            case R.id.btnRetrofit:


                break;


        }
    }


}//



