package com.app.mymooviapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.mymooviapp.fragments.MovieDetailsFrag;
import com.app.mymooviapp.fragments.MovieThumbsFragment;
import com.app.mymooviapp.handlers.MovieHandler;
import com.app.mymooviapp.models.Movie;
import com.app.mymooviapp.utils.Constants;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements MovieThumbsFragment.OnMovieSelected,MovieDetailsFrag.OnMovieFavorited {


    private Fragment thumbnailsFragment;

    private Fragment movieDetailsFragment;

    private FrameLayout mFragmentContainer;

    private Toolbar  mToolbar;

    private Menu mToolbarMenu;

    private String[] typeQueries;

    private Spinner mQuerySpinner;

    private String THUMB_FRAG_TAG;

    private String DETAILS_FRAG_TAG;

    private boolean isTwoPane = false;

    //HEader Fields
    private View mHeaderLayout;

    private ImageView headerImageView;

    private TextView mCurentRating;

    private TextView maxRating;

    //Static Fields
    public  static final String QUERY_TYPE_KEY="key";

    public static final String QUERY_TYPE_POPULAR="popular";

    public static final String QUERY_TYPE_TOP_RATED="topRated";

    public static final String QUERY_TYPE_FAVORITED="favorited";

    private static final String MOVIE_FRAG_TAG="movie_frag_tag";

    private static final String SPINNER_CURRENT_POS="currentPos";

    private static final String CONFIG_CHANGES_KEY="configChanges";

    private Movie mSelectedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentContainer = (FrameLayout) findViewById(R.id.container);

        isTwoPane = mFragmentContainer==null?true:false;

        THUMB_FRAG_TAG = getResources().getString(R.string.thumbnail_frag_tag);

        DETAILS_FRAG_TAG = getResources().getString(R.string.movie_details_frag_tag);

        if(isTwoPane) {

            thumbnailsFragment = (MovieThumbsFragment)getSupportFragmentManager().findFragmentByTag(THUMB_FRAG_TAG);

            movieDetailsFragment = (MovieDetailsFrag)getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG_TAG);

            mHeaderLayout = findViewById(R.id.headerLayout);

            headerImageView = (ImageView)mHeaderLayout.findViewById(R.id.header);

            mCurentRating = (TextView)mHeaderLayout.findViewById(R.id.current_rating);

            maxRating = (TextView)mHeaderLayout.findViewById(R.id.max_rating);

            if(savedInstanceState!=null){

                mSelectedMovie = savedInstanceState.getParcelable("movie");
                if(mSelectedMovie!=null){
                    setMovieHeaderDetails(mSelectedMovie);
                }

            }

        }
        else {

            thumbnailsFragment = MovieThumbsFragment.newInstance();

            getSupportFragmentManager().beginTransaction().replace(R.id.container,thumbnailsFragment,THUMB_FRAG_TAG).commit();
        }

        setUpToolbar(isTwoPane);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("MainActivity","State saved from activity");
        outState.putParcelable("movie",mSelectedMovie);
    }

    public void setUpToolbar(boolean isTwoPane)
    {
        mToolbar = (Toolbar)findViewById(R.id.toolbar);

        if(isTwoPane){

            mToolbar.inflateMenu(R.menu.menu_movie_details);

            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.action_favorite: {

                            ((MovieDetailsFrag) movieDetailsFragment).favoriteMovie();

                            break;
                        }
                        case R.id.action_share:{
                            ((MovieDetailsFrag) movieDetailsFragment).shareMovieTrailer();
                        }

                    }
                    return true;
                }
            });

            mToolbarMenu = mToolbar.getMenu();
        }

        typeQueries = getResources().getStringArray(R.array.movie_query_types);

        mQuerySpinner = (Spinner)findViewById(R.id.query_type);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,typeQueries);

        mQuerySpinner.setAdapter(adapter);

        mQuerySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                String queryType = "";
                switch (i) {
                    case 0:
                        queryType = QUERY_TYPE_POPULAR;
                        break;
                    case 1:
                        queryType = QUERY_TYPE_TOP_RATED;
                        break;
                    case 2:
                        queryType = QUERY_TYPE_FAVORITED;
                }

                Bundle bundle = new Bundle();


                bundle.putString(QUERY_TYPE_KEY, queryType);

                ((MovieThumbsFragment) thumbnailsFragment).restartLoader(bundle);

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }


    @Override
    public void onMovieSelected(int moviePosition, Movie movie) {

        Bundle bundle = new Bundle();

        bundle.putParcelable("movie", movie);

        mSelectedMovie = movie;


        if(isTwoPane) {
            //send data to the other fragment

            setMovieHeaderDetails(movie);
            ((MovieDetailsFrag)movieDetailsFragment).loadMovieDetails(bundle);
        }
        else {
            Intent intent = new Intent(this, MovieDetails.class);

            intent.putExtras(bundle);

            startActivity(intent);
        }
    }

    @Override
    public void onMovieFavorite(boolean isFavorite) {

        tintFavoriteStar(isFavorite);
    }

    public void setMovieHeaderDetails(Movie movie){

        mHeaderLayout.setVisibility(View.VISIBLE);

        String backdropUrl = Constants.BACKDROP_URL_BASE_PATH+movie.getBackDropPath();

        Picasso.with(getApplicationContext()).load(backdropUrl).into(headerImageView);

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
    }

    public void tintFavoriteStar(boolean favorite)
    {
        Log.i("MainActivity","Inside favorite and values is: "+favorite);

        MenuItem item = mToolbarMenu.findItem(R.id.action_favorite);

        Drawable icFavorite =  DrawableCompat.wrap(item.getIcon());

        int color = 0;

        if(favorite) {
            color  = getResources().getColor(R.color.colorYellow);
        }
        else{
            color = getResources().getColor(R.color.white);
            Log.i("MainActivity","Tinted white");
        }

        DrawableCompat.setTint(icFavorite, color);

    }


}
