package com.example.nightlifekosovo.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nightlifekosovo.Models.User;
import com.example.nightlifekosovo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;
    private StorageReference storageReference;

    SharedPreferences sharedPreferences;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    public SearchAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // Inflate the custom layout
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("ProfilePictures");
        View view = inflater.inflate(R.layout.item_search, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();




        final User user = userList.get(position);

        StorageReference fileReference = storageReference.child(user.getId() + "jpg");
        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.imageProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Glide.with(context).load(R.drawable.ic_user_icon).into(holder.imageProfile);
            }
        });
        holder.btnSeek.setVisibility(View.VISIBLE);
        holder.username.setText(user.getUsername());
        holder.fullName.setText(user.getName() + " " + user.getSurname());


        isSeeking(user.getId(), holder.btnSeek);
//        if (user.getImageUrl() != null) {
//            holder.imageProfile.setVisibility(View.VISIBLE);
//            Glide.with(context).load(user.getImageUrl()).into(holder.imageProfile);
//        } else {
//            Glide.with(context).load(R.drawable.ic_user_icon).into(holder.imageProfile);
//        }

        if (user.getId().equals(firebaseUser.getUid())) {
            holder.container.setVisibility(View.GONE);
        }

        if(user.getType().equals("User")){
            holder.btnSeek.setVisibility(View.INVISIBLE);
        }

//        if(user.getType().equals("Company")) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SharedPreferences.Editor edit = sharedPreferences.edit();
//                    edit.putString("profileID", user.getId());
//                    edit.apply();
//
//
//                }
//            });
//        }else{
//            Toast.makeText(context, "You can only view the Companies' Profiles!", Toast.LENGTH_SHORT).show();
//
//        }


        holder.btnSeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> seeking = new HashMap<>();
                seeking.put("Seeking", true);
                if (holder.btnSeek.getText().toString().equals("Seek")) {
                    db.collection("Seek").document(firebaseUser.getUid())
                            .collection("Seeking").document(user.getId()).set(seeking);
                    db.collection("Seek").document(user.getId())
                            .collection("Seekers").document(firebaseUser.getUid()).set(seeking);

                } else {
                    db.collection("Seek").document(firebaseUser.getUid())
                            .collection("Seeking").document(user.getId()).delete();

                    db.collection("Seek").document(user.getId())
                            .collection("Seekers").document(firebaseUser.getUid()).delete();

                }
                isSeeking(user.getId(), holder.btnSeek);
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void filterList(ArrayList<User> filteredlist) {
        userList = filteredlist;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View container;
        public TextView username;
        public TextView fullName;
        public ShapeableImageView imageProfile;
        public Button btnSeek;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.item_search_layout);
            username = itemView.findViewById(R.id.item_search_username_text_view);
            fullName = itemView.findViewById(R.id.item_search_fullname_text_view);
            imageProfile = itemView.findViewById(R.id.item_search_image_view);
            btnSeek = itemView.findViewById(R.id.item_button_seek);
        }
    }

    private void isSeeking(String userId, Button button) {
        DocumentReference documentReference= FirebaseFirestore.getInstance().collection("Seek").document(firebaseUser.getUid()).collection("Seeking").document(userId);
        documentReference.addSnapshotListener((value, error) -> {

            if(value.exists()){
                button.setText(context.getText(R.string.text_seeking));
            } else {
                button.setText(context.getText(R.string.text_seek));
            }

        });


    }
}
