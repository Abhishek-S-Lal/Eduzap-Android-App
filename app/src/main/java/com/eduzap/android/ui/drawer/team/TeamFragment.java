package com.eduzap.android.ui.drawer.team;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.eduzap.android.InternetConnection;
import com.eduzap.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeamFragment extends Fragment {

    private TextView team_titleTV;
    private CircleImageView member1IV, member2IV, member3IV, member4IV;
    private TextView member1NameTV, member1PositionTV;
    private ImageView member1EmailIV, member1MobileIV, member1WhatsappIV;

    private TextView member2NameTV, member2PositionTV;
    private ImageView member2EmailIV, member2MobileIV, member2WhatsappIV;

    private TextView member3NameTV, member3PositionTV;
    private ImageView member3EmailIV, member3MobileIV, member3WhatsappIV;

    private TextView member4NameTV, member4PositionTV;
    private ImageView member4EmailIV, member4MobileIV, member4WhatsappIV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root;
        root = inflater.inflate(R.layout.drawer_fragment_team, container, false);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        //team member 1
        DatabaseReference team_member1_Ref = ref.child("Team").child("0");
        team_member1_Ref.keepSynced(true);
        member1NameTV = root.findViewById(R.id.team_member_1_name);
        member1PositionTV = root.findViewById(R.id.team_member_1_position);
        member1EmailIV = root.findViewById(R.id.team_member_1_email);
        member1WhatsappIV = root.findViewById(R.id.team_member_1_whatsapp);
        member1MobileIV = root.findViewById(R.id.team_member_1_mobile);
        member1IV = root.findViewById(R.id.team_member_1_img);
        final ProgressBar member1PV = root.findViewById(R.id.team_member_1_img_PV);
        team_member1_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue(true).toString();
                String mobile = dataSnapshot.child("mobile").getValue(true).toString();
                String whatsapp = dataSnapshot.child("whatsapp").getValue(true).toString();
                String name = dataSnapshot.child("name").getValue(true).toString();
                String position = dataSnapshot.child("position").getValue(true).toString();
                String imageUrl = dataSnapshot.child("imageUrl").getValue(true).toString();

                Picasso.get().load(imageUrl).into(member1IV, new Callback() {
                    @Override
                    public void onSuccess() {
                        member1PV.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

                member1NameTV.setText(name);
                member1PositionTV.setText(position);

                member1EmailIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent i = new Intent(Intent.ACTION_SENDTO);
                            i.setData(Uri.parse("mailto:" + email));
                            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback/Support");
                            startActivity(Intent.createChooser(i, "Send feedback"));
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Gmail App not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                member1MobileIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + mobile));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Phone App not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                member1WhatsappIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(whatsapp));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Sorry. Whatsapp connection is temporarily unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading Team Member 1 details", Toast.LENGTH_SHORT).show();
            }
        });


        //team member 2
        DatabaseReference team_member2_Ref = ref.child("Team").child("1");
        team_member2_Ref.keepSynced(true);
        member2NameTV = root.findViewById(R.id.team_member_2_name);
        member2PositionTV = root.findViewById(R.id.team_member_2_position);
        member2EmailIV = root.findViewById(R.id.team_member_2_email);
        member2MobileIV = root.findViewById(R.id.team_member_2_mobile);
        member2WhatsappIV = root.findViewById(R.id.team_member_2_whatsapp);
        member2IV = root.findViewById(R.id.team_member_2_img);
        final ProgressBar member2PV = root.findViewById(R.id.team_member_2_img_PV);
        team_member2_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue(true).toString();
                String mobile = dataSnapshot.child("mobile").getValue(true).toString();
                String whatsapp = dataSnapshot.child("whatsapp").getValue(true).toString();
                String name = dataSnapshot.child("name").getValue(true).toString();
                String position = dataSnapshot.child("position").getValue(true).toString();
                String imageUrl = dataSnapshot.child("imageUrl").getValue(true).toString();

                member2NameTV.setText(name);
                member2PositionTV.setText(position);

                member2EmailIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent i = new Intent(Intent.ACTION_SENDTO);
                            i.setData(Uri.parse("mailto:" + email));
                            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback/Support");
                            startActivity(Intent.createChooser(i, "Send feedback"));
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Gmail App not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                member2MobileIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + mobile));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Phone App not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                member2WhatsappIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(whatsapp));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Sorry. Whatsapp connection is temporarily unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Picasso.get().load(imageUrl).into(member2IV, new Callback() {
                    @Override
                    public void onSuccess() {
                        member2PV.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading Team Member 2 details", Toast.LENGTH_SHORT).show();
            }
        });

        //team member 3
        DatabaseReference team_member3_Ref = ref.child("Team").child("2");
        team_member3_Ref.keepSynced(true);
        member3NameTV = root.findViewById(R.id.team_member_3_name);
        member3PositionTV = root.findViewById(R.id.team_member_3_position);
        member3EmailIV = root.findViewById(R.id.team_member_3_email);
        member3WhatsappIV = root.findViewById(R.id.team_member_3_whatsapp);
        member3MobileIV = root.findViewById(R.id.team_member_3_mobile);
        member3IV = root.findViewById(R.id.team_member_3_img);
        final ProgressBar member3PV = root.findViewById(R.id.team_member_3_img_PV);
        team_member3_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue(true).toString();
                String mobile = dataSnapshot.child("mobile").getValue(true).toString();
                String whatsapp = dataSnapshot.child("whatsapp").getValue(true).toString();
                String name = dataSnapshot.child("name").getValue(true).toString();
                String position = dataSnapshot.child("position").getValue(true).toString();
                String imageUrl = dataSnapshot.child("imageUrl").getValue(true).toString();

                member3NameTV.setText(name);
                member3PositionTV.setText(position);

                member3EmailIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent i = new Intent(Intent.ACTION_SENDTO);
                            i.setData(Uri.parse("mailto:" + email));
                            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback/Support");
                            startActivity(Intent.createChooser(i, "Send feedback"));
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Gmail App not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                member3MobileIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + mobile));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Phone App not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                member3WhatsappIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(whatsapp));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Sorry. Whatsapp connection is temporarily unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Picasso.get().load(imageUrl).into(member3IV, new Callback() {
                    @Override
                    public void onSuccess() {
                        member3PV.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading Team Member 3 details", Toast.LENGTH_SHORT).show();
            }
        });

        //team member 4
        DatabaseReference team_member4_Ref = ref.child("Team").child("3");
        team_member4_Ref.keepSynced(true);
        member4NameTV = root.findViewById(R.id.team_member_4_name);
        member4PositionTV = root.findViewById(R.id.team_member_4_position);
        member4EmailIV = root.findViewById(R.id.team_member_4_email);
        member4WhatsappIV = root.findViewById(R.id.team_member_4_whatsapp);
        member4MobileIV = root.findViewById(R.id.team_member_4_mobile);
        member4IV = root.findViewById(R.id.team_member_4_img);
        final ProgressBar member4PV = root.findViewById(R.id.team_member_4_img_PV);
        team_member4_Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("email").getValue(true).toString();
                String mobile = dataSnapshot.child("mobile").getValue(true).toString();
                String whatsapp = dataSnapshot.child("whatsapp").getValue(true).toString();
                String name = dataSnapshot.child("name").getValue(true).toString();
                String position = dataSnapshot.child("position").getValue(true).toString();
                String imageUrl = dataSnapshot.child("imageUrl").getValue(true).toString();

                member4NameTV.setText(name);
                member4PositionTV.setText(position);

                member4EmailIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent i = new Intent(Intent.ACTION_SENDTO);
                            i.setData(Uri.parse("mailto:" + email));
                            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback/Support");
                            startActivity(Intent.createChooser(i, "Send feedback"));
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Gmail App not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                member4MobileIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + mobile));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Phone App not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                member4WhatsappIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setData(Uri.parse(whatsapp));
                            startActivity(intent);
                        } catch (
                                ActivityNotFoundException e) {
                            Toast.makeText(getContext(), "Sorry. Whatsapp connection is temporarily unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Picasso.get().load(imageUrl).into(member4IV, new Callback() {
                    @Override
                    public void onSuccess() {
                        member4PV.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading Team Member 4 details", Toast.LENGTH_SHORT).show();
            }
        });

        if (!InternetConnection.checkConnection(getContext())) {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }

        return root;
    }


}
