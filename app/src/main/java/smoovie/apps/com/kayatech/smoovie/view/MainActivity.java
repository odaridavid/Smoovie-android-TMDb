package smoovie.apps.com.kayatech.smoovie.view;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
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
import smoovie.apps.com.kayatech.smoovie.room_database.AppExecutors;
import smoovie.apps.com.kayatech.smoovie.room_database.MovieDatabase;
import smoovie.apps.com.kayatech.smoovie.viewmodel.FavouritesViewModel;
import smoovie.apps.com.kayatech.smoovie.viewmodel.IMovieListCallback;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, FavouritesAdapter.IFavMovieClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isFetchingMovies = false;
    private int currentPage = 1;
    private TMDBMovies movieList;
    private static final String KEY_SORT = "sort_instance";
    private static String sortBy;
    private MoviesAdapter mMoviesAdapter;
    private GridLayoutManager gridLayoutManager;
    private IMovieClickHandler IMovieClickHandler;
    private MovieDatabase movieDatabase;
    FavouritesViewModel favouritesViewModel;
    private FavouritesAdapter favouritesAdapter;
    @BindView(R.id.iv_favcon_view_default)
    ImageView mFavconImage;

    @BindView(R.id.tv_fav_default)
    TextView mFavDefaultText;

    @BindView(R.id.pb_getmovie_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.rv_movies)
    RecyclerView mMoviesRecyclerView;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageTextView;

    @BindView(R.id.action_toolbar_main)
    Toolbar toolbarMainPage;

    @BindView(R.id.rv_favourite_movies)
    RecyclerView mFavouriteMoviesRecyclerView;

    final static String mFavouritesOption = "favourite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMainPage);
        movieList = TMDBMovies.getInstance();
        sortBy = TMDBMovies.POPULAR;
        setupViewModel();
        //Get Db instance
        movieDatabase = MovieDatabase.getMovieDatabaseInstance(getApplicationContext());
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_SORT)) {
                sortBy = savedInstanceState.getString(KEY_SORT);//Setup Recycler view depending on sort option
                if (isOnline()) {
                    if (sortBy != null) {

                        setUpRecyclerViewInstance(sortBy);
                        //Set Title for toolbar on rotate
                        setTitleToolbar(sortBy);
                    }
                } else {
                    mErrorMessageTextView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        } else {
            if (isOnline()) {
                if (!sortBy.equals(mFavouritesOption)) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    setUpMovieQueryRecyclerView();
                    getMovies(currentPage);
                    setupClickHandling();
                    mErrorMessageTextView.setVisibility(View.GONE);
                } else if (sortBy.equals(mFavouritesOption)) {
                    setupFavouritesRecyclerView();
                    setupFavouritesObserver();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    setUpMovieQueryRecyclerView();
                    getMovies(currentPage);
                    setupClickHandling();
                    mErrorMessageTextView.setVisibility(View.GONE);
                }
            } else {
                mErrorMessageTextView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        }
        //Setup Shared Preference
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        TMDBMovies.LANGUAGE = sp.getString(getString(R.string.pref_language_key), "");
        setUpLocale(TMDBMovies.LANGUAGE);
        sp.registerOnSharedPreferenceChangeListener(this);
    }

    //Locale
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

    //On Save Instance state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SORT, sortBy);
    }

    //Setup for Movies Recycler View
    void setUpMovieQueryRecyclerView() {
        //Disable Favourite Items
        mFavouriteMoviesRecyclerView.setVisibility(View.GONE);
        mFavDefaultText.setVisibility(View.GONE);
        mFavconImage.setVisibility(View.GONE);
        //Enable Recycler View Visibility
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
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

    //Setup for Favourite movie Recycler view
    void setupFavouritesRecyclerView() {
        //Disable Movie Item Views
        mMoviesRecyclerView.setVisibility(View.GONE);
        mErrorMessageTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        //Set Recycler Visiibility
        mFavouriteMoviesRecyclerView.setVisibility(View.VISIBLE);
        //Setup
        mFavouriteMoviesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerReview = new LinearLayoutManager(this);
        mFavouriteMoviesRecyclerView.setLayoutManager(linearLayoutManagerReview);
        favouritesAdapter = new FavouritesAdapter(this, this);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mFavouriteMoviesRecyclerView.addItemDecoration(decoration);
        mFavouriteMoviesRecyclerView.setAdapter(favouritesAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Movie> movies = favouritesAdapter.getMovies();
                        movieDatabase.movieDAO().removeMovieFromFavourites(movies.get(position));
                    }
                });
            }
        }).attachToRecyclerView(mFavouriteMoviesRecyclerView);
    }

    //For on save insantace state
    private void setUpRecyclerViewInstance(String sortBy) {
        if (!sortBy.equals(mFavouritesOption)) {
            mProgressBar.setVisibility(View.VISIBLE);
            setUpMovieQueryRecyclerView();
            getMovies(currentPage);
            setupClickHandling();
            mErrorMessageTextView.setVisibility(View.GONE);
        } else {
            setupFavouritesRecyclerView();
            setupFavouritesObserver();
        }
    }


    private void setupClickHandling() {
        //checks if state is checking or not
        IMovieClickHandler = new IMovieClickHandler() {
            @Override
            public void onClick(Movie movie) {
                Intent openDetailsActivity = new Intent(MainActivity.this, DetailActivity.class);
                openDetailsActivity.putExtra(DetailActivity.MOVIE_ID, Parcels.wrap(movie.getMovieId()));
                startActivity(openDetailsActivity);
            }
        };
    }

    void getMovies(int currentPage) {
        if (sortBy != null) {
            isFetchingMovies = true;
            movieList.getMovies(currentPage, sortBy, new IMovieListCallback() {
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
                    isFetchingMovies = false;
                    mErrorMessageTextView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure() {
                    Log.d(TAG, getString(R.string.error_network_message));
                }
            });
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
                        setupSortedData(sortBy);
                        return true;
                    case R.id.action_sort_high_rating:
                        sortBy = TMDBMovies.TOP_RATED;
                        setupSortedData(sortBy);
                        return true;
                    case R.id.action_sort_upcoming:
                        sortBy = TMDBMovies.UPCOMING;
                        setupSortedData(sortBy);
                        return true;
                    case R.id.action_sort_favourites:
                        sortBy = mFavouritesOption;
                        setTitleToolbar(sortBy);
                        setUpRecyclerViewInstance(sortBy);
                        setupFavouritesObserver();
                        return true;
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

    @Override
    public void onItemClickListener(int movieId) {
        //Open Movie In Detail Activity
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_ID_DB, movieId);
        Log.d(TAG, "onItemClickListener: " + movieId);
        startActivity(intent);
    }

    private void setupViewModel() {
        favouritesViewModel
                = ViewModelProviders
                .of(this)
                .get(FavouritesViewModel.class);
    }


    private void setupFavouritesObserver() {
        favouritesViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                favouritesAdapter.setmMovieReviewsList(movies);
                if (!(movies.size() > 0)) {
                    //When List Is Empty
                    final Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
                    mFavouriteMoviesRecyclerView.setVisibility(View.GONE);
                    mFavconImage.setVisibility(View.VISIBLE);
                    mFavDefaultText.setVisibility(View.VISIBLE);
                    mFavDefaultText.setTypeface(custom_font);
                }

            }
        });
    }

    private void setTitleToolbar(String sortBy) {
        switch (sortBy) {
            case TMDBMovies.TOP_RATED:
                setTitle(getString(R.string.action_sort_high_ratings));
                break;
            case TMDBMovies.UPCOMING:
                setTitle(getString(R.string.action_sort_upcoming));
                break;
            case TMDBMovies.POPULAR:
                setTitle(getString(R.string.action_sort_most_popular));
                break;
            case mFavouritesOption:
                setTitle(getString(R.string.action_sort_favourites));
                break;
        }
    }

    void setupSortedData(String sortBy) {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            setTitleToolbar(sortBy);
            setUpRecyclerViewInstance(sortBy);
            getMovies(currentPage);
            setupClickHandling();
        } else {
            setTitleToolbar(sortBy);
            mFavconImage.setVisibility(View.GONE);
            mFavDefaultText.setVisibility(View.GONE);
            mErrorMessageTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

}
