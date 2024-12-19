package com.example.firebasecrud;

import static android.view.View.inflate;
import static androidx.core.content.ContextCompat.startActivity;

import static com.example.firebasecrud.R.layout.activity_update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private final Context context;
    private List<DataClass> dataItems;

    // Constructor
    public Adapter(Context context, List<DataClass> dataItems) {
        this.context = context;
        this.dataItems = dataItems;
    }

    // ViewHolder Class
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rectitle, recdescription, reclanguage;
        CardView recCard;
        ImageButton deletebtn,editbtn;
        SearchView searchView;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            rectitle = itemView.findViewById(R.id.rectitle);
            recdescription = itemView.findViewById(R.id.recdescription);
            reclanguage = itemView.findViewById(R.id.reclanguage);
            recCard = itemView.findViewById(R.id.recCard);
            searchView = itemView.findViewById(R.id.MySearch);
            deletebtn = itemView.findViewById(R.id.deletebtn);
            editbtn=itemView.findViewById(R.id.editbtn);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the RecyclerView item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public void Searching(ArrayList<DataClass> Searchfinder) {
        dataItems = Searchfinder;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Safely check if position is valid
        if (position < 0 || position >= dataItems.size()) {
            return;
        }

        // Get current DataClass object
        DataClass dataItem = dataItems.get(position);

        // Bind data to views
        holder.rectitle.setText(dataItem.getTitle());
        holder.recdescription.setText(dataItem.getDescription());
        holder.reclanguage.setText(dataItem.getLanguage());

        holder.editbtn.setOnClickListener(view -> {
            // Get the data item at the current position

            // Create an Intent to open the Update activity
            Intent intent = new Intent(context, Update.class);

            // Pass the data to Update activity using Intent
            intent.putExtra("TITLE", dataItem.getTitle());
            intent.putExtra("DESCRIPTION", dataItem.getDescription());
            intent.putExtra("LANGUAGE", dataItem.getLanguage());
            intent.putExtra("IMAGE_URL", dataItem.getImageUrl());

            // Start the Update activity
            context.startActivity(intent);
        });


        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();

                // Additional safety check
                if (adapterPosition == RecyclerView.NO_POSITION || adapterPosition >= dataItems.size()) {
                    Toast.makeText(context, "Invalid item selection", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get the specific item to be deleted
                DataClass itemToDelete = dataItems.get(adapterPosition);

                // Reference to Firebase Realtime Database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference("Android Tutorials");

                // Query to find the specific item to delete
                Query deleteQuery = databaseReference.orderByChild("title").equalTo(itemToDelete.getTitle());

                deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            // Remove the specific item from the database
                            childSnapshot.getRef().removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // Safely remove from local list
                                            if (adapterPosition < dataItems.size()) {
                                                dataItems.remove(adapterPosition);

                                                // Notify RecyclerView of item removal
                                                notifyItemRemoved(adapterPosition);

                                                Toast.makeText(context,
                                                        "Item deleted successfully",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle any errors during deletion
                                            Toast.makeText(context,
                                                    "Failed to delete item: " + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                        holder.editbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Create an AlertDialog.Builder
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                // Set title and message for the dialog
                                builder.setTitle("Edit Item")
                                        .setMessage("This is where the editing functionality will go.")

                                        // Add the positive button (e.g., Save)
                                        .setPositiveButton("Save", (dialog, which) -> {
                                            // Handle save button click
                                            Toast.makeText(context, "Save clicked", Toast.LENGTH_SHORT).show();
                                        })

                                        // Add the negative button (e.g., Cancel)
                                        .setNegativeButton("Cancel", (dialog, which) -> {
                                            // Handle cancel button click
                                            dialog.dismiss();
                                        });

                                // Create and show the dialog
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any database errors
                        Toast.makeText(context,
                                "Database error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        ;
    }
}