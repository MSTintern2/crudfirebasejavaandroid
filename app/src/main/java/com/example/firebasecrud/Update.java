package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Update extends AppCompatActivity {

    DatabaseReference databaseReference;
    Button updateButton;
    EditText titleEditText, descriptionEditText, languageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateButton = findViewById(R.id.updatebutton);
        titleEditText = findViewById(R.id.editTitle); // Assuming you have EditTexts for each field
        descriptionEditText = findViewById(R.id.editDescription);
        languageEditText = findViewById(R.id.editLanguage);

        // Get the data passed from the previous activity
        String title = getIntent().getStringExtra("TITLE");
        String description = getIntent().getStringExtra("DESCRIPTION");
        String language = getIntent().getStringExtra("LANGUAGE");
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        // Set the data to EditText fields
        titleEditText.setText(title);
        descriptionEditText.setText(description);
        languageEditText.setText(language);

        // Initialize the Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials");

        // Set an OnClickListener on the update button
        updateButton.setOnClickListener(view -> {
            // Get the updated data from the EditTexts
            String updatedTitle = titleEditText.getText().toString();
            String updatedDescription = descriptionEditText.getText().toString();
            String updatedLanguage = languageEditText.getText().toString();

            // Use the key or ID to update the data in Firebase
            databaseReference.orderByChild("title").equalTo(title) // Assuming 'title' is unique
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // If the data exists in the database
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                // Get the reference of the data to update
                                String dataKey = dataSnapshot.getKey();
                                // Update the data with new values
                                assert dataKey != null;
                                databaseReference.child(dataKey).child("title").setValue(updatedTitle);
                                databaseReference.child(dataKey).child("description").setValue(updatedDescription);
                                databaseReference.child(dataKey).child("language").setValue(updatedLanguage);

                                // Optionally, update the image URL if needed
                                if (imageUrl != null) {
                                    databaseReference.child(dataKey).child("imageUrl").setValue(imageUrl);
                                }

                                // Show a message to the user and close the activity
                                Toast.makeText(Update.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                                finish(); // Close the activity after updating
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database error
                            Toast.makeText(Update.this, "Error updating item: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
