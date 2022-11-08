package com.example.nightlifekosovo.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nightlifekosovo.Activities.RegisterActivity;
import com.example.nightlifekosovo.Helpers.Constants;
import com.example.nightlifekosovo.Helpers.FragmentProcess;
import com.example.nightlifekosovo.R;
import com.example.nightlifekosovo.databinding.FragmentAddPostBinding;
import com.example.nightlifekosovo.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;


import java.util.HashMap;
import java.util.List;


public class AddPostFragment extends Fragment implements View.OnClickListener {
    public FragmentAddPostBinding binding;
    private Uri imageUri;
    private String imageUrl;
    private ImageView close;
    private ImageView imageAdded;
    private TextView post, description;
    ActivityResultLauncher<String> cropImage;


    public AddPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageAdded = binding.fragmentAddPostImageAdded;
     //   post = binding.fragmentAddPostPostTextView;
        description = binding.fragmentAddPostDescription;


    //    post.setOnClickListener(this);
      //  imageAdded.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
//        if (imageAdded == v) {
//            //    imagePermission();
//            try {
//                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                            Constants.READ_PERMISSION);
//
//                    return;
//                }
//                imageChooser();
//            } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        }
    }

//        private void imageChooser () {
//            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            addPhoto.launch(i);
//        }
//
//
//        ActivityResultLauncher<Intent> addPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            if (result.getResultCode() == Activity.RESULT_OK) {
//                Intent data = result.getData();
//                if (data != null && data.getData() != null) {
//                    Uri selectedImageUri = data.getData();
//                    imageAdded.setImageURI(selectedImageUri);
//                    imageUri = selectedImageUri;
//                }
//            }
//        });




}