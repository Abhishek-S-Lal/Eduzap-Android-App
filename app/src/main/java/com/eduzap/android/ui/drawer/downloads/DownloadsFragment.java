package com.eduzap.android.ui.drawer.downloads;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.eduzap.android.R;

public class DownloadsFragment extends Fragment {

    private DownloadsViewModel downloadsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        downloadsViewModel =
                ViewModelProviders.of(this).get(DownloadsViewModel.class);
        View root = inflater.inflate(R.layout.drawer_fragment_downloads, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        downloadsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
