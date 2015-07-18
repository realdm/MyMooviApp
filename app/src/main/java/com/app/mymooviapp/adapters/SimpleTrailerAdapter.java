package com.app.mymooviapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.mymooviapp.R;
import com.app.mymooviapp.models.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 6/20/2015.
 */
public class SimpleTrailerAdapter extends BaseAdapter {

    List<Trailer> trailers = new ArrayList<Trailer>();

    private Context mContext;

    public SimpleTrailerAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Trailer> newTrailers){
        this.trailers = newTrailers;
        notifyDataSetChanged();
    }

    public void clearData()
    {
        this.trailers.clear();
    }

    @Override
    public int getCount() {

        return trailers.size();
    }

    @Override
    public Object getItem(int i) {
        return trailers.get(i).getSource();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        TraillerViewHolder holder;

        if(view==null)
        {
            view = inflater.inflate(R.layout.trailer_item_row,viewGroup,false);

            holder = new TraillerViewHolder(view);

            view.setTag(holder);

        }
        else
        {
            holder = (TraillerViewHolder)view.getTag();
        }

        String name="";
        try{
            name = trailers.get(i).getName();
        }
        catch (NullPointerException e){

            name = "No Name";
        }


        holder.mTrailerName.setText(name);

        return view;
    }


    public class TraillerViewHolder
    {
        TextView mTrailerName;

        public TraillerViewHolder(View view)
        {
            mTrailerName = (TextView)view.findViewById(R.id.trailer_name);
        }
    }
}
