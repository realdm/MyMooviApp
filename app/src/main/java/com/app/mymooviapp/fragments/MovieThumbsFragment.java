package com.app.mymooviapp.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mymooviapp.MainActivity;
import com.app.mymooviapp.R;
import com.app.mymooviapp.adapters.MovieGridAdapter;
import com.app.mymooviapp.loaders.MovieAsyncLoader;
import com.app.mymooviapp.models.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 6/11/2015.
 */
public class MovieThumbsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private GridView mMoviesGrid;

    private View mEmptyScreen;

    private View mProgressBar;

    private ImageView emptyScreenIcon;

    private TextView emptyScreenText;

    private TextView mTryAgainButton;

    private MovieGridAdapter mMovieGridAdapter;

    private Bundle bundle;

    private boolean firstLoad = true;

    private String queryType;

    private List<Movie> movies = new ArrayList<Movie>();

    private OnMovieSelected mMovieSelectedCallbacks;

    private static final int LOADER_ID=1;

    private final String MOVIES_KEY="movies";

    public static MovieThumbsFragment newInstance()
    {
        return new MovieThumbsFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mMovieSelectedCallbacks = (OnMovieSelected)activity;
        }
        catch(Exception e){

        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovieGridAdapter = new MovieGridAdapter(getActivity());

        if(savedInstanceState!=null){
            bundle = savedInstanceState;

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(bundle!=null){
            restartLoader(bundle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies,container,false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
         outState.putParcelableArrayList(MOVIES_KEY, (ArrayList<Movie>) movies);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mMoviesGrid = (GridView)view.findViewById(R.id.movies_grid);

        mMoviesGrid.setAdapter(mMovieGridAdapter);

        mEmptyScreen = view.findViewById(R.id.error_screen);

        emptyScreenIcon = (ImageView)mEmptyScreen.findViewById(R.id.error_icon);

        emptyScreenText = (TextView)mEmptyScreen.findViewById(R.id.error_desc);

        mTryAgainButton = (TextView)mEmptyScreen.findViewById(R.id.try_again);

        mTryAgainButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                restartLoader(bundle);
            }
        });

        mProgressBar = view.findViewById(R.id.loading_spinner);

        mMoviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Movie movie = (Movie) mMovieGridAdapter.getItem(i);

                mMovieSelectedCallbacks.onMovieSelected(i,movie);

            }
        });

        if(savedInstanceState!=null) {
            mMovieGridAdapter.swapData(movies);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {

        queryType = args.getString(MainActivity.QUERY_TYPE_KEY);

        MovieAsyncLoader loader = new MovieAsyncLoader(getActivity(),queryType);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {

        String emptyText = "";
        if(data!=null) {

            if(data.size()>0) {

                mMovieGridAdapter.swapData(data);

                this.movies = data;

                crossfade(mMoviesGrid,mProgressBar);
            }
            else {

                if(queryType == MainActivity.QUERY_TYPE_FAVORITED){

                    emptyText = getResources().getString(R.string.error_empty_favorited);

                    customizeEmptyScreen(R.drawable.no_movie, emptyText, false);
                }
                else{
                    emptyText = getResources().getString(R.string.error_no_connection);

                    customizeEmptyScreen(R.drawable.cloud_error, emptyText, true);
                }
                crossfade(mEmptyScreen, mProgressBar);
            }
        }
        else {
            crossfade(mEmptyScreen,mProgressBar);
        }

        firstLoad = false;
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    public void customizeEmptyScreen(int showingIcon, String text, boolean canRetry){

        emptyScreenIcon.setImageDrawable(getResources().getDrawable(showingIcon));

        emptyScreenText.setText(text);

        if(canRetry == false){
            mTryAgainButton.setVisibility(View.GONE);
        }
        else {
            mTryAgainButton.setVisibility(View.VISIBLE);
        }
    }

    public void restartLoader(Bundle bundle)
    {

        movies = bundle.getParcelableArrayList(MOVIES_KEY);

        if(movies!=null){
            mMovieGridAdapter.swapData(movies);
        }
        else{

            this.bundle = bundle;

            if(firstLoad == false) {

                if(mEmptyScreen.getVisibility() == View.VISIBLE){

                    crossfade(mProgressBar,mEmptyScreen);
                }
                else{

                    crossfade(mProgressBar,mMoviesGrid);
                }

                getLoaderManager().restartLoader(LOADER_ID, bundle, this).forceLoad();
            }
            else {

                getLoaderManager().initLoader(LOADER_ID, bundle, this).forceLoad();

                firstLoad = false;

            }
        }


    }

    public interface OnMovieSelected{

        public void onMovieSelected(int moviePosition, Movie movie);
    }

    private void crossfade(final View mContentView,final View mLoadingView) {

        mContentView.setVisibility(View.VISIBLE);

        mLoadingView.setVisibility(View.GONE);

    }
}
