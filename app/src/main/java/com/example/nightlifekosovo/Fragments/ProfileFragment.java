package com.example.nightlifekosovo.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nightlifekosovo.Models.User;
import com.example.nightlifekosovo.R;
import com.example.nightlifekosovo.databinding.FragmentProfileBinding;
import com.example.nightlifekosovo.databinding.FragmentSearchBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ProfileFragment extends Fragment {
    public static final String TAG="PROFILE";
    private FragmentProfileBinding binding;
    private User user;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private String uID;
    private Context context;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context=requireActivity();

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uID=firebaseUser.getUid();

        fetchProfile(uID);
    }

    private void fetchProfile(String uID) {
        db.collection("Users").document(uID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        binding.fragmentProfileUsername.setText(user.getUsername());
                        binding.fragmentProfilePhone.setText(user.getPhone());
                    }
                });

        StorageReference fileReference = storageReference.child(uID + "jpg");
        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into( binding.fragmentProfileImage);
            // binding.fragmentProfileImage.setImageURI(uri);
                Log.d(TAG, "NOT WORKING");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onFailure(@NonNull Exception e) {
                //binding.fragmentProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_icon));
                //Glide.with(context).load(getResources().getDrawable(R.drawable.ic_user_icon)).into(binding.fragmentProfileImage);
            }
        });
    }
}