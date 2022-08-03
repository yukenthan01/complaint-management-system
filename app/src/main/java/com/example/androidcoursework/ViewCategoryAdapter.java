package com.example.androidcoursework;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ViewCategoryAdapter extends FirestoreRecyclerAdapter<CategoryModel,
        ViewCategoryAdapter.myViewHolder> {

    public ViewCategoryAdapter(@NonNull FirestoreRecyclerOptions<CategoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewCategoryAdapter.myViewHolder holder, int position, @NonNull CategoryModel model) {
        holder.textViewCategory.setText(model.getCategory());
        holder.textViewCategoryEmail.setText(model.getCategoryEmail());
        holder.textViewAction.setText("Edit");

        holder.textViewAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                snapshot.getId();
                AppCompatActivity activity =  (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                        new CategoryFragment(model,snapshot.getId())).addToBackStack(null).commit();
            }
        });
    }

    @NonNull
    @Override
    public ViewCategoryAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_table_layout,
                parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewCategory,textViewCategoryEmail,textViewAction;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewCategory = itemView.findViewById(R.id.tableCategory);
            textViewCategoryEmail = itemView.findViewById(R.id.tableCategoryEmail);
            textViewAction = itemView.findViewById(R.id.tableAction);


        }
    }
}
