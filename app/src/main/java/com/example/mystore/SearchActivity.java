package com.example.mystore;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    EditText searchInput;
    Spinner categorySpinner;


    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        searchInput = findViewById(R.id.searchInput);
        categorySpinner = findViewById(R.id.categorySpinner);


        searchButton = findViewById(R.id.searchButton);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                Intent intent = new Intent(SearchActivity.this, ResultsActivity.class);
               // intent.putExtra("query", searchInput.getText().toString());
                intent.putExtra("category", selectedCategory);
                startActivity(intent);
            }
        });
    }
}

