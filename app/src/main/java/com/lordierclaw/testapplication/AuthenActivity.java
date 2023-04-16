package com.lordierclaw.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class AuthenActivity extends AppCompatActivity {


    TextInputEditText email, password;
    MaterialButton loginFunction, registerFunction;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authen);

        init();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d("AuthenActivity", "User is signed in, go to message activity");
            Intent intentMessageActivity = new Intent(AuthenActivity.this, MessageActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intentMessageActivity);
            finish();
        }


    }

    private void init(){
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        loginFunction = findViewById(R.id.loginFunction);
        registerFunction = findViewById(R.id.registerFunction);

        loginFunction.setOnClickListener(v -> {
            login();
        });

        registerFunction.setOnClickListener(v -> {
            MotionToast.Companion.createColorToast(AuthenActivity.this,
                    "Currently not implemented ☹️",
                    "",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(AuthenActivity.this,R.font.opensanlight));
            return;
        });
    }

    public void login() {
        if (TextUtils.isEmpty(email.getText().toString())) {
            MotionToast.Companion.createColorToast(AuthenActivity.this,
                    "Failed ☹️",
                    "You need to enter email address",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(AuthenActivity.this,R.font.opensanlight));
            return;
        }

        //regex check email address
        if (!email.getText().toString().matches("^[A-Za-z\\d+_.-]+@(.+)$")) {
            MotionToast.Companion.createColorToast(AuthenActivity.this,
                    "Failed ☹️",
                    "You need to enter a valid email address",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(AuthenActivity.this,R.font.opensanlight));
            return;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            MotionToast.Companion.createColorToast(AuthenActivity.this,
                    "Failed ☹️",
                    "You need to enter password",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(AuthenActivity.this,R.font.opensanlight));
            return;
        }

        //regex check password 8 - 24 character
        if (password.getText().toString().length() < 8 || password.getText().toString().length() > 24) {
            MotionToast.Companion.createColorToast(AuthenActivity.this,
                    "Failed ☹️",
                    "Password must be 8 - 24 characters",
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(AuthenActivity.this,R.font.opensanlight));
            return;
        }

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        MotionToast.Companion.createColorToast(AuthenActivity.this,
                                "Success 😊",
                                "Login successfully",
                                MotionToastStyle.INFO,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(AuthenActivity.this,R.font.opensanlight));
                        Log.d("Login", "go to message activity");
                        //Enter main activity
                        Intent intent = new Intent(AuthenActivity.this, MessageActivity.class);
                        startActivity(intent);
                    } else {
                        MotionToast.Companion.createColorToast(AuthenActivity.this,
                                "Failed ☹️",
                                "Login failed",
                                MotionToastStyle.INFO,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(AuthenActivity.this,R.font.opensanlight));
                    }
                }
        );

    }
}