package com.example.firebasecrud;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    TextView detailDesc, detailTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailDesc = findViewById(R.id.descriptiontitle);
        detailTitle = findViewById(R.id.detailtitle);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Use the correct keys that match how they're set in the Adapter
            detailDesc.setText(bundle.getString("Desct"));
            detailTitle.setText(bundle.getString("TITLe"));
        }
    }
}