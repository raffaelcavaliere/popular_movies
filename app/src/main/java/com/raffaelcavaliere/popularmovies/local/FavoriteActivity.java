package com.raffaelcavaliere.popularmovies.local;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.raffaelcavaliere.popularmovies.R;
import com.raffaelcavaliere.popularmovies.SettingsActivity;
import com.raffaelcavaliere.popularmovies.data.MovieDbItem;
import com.raffaelcavaliere.popularmovies.remote.FetchMovieDetailFragment;

public class FavoriteActivity extends AppCompatActivity
        implements FavoriteMovieGridFragment.OnFragmentInteractionListener {

    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        if (findViewById(R.id.moviedetail_container) != null) {
            isTwoPane = true;
        }
        else {
            isTwoPane = false;
        }
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
            Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settings);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onFragmentInteraction(MovieDbItem item) {
        if (isTwoPane) {
            FavoriteMovieDetailFragment fragment = new FavoriteMovieDetailFragment();
            Bundle arguments = new Bundle();
            arguments.putString("overview", item.overview);
            arguments.putString("posterPath", item.posterPath);
            arguments.putString("backdropPath", item.backdropPath);
            arguments.putString("releaseDate", item.releaseDate);
            arguments.putString("title", item.title);
            arguments.putLong("id", item.id);
            arguments.putDouble("popularity", item.popularity);
            arguments.putDouble("voteAverage", item.voteAverage);
            arguments.putLong("voteCount", item.voteCount);
            arguments.putString("format", item.format);
            arguments.putBoolean("isFavorite", item.isFavorite);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.moviedetail_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
