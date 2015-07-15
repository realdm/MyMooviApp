package com.app.mymooviapp.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.mymooviapp.R;
import com.app.mymooviapp.models.Movie;
import com.app.mymooviapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 6/11/2015.
 */
public class MovieGridAdapter extends BaseAdapter {


    private List<Movie> movies = new ArrayList<Movie>();

    private Context mContext;

    public MovieGridAdapter(Context context)
    {
        this.mContext = context.getApplicationContext();

    }

    public void swapData(List<Movie> newMovies)
    {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    public void cleanData()
    {
        this.movies.clear();
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater infalter = LayoutInflater.from(mContext);

        MovieGridViewHolder holder;

        if(view == null)
        {
            view = infalter.inflate(R.layout.grid_movie_thumbnail,viewGroup,false);

            holder = new MovieGridViewHolder(view);

            view.setTag(holder);
        }
        else
        {
            holder = (MovieGridViewHolder)view.getTag();
        }

        String imgUrl = Constants.THUMB_URL_BASE_PATH+movies.get(i).getPosterPath();
        //set image using picasso


        Picasso.with(mContext).load(imgUrl).into(holder.mMoviePoster);


        return view;
    }

    public class MovieGridViewHolder
    {
        public  ImageView mMoviePoster;

        public MovieGridViewHolder(View view)
        {
             mMoviePoster  = (ImageView)view.findViewById(R.id.movie_thumbnail);
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        
    }
}
