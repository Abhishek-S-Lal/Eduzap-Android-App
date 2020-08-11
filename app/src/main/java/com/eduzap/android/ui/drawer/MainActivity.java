package com.eduzap.android.ui.drawer;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.eduzap.android.ui.drawer.home.UserHelperClass;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private FirebaseUser currentUser;
    private DatabaseReference userDataRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    CheckInternetConnection connectionChecker = new CheckInternetConnection();
    //tap back for multiple fragments
    //uncomment after implementing fragment switching without restarting
    boolean doubleBackToExitPressedOnce = false;
    private boolean connectionAvailable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    i.putExtra(Intent.EXTRA_SUBJECT, "Eduzap");
                    i.putExtra(Intent.EXTRA_TEXT, "Hey there!\nI am using Eduzap to understand and learn complex engineering concepts easily.\nDownload it now from playstore: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
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
                showDeveloperArjun();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_home_options_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.drawer_nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void showDeveloperArjun() {
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

                    navUserMail.setText(userData.getEmail());
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


    //DOUBLE TAP BACK PRESS for activity
//    boolean doubleBackToExitPressedOnce = false;
//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
//    }

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
//                    Toast.makeText(MainActivity.this, "Internet connection unavailable!", Toast.LENGTH_SHORT).show();
//                    connectionAvailable = false;
//                }
//                else if(!connectionAvailable && isConnectionAvailable) {
//                    Toast.makeText(MainActivity.this, "Internet connection is back again.", Toast.LENGTH_SHORT).show();
//                    connectionAvailable = true;
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