package com.thegiantgames.dodgerun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    ImageView bg_image ,bg_image2;
    float screenRatioX ,screenRatioY;
    GifImageView elephant_gif , pig_gif , dog_gif , teddy_gif;
    Animation animation ;
    ValueAnimator pig_animator , elephant_animator , teddy_animator , dog_animator;
    RectF elephant_rect , teddy_rect , dog_rect;
    int position_teddy_left ,position_teddy_right , position_elephant_left , position_elephant_right;
    int Counter = 0;
    int col = 0;
    boolean play = true;
    MediaPlayer bgm;
    String url ;

    CharSequence scoreForStorage ;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        bgm = MediaPlayer.create(this , R.raw.bgm);
        bgm.start();


        int whichCharacter = getIntent().getIntExtra("whichCharacter" , 1);
        String imgUrl = getIntent().getStringExtra("imageUrl");


        url  = "https://api-obstacle-dodge.vercel.app/tips";


        ProgressBar bar =  (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.INVISIBLE);
        boolean classicMode = getIntent().getBooleanExtra("ClassicMode" , false);
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT ,ViewGroup.LayoutParams.WRAP_CONTENT );
        dialog.setCancelable(false);
        TextView textView = (TextView) dialog.findViewById(R.id.scoreTextView);
        Button button = (Button) dialog.findViewById(R.id.homeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("char" , MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //editor.putString("Score" , (String) scoreForStorage);

                if (Integer.parseInt((String) scoreForStorage) >=  Integer.parseInt(sharedPreferences.getString("Score" , "1000"))){
                    editor.putString("Score" , (String) scoreForStorage);
                }

                editor.apply();
                startActivity(new Intent(MainActivity.this , Home.class));
                bgm.stop();
            }
        });
        TextView score = (TextView) findViewById(R.id.scoretv);
        if (classicMode){
            score.setVisibility(View.GONE);
        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = displayMetrics.heightPixels;
        float width = displayMetrics.widthPixels;


        float density = getResources().getDisplayMetrics().density;
        screenRatioX =  width / 2220f;
        screenRatioY  = height / 1080f;



        Point point = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(point);
        int  pointHeight =  point.y;
        int pointWidth  = point.x;








        bg_image = (ImageView) findViewById(R.id.bg_image);
        bg_image2 = (ImageView) findViewById(R.id.bg_image2);
        elephant_gif = findViewById(R.id.elephant);
        pig_gif  =findViewById(R.id.pig);
        teddy_gif = findViewById(R.id.teddy);
        dog_gif = findViewById(R.id.dog);
//         elephant_gif.setX(width/3f);


//
        elephant_gif.setAdjustViewBounds(true);
        elephant_gif.setMaxHeight((int) (height/4.5f));
        pig_gif.setAdjustViewBounds(true);
        pig_gif.setMaxHeight((int) (height/4.5f));
        teddy_gif.setAdjustViewBounds(true);
        teddy_gif.setMaxHeight((int) ((int) height/4.5f));
        //teddy_gif.setY(height/1.35f);
        dog_gif.setAdjustViewBounds(true);
        dog_gif.setMaxHeight((int) height/3);
        elephant_gif.setLeft((int)width/3);
        pig_gif.setLeft((int) width/3);






        //elephant_gif.getX();

        ConstraintLayout.LayoutParams elephant_layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        elephant_layoutParams.startToStart = R.id.layoutId;
        elephant_layoutParams.topToTop = R.id.layoutId;
        elephant_layoutParams.topMargin = (int) ((int)height/1.467);
        elephant_layoutParams.leftMargin = (int) width/3;
        elephant_gif.setLayoutParams(elephant_layoutParams);

        ConstraintLayout.LayoutParams teddy_layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        teddy_layoutParams.startToStart = R.id.layoutId;
        teddy_layoutParams.topToTop = R.id.layoutId;
        teddy_layoutParams.topMargin = (int) ((int)height/1.467);
        teddy_layoutParams.leftMargin = (int) ((int) width*1.5);
        teddy_gif.setLayoutParams(teddy_layoutParams);

        ConstraintLayout.LayoutParams pig_layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pig_layoutParams.startToStart = R.id.layoutId;
        pig_layoutParams.topToTop = R.id.layoutId;
        pig_layoutParams.topMargin = (int) ((int)height/1.467);
        pig_gif.setLayoutParams(pig_layoutParams);

        ConstraintLayout.LayoutParams dog_layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dog_layoutParams.startToStart = R.id.layoutId;
        dog_layoutParams.topToTop = R.id.layoutId;
        dog_layoutParams.topMargin = (int) ((int)height/1.75);
        dog_layoutParams.leftMargin = (int) ( (1.5)*width);
        dog_gif.setLayoutParams(dog_layoutParams);


            Glide.with(getApplicationContext()).load(imgUrl).into(elephant_gif);




        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, -1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = bg_image.getWidth();
                final float translationX = width * progress;
                bg_image.setTranslationX(translationX);
                bg_image2.setTranslationX(translationX + width);
            }
        });
        animator.start();











        pig_animator = ValueAnimator.ofFloat(0 , width/4.5f);
        pig_animator.setRepeatCount(1);
        pig_animator.setRepeatMode(ValueAnimator.REVERSE);
        pig_animator.setDuration(3000);
        pig_animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {

                float update  = (float) pig_animator.getAnimatedValue();
                pig_gif.setTranslationX(update);

            }
        });



        elephant_animator = ValueAnimator.ofFloat(0 , -(height/2f));
        elephant_animator.setRepeatCount(1);
        elephant_animator.setRepeatMode(ValueAnimator.REVERSE);
        elephant_animator.setInterpolator(new DecelerateInterpolator());
        elephant_animator.setDuration(2000);
        elephant_animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {

                float update  = (float) elephant_animator.getAnimatedValue();
                elephant_gif.setTranslationY(update);

            }
        });


        teddy_animator = ValueAnimator.ofFloat(0 , -width*2f);
        teddy_animator.setRepeatCount(0);
        teddy_animator.setRepeatMode(ValueAnimator.RESTART);
        teddy_animator.setInterpolator(new LinearInterpolator());
        teddy_animator.setDuration(6000);
        teddy_animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {

                float update  = (float) teddy_animator.getAnimatedValue();
                teddy_gif.setTranslationX(update);

            }
        });


        dog_animator = ValueAnimator.ofFloat(0 , -width*2f);
        dog_animator.setRepeatCount(0);
        dog_animator.setRepeatMode(ValueAnimator.RESTART);
        dog_animator.setDuration(6000);
        dog_animator.setInterpolator(new LinearInterpolator());
        dog_animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {

                float update  = (float) dog_animator.getAnimatedValue();
                dog_gif.setTranslationX(update);

            }
        });







        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                position_teddy_right = (int) teddy_gif.getX()  +  teddy_gif.getWidth();
                position_teddy_left = (int) teddy_gif.getX();
                position_elephant_left = (int) elephant_gif.getX();
                position_elephant_right = (int)  (elephant_gif.getX() + elephant_gif.getWidth()/1.3);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        elephant_rect = new RectF( elephant_gif.getLeft()  , elephant_gif.getTop() , elephant_gif.getRight() ,elephant_gif.getBottom() );
                        teddy_rect = new RectF( (int) teddy_gif.getX() , (int)teddy_gif.getY() , (int)teddy_gif.getX() + teddy_gif.getWidth() , (int)elephant_gif.getY()  + elephant_gif.getHeight());
                        dog_rect = new RectF((int) dog_gif.getX() , (int) dog_gif.getY() , (int) dog_gif.getX() + dog_gif.getWidth() , (int) dog_gif.getY() + dog_gif.getHeight());


//
                        if ( -20<=elephant_gif.getX() - teddy_gif.getX() && elephant_gif.getX() - teddy_gif.getX()<=20 && elephant_gif.getY() + elephant_gif.getHeight() >= teddy_gif.getY()){
                            pig_animator.start();
                            MediaPlayer player = MediaPlayer.create(MainActivity.this , R.raw.hits);
                            MediaPlayer player1 = MediaPlayer.create(MainActivity.this , R.raw.hit);
                            if (play){
                                player.start();
                                player1.start();
                                dialog.show();
                                textView.setText(score.getText());
                                scoreForStorage =  textView.getText();
                                play = false;
                            }



                        }

                        if (-20<=elephant_gif.getX() - dog_gif.getX() && elephant_gif.getX() - dog_gif.getX()<=20 && elephant_gif.getY()  + elephant_gif.getHeight()  >= dog_gif.getY()){
                            MediaPlayer player = MediaPlayer.create(MainActivity.this , R.raw.gameover);
                            if (play){
                                player.start();
                                dialog.show();
                                textView.setText(score.getText());
                                scoreForStorage =  textView.getText();
                                play = false;
                            }
                        }



                    }
                });
            }
        }  , 0 ,  1);



        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Random pick = new Random();
                        boolean bool = pick.nextBoolean();
                        if (bool){
                            teddy_animator.start();
                        }
                        else {
                            dog_animator.start();
                        }
                        Log.v("pick" , ""+ Counter);

                        score.setText((""+ Counter));

                        if (classicMode){
                            bar.setVisibility(View.VISIBLE);
                            bar.setProgress(10*Counter);
                            if (bar.getProgress() == 100){


                                dialog.show();

                            }
                        }

                        Counter = Counter + 1 ;
                    }


                });




            }
        }  , 5000 , 6000);




    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        elephant_animator.start();
        MediaPlayer.create(MainActivity.this , R.raw.jump).start();
        return super.onTouchEvent(event);

    }


}