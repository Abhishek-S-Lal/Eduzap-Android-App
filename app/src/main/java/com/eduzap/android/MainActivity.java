package com.eduzap.android;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button callSignUp, loginBtn;
    ImageView image;
    TextView sloganText;
    TextInputLayout email, password;
    ProgressBar progressBar;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hooks
        callSignUp = findViewById(R.id.reg_login_btn);
        image = findViewById(R.id.logo_image);
        sloganText = findViewById(R.id.slogan_name);
        email = findViewById(R.id.log_email);
        password = findViewById(R.id.log_password);
        loginBtn = findViewById(R.id.login_btn);

        progressBar = findViewById(R.id.loginProgressBar);


        //login authentication
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null && mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                }

            }
        };

        mFirebaseAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);
//                //Extract email and password
                String UserEmail = email.getEditText().getText().toString();
                String UserPassword = password.getEditText().getText().toString();

                if (UserEmail.isEmpty() && UserPassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are empty.", Toast.LENGTH_SHORT).show();
                } else if (UserEmail.isEmpty()) {
                    email.setError("Please enter email id");
                    email.requestFocus();
                } else if (UserPassword.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else {
                    mFirebaseAuth.signInWithEmailAndPassword(UserEmail, UserPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Login Failed! \n Please try again", Toast.LENGTH_SHORT).show();
                            } else {
                                if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Please verify your email to login.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }

            }
        });


        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup_Form.class);
                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(sloganText, "logo_text");
                pairs[2] = new Pair<View, String>(email, "email_tran");
                pairs[3] = new Pair<View, String>(password, "password_tran");
                pairs[4] = new Pair<View, String>(loginBtn, "button_tran");
                pairs[5] = new Pair<View, String>(callSignUp, "login_signup_tran");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
