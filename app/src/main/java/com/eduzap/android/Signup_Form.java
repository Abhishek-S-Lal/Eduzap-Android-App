package com.eduzap.android;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eduzap.android.ui.drawer.home.UserHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_Form extends AppCompatActivity {

    Button callLogIn, regBtn;
    RadioButton radioGenderMale, radioGenderFemale;
    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    ImageView image;
    TextView sloganText;
    String gender = "";
    ProgressBar progressBar;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__form);

        //Hooks
        image = findViewById(R.id.logo_image);
        sloganText = findViewById(R.id.slogan_name);

        regUsername = findViewById(R.id.reg_username);
        regPassword = findViewById(R.id.reg_password);
        regName = findViewById(R.id.reg_name);
        regEmail = findViewById(R.id.reg_email);
        regPhoneNo = findViewById(R.id.reg_phoneNo);

        radioGenderMale = findViewById(R.id.radio_male);
        radioGenderFemale = findViewById(R.id.radio_female);

        callLogIn = findViewById(R.id.reg_login_btn);
        regBtn = findViewById(R.id.reg_btn);

        progressBar = findViewById(R.id.signUpProgressBar);


        //Save data in FireBase on button click
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                //register user in email authentication
                registerEmailAuthentication(view);

            }

        });//Register Button method end

        callLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup_Form.this, LoginActivity.class);
                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(sloganText, "logo_text");
                pairs[2] = new Pair<View, String>(regUsername, "email_tran");
                pairs[3] = new Pair<View, String>(regPassword, "password_tran");
                pairs[4] = new Pair<View, String>(regBtn, "button_tran");
                pairs[5] = new Pair<View, String>(callLogIn, "login_signup_tran");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup_Form.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();

        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            regUsername.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            regUsername.setError("White Spaces are not allowed");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = regPhoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPhoneNo.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak. Please enter a password that contain atleast 6 characters with no white spaces.");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    //This function will save usr details in firebase realtime database
    public void registerUser(View view) {

        if (!validateName() | !validatePassword() | !validatePhoneNo() | !validateEmail() | !validateUsername()) {
            return;
        }
        //Get all the values
        String name = regName.getEditText().getText().toString();
        String username = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phoneNo = regPhoneNo.getEditText().getText().toString();

        if (radioGenderMale.isChecked()) {
            gender = "Male";
        }
        if (radioGenderFemale.isChecked()) {
            gender = "Female;";
        }

        UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, gender);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(helperClass);
    }

    //This function will add new user in e-mail authentication
    public void registerEmailAuthentication(final View view) {

        String email = regEmail.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

        if (!validateName() | !validatePassword() | !validatePhoneNo() | !validateEmail() | !validateUsername()) {
            return;
        }
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Signup_Form.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(Signup_Form.this, "User Already Registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Signup_Form.this, "SignUp Unsuccessful!\n Please try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //register user in realtime database
                    registerUser(view);

                    //email verification
                    mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Signup_Form.this, "Registered Successfully.Please check your email for verification.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Signup_Form.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(Signup_Form.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

        });
    }


}