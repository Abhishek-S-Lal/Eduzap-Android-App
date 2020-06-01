package com.eduzap.android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LearnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_videos, R.id.navigation_documents, R.id.navigation_quiz)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.bottom_nav_host_fragment);
        //This will set the action bar label corresponding to bottom navigation fragment names
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //String course = getIntent().getStringExtra("course_name");
        String subject = getIntent().getStringExtra("subject_name");

        int coursePosition = getIntent().getIntExtra("course_position", 0);
        int subjectPosition = getIntent().getIntExtra("subject_position", 0);

        setTitle(subject);

    }

}
