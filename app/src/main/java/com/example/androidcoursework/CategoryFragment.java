package com.example.androidcoursework;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;


public class CategoryFragment extends Fragment {
    View view;
    EditText category,categoryEmail;
    Button addCategoryButton,updateCategoryButton;
    TextView errorView;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Validations validations;
    boolean isCategoryChecked,isEmailChecked;
    String categoryString,categoryEmailString,documentId;
    public CategoryFragment() {
        // Required empty public constructor
    }
    public CategoryFragment(CategoryModel categoryModel,String documentId) {
        this.categoryString = categoryModel.getCategory();
        this.categoryEmailString = categoryModel.getCategoryEmail();
        this.documentId = documentId;
    }
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);
        category = view.findViewById(R.id.editTextCategory);
        categoryEmail =view.findViewById(R.id.editTextCategoryEmail);
        addCategoryButton = view.findViewById(R.id.addCategoryButton);
        updateCategoryButton = view.findViewById(R.id.updateCategoryButton);
        if(categoryString != null && categoryEmailString != null){
            category.setText(documentId);
            categoryEmail.setText(categoryEmailString);
            addCategoryButton.setVisibility(View.GONE);
            updateCategoryButton.setVisibility(View.VISIBLE);
        }
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCategory();
            }
        });
        updateCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCategory();
            }
        });
        return  view;
    }

    private void updateCategory() {
        validations = new Validations();
        isCategoryChecked = validations.aliphaticValidation(category);
        isEmailChecked   = validations.isValidEmail(categoryEmail);
        if(isCategoryChecked && isEmailChecked ){

            firebaseFirestore.collection("categories").document(documentId).update(
                    "category",category.getText().toString(),
                    "categoryEmail",categoryEmail.getText().toString()
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(),"Updated successful",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            errorView.setText("Please Fill in the blanks!!");
            errorView.setVisibility(View.VISIBLE);
        }
    }

    private void saveCategory() {
        validations = new Validations();
        isCategoryChecked = validations.aliphaticValidation(category);
        isEmailChecked   = validations.isValidEmail(categoryEmail);
        if(isCategoryChecked && isEmailChecked ){

            CategoryModel categoryModel = new CategoryModel(
                    category.getText().toString(),
                    categoryEmail.getText().toString()
            );
            firebaseFirestore.collection("categories").add(categoryModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getActivity(),"New Category added",Toast.LENGTH_LONG).show();
                    reloadFragment(new CategoryFragment());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                    errorView.setText(e.getMessage());
                    errorView.setVisibility(View.VISIBLE);
                }
            });

        }
        else {
            errorView.setText("Please Fill in the blanks!!");
            errorView.setVisibility(View.VISIBLE);
        }


    }

    private void reloadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}