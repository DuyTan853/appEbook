package com.example.app_ebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BookAdapter.OnBookClickListener {

    private RecyclerView recyclerView;
    private List<String> bookList = new ArrayList<>();
    private BookAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadPdfFiles();

        adapter = new BookAdapter(this, bookList, this);
        recyclerView.setAdapter(adapter);
    }

    private void loadPdfFiles() {
        try {
            AssetManager assetManager = getAssets();
            String[] files = assetManager.list("books");
            if (files != null) {
                for (String file : files) {
                    if (file.toLowerCase().endsWith(".pdf")) {
                        bookList.add(file);
                        Log.d("PDF_DEBUG", "Found in assets/books: " + file);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBookClick(String bookName) {
        Intent intent = new Intent(this, ReaderActivity.class);
        intent.putExtra("pdfFileName", bookName);
        startActivity(intent);
    }
}
