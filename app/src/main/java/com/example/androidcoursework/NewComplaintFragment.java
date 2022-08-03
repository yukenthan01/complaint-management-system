package com.example.androidcoursework;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NewComplaintFragment extends Fragment {

    FusedLocationProviderClient fusedLocationProviderClient;

    EditText subject,description,getPostCode,
            address,cities,postcode,state;
    TextView errorView;
    RadioButton radioCurrent,radioPostCode;
    Button findPostCodeButton,newCompliantButton,uploadImageButton;
    LinearLayout selectLocation;
    View view;
    ImageView imageView;
    private final static int REQUEST_CODE = 100;
    Validations validations ;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference ;
    Boolean isSubject,isDescriptionChecked,isCategoryChecked,isAddressChecked,isPostcodeChecked,
            isStateChecked,isImgeChecked,isCityChecked;
    Uri resultUri;
    String imageUploadedUrl,category ;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference(
            "complaints");

    MaterialToolbar toolbar;

    Spinner categoryDropdown;
    ArrayAdapter<String> adapter;
    List<String> categories ;
    private StorageTask storageTask;


    public NewComplaintFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewComplaintFragment newInstance(String param1, String param2) {
        NewComplaintFragment fragment = new NewComplaintFragment();
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
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_new_complaint, container, false);
        subject = view.findViewById(R.id.editTextSubject);
        description = view.findViewById(R.id.editTextDescription);
        getPostCode = view.findViewById(R.id.editTextGetPostcode);
        address = view.findViewById(R.id.editTextAddress);
        cities = view.findViewById(R.id.editTextCity);
        postcode = view.findViewById(R.id.editTextPostCode);
        state = view.findViewById(R.id.editTextState);
        radioCurrent = view.findViewById(R.id.radioCurrent);
        radioPostCode = view.findViewById(R.id.radioPostCode);
        selectLocation = view.findViewById(R.id.selectLocation);
        findPostCodeButton = view.findViewById(R.id.findPostCodeButton);
        newCompliantButton = view.findViewById(R.id.newCompliantButton);
        uploadImageButton = view.findViewById(R.id.uploadImageButton);
        imageView = view.findViewById(R.id.imageView);
        errorView = view.findViewById(R.id.errorView);
        categoryDropdown = view.findViewById(R.id.categoryDropdown);

        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(getActivity());

        toolbar=view.findViewById(R.id.topAppBar);
        selectLocationMode(); //get the location from current and
        imageUpload();

        newCompliantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDateToFirebase();
            }
        });
        selectedDataCategrry();
        adapter=new ArrayAdapter<String>(getContext(),
                R.layout.category_list,categories);
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

        return  view;
    }

    public void selectedDataCategrry(){
        categories = new ArrayList<String>();
        categories.add("Select the Category");
        firebaseFirestore.collection("categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        categories.add(document.getString("category").toString());
                    }

                } else {
                    Log.d("spein", "Error getting documents: ", task.getException());
                }
            }
        });

    }
    public final ActivityResultLauncher<Intent> startForMediaPickerResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
                    resultUri = data.getData();
                            if (resultUri != Uri.EMPTY) {
                                imageView.setImageURI(resultUri);
                                getFileExtension(resultUri);

                            }
                }
                else {
                    Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                }

            });
    private void imageUpload() {
        //image upload button click
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] mimeTypes = {"image/png", "image/jpg", "image/jpeg"};
                ImagePicker.Companion.with(getActivity())
                        .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                        .createIntent(intent -> {
                            startForMediaPickerResult.launch(intent);
                            return null;
                        });
            }
        });
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void reloadFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
    //save data to firebase
    private void saveDateToFirebase() {
        validations = new Validations();
        isSubject = validations.aliphaticValidation(subject);
        isDescriptionChecked = validations.aliphaticValidation(description);
        isCategoryChecked = validations.setSpinnerError(categoryDropdown);
        Log.d("TAG11q", "saveDateToFirebase: "+isCategoryChecked);
        isAddressChecked = validations.aliphaticValidation(address);
        isPostcodeChecked = validations.aliphaticValidation(postcode);
        isStateChecked = validations.aliphaticValidation(state);
        isImgeChecked = validations.isValidImage(resultUri,errorView);
        isCityChecked = validations.aliphaticValidation(cities);
        if(isSubject && isDescriptionChecked && isCategoryChecked && isAddressChecked && isPostcodeChecked && isStateChecked && isImgeChecked && isCityChecked){
            String sub = subject.getText().toString();
            String des = description.getText().toString();
            String cat = category;
            String add = address.getText().toString();
            String city = cities.getText().toString();
            String pos = postcode.getText().toString();
            String sta = state.getText().toString();
            String status = "pending";
            String userId = firebaseAuth.getCurrentUser().getUid().toString();
            StorageReference fileReference =
                    storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(resultUri));
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
                        //get get upload image url
                        Uri uri = task.getResult();
                        imageUploadedUrl = uri.toString();
                        String date =  Calendar.getInstance().getTime().toString();//get the
                        // current date
                        NewComplaintModel newComplaintModel = new NewComplaintModel(sub,des,cat,add,
                                city,pos,sta,date,userId,imageUploadedUrl,status);//assign to new
                        // complaints model to values
                        firebaseFirestore.collection("complaints").add(newComplaintModel)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getActivity(),"New complaint added",Toast.LENGTH_LONG).show();
                                reloadFragment(new NewComplaintFragment());//reload the fragment
                                // for textbox clear
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            });
        }
        else {
            errorView.setText("Please fill the correct details");
            errorView.setVisibility(View.VISIBLE);
        }
    }
    //Check GPS on of off
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void selectLocationMode() {
        final LocationManager manager =
                (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        radioCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLocation.setVisibility(View.GONE);
                getLocation();
            }
        });
        radioPostCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLocation.setVisibility(View.VISIBLE);
                address.setText("");
                cities.setText("");
                postcode.setText("");
                state.setText("");
                findPostCodeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getLocation();
                    }
                });
            }
        });
    }

    private void getLocation() {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                if (location != null){
                                    try {
                                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                        List<Address> addresses = null;

                                        if (radioCurrent.isChecked()){
                                            addresses =
                                                    geocoder.getFromLocation(location.getLatitude(),
                                                            location.getLongitude(),1);
                                        }
                                        else if(radioPostCode.isChecked()) {
                                            List<Address>
                                                    addresses_postcode =
                                                    geocoder.getFromLocationName( getPostCode.getText().toString(),
                                                            1);

                                            addresses =
                                                    geocoder.getFromLocation(addresses_postcode.get(0).getLatitude(),
                                                            addresses_postcode.get(0).getLongitude(),1);
                                        }
                                        address.setText(addresses.get(0).getAddressLine(0));
                                        cities.setText(addresses.get(0).getLocality());
                                        postcode.setText(addresses.get(0).getPostalCode());
                                        state.setText(addresses.get(0).getAdminArea());

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                });
            }else {
                askPermission();
            }
    }
    private void askPermission(){
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation();
            }else {
                Toast.makeText(getActivity(),"Please provide the required permission",
                        Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}