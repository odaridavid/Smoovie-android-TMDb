package smoovie.apps.com.kayatech.smoovie.view;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.network.TMDBMovies;
import smoovie.apps.com.kayatech.smoovie.viewmodel.IMovieListCallback;

;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isFetchingMovies = false;
    private int currentPage = 1;
    private TMDBMovies movieList;
    private String sortBy = TMDBMovies.POPULAR;
    private MoviesAdapter mMoviesAdapter;
    private GridLayoutManager gridLayoutManager;
    private IMovieClickHandler IMovieClickHandler;


    @BindView(R.id.pb_getmovie_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.rv_movies)
    RecyclerView mMoviesRecyclerView;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageTextView;

    @BindView(R.id.action_toolbar_main)
    Toolbar toolbarMainPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMainPage);


        movieList = TMDBMovies.getInstance();

        setUpRecyclerView();


        //Start on Page 1 and checks for network connectivity
        if (isOnline()) {
            getMovies(currentPage);
            mErrorMessageTextView.setVisibility(View.INVISIBLE);

        } else {
            mErrorMessageTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);

        }
        //Set Pagination , On Scroll Continues to load Items
//        mMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                //on ui and cached
//                int totalItemCount = gridLayoutManager.getItemCount();
//                //in cache
//                int visibleItemCount = gridLayoutManager.getChildCount();
//                //on ui
//                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
//
//                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
//                    mProgressBar.setVisibility(View.VISIBLE);
//                    //if reached the end fetch more movies move to next page
//                    if (!isFetchingMovies) {
//                        mProgressBar.setVisibility(View.GONE);
//                        getMovies(currentPage + 1);
//                    }
//                }
//            }
//        });

        //Setup Shared Preference
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        TMDBMovies.LANGUAGE = sp.getString(getString(R.string.pref_language_key), "");
        setUpLocale(TMDBMovies.LANGUAGE);
        sp.registerOnSharedPreferenceChangeListener(this);


    }

    private void setUpLocale(String language) {
        if (language.equals(getString(R.string.pref_language_val_chinese))) {
            Locale locale = new Locale("zh");
            Configuration config = getBaseContext().getResources().getConfiguration();
            Locale.setDefault(locale);
            config.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        if (language.equals(getString(R.string.pref_language_val_french))) {
            Locale locale = new Locale("fr");
            Configuration config = getBaseContext().getResources().getConfiguration();
            Locale.setDefault(locale);
            config.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        if (language.equals(getString(R.string.pref_language_val_german))) {
            Locale locale = new Locale("de");
            Locale.setDefault(locale);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        if (language.equals(getString(R.string.pref_language_val_english))) {
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        Log.d(TAG, "Language is :" + TMDBMovies.LANGUAGE);
    }

    private void setUpRecyclerView() {
        //Reference
        mMoviesRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        mMoviesRecyclerView.setItemViewCacheSize(20);
        mMoviesRecyclerView.setDrawingCacheEnabled(true);
        mMoviesRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        //Gets Recycler width and card width and arranges elements in layout as per screen size
        mMoviesRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mMoviesRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int viewWidth = mMoviesRecyclerView.getMeasuredWidth();
                float cardViewWidth = getApplication().getResources().getDimension(R.dimen.size_layout);
                int newSpanCunt = (int) Math.floor(viewWidth / cardViewWidth);
                gridLayoutManager.setSpanCount(newSpanCunt);
                gridLayoutManager.requestLayout();
            }
        });
    }

    /**
     * @param page sets current page
     */
    private void getMovies(int page) {
        mErrorMessageTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        //checks if state is checking or not
        isFetchingMovies = true;
        IMovieClickHandler = new IMovieClickHandler() {
            @Override
            public void onClick(Movie movie) {
                Intent openDetailsActivity = new Intent(MainActivity.this, DetailActivity.class);
                openDetailsActivity.putExtra(DetailActivity.MOVIE_ID, Parcels.wrap(movie.getMovieId()));
                startActivity(openDetailsActivity);
            }
        };


        movieList.getMovies(page, sortBy, new IMovieListCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {

                if (mMoviesAdapter == null) {
                    mMoviesAdapter = new MoviesAdapter(movies, IMovieClickHandler);
                    mMoviesRecyclerView.setAdapter(mMoviesAdapter);
                } else {
                    if (page == 1) {
                        mMoviesAdapter.clearMovies();
                    }
                    mProgressBar.setVisibility(View.GONE);
                    //appends movie results to list and updates recycler view
                    mMoviesAdapter.setmMovieList(movies);
                }
                currentPage = page;
                isFetchingMovies = false;

                setTitleBar();
            }

            @Override
            public void onFailure() {
                Log.d(TAG, getString(R.string.error_network_message));
            }
        });
    }

    //Set Title Bar on action Bar depending on get request from Singleton Class
    private void setTitleBar() {
        switch (sortBy) {
            case TMDBMovies.POPULAR:
                setTitle(getString(R.string.action_sort_most_popular));
                break;
            case TMDBMovies.TOP_RATED:
                setTitle(getString(R.string.action_sort_high_ratings));
                break;
            case TMDBMovies.UPCOMING:
                setTitle(getString(R.string.action_sort_upcoming));
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_sort) {
            showSortPopUpMenu();
            return true;
        } else if (itemId == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);


    }

    private void showSortPopUpMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.action_sort));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //* Every time we sort, we go back to page 1
                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.action_sort_most_popular:
                        sortBy = TMDBMovies.POPULAR;
                        getMovies(currentPage);
                        return true;
                    case R.id.action_sort_high_rating:
                        sortBy = TMDBMovies.TOP_RATED;
                        getMovies(currentPage);
                        return true;
                    case R.id.action_sort_upcoming:
                        sortBy = TMDBMovies.UPCOMING;
                        getMovies(currentPage);
                        return true;
                    case R.id.action_sort_favourites:
                        Intent openFavouritesActivity = new Intent(MainActivity.this, FavouritesActivity.class);
                        startActivity(openFavouritesActivity);
                    default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.main_menu_items_sort);
        sortMenu.show();
    }

    /**
     * Checks Network State returns true if connected
     *
     * @return Network State
     */
    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnline()) {
            getMovies(currentPage);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_language_key))) {
            TMDBMovies.LANGUAGE = sharedPreferences.getString(key, getResources().getString(R.string.pref_language_val_english));
            setUpLocale(TMDBMovies.LANGUAGE);
            Log.d(TAG, "Language is :" + TMDBMovies.LANGUAGE);
        }
    }
}
