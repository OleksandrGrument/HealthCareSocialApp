package com.comeonbabys.android.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comeonbabys.android.R;
import com.comeonbabys.android.app.common.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;



public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> title;
    private final ArrayList<String> date;
    private final ArrayList<String> url_img;
    private final ArrayList<String> content;

    public CustomListAdapter(Activity context, ArrayList<String> title, ArrayList<String> url_img, ArrayList<String> content, ArrayList<String> date ) {
        super(context, R.layout.list_recepi, title);

        this.context = context;
        this.title = title;
        this.url_img = url_img;
        this.content = content;
        this.date= date;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_recepi, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        TextView extratxt = (TextView) rowView.findViewById(R.id.shortDescription);

        txtTitle.setText(title.get(position));
        ImageLoader.getInstance().displayImage(url_img.get(position), imageView, Constants.PROFILE_AVATAR_MALE_DISPLAY_OPTIONS);

        extratxt.setText(date.get(position));
        return rowView;
    };
}
