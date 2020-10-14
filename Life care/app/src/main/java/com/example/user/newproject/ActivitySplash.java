package com.example.user.newproject;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.lang.annotation.Annotation;

public class ActivitySplash extends AppCompatActivity {

    private static int SPLASH_TIME = 3000; //This is 4 seconds
    Animation headline;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //for hiding title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        //logo design
        txt= (TextView) findViewById(R.id.textView4);
       headline =  AnimationUtils.loadAnimation(this,R.anim.headline);

       txt.setAnimation((Animation) headline);



        //Code to start timer and take action after the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do any action here. Now we are moving to next page
                Intent mySuperIntent = new Intent(ActivitySplash.this, login.class);
                startActivity(mySuperIntent);
                /* This 'finish()' is for exiting the app when back button pressed
                 *  from Home page which is ActivityHome
                 */
                finish();
            }
        }, SPLASH_TIME);
    }
}
