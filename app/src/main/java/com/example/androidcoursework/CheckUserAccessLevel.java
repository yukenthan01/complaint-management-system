package com.example.androidcoursework;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CheckUserAccessLevel {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String finalUserRole ;
    String userRoleReturn;
    public void checkUserLevel(callBackUserLevel callBackUserLevel){

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getString("accessLevel").toString().equals("1")){
                    finalUserRole = "admin";
                    callBackUserLevel.onCallback(finalUserRole);
                }
                else if(documentSnapshot.getString("accessLevel") .equals("0")){
                    finalUserRole = "user";
                    callBackUserLevel.onCallback(finalUserRole);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAGfaill", "onFailure: "+e.getMessage().toString());
            }
        });
    }
    public void getFullname(fullnameCallBack fullnameCallBack){

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fullname =
                        documentSnapshot.getString("firstname").toString() +" "+documentSnapshot.getString("lastname").toString();
                fullnameCallBack.onCallback(fullname);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAGfaill", "onFailure: "+e.getMessage().toString());
            }
        });
    }
    public void getProfileImageUrl(imageUrlCallBack imageUrlCallBack){

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                imageUrlCallBack.onCallback(documentSnapshot.getString("imageUrl").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAGfaill", "onFailure: "+e.getMessage().toString());
            }
        });
    }

    public interface callBackUserLevel {
        void onCallback(String userRole);
    }
    public interface fullnameCallBack {
        void onCallback(String fullname);
    }
    public interface imageUrlCallBack {
        void onCallback(String imageUrl);
    }
}
