package com.app.mymooviapp;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.mymooviapp.fragments.MovieThumbsFragment;


public class MainActivity extends AppCompatActivity {

    private String[] typeQueries;

    private Toolbar mToolbar;

    private Spinner mQuerySpinner;

    private Fragment fragment;

    private Bundle fragARgs;

    public  static final String QUERY_TYPE_KEY="isPopular";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        fragARgs = new Bundle();

        fragARgs.putBoolean(QUERY_TYPE_KEY,true);

        fragment = new MovieThumbsFragment();

        fragment.setArguments(fragARgs);



        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
    }


    public void setUpToolbar()
    {
            mToolbar = (Toolbar)findViewById(R.id.toolbar);

            typeQueries = getResources().getStringArray(R.array.movie_query_types);

            mQuerySpinner = (Spinner)findViewById(R.id.query_type);

            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,typeQueries);

            mQuerySpinner.setAdapter(adapter);

            mQuerySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String item = adapter.getItem(i);

                    boolean isPopular = true;

                    if(i==1)
                    {
                        isPopular = false;
                    }

                    if(fragARgs!=null)
                    {
                        fragARgs.putBoolean(QUERY_TYPE_KEY,isPopular);
                    }

                    ((MovieThumbsFragment)fragment).restartLoader(fragARgs);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
