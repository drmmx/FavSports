package com.mdagl.favsports;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mdagl.favsports.R;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NewsDetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity_";
    public static final String NEWS_LINK = "news_link";

    private String newsLink;

    private Elements arTitle;
    private Elements arText;
    private String imageSrcValue;

    private TextView articleTitle;
    private ImageView articleImage;
    private TextView articleText;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent intent = getIntent();
        newsLink = intent.getStringExtra(NEWS_LINK);


        articleTitle = findViewById(R.id.articleTitle);
        articleImage = findViewById(R.id.articleImage);
        articleText = findViewById(R.id.articleText);

        progressBar = findViewById(R.id.detailPageProgressBar);

        new DetailThread().execute();

    }

    public class DetailThread extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            progressBar.setVisibility(View.VISIBLE);
            Document document;

            try {
                Log.d(TAG, "onCreate: loading information");
                document = Jsoup.connect(newsLink).get();
                Elements fetchedItems = document.getElementsByAttributeValueMatching("class",
                        "news-content");

                arTitle = fetchedItems.select("h1");
                Elements arImage = document.select("img");
                imageSrcValue = arImage.attr("src");

                arText = fetchedItems.select(".detail-text-holder");

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);

            articleTitle.setText(arTitle.text());
            articleText.setText(arText.text());
            Picasso.get().load("http://xsport.ua/" + imageSrcValue).placeholder(R.drawable.ic_launcher_background).into(articleImage);
        }
    }
}
