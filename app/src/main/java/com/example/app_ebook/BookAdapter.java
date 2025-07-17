package com.example.app_ebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private final Context context;
    private final List<String> bookList;
    private final OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(String bookName);
    }

    public BookAdapter(Context context, List<String> bookList, OnBookClickListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String bookName = bookList.get(position);
        holder.bookTitle.setText(bookName);

        // Map tên PDF sang ảnh bìa
        holder.bookCover.setImageResource(getCoverResource(bookName));

        holder.itemView.setOnClickListener(v -> listener.onBookClick(bookName));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // Map PDF filename -> cover image
    private int getCoverResource(String pdfName) {
        if (pdfName.equalsIgnoreCase("Basic_Programming.pdf")) return R.drawable.bg1;
        if (pdfName.equalsIgnoreCase("Big_Data.pdf")) return R.drawable.bg2;
        if (pdfName.equalsIgnoreCase("Sổ tay nghề lập trình (new).pdf")) return R.drawable.bg3;
        if (pdfName.equalsIgnoreCase("Thiết kế và phát triển website.pdf")) return R.drawable.bg4;
        if (pdfName.equalsIgnoreCase("Tuyển tập 50 thuật toán lập trình cho sinh viên IT.pdf")) return R.drawable.bg5;
        if (pdfName.equalsIgnoreCase("Tài liệu Những điều cần biết về nghề Công nghệ Thông tin.pdf")) return R.drawable.bg6;
        return R.drawable.nobook;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle;
        ImageView bookCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookCover = itemView.findViewById(R.id.bookCover);
        }
    }
}
