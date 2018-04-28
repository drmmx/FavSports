package com.mdagl.favsports;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {


    public NewsFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "MainActivity_";

    private NewsRecyclerViewAdapter arrayAdapter;
    private RecyclerView mRecyclerView;
    private List<News> mNewsList = new ArrayList<>();

    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerViewMain);
        progressBar = view.findViewById(R.id.mainPageProgressBar);

        progressBar.setVisibility(View.VISIBLE);

        new NewThread().execute();

        arrayAdapter = new NewsRecyclerViewAdapter(mNewsList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String itemUrl = mNewsList.get(position).getUrlNews();

                Intent intent = new Intent(v.getContext(), NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.NEWS_LINK, "https://www.breakingnews.ie" + itemUrl);
                startActivity(intent);
            }
        });

        return  view;
    }

    public class NewThread extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.d(TAG, "doInBackground: parsing HTML");
                Document document = Jsoup.connect("https://www.breakingnews.ie/sport/").get();
                Elements fetchedItems = document.getElementsByAttributeValueMatching("class", "with-preview-pic");

                for (Element fetchedItem : fetchedItems) {
                    //parsing pictures
                    Elements images = fetchedItem.select("img");
                    String imageSrcValue = images.attr("abs:src");

                    //parsing urls
                    Elements urls = fetchedItem.select("a");
                    String urlValue = urls.attr("href");

                    //parsing text
                    String titleValue = fetchedItem.select("h3").text();
                    String articleTextValue = fetchedItem.select("p").text();

                    //add items to model
                    mNewsList.add(new News(titleValue, articleTextValue, imageSrcValue, urlValue, "Football"));

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            mRecyclerView.setAdapter(arrayAdapter);
        }
    }

}
