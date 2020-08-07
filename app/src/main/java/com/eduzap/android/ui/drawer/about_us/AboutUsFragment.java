package com.eduzap.android.ui.drawer.about_us;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AboutUsFragment extends Fragment {

    private AboutUsViewModel mViewModel;
    private TextView supportEmailTV, facebookTV, instagramTV, telegramTV;

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.drawer_fragment_about_us, container, false);

        //
        supportEmailTV = root.findViewById(R.id.support_email);
        facebookTV = root.findViewById(R.id.facebook);
        instagramTV = root.findViewById(R.id.instagram);
        telegramTV = root.findViewById(R.id.telegram);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference contactUsRef = ref.child("ContactUs");

        contactUsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("support_mail").getValue(true).toString();
                String facebook = dataSnapshot.child("facebook").getValue(true).toString();
                String instagram = dataSnapshot.child("instagram").getValue(true).toString();
                String telegram = dataSnapshot.child("telegram").getValue(true).toString();
                supportEmailTV.setText(email);
                facebookTV.setText(facebook);
                instagramTV.setText(instagram);
                telegramTV.setText(telegram);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading contact details", Toast.LENGTH_SHORT).show();
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