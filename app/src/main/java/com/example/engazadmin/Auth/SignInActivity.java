package com.example.engazadmin.Auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.engazadmin.MainActivity;
import com.example.engazadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout email_tv, password_tv;
    private Button button_signIn;
    private TextView button_signUp;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Views();
        Buttons();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }
    }

    private void Buttons() {
        button_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_tv.getEditText().getText().toString().trim();
                String password = password_tv.getEditText().getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    email_tv.setError("Email is required ");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    password_tv.setError("Password is required ");
                    return;
                }
                if (password.length() < 6) {
                    password_tv.setError("Password is too short");
                    return;
                }

                progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("Wait ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {

                            Toast.makeText(SignInActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });

        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
    }

    private void Views() {
        email_tv = findViewById(R.id.email_tv);
        password_tv = findViewById(R.id.password_tv);
        button_signIn = findViewById(R.id.button_signIn);
        button_signUp = findViewById(R.id.button_signUp);
        auth = FirebaseAuth.getInstance();
    }
}