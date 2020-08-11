package com.eduzap.android.ui.drawer.team;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.eduzap.android.InternetConnection;
import com.eduzap.android.R;
import com.eduzap.android.ui.drawer.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamFragment extends Fragment {

    private TextView team_titleTV;
    private CircleImageView member1IV, member2IV, member3IV, member4IV;
    private TextView member1NameTV, member1PositionTV, member1EmailTV, member1MobileTV;
    private TextView member2NameTV, member2PositionTV, member2EmailTV, member2MobileTV;
    private TextView member3NameTV, member3PositionTV, member3EmailTV, member3MobileTV;
    private TextView member4NameTV, member4PositionTV, member4EmailTV, member4MobileTV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root;
        if (!InternetConnection.checkConnection(getContext())) {
            root = inflater.inflate(R.layout.no_internet, container, false);
            Button retry = root.findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!InternetConnection.checkConnection(getContext())) {
                        Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }

                }
            });
            return root;
        }

        root = inflater.inflate(R.layout.drawer_fragment_team, container, false);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //team member 1
        DatabaseReference team_member1_Ref = ref.child("Team").child("0");
        member1NameTV = root.findViewById(R.id.team_member_1_name);
        member1PositionTV = root.findViewById(R.id.team_member_1_position);
        member1EmailTV = root.findViewById(R.id.team_member_1_email);
        member1MobileTV = root.findViewById(R.id.team_member_1_mobile);
        member1IV = root.findViewById(R.id.team_member_1_img);
        team_member1_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue(true).toString();
                String mobile = dataSnapshot.child("mobile").getValue(true).toString();
                String name = dataSnapshot.child("name").getValue(true).toString();
                String position = dataSnapshot.child("position").getValue(true).toString();
                String imageUrl = dataSnapshot.child("imageUrl").getValue(true).toString();

                member1NameTV.setText(name);
                member1EmailTV.setText(email);
                member1PositionTV.setText(position);
                member1MobileTV.setText(mobile);
                Picasso.get().load(imageUrl).into(member1IV);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading Team Member 1 details", Toast.LENGTH_SHORT).show();
            }
        });


        //team member 2
        DatabaseReference team_member2_Ref = ref.child("Team").child("1");
        member2NameTV = root.findViewById(R.id.team_member_2_name);
        member2PositionTV = root.findViewById(R.id.team_member_2_position);
        member2EmailTV = root.findViewById(R.id.team_member_2_email);
        member2MobileTV = root.findViewById(R.id.team_member_2_mobile);
        member2IV = root.findViewById(R.id.team_member_2_img);
        team_member2_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue(true).toString();
                String mobile = dataSnapshot.child("mobile").getValue(true).toString();
                String name = dataSnapshot.child("name").getValue(true).toString();
                String position = dataSnapshot.child("position").getValue(true).toString();
                String imageUrl = dataSnapshot.child("imageUrl").getValue(true).toString();

                member2NameTV.setText(name);
                member2EmailTV.setText(email);
                member2PositionTV.setText(position);
                member2MobileTV.setText(mobile);
                Picasso.get().load(imageUrl).into(member2IV);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading Team Member 2 details", Toast.LENGTH_SHORT).show();
            }
        });

        //team member 3
        DatabaseReference team_member3_Ref = ref.child("Team").child("2");
        member3NameTV = root.findViewById(R.id.team_member_3_name);
        member3PositionTV = root.findViewById(R.id.team_member_3_position);
        member3EmailTV = root.findViewById(R.id.team_member_3_email);
        member3MobileTV = root.findViewById(R.id.team_member_3_mobile);
        member3IV = root.findViewById(R.id.team_member_3_img);
        team_member3_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue(true).toString();
                String mobile = dataSnapshot.child("mobile").getValue(true).toString();
                String name = dataSnapshot.child("name").getValue(true).toString();
                String position = dataSnapshot.child("position").getValue(true).toString();
                String imageUrl = dataSnapshot.child("imageUrl").getValue(true).toString();

                member3NameTV.setText(name);
                member3EmailTV.setText(email);
                member3PositionTV.setText(position);
                member3MobileTV.setText(mobile);
                Picasso.get().load(imageUrl).into(member3IV);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading Team Member 3 details", Toast.LENGTH_SHORT).show();
            }
        });

        //team member 4
        DatabaseReference team_member4_Ref = ref.child("Team").child("3");
        member4NameTV = root.findViewById(R.id.team_member_4_name);
        member4PositionTV = root.findViewById(R.id.team_member_4_position);
        member4EmailTV = root.findViewById(R.id.team_member_4_email);
        member4MobileTV = root.findViewById(R.id.team_member_4_mobile);
        member4IV = root.findViewById(R.id.team_member_4_img);
        team_member4_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue(true).toString();
                String mobile = dataSnapshot.child("mobile").getValue(true).toString();
                String name = dataSnapshot.child("name").getValue(true).toString();
                String position = dataSnapshot.child("position").getValue(true).toString();
                String imageUrl = dataSnapshot.child("imageUrl").getValue(true).toString();

                member4NameTV.setText(name);
                member4EmailTV.setText(email);
                member4PositionTV.setText(position);
                member4MobileTV.setText(mobile);
                Picasso.get().load(imageUrl).into(member4IV);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading Team Member 4 details", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }


}
