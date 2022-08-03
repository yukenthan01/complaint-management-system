package com.example.androidcoursework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    Button registerBtn;
    EditText firstname,lastname,email,password,phone;
    Validations validations ;
    Boolean isfirstnameChecked,isLastnameChecked,isEmailChecked,isPasswordChecked,isPhoneChecked;
    String userId;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        validations = new Validations();

        registerBtn = findViewById(R.id.registerButton);
        firstname = findViewById(R.id.editTextFirstname);
        lastname = findViewById(R.id.editTextLastname);
        email = findViewById(R.id.editTextEmail);
        phone = findViewById(R.id.editTextMobile);
        password = findViewById(R.id.editTextPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isfirstnameChecked = validations.aliphaticValidation(firstname);
                isLastnameChecked = validations.aliphaticValidation(lastname);
                isEmailChecked = validations.isValidEmail(email);
                isPhoneChecked = validations.aliphaticValidation(phone);
                isPasswordChecked = validations.isValidPassword(password);

                //check the validation all fields
                if(isfirstnameChecked && isLastnameChecked && isEmailChecked && isPhoneChecked && isPasswordChecked ){
                    //firebase store the details
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                            password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            userId = firebaseAuth.getCurrentUser().getUid();
                            
                            DocumentReference documentReference = firebaseFirestore.collection(
                                    "users").document(userId);
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("firstname",firstname.getText().toString());
                            userInfo.put("lastname",lastname.getText().toString());
                            userInfo.put("phone",phone.getText().toString());
                            userInfo.put("email",email.getText().toString());
                            userInfo.put("imageUrl","https://firebasestorage.googleapis.com/v0/b/androidcoursework-4765d.appspot.com/o/profilepic.png?alt=media&token=317280a9-5afa-40f4-8c18-f374890e315e");
                            userInfo.put("accessLevel","0");
                            documentReference.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(RegisterActivity.this,"User Added Successfully",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onSuccess: "+e.getMessage());
                                    Toast.makeText(RegisterActivity.this,e.getMessage().toString(),
                                            Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage().toString(),
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));

                        }
                    });

                }
                else {

                }

            }
        });

    }

    public void onRegisterButton(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void changeStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

}
