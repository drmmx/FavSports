package com.mdagl.favsports;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

    private NewsAdapter arrayAdapter;
    private ListView listView;
    private List<News> mNewsList = new ArrayList<>();

    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        listView = view.findViewById(R.id.listViewMain);
        progressBar = view.findViewById(R.id.mainPageProgressBar);

        progressBar.setVisibility(View.VISIBLE);

        new NewThread().execute();

        arrayAdapter = new NewsAdapter(view.getContext(), mNewsList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemUrl = mNewsList.get(position).getUrlNews();

                Intent intent = new Intent(view.getContext(), NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.NEWS_LINK, "http://xsport.ua" + itemUrl);
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
                Document document = Jsoup.connect("http://xsport.ua/news/").get();
                Elements fetchedItems = document.getElementsByAttributeValueMatching("class",
                        "small-news-item item");

                for (Element fetchedItem : fetchedItems) {
                    //parsing pictures
                    Elements images = fetchedItem.select("img");
                    String imageSrcValue = images.attr("src");

                    //parsing urls
                    Elements urls = fetchedItem.select("a");
                    String urlValue = urls.attr("href");

                    //add items to model
                    mNewsList.add(new News(fetchedItem.select(".news-header").text(),
                            fetchedItem.select(".news-preview").text(), imageSrcValue, urlValue));

                }
                //Using iterator Andrey`s code
                /*Iterator<Element> iterator = contentText.iterator();
                mNewsList.clear();
                for (Element titleElement : contentTitle) {
                    Element textElement = iterator.next();
                    mNewsList.add(new News(titleElement.text(), textElement.text()));
                }*/

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            listView.setAdapter(arrayAdapter);
        }
    }

}
