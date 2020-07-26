package com.eduzap.android.ui.drawer.contact_us;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.eduzap.android.R;

public class ContactUsFragment extends Fragment {

    private ContactUsViewModel contact_us_ViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contact_us_ViewModel =
                ViewModelProviders.of(this).get(ContactUsViewModel.class);
        View root = inflater.inflate(R.layout.drawer_fragment_contact_us, container, false);

        return root;
    }

}
