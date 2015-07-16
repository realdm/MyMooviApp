package com.app.mymooviapp.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.mymooviapp.R;
import com.app.mymooviapp.adapters.CastListAdapter;
import com.app.mymooviapp.adapters.SimpleTrailerAdapter;
import com.app.mymooviapp.handlers.MovieHandler;
import com.app.mymooviapp.loaders.MovieDetailsLoader;
import com.app.mymooviapp.models.Movie;

/**
 * Created by Mac on 6/18/2015.
 */
public class MovieDetailsFrag extends Fragment implements LoaderManager.LoaderCallbacks<Movie>{

    //UI Fields
    private TextView movieDirector;

    private TextView movieDuration;

    private TextView movieReleaseDate;

    private TextView movieGenres;

    private TextView movieSynopsis;

    private ProgressBar mProgressBar;

    private View mDetailsLayout;

    private View trailers;

    private ListView mTrailersList;

    private View cast;

    private ListView mCastList;

    private View emptyLayout;

    private ImageView mEmptyIcon;

    private TextView mErrorMessage;

    private TextView mErrorDescription;

    private TextView mTryAgain;

    //Other variables
    private boolean isFirstLoad = true;

    private boolean loadedSuccessfully = false;

    private MovieHandler movieHandler;

    private Movie movie;

    private Bundle bundle;

    private OnMovieFavorited mMovieFavoritedCallback;

    SimpleTrailerAdapter mtrailerAdapter;

    CastListAdapter mCastListAdapter;

    private final int LOADER_ID=2;

    private final String MOVIE_KEY="movie";

    public static MovieDetailsFrag newInstance() {
        return new MovieDetailsFrag();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mMovieFavoritedCallback = (OnMovieFavorited)activity;
        }
        catch(Exception e){}

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            bundle = getArguments();

            movieHandler = new MovieHandler(getActivity().getApplicationContext());

            mtrailerAdapter = new SimpleTrailerAdapter(getActivity().getApplicationContext());

            mCastListAdapter = new CastListAdapter(getActivity().getApplicationContext());

            if(bundle!=null){

                getLoaderManager().initLoader(LOADER_ID,bundle,this).forceLoad();
            }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_details_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState!=null){
                bundle = savedInstanceState;
        }
        initFields(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_KEY, movie);
    }

    public void initFields(View view)
    {
        //Init empty layout
        emptyLayout = view.findViewById(R.id.error_screen);

        mEmptyIcon = (ImageView)emptyLayout.findViewById(R.id.error_icon);

        mErrorMessage = (TextView)emptyLayout.findViewById(R.id.error_message);

        mErrorDescription =(TextView) emptyLayout.findViewById(R.id.error_desc);

        mTryAgain = (TextView)emptyLayout.findViewById(R.id.try_again);

        mTryAgain.setVisibility(View.INVISIBLE);

        //Init the Details Layout

        mDetailsLayout = view.findViewById(R.id.layout_details);

        movieDirector = (TextView)mDetailsLayout.findViewById(R.id.movie_director);

        movieDuration = (TextView)mDetailsLayout.findViewById(R.id.movie_duration);

        movieReleaseDate = (TextView)mDetailsLayout.findViewById(R.id.movie_releaseDate);

        movieGenres = (TextView)mDetailsLayout.findViewById(R.id.movie_genres);

        movieSynopsis = (TextView)mDetailsLayout.findViewById(R.id.movie_synopsis);

        //progress bar
        mProgressBar = (ProgressBar)view.findViewById(R.id.loading_spinner);

        //init trailer layout
        trailers = mDetailsLayout.findViewById(R.id.trailers);

        mTrailersList = (ListView)trailers.findViewById(R.id.list);

        mTrailersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String mYoutubeId = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + mYoutubeId));
                intent.putExtra("VIDEO_ID", mYoutubeId);
                startActivity(intent);
            }
        });

        mTrailersList.setEmptyView(trailers.findViewById(android.R.id.empty));

        //init cast layout
        cast = mDetailsLayout.findViewById(R.id.cast);

        mCastList = (ListView)cast.findViewById(R.id.list);

        if(bundle!=null){
            movie = bundle.getParcelable(MOVIE_KEY);
            if(movie!=null){
                fillData(movie);
                crossfade(mDetailsLayout,mProgressBar);
            }
            else{
                customizeEmptyScreen(R.drawable.no_movie,"Please Select a Movie on your Left");
                crossfade(emptyLayout,mProgressBar);
            }

        }else{

            customizeEmptyScreen(R.drawable.no_movie,"Please Select a Movie on your Left");
            crossfade(emptyLayout,mProgressBar);
        }

    }

    public void customizeEmptyScreen(int icon, String text)
    {
        mEmptyIcon.setImageDrawable(getResources().getDrawable(icon));
        mErrorMessage.setText("");
        mErrorDescription.setText(text);
    }

    public void fillData(Movie movie) {

        movieDirector.setText(movie.getDirector());
        movieDuration.setText(movie.getRuntime() + " mins");
        movieReleaseDate.setText(movie.getReleaseDate());
        movieGenres.setText(movie.getGenres());
        movieSynopsis.setText(movie.getOverview());


        mtrailerAdapter.setData(movie.getTrailers());
        mTrailersList.setAdapter(mtrailerAdapter);


        mCastListAdapter.setData(movie.getCast());
        mCastList.setAdapter(mCastListAdapter);

    }


    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {

        Movie movie = (Movie)args.getParcelable(MOVIE_KEY);

        MovieDetailsLoader loader = new MovieDetailsLoader(getActivity(),movie);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {

        if(data!=null) {


            fillData(data);

            movie = data;

            loadedSuccessfully = true;


            mMovieFavoritedCallback.onMovieFavorite(movie.isFavorited());

            crossfade(mDetailsLayout,mProgressBar);
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }


    public void loadMovieDetails(Bundle bundle)
    {

        if(isFirstLoad){

            getLoaderManager().initLoader(2,bundle,this).forceLoad();

            isFirstLoad = !isFirstLoad;

            if(emptyLayout.getVisibility() == View.VISIBLE) {

                crossfade(mProgressBar,emptyLayout);
            }
        }
        else
        {
            getLoaderManager().restartLoader(2,bundle,this).forceLoad();
            crossfade(mProgressBar,mDetailsLayout);
        }

    }


    public void favoriteMovie()
    {
        if(loadedSuccessfully){

            boolean favorite = movie.isFavorited();

            if(!favorite){
                movieHandler.insertOrUpdate(movie);

            }
            else {

                if(!movieHandler.deleteMovie(movie)){
                    favorite = !favorite;
                }
            }
            mMovieFavoritedCallback.onMovieFavorite(!favorite);
            movie.setFavorited(!favorite);
        }

    }

    public void shareMovieTrailer(){
        if(loadedSuccessfully){
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,"https://www.youtube.com/watch?v="+movie.getTrailers().get(0).getSource());
            getActivity().startActivity(sendIntent);
        }
    }

    public interface OnMovieFavorited{
        public void onMovieFavorite(boolean isFavorite);
    }

    private void crossfade(final View mContentView,final View mLoadingView) {

        mContentView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.INVISIBLE);

    }

}
