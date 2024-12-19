package com.example.firebasecrud;

import android.widget.ImageButton;

public class DataClass {

    // Fields must match the structure of the data in the Firebase Realtime Database
    private String title;
    private String description;
    private String language;
    private String imageUrl;







    // Default constructor required for Firebase
    public DataClass() {
        // Default constructor for Firebase
    }

    // Parameterized constructor for convenience
    public DataClass(String title, String description, String language, String imageUrl) {
        this.title = title;
        this.description = description;
        this.language = language;
        this.imageUrl = imageUrl;

    }

    // Getters and Setters
    // Firebase requires these for serialization and deserialization
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
