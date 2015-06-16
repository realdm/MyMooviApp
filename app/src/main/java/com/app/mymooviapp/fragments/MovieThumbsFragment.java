package com.app.mymooviapp.fragments;

import android.content.Intent;
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
import android.widget.Toast;

import com.app.mymooviapp.MainActivity;
import com.app.mymooviapp.MovieDetails;
import com.app.mymooviapp.R;
import com.app.mymooviapp.adapters.MovieGridAdapter;
import com.app.mymooviapp.loaders.MovieAsyncLoader;
import com.app.mymooviapp.models.Movie;

import java.util.List;

/**
 * Created by Mac on 6/11/2015.
 */
public class MovieThumbsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private GridView mMoviesGrid;

    private MovieGridAdapter mMovieGridAdapter;

    private Bundle bundle;

    private boolean firstLoad = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovieGridAdapter = new MovieGridAdapter(getActivity());

        bundle = getArguments();

        getLoaderManager().initLoader(1,bundle,this).forceLoad();




    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_movies,container,false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mMoviesGrid = (GridView)view.findViewById(R.id.movies_grid);

        mMoviesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Movie movie = (Movie)mMovieGridAdapter.getItem(i);

                Intent intent = new Intent(getActivity(), MovieDetails.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable("movie",movie);

                intent.putExtras(bundle);

                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {


        boolean isPopular = args.getBoolean(MainActivity.QUERY_TYPE_KEY);


        MovieAsyncLoader loader = new MovieAsyncLoader(getActivity(),isPopular);

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {

        if(data!=null)
        {
            if(data.size()>0)
            {
                mMovieGridAdapter.swapData(data);
                mMoviesGrid.setAdapter(mMovieGridAdapter);
                firstLoad = false;
            }
            else
            {
                Log.i("MovieThumbFragment", "Empty list");
            }
        }
        else
        {
            Log.i("MovieThumbFragment", "null list");
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }


    public void restartLoader(Bundle bundle)
    {
        if(firstLoad==false)
        {
            getLoaderManager().restartLoader(1,bundle,this).forceLoad();
            mMovieGridAdapter.cleanData();
        }

    }
}
