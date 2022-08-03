package com.example.androidcoursework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordChangeFragment extends Fragment {


    Boolean isNewPasswordChecked,isCurrentPasswordChecked;
    View view;
    EditText newPassword,currentPassword;
    TextView errorTextView;
    Button passwordChangeButton;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public PasswordChangeFragment() {
        // Required empty public constructor
    }


    public static PasswordChangeFragment newInstance(String param1, String param2) {
        PasswordChangeFragment fragment = new PasswordChangeFragment();
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
        view =  inflater.inflate(R.layout.fragment_password_change, container, false);
        newPassword = view.findViewById(R.id.editTextNewPassword);
        currentPassword = view.findViewById(R.id.editTextOldPassword);
        passwordChangeButton = view.findViewById(R.id.passwordChangeButton);
        errorTextView = view.findViewById(R.id.errorView);
        passwordChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
        return view;
    }

    private void changePassword() {
        Validations validations = new Validations();
        String email  = firebaseAuth.getCurrentUser().getEmail();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        isCurrentPasswordChecked = validations.isValidPassword(currentPassword);
        isNewPasswordChecked = validations.isValidPassword(newPassword);

        if(isCurrentPasswordChecked && isCurrentPasswordChecked){

            firebaseAuth.signInWithEmailAndPassword(email,
                    currentPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you confirm change the password?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    user.updatePassword(newPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(),
                                                    "Password Rest Successfully".toString(),
                                                    Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(),
                                                    LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    errorTextView.setText("Current Password NOT matched");
                    errorTextView.setVisibility(View.VISIBLE);
                }
            });
        }
        else {
            errorTextView.setText("Please Fill the data");
            errorTextView.setVisibility(View.VISIBLE);
        }
    }
}