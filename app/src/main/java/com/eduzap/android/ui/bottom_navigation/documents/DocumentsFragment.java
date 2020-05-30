package com.eduzap.android.ui.bottom_navigation.documents;

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

public class DocumentsFragment extends Fragment {

    private DocumentsViewModel documentsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        documentsViewModel =
                ViewModelProviders.of(this).get(DocumentsViewModel.class);
        View root = inflater.inflate(R.layout.bottom_nav_fragment_documents, container, false);
        final TextView textView = root.findViewById(R.id.text_documents);
        documentsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
