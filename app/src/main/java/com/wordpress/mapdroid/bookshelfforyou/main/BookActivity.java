package com.wordpress.mapdroid.bookshelfforyou.main;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wordpress.mapdroid.bookshelfforyou.R;

import java.util.Arrays;

import static com.wordpress.mapdroid.bookshelfforyou.main.MainActivity.bookModelArrayList;

public class BookActivity extends AppCompatActivity {

    TextView textViewBookTitle, textViewBookDescription, textViewBookAuthors,
            textViewBookPageCount, textViewBookPublisher, textViewBookPublishDate;
    ImageView imageViewBookThumbnail;
    Button buttonBookPreviewLink, buttonBookInfoLink;
    BookModel bookModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        imageViewBookThumbnail = findViewById(R.id.image_view_book_thumbnail);
        buttonBookPreviewLink = findViewById(R.id.button_previewLink);
        buttonBookInfoLink = findViewById(R.id.button_infoLink);
        textViewBookTitle = findViewById(R.id.text_view_book_title);
        textViewBookDescription = findViewById(R.id.text_view_book_description);
        textViewBookAuthors = findViewById(R.id.text_view_book_authors);
        textViewBookPageCount = findViewById(R.id.text_view_book_page_count);
        textViewBookPublisher = findViewById(R.id.text_view_book_publisher);
        textViewBookPublishDate = findViewById(R.id.text_view_book_publish_date);

        bookModel = bookModelArrayList.get(getIntent().getIntExtra("position", 0));

        if (!bookModel.getThumbnail().equals("null"))
            Picasso.get().load(bookModel.getThumbnail()).into(imageViewBookThumbnail);
        else imageViewBookThumbnail.setVisibility(View.GONE);

        if (!bookModel.getTitle().equals("null"))
            textViewBookTitle.setText(bookModel.getTitle());
        else textViewBookTitle.setVisibility(View.GONE);

        String[] authors = bookModel.getAuthors();
        if (!authors[0].equals("null"))
            textViewBookAuthors.setText(Arrays.toString(bookModel.getAuthors()));
        else textViewBookAuthors.setVisibility(View.GONE);

        if (!bookModel.getPublisher().equals("null"))
            textViewBookPublisher.setText(bookModel.getPublisher());
        else textViewBookPublisher.setVisibility(View.GONE);

        if (!bookModel.getPageCount().equals("null"))
            textViewBookPageCount.setText(bookModel.getPageCount());
        else textViewBookPageCount.setVisibility(View.GONE);

        if (!bookModel.getPublishedDate().equals("null"))
            textViewBookPublishDate.setText(bookModel.getPublishedDate());
        else textViewBookPublishDate.setVisibility(View.GONE);

        if (!bookModel.getDescription().equals("null"))
            textViewBookDescription.setText(bookModel.getDescription());
        else textViewBookDescription.setVisibility(View.GONE);

        if (!bookModel.getPreviewLink().equals("null"))
            buttonBookPreviewLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(bookModel.getPreviewLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        else buttonBookPreviewLink.setVisibility(View.GONE);

        if (!bookModel.getInfoLink().equals("null"))
            buttonBookInfoLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(bookModel.getInfoLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        else buttonBookInfoLink.setVisibility(View.GONE);
    }
}
