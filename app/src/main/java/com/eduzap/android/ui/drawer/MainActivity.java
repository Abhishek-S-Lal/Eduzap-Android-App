package com.eduzap.android.ui.drawer;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.eduzap.android.InternetConnection;
import com.eduzap.android.R;
import com.eduzap.android.ui.LoginActivity;
import com.eduzap.android.ui.VideoPlayerPip.VideoPlayer;
import com.eduzap.android.ui.drawer.home.UserHelperClass;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Context context;
    private FirebaseUser currentUser;
    private DatabaseReference userDataRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
//    private CheckInternetConnection connectionChecker = new CheckInternetConnection();
    private boolean doubleBackToExitPressedOnce = false;
    private boolean connectionAvailable = true;
    private boolean loadingError = false;

    private String abhi_email_address, abhi_whatsapp, abhi_subtitle, abhi_github, abhi_linkedin, abhi_imageUrl;
    private String arjun_email_address, arjun_whatsapp, arjun_subtitle, arjun_github, arjun_linkedin, arjun_imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.drawer_nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_downloads, R.id.nav_saved_videos)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.drawer_nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Fragment fragment = null;
                if (id == R.id.nav_share_now) {

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "EDUzap");
                    i.putExtra(Intent.EXTRA_TEXT, "Hey there!\nI am using EDUzap to understand and learn complex engineering concepts easily.\nDownload it now from Play Store: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                    startActivity(Intent.createChooser(i, "Share With"));
                    return true;
                } else if (id == R.id.nav_developers) {
                    showDeveloperAbhi();
                } else if (id == R.id.nav_goto_youtube) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCl2qtfsZbmTuue-i7z413ng")));
                }


                NavigationUI.onNavDestinationSelected(menuItem, navController);
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    //Do anything here which needs to be done after signout is complete
                    Toast.makeText(getApplicationContext(), "Successfully Signed out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                }
            }
        };
        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        updateNavHeader();

    }

    private void showDeveloperAbhi() {
        loadingError = false;

        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        View developerAbhiPopupView = getLayoutInflater().inflate(R.layout.developer_abhi_popup, null);

        dialogBuilder.setView(developerAbhiPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        ImageView close = developerAbhiPopupView.findViewById(R.id.close_popup);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageView next = developerAbhiPopupView.findViewById(R.id.abhishek_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showDeveloperArjun();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference developer_Ref = ref.child("Developers").child("abhishek");
        developer_Ref.keepSynced(true);

        developer_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                abhi_email_address = dataSnapshot.child("email").getValue(true).toString();
                abhi_whatsapp = dataSnapshot.child("whatsapp").getValue(true).toString();
                abhi_subtitle = dataSnapshot.child("subtitle").getValue(true).toString();
                abhi_github = dataSnapshot.child("github").getValue(true).toString();
                abhi_linkedin = dataSnapshot.child("linkedin").getValue(true).toString();
                abhi_imageUrl = dataSnapshot.child("imageUrl").getValue(true).toString();

                CircleImageView proImg = developerAbhiPopupView.findViewById(R.id.developerAbhiIV);
                final ProgressBar progressView = developerAbhiPopupView.findViewById(R.id.developerAbhiPV);

                if (abhi_imageUrl != null){
                    Picasso.get().load(abhi_imageUrl).into(proImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }else{
                    loadingError = true;
                }


                TextView descriptionTV = developerAbhiPopupView.findViewById(R.id.developer_descTV);
                if (abhi_subtitle!=null){
                    descriptionTV.setText(abhi_subtitle);
                }else{
                    loadingError = true;
                }


                ImageView gmail = developerAbhiPopupView.findViewById(R.id.abhi_gmail);
                if (abhi_email_address==null){
                    loadingError = true;
                    abhi_email_address = "abhishekslaltvm@gmail.com";
                }
                gmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent i = new Intent(Intent.ACTION_SENDTO);
                            i.setData(Uri.parse("mailto:" + abhi_email_address));
                            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback/Support");
                            startActivity(Intent.createChooser(i, "Send feedback"));
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "Gmail App not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ImageView linkedinIV = developerAbhiPopupView.findViewById(R.id.abhi_linked_in);
                if (abhi_linkedin == null){
                    loadingError = true;
                    abhi_linkedin = "https://www.linkedin.com/in/abhishekslal";
                }
                linkedinIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(abhi_linkedin));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "Some problem with your browser.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ImageView githubIV = developerAbhiPopupView.findViewById(R.id.abhi_github);
                if (abhi_github == null){
                    loadingError = true;
                    abhi_github = "https://github.com/Abhishek-S-Lal";
                }
                githubIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(abhi_github));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "Some problem with your browser.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ImageView whatsappIV = developerAbhiPopupView.findViewById(R.id.abhi_whatsapp);
                if (abhi_whatsapp == null){
                    loadingError = true;
                    abhi_whatsapp = "https://wa.me/918921440482";
                }
                whatsappIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(abhi_whatsapp));

                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "Sorry. Whatsapp connection is temporarily unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Error loading Developer details", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        if (loadingError){
            Toast.makeText(context, "Failed to load or update details.", Toast.LENGTH_SHORT).show();
        }

    }

    private void showDeveloperArjun() {
        loadingError = false;

        View developerArjunPopupView = getLayoutInflater().inflate(R.layout.developer_arjun_pop_up, null);
        dialogBuilder.setView(developerArjunPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        ImageView close = developerArjunPopupView.findViewById(R.id.close_popup_arjun);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ImageView prev = developerArjunPopupView.findViewById(R.id.arjun_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                showDeveloperAbhi();
            }
        });

        //
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference developer_Ref = ref.child("Developers").child("arjun");
        developer_Ref.keepSynced(true);

        developer_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arjun_email_address = dataSnapshot.child("email").getValue(true).toString();
                arjun_whatsapp = dataSnapshot.child("whatsapp").getValue(true).toString();
                arjun_subtitle = dataSnapshot.child("subtitle").getValue(true).toString();
                arjun_github = dataSnapshot.child("github").getValue(true).toString();
                arjun_linkedin = dataSnapshot.child("linkedin").getValue(true).toString();
                arjun_imageUrl = dataSnapshot.child("imageUrl").getValue(true).toString();

                CircleImageView proImg = developerArjunPopupView.findViewById(R.id.developerArjunIV);
                final ProgressBar progressView = developerArjunPopupView.findViewById(R.id.developerArjunPV);

                if (arjun_imageUrl == null){
                    loadingError = true;
                }
                else{
                    Picasso.get().load(arjun_imageUrl).into(proImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }

                TextView descriptionTV = developerArjunPopupView.findViewById(R.id.developer_arjun_descTV);
                if (arjun_subtitle != null){
                    descriptionTV.setText(arjun_subtitle);
                }else{
                    loadingError = true;
                }


                ImageView gmail = developerArjunPopupView.findViewById(R.id.arjun_gmail);
                if (arjun_email_address == null){
                    loadingError = true;
                    arjun_email_address = "arjunsg13@gmail.com@gmail.com";
                }
                gmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent i = new Intent(Intent.ACTION_SENDTO);
                            i.setData(Uri.parse("mailto:" + arjun_email_address));
                            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback/Support");
                            startActivity(Intent.createChooser(i, "Send feedback"));
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "Gmail App not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ImageView linkedinIV = developerArjunPopupView.findViewById(R.id.arjun_linked_in);
                if (arjun_linkedin  == null){
                    loadingError = true;
                    arjun_linkedin = "https://www.linkedin.com/in/arjun-s-g-7a36771ab";
                }
                linkedinIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(arjun_linkedin));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "Some problem with your browser.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ImageView githubIV = developerArjunPopupView.findViewById(R.id.arjun_github);
                if (arjun_github == null){
                    loadingError = true;
                    arjun_github = "https://github.com/Arjunsg13";
                }
                githubIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(arjun_github));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "Some problem with your browser.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                ImageView whatsappIV = developerArjunPopupView.findViewById(R.id.arjun_whatsapp);
                if (arjun_whatsapp == null){
                    loadingError = true;
                    arjun_whatsapp = "https://wa.me/919745816141";
                }
                whatsappIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(arjun_whatsapp));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "Sorry. Whatsapp connection is temporarily unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Error loading Developer details", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        if (loadingError){
            Toast.makeText(context, "Failed to load or update details.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.drawer_nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void updateNavHeader() {

        NavigationView navigationView = findViewById(R.id.drawer_nav_view);
        View headerView = navigationView.getHeaderView(0);
        final TextView navUserName = headerView.findViewById(R.id.nav_username);
        final TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
        //final ImageView navUserPic = headerView.findViewById(R.id.nav_user_photo);

        if (currentUser != null) {
            userDataRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            userDataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    UserHelperClass userData = dataSnapshot.getValue(UserHelperClass.class);

                    if (userData.getEmail() != null)
                        navUserMail.setText(userData.getEmail());
                    if (userData.getName() != null)
                        navUserName.setText(userData.getName());
                    //Glide to load user image
                    // Glide.with(HomeActivity.this).load(picUrl).into(navUserPic);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e(error.toString(), "Database error");
                    // Failed to read value
                    Toast.makeText(MainActivity.this, "Error in loading user details", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();

            return;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth != null && InternetConnection.checkConnection(getApplicationContext())) {
            firebaseAuth.addAuthStateListener(authStateListener);
        }

//        connectionChecker.addConnectionChangeListener(new ConnectionChangeListener() {
//            @Override
//            public void onConnectionChanged(boolean isConnectionAvailable) {
//                if(connectionAvailable && !isConnectionAvailable) {
//                    if (!InternetConnection.checkConnection(context) ) {
//                        connectionAvailable = false;
//                        Toast.makeText(context, "Internet connection unavailable!", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//                else if(!connectionAvailable && isConnectionAvailable) {
//                    if (InternetConnection.checkConnection(context)){
//                        connectionAvailable = true;
//                        Toast.makeText(MainActivity.this, "Internet connection is back again.", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }
//        });
    }

    @Override
    protected void onStop() {
        super.onStop();
//        connectionChecker.removeConnectionChangeListener();
        if (firebaseAuth != null && InternetConnection.checkConnection(getApplicationContext())) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

}