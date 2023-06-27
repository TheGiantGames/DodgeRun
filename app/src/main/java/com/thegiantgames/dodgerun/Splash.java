package com.thegiantgames.dodgerun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Splash extends AppCompatActivity {

    TextView tv_tip;
    ProgressBar progressBar;
    ImageView imageView;
    String img = null , tip;
    JSONObject root , character;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        tv_tip = (TextView) findViewById(R.id.tv_tip);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.imageView3);
        JsonObject object = new JsonObject();
        object.addProperty("type"  , "player");


        Intent intent = new Intent(Splash.this , Home.class);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-obstacle-dodge.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DodgeApi dodgeApi = retrofit.create(DodgeApi.class);
        Call<JsonObject> call = dodgeApi.getTip();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    root = new JSONObject(response.body().toString());
                    tip = root.getString("tip");

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if (tip != null){
                    tv_tip.setText("Tip: \n" + tip.toString());
                    progressBar.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                            finish();
                        }
                    }, 1500);
                }


//                if (response.isSuccessful()){
//                    JsonObject jsonObject = response.body();
//                   String tip = String.valueOf(jsonObject.get("tip"));
//                    tv_tip.setText("Tip: \n" + tip.toString());
//                    progressBar.setVisibility(View.GONE);
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            //startActivity(intent);
//                        }
//                    }, 1500);
//                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });






    }
}