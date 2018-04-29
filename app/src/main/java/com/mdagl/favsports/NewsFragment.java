package com.mdagl.favsports;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mdagl.favsports.model.News;
import com.mdagl.favsports.utils.CustomItemClickListener;
import com.mdagl.favsports.utils.NewsRecyclerViewAdapter;

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

    private NewsRecyclerViewAdapter mArrayAdapter;
    private RecyclerView mRecyclerView;
    private List<News> mNewsList = new ArrayList<>();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerViewMain);
        mProgressBar = view.findViewById(R.id.mainPageProgressBar);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        mProgressBar.setVisibility(View.VISIBLE);

        new NewThread().execute();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRecyclerView.setAdapter(mArrayAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mArrayAdapter = new NewsRecyclerViewAdapter(mNewsList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String itemUrl = mNewsList.get(position).getUrlNews();

                Intent intent = new Intent(v.getContext(), NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.NEWS_LINK, "https://www.breakingnews.ie" + itemUrl);
                startActivity(intent);
            }
        });

        return view;
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
                    String howSportValue = fetchedItem.select(".category-sports").text();

                    if (howSportValue.equals("soccer")) {
                        howSportValue = "football";
                    }

                    //add items to model
                    if (!howSportValue.isEmpty()) {
                        mNewsList.add(new News(titleValue, articleTextValue, imageSrcValue, urlValue, howSportValue));
                    } else {
                        mNewsList.add(new News(titleValue, articleTextValue, imageSrcValue, urlValue, "sport"));
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setAdapter(mArrayAdapter);
        }
    }

}
