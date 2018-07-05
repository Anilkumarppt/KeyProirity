package com.example.admin.keyproirityapp.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.admin.keyproirityapp.MainActivity;
import com.example.admin.keyproirityapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class WelcomeScreen extends AppCompatActivity {
    public ProgressBar mprogressBar;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Animation anim1 = AnimationUtils.loadAnimation(this,R.anim.anim);
        ImageView img =(ImageView)findViewById(R.id.imageView);
        img.setAnimation(anim1);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
        anim.setDuration(10000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkBeforeLogin();

            }
        },3000);


    }

    private void checkBeforeLogin() {
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent loginIntent=new Intent(WelcomeScreen.this,MainActivity.class);
            startActivity(loginIntent);
        }
        else {
            Intent loginIntent=new Intent(WelcomeScreen.this,LoginActivity.class);
            startActivity(loginIntent);
        }
    }
}
