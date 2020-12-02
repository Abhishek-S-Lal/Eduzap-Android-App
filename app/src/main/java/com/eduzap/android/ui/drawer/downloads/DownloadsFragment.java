package com.eduzap.android.ui.drawer.downloads;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;

import java.io.File;
import java.util.ArrayList;

public class DownloadsFragment extends Fragment {
    File externalStorageDirectory, folder;
    ArrayList<File> DocumentsList = new ArrayList<>();
    private Uri fileUri;
    String[] documentsExtensions = {".pdf", ".ppt"};
    TextView emptyTextViw;
    private RecyclerView downloadsRecyclerView;
    private DownloadsAdapter downloadsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //In marshmallow and above we need to ask for permission first
        externalStorageDirectory = getActivity().getExternalFilesDir(null);
        folder = new File(externalStorageDirectory.getAbsolutePath() + "/documents");
        checkStorageAccessPermission();

        View root = inflater.inflate(R.layout.drawer_fragment_downloads, container, false);

        downloadsRecyclerView = root.findViewById(R.id.download_recycler_view);

        downloadsRecyclerView.setHasFixedSize(true);
        downloadsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //if you face lack in scrolling then add following lines
        downloadsRecyclerView.setHasFixedSize(true);
        downloadsRecyclerView.setItemViewCacheSize(20);
        downloadsRecyclerView.setNestedScrollingEnabled(false);

        load_Directory_Files(folder);
        int item_count = get_files_count(folder);
        if (item_count == 0) {
            LinearLayout emptyDownloads = root.findViewById(R.id.empty_downloads);
            emptyDownloads.setVisibility(View.VISIBLE);
        }

        downloadsAdapter = new DownloadsAdapter(this.getActivity(), DocumentsList);
        downloadsRecyclerView.setAdapter(downloadsAdapter);
        return root;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //load data here
                //for first time data will be loaded here
                //then it will be loaded in splash screen
                //because if we could not have permission then we could not load data in splash screen window

            }
        }
    }

    private void checkStorageAccessPermission() {
        //ContextCompat use to retrieve resources. It provide uniform interface to access resources.
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new androidx.appcompat.app.AlertDialog.Builder(this.getActivity())
                        .setTitle("Permission Needed")
                        .setMessage("This permission is needed to access media file in your phone")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(DownloadsFragment.this.getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        1);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new androidx.appcompat.app.AlertDialog.Builder(this.getActivity())
                        .setTitle("Permission Needed")
                        .setMessage("This permission is needed to access media file in your phone")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(DownloadsFragment.this.getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        1);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    fileUri = data.getData();
                    getActivity().getApplicationContext().getContentResolver().takePersistableUriPermission(fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
        }
    }

    public void load_Directory_Files(File directory) {
        File[] fileList = directory.listFiles();
        if (fileList != null && fileList.length > 0) {
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    load_Directory_Files(fileList[i]);
                } else {
                    String name = fileList[i].getName().toLowerCase();
                    for (String extension : documentsExtensions) {
                        //check the type of file
                        if (name.endsWith(extension)) {
                            DocumentsList.add(fileList[i]);
                            //when we found file
                            break;
                        }
                    }
                }
            }
        }
    }

    public int get_files_count(File directory) {
        File[] fileList = directory.listFiles();
        if (fileList != null) {
            return fileList.length;
        } else
            return 0;
    }
}
