package com.example.androidcoursework;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SingleComplaintFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    FloatingActionButton btnSendEmail ;

    String subject,description,category,
            address,city,postcode,state,date,userId,imageUrl,status;
    NewComplaintModel getModel;
    public SingleComplaintFragment() {
        // Required empty public constructor

    }
    public SingleComplaintFragment(NewComplaintModel model) {
        this.subject = model.getSubject();
        this.description = model.getDescription();
        this.category = model.getCategory();
        this.address = model.getAddress();
        this.city = model.getCity();
        this.postcode = model.getPostcode();
        this.state = model.getState();
        this.date = model.getDate();
        this.userId = model.userId;
        this.imageUrl = model.getImageUrl();
        this.status = model.getStatus();
        this.getModel = model;

    }

    public static SingleComplaintFragment newInstance(String param1, String param2) {
        SingleComplaintFragment fragment = new SingleComplaintFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        View view = inflater.inflate(R.layout.fragment_single_complaint, container, false);

        TextView subjectTextView,descriptionTextView,categoryTextView,addressTextView,cityTextView,postcodeTextView,stateTextView,dateTextView,userIdTextView,
                statusTextView;
        ImageView imageView = view.findViewById(R.id.imageViewSingleView);
        subjectTextView = view.findViewById(R.id.textViewSubject);
        descriptionTextView = view.findViewById(R.id.textViewDescription);
        categoryTextView = view.findViewById(R.id.textViewCategory);
        addressTextView = view.findViewById(R.id.textViewAddress);
        cityTextView = view.findViewById(R.id.textViewCity);
        postcodeTextView = view.findViewById(R.id.textViewPostCode);
        stateTextView = view.findViewById(R.id.textViewState);
        statusTextView = view.findViewById(R.id.textViewStatus);
        btnSendEmail = view.findViewById(R.id.floatBtnSendEmail);

        subjectTextView.setText(subject);
        descriptionTextView.setText(description);
        categoryTextView.setText(category);
        cityTextView.setText(city);
        addressTextView.setText(address);
        cityTextView.setText(category);
        postcodeTextView.setText(postcode);
        stateTextView.setText(state);

        Glide.with(getContext()).load(imageUrl).into(imageView);
        CheckUserAccessLevel checkUserAccessLevel = new CheckUserAccessLevel();//role and full name
        //checkUserAccessLevel.checkUserLevel(role -> { finalUserRole = role;});
        checkUserAccessLevel.checkUserLevel(new CheckUserAccessLevel.callBackUserLevel() {
            @Override
            public void onCallback(String userRole) {
                if (userRole.equals("admin")){
                    btnSendEmail.setVisibility(View.VISIBLE);
                }
                else  if (userRole.equals("user")){
                    btnSendEmail.setVisibility(View.GONE);
                }
            }
        });

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity =  (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                        new SendEMailFragment(getModel)).addToBackStack(null).commit();
            }
        });
        if(status.equals("pending")){
            statusTextView.setBackgroundResource(R.drawable.rounded_corner_textview);
            statusTextView.setText("Pending");
        }
        else if(status.equals("completed")){
            statusTextView.setBackgroundResource(R.color.completed);
            statusTextView.setText("Completed");
        }
        return view;
    }

    public void onBackPressed(){
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                new ViewComplaintsFragment()).addToBackStack(null).commit();
    }
}