package com.example.random.coolweather.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.random.coolweather.R;
import com.example.random.coolweather.db.db_City;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by random on 17/3/9.
 */

public class CityAdapter extends ArrayAdapter<db_City> {

    private int resourceId;
    public CityAdapter(Context context, int resource, List<db_City> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        db_City city=getItem(position);
        View view;
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }else {
            view=convertView;
        }
        TextView cityName= (TextView) view.findViewById(R.id.tv_city);
        cityName.setText(city.getCity());
        return view;
    }
}
