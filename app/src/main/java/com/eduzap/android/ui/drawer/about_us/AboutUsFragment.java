package com.eduzap.android.ui.drawer.about_us;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.eduzap.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AboutUsFragment extends Fragment {

    private AboutUsViewModel mViewModel;
    private TextView supportEmailTV, facebookTV, instagramTV, telegramTV, twitterTV, websiteTV;
    private TextView about_1_title, about_1_description, about_2_title, about_2_description;
    private ImageView about_1_image, about_2_image;

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.drawer_fragment_about_us, container, false);

        //
        about_1_title = root.findViewById(R.id.about_title_1);
        about_1_description = root.findViewById(R.id.about_description_1);
        about_1_image = root.findViewById(R.id.about_image_1);

        about_2_title = root.findViewById(R.id.about_title_2);
        about_2_description = root.findViewById(R.id.about_description_2);
        about_2_image = root.findViewById(R.id.about_image_2);

        supportEmailTV = root.findViewById(R.id.support_email);
        facebookTV = root.findViewById(R.id.facebook);
        instagramTV = root.findViewById(R.id.instagram);
        telegramTV = root.findViewById(R.id.telegram);
        twitterTV = root.findViewById(R.id.twitter);
        websiteTV = root.findViewById(R.id.website);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference contactUsRef = ref.child("ContactUs");

        contactUsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("support_mail").getValue(true).toString();
                String facebook = dataSnapshot.child("facebook").getValue(true).toString();
                String instagram = dataSnapshot.child("instagram").getValue(true).toString();
                String telegram = dataSnapshot.child("telegram").getValue(true).toString();
                String website = dataSnapshot.child("website").getValue(true).toString();
                String twitter = dataSnapshot.child("twitter").getValue(true).toString();
                supportEmailTV.setText(email);
                facebookTV.setText(facebook);
                instagramTV.setText(instagram);
                telegramTV.setText(telegram);
                websiteTV.setText(website);
                twitterTV.setText(twitter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading contact details", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference aboutUsRef1 = ref.child("AboutUs").child("0");
        aboutUsRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String title = snapshot.child("title").getValue(true).toString();
                String description = snapshot.child("description").getValue(true).toString();
                String image = snapshot.child("imageUrl").getValue(true).toString();
                about_1_title.setText(title);
                about_1_description.setText(description);
                Picasso.get().load(image).into(about_1_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading About Us 1 details", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference aboutUsRef2 = ref.child("AboutUs").child("1");
        aboutUsRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String title = snapshot.child("title").getValue(true).toString();
                String description = snapshot.child("description").getValue(true).toString();
                String image = snapshot.child("imageUrl").getValue(true).toString();
                about_2_title.setText(title);
                about_2_description.setText(description);
                Picasso.get().load(image).into(about_2_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading About Us 2 Details", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AboutUsViewModel.class);
        // TODO: Use the ViewModel
    }

}