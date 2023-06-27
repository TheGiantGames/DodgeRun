package com.thegiantgames.dodgerun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {


    Button  btn_forward , btn_backward , btn_select;
     ImageView character_img;
    TextView tv_sharacter ,  tv_leaderBoard , dialog_score_tv , dialog_names_tv , rank_tv;

    public String Str;
    int getScore;
    boolean isAdded = true;

    String names = "" , score = "";
    HashMap<String , Integer> map;

    int charact ;
    String charactImage;





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        SharedPreferences sharedPreferences = getSharedPreferences("char" , MODE_PRIVATE);
        map = new HashMap<>();


        final int[] whichCharacter = {0};
        tv_leaderBoard = findViewById(R.id.leaderBoard);
        final String[] rootStr = {"" , ""};
        final String[] rootScore = {""};



        JSONObject root = new JSONObject();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> imgUrl = new ArrayList<>();
        ArrayList<String> names_score = new ArrayList<>();
        ArrayList<Integer> scoresArray = new ArrayList<>();


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float width = displayMetrics.widthPixels;
        float height = displayMetrics.heightPixels;


        Dialog dialog = new Dialog(Home.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.character_dialog);
        dialog.getWindow().setLayout((int) (width*0.9f), (int) (height*0.9));
        character_img = dialog.findViewById(R.id.character_img);
        TextView character_name = dialog.findViewById(R.id.character_name);
        TextView character_desc = dialog.findViewById(R.id.character_description);


        btn_backward = (Button) dialog.findViewById(R.id.btn_back);
        btn_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                whichCharacter[0] = whichCharacter[0] - 1 ;

                if (whichCharacter[0] < 0  ){
                    whichCharacter[0] = 2;
                }



                    if (!imgUrl.isEmpty()){
                        Glide.with(getApplicationContext()).load(imgUrl.get(whichCharacter[0])).into(character_img);
                    }

                    character_name.setText(names.get(whichCharacter[0]));
                    character_desc.setText( "Description: \n\n" + description.get(whichCharacter[0]));
            }
        });
        btn_forward = (Button) dialog.findViewById(R.id.btn_forward);
        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichCharacter[0] = whichCharacter[0] + 1;

                if (!imgUrl.isEmpty()){
                    Glide.with(getApplicationContext()).load(imgUrl.get(whichCharacter[0])).into(character_img);
                }

                character_name.setText(names.get(whichCharacter[0]));
                character_desc.setText( "Description: \n" + description.get(whichCharacter[0]));

                if (whichCharacter[0] > 2){
                    whichCharacter[0] = 0;
                }

            }



        });

        btn_select = dialog.findViewById(R.id.btn_select);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("character" , whichCharacter[0]);
                editor.apply();
                dialog.dismiss();
            }
        });




        Dialog scoreDialog =  new Dialog(Home.this);
        scoreDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        scoreDialog.setContentView(R.layout.score_layout);
        scoreDialog.setCancelable(true);
        scoreDialog.getWindow().setLayout((int) (width*0.9f), (int) (height*0.9));
        dialog_names_tv = scoreDialog.findViewById(R.id.dialog_names_tv);
        dialog_score_tv = scoreDialog.findViewById(R.id.dialog_score_tv);
        rank_tv = scoreDialog.findViewById(R.id.rank_tv);



        tv_leaderBoard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              getScore = Integer.parseInt(sharedPreferences.getString("Score" , "0"));

               // getScore = 71;
                if (isAdded){
                    map.put("-----Vishal-----" , getScore);
                   // map.put("hello" , 10);
                    //names_score.add("Vishal");
                   // scoresArray.add(getScore);
                    isAdded = false;
                }




//                Collections.sort(scoresArray);
//                ArrayList<Integer> nstore = new ArrayList<>(scoresArray);
//                int[] indexes = new int[scoresArray.size()];
//                for (int n = 0; n < scoresArray.size(); n++){
//                    indexes[n] = nstore.indexOf(scoresArray.get(n));
//                }
//                String name ="";
//                String score = "";
//
//                for (int i = 0 ; i < names_score.size() ; i++){
//                    name = name + "\n" +  names_score.get(indexes[i]);
//                }
//                for (int i = 0 ; i < scoresArray.size() ; i++){
//                    score = score + "\n" +  scoresArray.get(i);
//                }
//
//
//                int rank = 25 - nstore.indexOf(getScore);


//
//                for (int i = 0 ; i< scoresArray.size() + 1 ; i++){
//                    map.put(names_score.get(i) , scoresArray.get(i) );
//                }
//               HashMap<String , Integer> hashMap = new HashMap<>();
//                TreeMap<Integer,String> tm=new  TreeMap<Integer,String> ();
//                Iterator itr=tm.values().iterator();
//                while(itr.hasNext())
//                {
//                    int key=(int)itr.next();
//
//                    hashMap.put(String.valueOf(map.get(key)), key);
//
//                }

                String string = "";
                String str = "";

                for (Integer ss : sortHashMap(map).values()){
                    str = str + "\n" + ss;
                }

                int i = 0;
                boolean tr = true;
                for (String s : sortHashMap(map).keySet()){
                    if (tr){
                        i++;
                    }

                    if (s.equals("-----Vishal-----")) {
                        tr = false;
                    }
                     string = string +  "\n" + s;
                }








                    //dialog_names_tv.setText("" + rank);
                scoreDialog.show();
                dialog_names_tv.setText( "PLAYERS: " + "\n\n" +  string);
               dialog_score_tv.setText( "SCORES: " + "\n\n" +   str);
                rank_tv.setText("Current Rank: " + (26 - i));
              // rank_tv.setText("" + i);


               //dialog_score_tv.setText(Arrays.toString(indexes));
            }
        });




        tv_sharacter = findViewById(R.id.btn_character);
        tv_sharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                if (!imgUrl.isEmpty()){
                    Glide.with(getApplicationContext()).load(imgUrl.get(whichCharacter[0])).into(character_img);
                }

                character_name.setText(names.get(whichCharacter[0]));
                character_desc.setText( "Description: \n" + description.get(whichCharacter[0]));


            }
        });









        ArrayList<String> list = new ArrayList<>();

        TextView infinte = (TextView) findViewById(R.id.infinte_run_tv);
        infinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                charact  = sharedPreferences.getInt("character"  , 0 );
                if (imgUrl.size() != 0){
                    charactImage = imgUrl.get(charact);
                }
                Log.v("IMAGE" , "" + imgUrl.size());

                Intent intent = new Intent(Home.this ,MainActivity.class);
                intent.putExtra("whichCharacter"  ,charact);
                intent.putExtra("imageUrl" , charactImage);
                // startActivity(new Intent(Home.this, MainActivity.class));
                startActivity(intent);
            }
        });

        TextView textView = (TextView) findViewById(R.id.classic_run_tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                charact  = sharedPreferences.getInt("character"  , 0 );
                if (imgUrl.size() != 0){
                    charactImage = imgUrl.get(charact);
                }
                Log.v("IMAGE" , "" + imgUrl.size());

                Intent intent = new Intent(Home.this ,MainActivity.class);
                intent.putExtra("ClassicMode" , true);
                intent.putExtra("whichCharacter"  ,charact);
                intent.putExtra("imageUrl" , charactImage);
                startActivity(intent);
            }
        });


        JsonObject object = new JsonObject();
        object.addProperty("type"  , "player");



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-obstacle-dodge.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DodgeApi dodgeApi = retrofit.create(DodgeApi.class);

       Call<JsonObject> call = dodgeApi.createPost(object);
       call.enqueue(new Callback<JsonObject>() {
           @Override
           public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
               rootStr[0] = response.body().toString() ;
               //textView.setText("" + response.body().toString());
               //Str = response.body().toString();
               //tv_leaderBoard.setText(Str);
           }

           @Override
           public void onFailure(Call<JsonObject> call, Throwable t) {

           }
       });



       Call<JsonObject> callScore = dodgeApi.getScores();
       callScore.enqueue(new Callback<JsonObject>() {
           JSONObject rootjson;
           JSONArray scoreArr;
           JSONObject score;
           @Override
           public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


               //rootStr[1] = response.body().toString();
               try {
                   rootjson = new JSONObject(response.body().toString());
                   scoreArr = rootjson.getJSONArray("scores");
                   for (int i = 0 ; i <scoreArr.length() ; i++){
                       score = scoreArr.getJSONObject(i);
                       String name = score.getString("name");
                       int sc = score.getInt("score");
                       map.put(name , sc);
                      // names_score.add(name);
                       //scoresArray.add(sc);
                   }

               } catch (JSONException e) {
                   throw new RuntimeException(e);
               }



           }

           @Override
           public void onFailure(Call<JsonObject> call, Throwable t) {

           }
       });



//       Thread thread = new Thread(new Runnable() {
//           JSONObject root;
//
//           {
//               try {
//                   root = new JSONObject(rootScore[0]);
//                  // tv_leaderBoard.setText(root.toString());
//               } catch (JSONException e) {
//                   throw new RuntimeException(e);
//               }
//           }
//
//           @Override
//           public void run() {
//
//           }
//       });
//       thread.start();


        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    JSONObject root;
                    JSONArray characters;
                    @Override
                    public void run() {
                        if (!rootStr[0].equals("") ){
                            try {
                                root = new JSONObject(rootStr[0]);
                                characters = root.getJSONArray("characters");
                                for (int i = 0 ; i < characters.length(); i++){
                                    JSONObject character = characters.getJSONObject(i);
                                    String name = character.getString("name");
                                    String des = character.getString("description");
                                    String url = character.getString("imageUrl");
                                    imgUrl.add(url);
                                    description.add(des);
                                    names.add(name);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            //textView.setText(imgUrl.get(0));

                        }
                    }
                });
            }
        }, 100 , 1000);


        Log.v("STRING LENGTH" , "" + rootStr[0].equals(""));



    }

    public static HashMap<String , Integer> sortHashMap(HashMap<String , Integer> hashMap){
        List<Map.Entry<String ,Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hashMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue().compareTo(o2.getValue()));
            }
        });

        HashMap<String , Integer> hashMap1 = new LinkedHashMap<String , Integer>();
        for (Map.Entry<String , Integer> a : list){
            hashMap1.put(a.getKey(), a.getValue());

        }
        return hashMap1;
    }

}