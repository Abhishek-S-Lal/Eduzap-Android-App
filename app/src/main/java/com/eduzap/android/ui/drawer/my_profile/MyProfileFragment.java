package com.eduzap.android.ui.drawer.my_profile;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.eduzap.android.R;
import com.google.firebase.auth.FirebaseAuth;

public class MyProfileFragment extends Fragment {

    private MyProfileViewModel mViewModel;

    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.drawer_fragment_my_profile, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyProfileViewModel.class);
        // TODO: Use the ViewModel

    }

}
