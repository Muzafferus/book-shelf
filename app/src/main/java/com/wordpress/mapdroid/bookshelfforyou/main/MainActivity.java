package com.wordpress.mapdroid.bookshelfforyou.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wordpress.mapdroid.bookshelfforyou.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editTextSearch;
    Button buttonSearch;
    RequestQueue mQueue;
    String searchText;
    static ArrayList<BookModel> bookModelArrayList;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView textViewNoData, textViewNoInternet;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextSearch = findViewById(R.id.edit_text_search);
        buttonSearch = findViewById(R.id.button_search);
        recyclerView = findViewById(R.id.recycle_view);
        progressBar = findViewById(R.id.progress_loading);
        textViewNoData = findViewById(R.id.text_view_no_data);
        textViewNoInternet = findViewById(R.id.text_view_no_internet);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        textViewNoInternet.setVisibility(View.GONE);
        textViewNoData.setVisibility(View.GONE);

        bookModelArrayList = new ArrayList<>();
        mQueue = Volley.newRequestQueue(this);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = editTextSearch.getText().toString();
                if (!TextUtils.isEmpty(searchText)) {
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    textViewNoInternet.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.GONE);
                    bookModelArrayList.clear();
                    volley(searchText, false);
                    editTextSearch.setText("");
                }
            }
        });

        volley("Android", true);
    }

    private void volley(String searchText, final boolean firstTime) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + searchText + "&maxResults=22";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    Log.i("Muzafferus-itemsArray", itemsArray.length() + "");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        Log.i("Muzafferus-i", i + "");
                        JSONObject book = itemsArray.getJSONObject(i);
                        BookModel bookModel = new BookModel();
                        JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                        if (volumeInfo.has("title"))
                            bookModel.setTitle(volumeInfo.getString("title"));
                        else bookModel.setTitle("null");

                        if (volumeInfo.has("pageCount"))
                            bookModel.setPageCount(volumeInfo.getString("pageCount") + " pages");
                        else bookModel.setPageCount("null");

                        if (volumeInfo.has("description"))
                            bookModel.setDescription(volumeInfo.getString("description"));
                        else bookModel.setDescription("null");

                        if (volumeInfo.has("publisher"))
                            bookModel.setPublisher(volumeInfo.getString("publisher"));
                        else bookModel.setPublisher("null");

                        if (volumeInfo.has("publishedDate"))
                            bookModel.setPublishedDate(volumeInfo.getString("publishedDate"));
                        else bookModel.setPublishedDate("null");

                        if (volumeInfo.has("authors")) {
                            JSONArray authorArray = volumeInfo.getJSONArray("authors");
                            String[] authors = new String[authorArray.length()];
                            for (int j = 0; j < authorArray.length(); j++) {
                                authors[j] = authorArray.getString(j);
                            }
                            bookModel.setAuthors(authors);
                        } else {
                            String[] authors = new String[1];
                            authors[0] = "null";
                            bookModel.setAuthors(authors);
                        }

                        if (volumeInfo.has("previewLink")) {
                            bookModel.setPreviewLink(volumeInfo.getString("previewLink"));
                            Log.i("Muzafferus-previewLink", volumeInfo.getString("previewLink") + "");
                        } else bookModel.setPreviewLink("null");


                        if (volumeInfo.has("infoLink")) {
                            bookModel.setInfoLink(volumeInfo.getString("infoLink"));
                            Log.i("Muzafferus-infoLink", volumeInfo.getString("infoLink") + "");
                        } else bookModel.setInfoLink("null");


                        if (volumeInfo.has("imageLinks")) {
                            JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                            if (imageLinks.has("smallThumbnail")) {
                                bookModel.setSmallThumbnail(imageLinks.getString("smallThumbnail"));
                                Log.i("Muzafferus-small", imageLinks.getString("smallThumbnail") + "");
                            } else bookModel.setSmallThumbnail("null");

                            if (imageLinks.has("thumbnail")) {
                                bookModel.setThumbnail(imageLinks.getString("thumbnail"));
                                Log.i("Muzafferus-thumbnail", imageLinks.getString("thumbnail") + "");
                            } else bookModel.setThumbnail("null");
                        } else {
                            bookModel.setSmallThumbnail("null");
                            bookModel.setThumbnail("null");
                        }

                        bookModelArrayList.add(bookModel);
                    }
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    textViewNoInternet.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.GONE);
                    if (firstTime) {
                        adapter = new ListAdapter(getApplicationContext(), bookModelArrayList);
                        recyclerView.setAdapter(adapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    } else {
                        adapter.notifyDataSetChanged();
                        editTextSearch.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("Muzafferus-error", error.toString());
                if (error.toString().equals("com.android.volley.ClientError")) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    textViewNoInternet.setVisibility(View.GONE);
                    textViewNoData.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    textViewNoInternet.setVisibility(View.VISIBLE);
                    textViewNoData.setVisibility(View.GONE);
                }
            }
        });

        mQueue.add(request);


    }
}