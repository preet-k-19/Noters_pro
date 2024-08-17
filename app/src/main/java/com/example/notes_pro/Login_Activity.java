package com.example.notes_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {
    EditText emailEdt,passwordEdt;
    Button loginBtn;
    ProgressBar progressBar;
    TextView create_acc_txt;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdt=findViewById(R.id.email_edt);
        passwordEdt=findViewById(R.id.password_edt);
        loginBtn=findViewById(R.id.login_acc_btn);
        progressBar=findViewById(R.id.progress_bar);
        create_acc_txt=findViewById(R.id.create_acc_txt_view_btn);

        loginBtn.setOnClickListener(v->loginUser());
        create_acc_txt.setOnClickListener(v->startActivity(new Intent(Login_Activity.this,CreateAcount.class)));
    }

    void loginUser()
    {
        String email= emailEdt.getText().toString();
        String password= passwordEdt.getText().toString();


        boolean isvalidated =validate(email,password);
        if(!isvalidated){
            return;
        }
        loginAccountInFireBase(email,password);

    }

    void loginAccountInFireBase(String email,String password)
    {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                     changeInProgress(false);
                     if(task.isSuccessful())
                     {
                         //log in success
                         if(firebaseAuth.getCurrentUser().isEmailVerified())
                         {
                             // go to mainActivity
                             startActivity(new Intent(Login_Activity.this,MainActivity.class));
                             finish();
                         }
                         else
                         {
                             utility.showtoast(Login_Activity.this,"Email not Verified, Please verify your Email");
                         }
                     }
                     else{
                         //log in failed
                         utility.showtoast(Login_Activity.this,task.getException().getLocalizedMessage());
                     }
            }
        });
    }

    void changeInProgress(boolean inProgress)
    {
        if(inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validate(String email,String password)
    {
        //will validate the data that are input by user
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEdt.setError("invalid Email");
            return false;
        }
        if(password.length()<6)
        {
            passwordEdt.setError("Password must be at least 6 chars");
            return false;
        }

        return  true;
    }


}