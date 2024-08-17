package com.example.notes_pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_avtivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_avtivity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser==null)
                {
                    startActivity(new Intent(Splash_avtivity.this, Login_Activity.class));
                }
                else
                {
                    startActivity(new Intent(Splash_avtivity.this,MainActivity.class));
                }
                finish();
            }
    },2200);
    }
}