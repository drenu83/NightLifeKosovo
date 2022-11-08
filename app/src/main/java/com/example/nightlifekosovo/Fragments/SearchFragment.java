package com.example.nightlifekosovo.Fragments;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.nightlifekosovo.Adapters.SearchAdapter;
import com.example.nightlifekosovo.Models.User;
import com.example.nightlifekosovo.R;
import com.example.nightlifekosovo.databinding.FragmentSearchBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<User> userList;
    private FirebaseFirestore db;

    SearchView searchBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.fragmentSearchRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchBar = binding.fragmentSearchBar;

        readUsers();

        searchAdapter = new SearchAdapter(getContext(), userList);
        recyclerView.setAdapter(searchAdapter);

//        searchBar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                searchUser(s.toString().toLowerCase());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String text) {
        ArrayList<User> filteredlist = new ArrayList<>();

        for (User item : userList) {

            if (item.getUsername().toLowerCase().contains(text.toLowerCase())) {

                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {

            Toast.makeText(requireActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            searchAdapter.filterList(filteredlist);
        }
    }

    private void searchUser(String searchedUser) {
        userList = new ArrayList<>();
        db.collection("Users").orderBy("Username")
                .startAt(searchedUser).endAt(searchedUser + "\uf8ff")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            User user = d.toObject(User.class);
                            user.setId(d.getId());
                            userList.add(user);
                        }
                        
                        searchAdapter.notifyDataSetChanged();


                    }
                });

    }



    private void readUsers() {
        userList = new ArrayList<>();
        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    User user = d.toObject(User.class);
                    assert user != null;
                    user.setId(d.getId());
                    userList.add(user);
                }
                searchAdapter.notifyDataSetChanged();
            }

        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}