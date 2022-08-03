package com.example.androidcoursework;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MyAdapter extends FirestoreRecyclerAdapter<NewComplaintModel,
        MyAdapter.myViewHolder> {

    public MyAdapter(@NonNull FirestoreRecyclerOptions<NewComplaintModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyAdapter.myViewHolder holder, int position, @NonNull NewComplaintModel model) {
        holder.textViewSubject.setText(model.getSubject());
        holder.textViewDescription.setText(model.getDescription());
        Glide.with(holder.imageRecycleView.getContext()).load(model.getImageUrl()).into(holder.imageRecycleView);

        holder.imageRecycleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity =  (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                        new SingleComplaintFragment(model)).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public MyAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design,
                parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageRecycleView;
        TextView textViewSubject,textViewDescription;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRecycleView = itemView.findViewById(R.id.imageViewRecyleView);
            textViewSubject = itemView.findViewById(R.id.textViewSubject);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);


        }
    }
}
