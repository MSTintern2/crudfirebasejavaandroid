package com.example.firebasecrud;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadActivity extends AppCompatActivity {
    ImageView uploadImage ;
    Button saveButton;
    EditText uploadTopic, uploadDescription, uploadLanguage;
    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Initialize UI elements
        uploadImage = findViewById(R.id.uploadImage);
        saveButton = findViewById(R.id.SaveButton);
        uploadTopic = findViewById(R.id.UploadTopic);
        uploadDescription = findViewById(R.id.UploadDescription);
        uploadLanguage = findViewById(R.id.UploadLanguage);

        // Image upload launcher (commented out for now)
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        uri = data.getData();
                        uploadImage.setImageURI(uri);
                    } else {
                        Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                });

        // Image picker setup (commented out)
        uploadImage.setOnClickListener(view -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });

        // Save button functionality
        saveButton.setOnClickListener(view -> uploadData());
    }

    public void uploadData() {
        String title = uploadTopic.getText().toString().trim();
        String desc = uploadDescription.getText().toString().trim();
        String language = uploadLanguage.getText().toString().trim();

        // Validate inputs
        if (title.isEmpty() || desc.isEmpty() || language.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get a reference to the database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials");

        // Generate a unique key for each entry
        String uniqueKey = databaseReference.push().getKey();

        // Create dummy image URL for testing
        imageURL = "https://via.placeholder.com/150";

        // Create DataClass object
        DataClass dataClass = new DataClass(title, desc, language, imageURL);

        // Upload data with unique key
        databaseReference.child(uniqueKey).setValue(dataClass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UploadActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();

                        // Clear input fields after successful upload
                        clearInputFields();

                        // Optionally, finish the activity or navigate back
                        finish();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(UploadActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void clearInputFields() {
        uploadTopic.setText("");
        uploadDescription.setText("");
        uploadLanguage.setText("");
    }
}