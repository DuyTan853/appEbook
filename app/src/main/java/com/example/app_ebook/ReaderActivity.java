package com.example.app_ebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Bitmap> pages = new ArrayList<>();
    private PdfRenderer pdfRenderer;
    private ParcelFileDescriptor fileDescriptor;
    private PdfPageAdapter adapter;
    private String pdfFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pdfFileName = getIntent().getStringExtra("pdfFileName");
        Log.d("PDF_DEBUG", "Received pdfFileName = " + pdfFileName);

        if (pdfFileName == null || pdfFileName.trim().isEmpty()) {
            Toast.makeText(this, "No PDF selected!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        try {
            File cachedFile = copyPdfFromAssets(pdfFileName);
            openRenderer(cachedFile);
            renderAllPages();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private File copyPdfFromAssets(String filename) throws IOException {
        String simpleName = new File(filename).getName();
        File outFile = new File(getCacheDir(), simpleName);

        if (!outFile.exists()) {
            Log.d("PDF_DEBUG", "Copying assets/books/" + filename + " to cache");
            try (InputStream in = getAssets().open("books/" + filename);
                 OutputStream out = new FileOutputStream(outFile)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            }
        } else {
            Log.d("PDF_DEBUG", "Cache hit: " + outFile.getAbsolutePath());
        }

        return outFile;
    }

    private void openRenderer(File file) throws IOException {
        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        pdfRenderer = new PdfRenderer(fileDescriptor);
    }

    private void renderAllPages() {
        int pageCount = pdfRenderer.getPageCount();
        Log.d("PDF_DEBUG", "Total pages = " + pageCount);

        for (int i = 0; i < pageCount; i++) {
            PdfRenderer.Page page = pdfRenderer.openPage(i);
            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            pages.add(bitmap);
            page.close();
        }

        adapter = new PdfPageAdapter(this, pages);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        try {
            if (pdfRenderer != null) pdfRenderer.close();
            if (fileDescriptor != null) fileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
