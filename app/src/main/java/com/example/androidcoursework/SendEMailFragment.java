package com.example.androidcoursework;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class SendEMailFragment extends Fragment {
    View view;
    String subject,description,category,
            address,city,postcode,state,date,userId,imageUrl,status,fullname,getcategoryEmail,
            userEmail;
    EditText subjectEditText,descriptionEditText,getPostCodeEditText,
            addressEditText,citiesEditText,postcodeEditText,stateEditText;
    ImageView imageView;
    TextView errorView;
    Button btnSendEmail;
    Spinner categoryDropdown;
    ArrayAdapter<String> adapter;
    ArrayList<String> categories = new ArrayList<String>();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference(
            "complaints");
    public SendEMailFragment() {
        // Required empty public constructor

    }
    public SendEMailFragment(NewComplaintModel getModel) {
        this.subject = getModel.getSubject();
        this.description = getModel.getDescription();
        this.category = getModel.getCategory();
        this.address = getModel.getAddress();
        this.city = getModel.getCity();
        this.postcode = getModel.getPostcode();
        this.state = getModel.getState();
        this.date = getModel.getDate();
        this.userId = getModel.userId;
        this.imageUrl = getModel.getImageUrl();
        this.status = getModel.getStatus();

    }

    public static SendEMailFragment newInstance(String param1, String param2) {
        SendEMailFragment fragment = new SendEMailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        selectedDataCategory();
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_send_e_mail, container, false);
        findCategoryEmail(new categoryEmailCallBack() {
            @Override
            public void onCallback(String categoryEmail) {
                getcategoryEmail = categoryEmail;
            }
        });
        getUserEmail(userId, new userEmailCallBack() {
            @Override
            public void onCallback(String userEmail) {
                userEmail = userEmail;
            }
        });
        subjectEditText = view.findViewById(R.id.editTextSubject);
        descriptionEditText = view.findViewById(R.id.editTextDescription);
        addressEditText = view.findViewById(R.id.editTextAddress);
        citiesEditText = view.findViewById(R.id.editTextCity);
        getPostCodeEditText = view.findViewById(R.id.editTextPostCode);
        stateEditText = view.findViewById(R.id.editTextState);
        imageView = view.findViewById(R.id.imageView);
        errorView = view.findViewById(R.id.errorView);
        categoryDropdown = view.findViewById(R.id.categoryDropdownSendMail);
        btnSendEmail = view.findViewById(R.id.BtnSendEMail);

        subjectEditText.setText(subject);
        descriptionEditText.setText(description);
        addressEditText.setText(address);
        citiesEditText.setText(city);
        getPostCodeEditText.setText(postcode);
        stateEditText.setText(state);
        Glide.with(getContext()).load(imageUrl).into(imageView);
        selectedDataCategory();
        adapter = new ArrayAdapter<String>(getContext(),R.layout.category_list,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryDropdown.setAdapter(adapter);

        categoryDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                if (i == 0){
                    category = null;
                }
                else {
                    category = adapterView.getItemAtPosition(i).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
        return  view;
    }
    //send mail function linkt to java mail api file
    private void sendMail() {
        //Send Mail
        JavaMailApi javaMailAPI = new JavaMailApi(
                getContext(),
                subjectEditText.getText().toString(),
                descriptionEditText.getText().toString(),
                category,
                addressEditText.getText().toString(),
                citiesEditText.getText().toString(),
                getPostCodeEditText.getText().toString(),
                stateEditText.getText().toString(),
                date,
                fullname,
                imageUrl,
                getcategoryEmail,
                userEmail
        );
        javaMailAPI.execute();
    }
    public  void findCategoryEmail(categoryEmailCallBack categoryEmailCallBack){
        firebaseFirestore.collection("categories").whereEqualTo(
                        "category",category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        categoryEmailCallBack.onCallback(documentSnapshot.getString("categoryEmail").toString());
                    }
                }
            }
        });

    }
    public interface categoryEmailCallBack{
        void onCallback(String categoryEmail);
    }
    public interface userEmailCallBack{
        void onCallback(String userEmail);
    }
    private void getUserEmail(String uid,userEmailCallBack userEmailCallBack){
        DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fullname =
                        documentSnapshot.getString("firstname").toString() +" "+documentSnapshot.getString("lastname").toString();
                userEmail = documentSnapshot.getString("email").toString();
                userEmailCallBack.onCallback(documentSnapshot.getString("email").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAGfaill", "onFailure: "+e.getMessage().toString());
            }
        });
    }
    private void selectedDataCategory() {
        categories = new ArrayList<>();
        categories.add("Select the Category");
        firebaseFirestore.collection("categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        categories.add(documentSnapshot.getString("category").toString());
                    }
                }
                else
                {
                    Log.d("TAG22", "selectedDataCategory: ERROR" );
                }

            }
        });

    }

}