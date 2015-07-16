package com.app.mymooviapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mymooviapp.fragments.MovieDetailsFrag;
import com.app.mymooviapp.loaders.MovieDetailsLoader;
import com.app.mymooviapp.models.Movie;
import com.app.mymooviapp.utils.Constants;
import com.squareup.picasso.Picasso;


public class MovieDetails extends AppCompatActivity implements MovieDetailsFrag.OnMovieFavorited {

    private Movie movie;

    private Toolbar mToolbar;

    private Menu  mToolbarMenu;

    private CollapsingToolbarLayout colapsingToolbarLayout;

    private View mHeaderLayout;

    private ImageView headerImageView;

    private TextView mCurentRating;

    private TextView maxRating;

    private Fragment fragment;

    private  String FRAGMENT_TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Bundle bundle = getIntent().getExtras();

        FRAGMENT_TAG = getResources().getString(R.string.movie_details_frag_tag);

        movie =(Movie) bundle.getParcelable("movie");

        configureToolbar();

        fragment = MovieDetailsFrag.newInstance();

        fragment.setArguments(bundle);

        setFragment(fragment);

    }

    public void configureToolbar()
    {

        colapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsingToolbar);

        colapsingToolbarLayout.setTitle(movie.getTitle());

        mHeaderLayout = findViewById(R.id.headerLayout);

        headerImageView = (ImageView)mHeaderLayout.findViewById(R.id.header);

        String backdropUrl = Constants.BACKDROP_URL_BASE_PATH+movie.getBackDropPath();

        Picasso.with(getApplicationContext()).load(backdropUrl).into(headerImageView);

        mCurentRating = (TextView)mHeaderLayout.findViewById(R.id.current_rating);

        maxRating = (TextView)mHeaderLayout.findViewById(R.id.max_rating);

        float rating = movie.getUserRating();

        if(rating > 9){
            maxRating.setVisibility(View.GONE);

        }
        else{
            if(maxRating.getVisibility() != View.VISIBLE){
                maxRating.setVisibility(View.VISIBLE);

            }
        }

        mCurentRating.setText(Float.toString(rating));



        mToolbar = (Toolbar)colapsingToolbarLayout.findViewById(R.id.toolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavUtils.navigateUpFromSameTask(MovieDetails.this);

            }
        });

        mToolbar =(Toolbar)findViewById(R.id.toolbar);

        mToolbar.inflateMenu(R.menu.menu_movie_details);

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_favorite: {

                        ((MovieDetailsFrag) fragment).favoriteMovie();
                        break;
                    }
                    case R.id.action_share:{
                        ((MovieDetailsFrag) fragment).shareMovieTrailer();
                    }
                }
                return true;
            }
        });

        mToolbarMenu = mToolbar.getMenu();

    }

    public void setFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.details_container, fragment, FRAGMENT_TAG).commit();
    }



    @Override
    public void onMovieFavorite(boolean isFavorite) {

        tintFavoriteStar(isFavorite);
    }


    public void tintFavoriteStar(boolean favorite)
    {
        MenuItem item = mToolbarMenu.findItem(R.id.action_favorite);

        Drawable icFavorite =  DrawableCompat.wrap(item.getIcon());

        int color = 0;
        if(favorite) {
            color  = getResources().getColor(R.color.colorYellow);
        }
        else{
            color = getResources().getColor(R.color.white);
        }

        DrawableCompat.setTint(icFavorite, color);

    }


}
