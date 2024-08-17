package com.example.notes_pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAcount extends AppCompatActivity {
EditText emailEdt,passwordEdt,confirmEdt;
Button create_btn;
ProgressBar progressBar;
TextView login_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);

        emailEdt=findViewById(R.id.email_edt);
        passwordEdt=findViewById(R.id.password_edt);
        confirmEdt=findViewById(R.id.confirm_password_edt);
        create_btn=findViewById(R.id.create_acc_btn);
        progressBar=findViewById(R.id.progress_bar);
        login_txt=findViewById(R.id.login_txt_view_btn);

        create_btn.setOnClickListener(v->createAccount());
        login_txt.setOnClickListener(v->finish());
    }

    void createAccount(){
        String email= emailEdt.getText().toString();
        String password= passwordEdt.getText().toString();
        String confirmPassword= confirmEdt.getText().toString();

        boolean isvalidated =validate(email,password,confirmPassword);
        if(!isvalidated){
            return;
        }
        createAccountInFirebase(email,password);
    }

    void createAccountInFirebase(String email,String password)
    {
        changeInProgress(true);

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAcount.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()) {
                            //creating account is done
                            utility.showtoast(CreateAcount.this,"Successfully created account, check email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        } else {
                            // failure
                            utility.showtoast(CreateAcount.this,task.getException().getLocalizedMessage());
                        }
                    }
                });
    }

    void changeInProgress(boolean inProgress)
    {
        if(inProgress)
        {
            progressBar.setVisibility(View.VISIBLE);
            create_btn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            create_btn.setVisibility(View.VISIBLE);
        }
    }

    boolean validate(String email,String password,String confirmPassword)
    {
        //will validate the data that are input by user
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailEdt.setError("invalid email");
            return false;
        }
        if(password.length()<6)
        {
            passwordEdt.setError("Password must be at least 6 chars");
            return false;
        }
        if(!password.equals(confirmPassword))
        {
            confirmEdt.setError("password does not match");
            return false;
        }
        return  true;
    }



}

