package com.example.engazadmin.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engazadmin.MainActivity;
import com.example.engazadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout tv_id, tv_username, tv_email, tv_password, tv_confirmPassword;
    private Button button_signUp;
    private TextView button_signIn;
    private FirebaseAuth mAuth;
    private String valPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Views();
        InitButton();
//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }
    }

    private Boolean validatePersonId() {
        String valPersonID = tv_id.getEditText().getText().toString().trim();
        if (valPersonID.isEmpty()) {
            tv_id.setError("Field cannot be empty");
            return false;
        } else if (valPersonID.length() != 15) {
            tv_id.setError("Person id not correct");
            return false;
        } else {
            tv_id.setError(null);
            return true;
        }
    }

    private Boolean validateUsername() {
        String valName = tv_username.getEditText().getText().toString().trim();
        String noSpace = "\\A\\w{4,20}\\z";

        if (valName.isEmpty()) {
            tv_username.setError("Field cannot be empty");
            return false;
        } else if (valName.length() >= 20) {
            tv_username.setError("Username too long");
            return false;
        } else if (!valName.matches(noSpace)) {
            tv_username.setError("Spaces is not allowed");
            return false;
        } else {
            tv_username.setError(null);
            tv_username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String valEmail = tv_email.getEditText().getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (valEmail.isEmpty()) {
            tv_email.setError("Field cannot be empty");
            return false;
        } else if (!valEmail.matches(emailPattern)) {
            tv_email.setError("Invalid email address");
            return false;
        } else
            tv_username.setError(null);
        tv_email.setErrorEnabled(false);
        return true;
    }

    private Boolean validatePassword() {
        valPassword = tv_password.getEditText().getText().toString().trim();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (valPassword.isEmpty()) {
            tv_password.setError("Field cannot be empty");
            return false;
        } else if (!valPassword.matches(passwordVal)) {
            tv_password.setError("Password is too weak");
            return false;
        } else {
            tv_password.setError(null);
            tv_password.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateConfirmPassword() {
        String valConfirmPassword = tv_confirmPassword.getEditText().getText().toString().trim();
        if (valConfirmPassword.isEmpty()) {
            tv_confirmPassword.setError("Field cannot be empty");
            return false;
        } else if (!valConfirmPassword.equals(valPassword)) {
            tv_confirmPassword.setError("Password not match");
            return false;
        } else {
            tv_confirmPassword.setError(null);
            tv_confirmPassword.setErrorEnabled(false);
            return true;
        }
    }

    private void InitButton() {
        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePersonId() | !validateUsername() | !validateEmail() |
                        !validatePassword() | !validateConfirmPassword()) {
                    return;
                }
                String id = tv_id.getEditText().getText().toString().trim();
                String username = tv_username.getEditText().getText().toString().trim();
                String email = tv_email.getEditText().getText().toString().trim();
                String password = tv_password.getEditText().getText().toString().trim();
                String confirm_password = tv_confirmPassword.getEditText().getText().toString().trim();

                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Wait ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            String uid = task.getResult().getUser().getUid();
                            AdminModel userModel = new AdminModel(id, username, email);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Admins");
                            reference.child(uid).setValue(userModel);

                            Toast.makeText(SignUpActivity.this, "Registered Successfully..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        button_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Views() {
        tv_id = findViewById(R.id.person_id_tv);
        tv_username = findViewById(R.id.username_tv);
        tv_email = findViewById(R.id.email_tv);
        tv_password = findViewById(R.id.password_tv);
        tv_confirmPassword = findViewById(R.id.confirm_password_tv);
        button_signUp = findViewById(R.id.button_signUp);
        button_signIn = findViewById(R.id.button_signIn);
        mAuth = FirebaseAuth.getInstance();
    }

}