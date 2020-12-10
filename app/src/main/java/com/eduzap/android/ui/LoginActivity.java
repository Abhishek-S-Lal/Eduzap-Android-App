
package com.eduzap.android.ui;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eduzap.android.InternetConnection;
import com.eduzap.android.R;
import com.eduzap.android.ui.drawer.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

public class LoginActivity extends AppCompatActivity {

    private Button callSignUp, loginBtn, forgotpassword;
    private ImageView image;
    private TextView sloganText, statusMsg;
    private TextInputLayout email, password;
    private ProgressBar progressBar;
    private Context context;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        //Hooks
        callSignUp = findViewById(R.id.reg_login_btn);
        image = findViewById(R.id.logo_image);
        sloganText = findViewById(R.id.slogan_name);
        email = findViewById(R.id.log_email);
        password = findViewById(R.id.log_password);
        loginBtn = findViewById(R.id.login_btn);
        statusMsg = findViewById(R.id.status_msg);
        forgotpassword = findViewById(R.id.forgot_password_btn);

        progressBar = findViewById(R.id.loginProgressBar);

        int status = getIntent().getIntExtra("status", 0);
        if (status == 1) {
            statusMsg.setText("Email verification link sent to your registered email. Please verify it to continue.");
        }

        //login authentication
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
            if (mFirebaseUser != null && mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                progressBar.setVisibility(View.GONE);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                progressBar.setVisibility(View.GONE);
            }

        };
        mFirebaseAuth = FirebaseAuth.getInstance();

        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtil.hideKeyboard(LoginActivity.this);
                progressBar.setVisibility(View.VISIBLE);
//                //Extract email and password
                String UserEmail = email.getEditText().getText().toString();
                String UserPassword = password.getEditText().getText().toString();

                if (UserEmail.isEmpty() && UserPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are empty.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else if (UserEmail.isEmpty()) {
                    email.setError("Please enter email id");
                    email.requestFocus();
                    progressBar.setVisibility(View.GONE);
                } else if (UserPassword.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                    progressBar.setVisibility(View.GONE);
                } else {

                    mFirebaseAuth.signInWithEmailAndPassword(UserEmail, UserPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                if (!InternetConnection.checkConnection(context)) {
                                    Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid Credentials\nLogin Failed!", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please verify your email to login.", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(LoginActivity.this, Signup_Form.class);
                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(sloganText, "logo_text");
                pairs[2] = new Pair<View, String>(email, "email_tran");
                pairs[3] = new Pair<View, String>(password, "password_tran");
                pairs[4] = new Pair<View, String>(loginBtn, "button_tran");
                pairs[5] = new Pair<View, String>(callSignUp, "login_signup_tran");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());

                final EditText resetMail = new EditText(v.getContext());
                resetMail.setSingleLine();

                FrameLayout container = new FrameLayout(context);
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                resetMail.setLayoutParams(params);
                container.addView(resetMail);

                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your registered email to receive password reset link.");
                passwordResetDialog.setView(container);
                resetMail.setTextSize(18);
                resetMail.setMinimumHeight(48);



                passwordResetDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the mail and reset link
                        String mail = resetMail.getText().toString();
                        if (mail.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                            UIUtil.hideKeyboard(LoginActivity.this);
                            mFirebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(LoginActivity.this, "Reset Link Sent To Email", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                        }
                    }
                });


                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //closing dialog
                    }
                });
                passwordResetDialog.create().show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);

        }
    }
}