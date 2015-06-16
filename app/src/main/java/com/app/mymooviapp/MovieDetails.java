package com.app.mymooviapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mymooviapp.models.Movie;
import com.app.mymooviapp.utils.Constants;
import com.squareup.picasso.Picasso;


public class MovieDetails extends AppCompatActivity {

    private Movie movie;

    private Toolbar mToolbar;

    private ImageView mMovieThumb;

    private TextView mReleaseDate;

    private TextView mRating;

    private TextView mSynopsis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Bundle bundle = getIntent().getExtras();

        movie =(Movie) bundle.getSerializable("movie");

        configureToolbar();

        initFieldsWithData();
    }

    public void configureToolbar()
    {
        mToolbar = (Toolbar)findViewById(R.id.toolbar);

        mToolbar.setTitle(movie.getTitle());

        this.setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }

    public void initFieldsWithData()
    {
        mMovieThumb = (ImageView)findViewById(R.id.movie_thumb);

        mReleaseDate = (TextView)findViewById(R.id.release_date);

        mRating = (TextView)findViewById(R.id.rating);

        mSynopsis = (TextView)findViewById(R.id.synopsis);

        String imgUrl = Constants.THUMB_URL_BASE_PATH+movie.getPosterPath();

        Picasso.with(this).load(imgUrl).into(mMovieThumb);

        mReleaseDate.setText(movie.getReleaseDate());


        mRating.setText(Float.toString(movie.getUserRating()));

        mSynopsis.setText(movie.getOverview());


    }



}
