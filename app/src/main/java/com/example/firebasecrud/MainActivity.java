package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    Adapter adapter;
    List<DataClass> dataList;
    SearchView searchView;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    ImageButton editbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.MySearch);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        adapter = new Adapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Android Tutorials");

        // Clear the list before adding data to prevent duplicates
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list to prevent duplicate entries
                dataList.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DataClass dataClass = itemSnapshot.getValue(DataClass.class);

                    // Optional: Add a check to prevent null entries
                    if (dataClass != null) {
                        dataList.add(dataClass);
                    }
                }

                // Notify the adapter about the data change
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle potential errors
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                nowSearch(newText);
                return true;
            }
        });

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UploadActivity.class);
            startActivity(intent);
        });
    }

    public void nowSearch(String text) {
        ArrayList<DataClass> nowSearch = new ArrayList<>();
        for (DataClass dataClass : dataList) {
            if (dataClass.getTitle().toLowerCase().contains(text.toLowerCase())) {
                nowSearch.add(dataClass);
            }
        }
        adapter.Searching(nowSearch);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the listener to prevent memory leaks
        if (databaseReference != null && eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }
}