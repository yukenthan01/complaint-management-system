package com.example.androidcoursework;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewComplaintsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private  View view;
    private String mParam1;
    private String mParam2;
    String userRole;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance() ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance() ;
    MyAdapter myAdapter;
    TextView hiddenUserRole;
    Query keyQuery = null;
    public ViewComplaintsFragment(String mParam1) {
        this.userRole = mParam1;
    }

    public ViewComplaintsFragment() {
        // Required empty public constructor

    }

    public static ViewComplaintsFragment newInstance(String param1) {
        ViewComplaintsFragment fragment = new ViewComplaintsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_complaints, container, false);

        Log.d("TAG", "onCreateView: "+userRole);
        recyclerView = view.findViewById(R.id.viewComplaintsRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(userRole.equals("admin")){

            keyQuery = firebaseFirestore.collection("complaints").whereEqualTo("status","pending");

        }else if(userRole.equals("user")){

            keyQuery = firebaseFirestore.collection("complaints").whereEqualTo("userId",
                    firebaseAuth.getCurrentUser().getUid());

        }
        FirestoreRecyclerOptions<NewComplaintModel> options = new FirestoreRecyclerOptions.Builder<NewComplaintModel>()
                .setQuery(keyQuery,
                        NewComplaintModel.class)
                .build();
        myAdapter = new MyAdapter(options);
        recyclerView.setAdapter(myAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myAdapter.startListening();


    }
    @Override
    public void onStop() {
        super.onStop();
        myAdapter.stopListening();

    }
}