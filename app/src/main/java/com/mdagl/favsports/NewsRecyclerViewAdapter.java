package com.mdagl.favsports;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {

    private static final String TAG = "NewsAdapter_";

    private List<News> mNewsList;

    private CustomItemClickListener listener;

    public NewsRecyclerViewAdapter(List<News> newsList, CustomItemClickListener listener) {
        this.mNewsList = newsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_list, parent, false);
        final NewsViewHolder viewHolder = new NewsViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        News news = mNewsList.get(position);
        holder.setData(news, position);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG1 = "NewsViewHolder_";
        private TextView titleNews, textNews, textHowSport;
        private ImageView imageNews;

        private NewsViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG1, "NewsViewHolder: NewsViewHolder");

            titleNews = itemView.findViewById(R.id.listItemTitle);
            textNews = itemView.findViewById(R.id.listItemText);
            textHowSport = itemView.findViewById(R.id.listItemHowSport);
            imageNews = itemView.findViewById(R.id.listItemImage);
        }

        private void setData(News news, int position) {
            Log.d(TAG1, "setData");
            titleNews.setText(String.valueOf(news.getTitleNews()));
            textNews.setText(String.valueOf(news.getTextNews()));
            textHowSport.setText(String.valueOf(news.getTextHowSport()));
            if (!news.getImageNews().isEmpty()) {
                Picasso.get().load(news.getImageNews()).into(imageNews);
            } else {
                Picasso.get().load("http://e1.365dm.com/18/04/16-9/30/skysports-f1-daniel-ricciardo_4294402.jpg?20180427162917").into(imageNews);
            }
        }
    }

}
