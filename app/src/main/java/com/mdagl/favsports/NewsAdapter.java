package com.mdagl.favsports;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView listItemTitle = listItemView.findViewById(R.id.listItemTitle);
        listItemTitle.setText(currentNews.getTitleNews());


        TextView listItemText = listItemView.findViewById(R.id.listItemText);
        listItemText.setText(currentNews.getTextNews());

        CircleImageView listItemImage = listItemView.findViewById(R.id.listItemImage);

        if (!currentNews.getImageNews().isEmpty()) {
            Picasso.get().load("http://xsport.ua" + currentNews.getImageNews()).into(listItemImage);
        } else {
            Picasso.get().load("http://xsport.ua/upload/resize_cache/iblock/c94/375_282_2/c94b43c03ae7858dd0b2b37b1647d2fb.JPG").into(listItemImage);
        }

        return listItemView;
    }
}
