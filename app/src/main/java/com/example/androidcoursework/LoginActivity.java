package com.example.androidcoursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    TextView errorTextView;
    Button loginButton;
    Validations validations;
    Boolean isEmailChecked,isPasswordChecked;
    FirebaseAuth firebaseAuth ;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FusedLocationProviderClient fusedLocationProviderClient;
    public String finalUserRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        validations = new Validations();
        loginButton = findViewById(R.id.loginButton);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        errorTextView = findViewById(R.id.invalidEmailPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEmailChecked = validations.isValidEmail(email);
                isPasswordChecked = validations.isValidPassword(password);
                if(isEmailChecked && isPasswordChecked){

                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),
                            password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            Toast.makeText(LoginActivity.this,"login ok",Toast.LENGTH_SHORT);
                            checkUserAccessLevel(authResult.getUser().getUid());
                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(LoginActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT);
                        }
                    });
                }
                else {
                    errorTextView.setText("Please Fill the data");
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void checkUserAccessLevel(String uid) {
        Log.d("TAG123", "asd | " + uid);
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.getString("accessLevel").toString().equals("1")){

                    Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),AdminDashboardActivity.class));
                    finish();
                }
                else if(documentSnapshot.getString("accessLevel") .equals("0")){
                    startActivity(new Intent(getApplicationContext(),UserDashboardActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onRegisterClick(View view){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(checkInternetStatus()){
            if(currentUser != null){
                checkUserAccessLevel(currentUser.getUid());
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(checkInternetStatus()){
            if(currentUser != null){
                checkUserAccessLevel(currentUser.getUid());
            }
        }
    }

    public boolean  checkInternetStatus() {
        boolean internetCheck = false;
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnected () || mobile.isConnected ()) {
            internetCheck = true;
        } else {
            Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show();
            internetCheck = false;
            buildAlertMessageNoIntenet();
        }
        return  internetCheck;
    }
    private void buildAlertMessageNoIntenet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your INTERNET seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}