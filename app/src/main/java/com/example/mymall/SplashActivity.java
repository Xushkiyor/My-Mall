package com.example.mymall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

        SystemClock.sleep(3000);
        Intent registerIntent = new Intent(SplashActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
        finish();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//
//        if (currentUser == null) {
//
//        } else {
//            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//            startActivity(mainIntent);
//            finish();
//        }
//    }
}
