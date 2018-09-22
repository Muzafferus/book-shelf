package com.wordpress.mapdroid.bookshelfforyou.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wordpress.mapdroid.bookshelfforyou.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<BookModel> bookModelArrayList;
    private BookModel bookModel;

    ListAdapter(Context context, ArrayList<BookModel> bookModelArrayList) {
        this.context = context;
        this.bookModelArrayList = bookModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        bookModel = bookModelArrayList.get(position);

        holder.bookView.setTag(position);

        if (!bookModel.getSmallThumbnail().equals("null"))
            Picasso.get().load(bookModel.getSmallThumbnail()).into(holder.imageView);
        else holder.imageView.setVisibility(View.GONE);

        if (!bookModel.getTitle().equals("null"))
            holder.textViewTitle.setText(bookModel.getTitle());
        else holder.textViewTitle.setVisibility(View.GONE);

        String[] authors = bookModel.getAuthors();
        if (!authors[0].equals("null"))
            holder.textViewAuthors.setText(Arrays.toString(bookModel.getAuthors()));
        else holder.textViewAuthors.setVisibility(View.GONE);

        if (!bookModel.getPublisher().equals("null"))
            holder.textViewPublisher.setText(bookModel.getPublisher());
        else holder.textViewPublisher.setVisibility(View.GONE);

        if (!bookModel.getPageCount().equals("null"))
            holder.textViewPageCount.setText(bookModel.getPageCount());
        else holder.textViewPageCount.setVisibility(View.GONE);

        holder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMain = new Intent(v.getContext(), BookActivity.class);
                openMain.putExtra("position", Integer.parseInt(holder.bookView.getTag().toString()));
                openMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(openMain);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewAuthors, textViewPublisher, textViewPageCount;
        ImageView imageView;
        View bookView;
        MyViewHolder(View itemView) {
            super(itemView);
            bookView = itemView;
            textViewTitle = itemView.findViewById(R.id.text_view_book_title);
            textViewAuthors = itemView.findViewById(R.id.text_view_book_authors);
            textViewPublisher = itemView.findViewById(R.id.text_view_book_publisher);
            textViewPageCount = itemView.findViewById(R.id.text_view_book_page_count);
            imageView = itemView.findViewById(R.id.image_view_book_small_thumbnail);
        }

    }
}