package com.example.androidcoursework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewCategoryFragment extends Fragment {
    View view;
    RecyclerView categoryRecycleView;
    ViewCategoryAdapter viewCategoryAdapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public ViewCategoryFragment() {
        // Required empty public constructor
    }

    public static ViewCategoryFragment newInstance(String param1, String param2) {
        ViewCategoryFragment fragment = new ViewCategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        view = inflater.inflate(R.layout.fragment_view_category, container, false);
        categoryRecycleView = view.findViewById(R.id.categoryRecycleView);
        categoryRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query keyQuery = firebaseFirestore.collection("categories");
        FirestoreRecyclerOptions<CategoryModel> options =
                new FirestoreRecyclerOptions.Builder<CategoryModel>()
                .setQuery(keyQuery,
                        CategoryModel.class)
                .build();
        viewCategoryAdapter = new ViewCategoryAdapter(options);
        categoryRecycleView.setAdapter(viewCategoryAdapter);
        return  view;
    }
    @Override
    public void onStart() {
        super.onStart();
        viewCategoryAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        viewCategoryAdapter.stopListening();
    }

}