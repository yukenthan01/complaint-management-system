package com.example.androidcoursework;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {


    Uri resultUri;
    String imageUploadedUrl ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference(
            "users");
    private StorageTask storageTask;
    DocumentReference documentReference ;

    EditText firstname,lastname,email,phone;
    Validations validations ;
    Boolean isfirstnameChecked,isLastnameChecked,isEmailChecked,isPasswordChecked,isPhoneChecked;
    View view;
    Button updateButton;
    FloatingActionButton updateImageButton;
    ImageView updateImageView;
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//    email,password,phone,imageUrl;
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        firstname = view.findViewById(R.id.editTextFirstname);
        lastname = view.findViewById(R.id.editTextLastname);
        email = view.findViewById(R.id.editTextEmail);
        phone = view.findViewById(R.id.editTextMobile);
        updateImageView = view.findViewById(R.id.profileImage);
        updateButton = view.findViewById(R.id.updateButton);

        updateImageView = view.findViewById(R.id.profileImage);
        updateImageButton = (FloatingActionButton) view.findViewById(R.id.updateImageFloat);
        getUserDetails();
        updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImage();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateFirebase();
            }
        });
        return  view;
    }
    //save data to firebase
    private void UpdateFirebase() {
        validations = new Validations();
        isfirstnameChecked = validations.aliphaticValidation(firstname);
        isLastnameChecked = validations.aliphaticValidation(lastname);
        isEmailChecked = validations.isValidEmail(email);
        isPhoneChecked = validations.aliphaticValidation(phone);
        String userId = firebaseAuth.getCurrentUser().getUid().toString();
        UserModel userModel = new UserModel(firstname.getText().toString(),
                lastname.getText().toString(),email.getText().toString(),phone.getText().toString(),imageUploadedUrl);

        firebaseFirestore.collection("users").document(userId).update(
                "firstname",firstname.getText().toString(),
                "lastname",lastname.getText().toString(),
                "phone",phone.getText().toString(),
                "email",email.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void updateImage(){
        String[] mimeTypes = {"image/png", "image/jpg", "image/jpeg"};
        ImagePicker.Companion.with(getActivity())
                .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                .createIntent(intent -> {
                    startForMediaPickerResult.launch(intent);
                    return null;
                });

    }
    //update Profile image
    public final ActivityResultLauncher<Intent> startForMediaPickerResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    resultUri = data.getData();
                    if (resultUri != Uri.EMPTY) {
                        //image upload to firebase storage
                        updateImageView.setImageURI(resultUri);
                        getFileExtension(resultUri);
                        String userId = firebaseAuth.getCurrentUser().getUid().toString();
                        StorageReference fileReference =
                                storageReference.child(System.currentTimeMillis()
                                        + "." + getFileExtension(resultUri));
                        Log.d("TAG12", ": "+fileReference);
                        storageTask = fileReference.putFile(resultUri);
                        storageTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {
                                if(!task.isSuccessful()){
                                    throw task.getException();
                                }
                                return fileReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()){

                                    Uri uri = task.getResult();
                                    imageUploadedUrl = uri.toString();
                                    Log.d("TAG12", "onComplete: "+imageUploadedUrl);
                                    String date =  Calendar.getInstance().getTime().toString();

                                    documentReference = firebaseFirestore.collection(
                                            "users").document(userId);

                                    documentReference.update("imageUrl",imageUploadedUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(),"Image Updated",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(),e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG12", "onFailure: "+e.getMessage());
                            }
                        });

                    }
                }
                else {
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }

            });
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void getUserDetails() {
        String uid = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                firstname.setText(documentSnapshot.getString("firstname").toString());
                lastname.setText(documentSnapshot.getString("lastname").toString());
                email.setText(documentSnapshot.getString("email").toString());
                phone.setText(documentSnapshot.getString("phone").toString());
                Glide.with(updateImageView.getContext()).load(documentSnapshot.getString("imageUrl").toString()).into(updateImageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAGfaill", "onFailure: "+e.getMessage().toString());
            }
        });
    }
}