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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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
import smoovie.apps.com.kayatech.smoovie.db.MovieDatabase;
import smoovie.apps.com.kayatech.smoovie.viewmodel.FavouritesViewModel;
import smoovie.apps.com.kayatech.smoovie.viewmodel.IMovieListCallback;

import static smoovie.apps.com.kayatech.smoovie.util.Constants.MOVIE_ID;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.MOVIE_ID_ROOM_DB;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, FavouritesAdapter.IFavMovieClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isFetchingMovies = false;
    private int currentPage = 1;
    private TMDBMovies movieList;
    private static final String KEY_SORT = "sort_instance";
    private static final String KEY_LANGUAGE_SORT = "language";
    private static String sortBy;
    private MoviesAdapter mMoviesAdapter;
    private GridLayoutManager gridLayoutManager;
    private static IMovieClickHandler IMovieClickHandler;
    private MovieDatabase movieDatabase;
    private FavouritesViewModel favouritesViewModel;
    public static List<Movie> moviesPersistance;
    private FavouritesAdapter favouritesAdapter;
    private final String KEY_MOVIES_PARCELABLE = "movie_list";

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

    final static String mFavouritesOption = "favourite";
    private static List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMainPage);
        movieList = TMDBMovies.getInstance();
        sortBy = TMDBMovies.POPULAR;
        //Setup recycler view
        setupRecyclerView();
        //Get Db instance
        movieDatabase = MovieDatabase.getMovieDatabaseInstance(getApplicationContext());
        //if saved instance state has values
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_SORT)) {
                if (savedInstanceState.containsKey(KEY_LANGUAGE_SORT)) {
                    if (savedInstanceState.containsKey(KEY_MOVIES_PARCELABLE)) {
                        //get saved movie list as parcel
                        movies = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIES_PARCELABLE));
                        Log.d(TAG, "movie exists::" + movies);
                        //get language
                        String lang = savedInstanceState.getString(KEY_LANGUAGE_SORT);
                        Log.d(TAG, "movie language::" + lang);
                        //get current sort option
                        sortBy = savedInstanceState.getString(KEY_SORT);
                        Log.d(TAG, "movie sort option::" + sortBy);
                        //Setup Recycler view depending on sort option
                        if (isOnline()) {
                            if (sortBy != null) {
                                if (movies != null && !sortBy.equals(mFavouritesOption)) {
                                    setupRecyclerView();
                                    setUpMoviesAdapter(movies, 1);
                                } else {
                                    setUpRecyclerViewInstance(sortBy);
                                }
                                //Set Title for toolbar on rotate
                                setUpLocale(lang);
                                setTitleToolbar(sortBy);
                            }
                        } else {
                            mErrorMessageTextView.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } else {
            if (isOnline()) {
                if (!sortBy.equals(mFavouritesOption)) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    setupSortedMovie();
                    mErrorMessageTextView.setVisibility(View.GONE);
                } else if (sortBy.equals(mFavouritesOption)) {
                    setupFavourites();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    setupSortedMovie();
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


    //On Save Instance state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //s
        outState.putString(KEY_SORT, sortBy);
        //save current language
        outState.putString(KEY_LANGUAGE_SORT, TMDBMovies.LANGUAGE);
        //Save movie list
        outState.putParcelable(KEY_MOVIES_PARCELABLE, Parcels.wrap(moviesPersistance));
    }


    void setFavouriteViewsVisibility() {
        //Disable Favourite Items
        mFavDefaultText.setVisibility(View.GONE);
        mFavconImage.setVisibility(View.GONE);
    }

    //Setup for Favourite movie Recycler view
    void setupFavouritesRecyclerViewAdapter() {
        //Disable Movie Item Views
        mErrorMessageTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        //remove movies adapter
        mMoviesAdapter = null;
        //Set Adapter
        favouritesAdapter = new FavouritesAdapter(this, this);
        mMoviesRecyclerView.setAdapter(favouritesAdapter);
    }

    //For on save insantace state
    private void setUpRecyclerViewInstance(String sortBy) {
        if (!sortBy.equals(mFavouritesOption)) {
            mProgressBar.setVisibility(View.VISIBLE);
            setupSortedMovie();
            mErrorMessageTextView.setVisibility(View.GONE);
        } else {
           setupFavourites();
        }
    }


    private void setupClickHandling() {
        //checks if state is checking or not
        IMovieClickHandler = new IMovieClickHandler() {
            @Override
            public void onClick(Movie movie) {
                Intent openDetailsActivity = new Intent(MainActivity.this, DetailActivity.class);
                openDetailsActivity.putExtra(MOVIE_ID, Parcels.wrap(movie.getMovieId()));
                startActivity(openDetailsActivity);
            }
        };
    }

    void getMovies(int currentPage) {
        isFetchingMovies = true;
        movieList.getMovies(currentPage, sortBy, new IMovieListCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                setUpMoviesAdapter(movies, page);
                moviesPersistance = movies;
            }

            @Override
            public void onFailure() {
                Log.d(TAG, getString(R.string.error_network_message));
            }
        });

    }

    void setUpMoviesAdapter(List<Movie> movies, int page) {
        if (mMoviesAdapter == null) {
            Log.d(TAG, "Movies Adpter::set::" + mMoviesAdapter);
            mMoviesAdapter = new MoviesAdapter(movies, IMovieClickHandler);
            mMoviesRecyclerView.setAdapter(mMoviesAdapter);

        } else {
            Log.d(TAG, "Movies Adpter::not set::" + mMoviesAdapter);
            if (page == 1) {
                mMoviesAdapter.clearMovies();
            }
            mProgressBar.setVisibility(View.GONE);
            //appends movie results to list and updates recycler view
            mMoviesAdapter.setmMovieList(movies);
            mMoviesRecyclerView.setAdapter(mMoviesAdapter);
        }
        isFetchingMovies = false;
        mErrorMessageTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
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
                        setupViewModel();
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
            setupSortedMovie();
            Log.d(TAG, "Language is :" + TMDBMovies.LANGUAGE);
        }
    }

    /**
     * @param movieId movie id from favourites database to be opened in detail activity
     */
    @Override
    public void onItemClickListener(int movieId) {
        //Open Movie In Detail Activity
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(MOVIE_ID_ROOM_DB, movieId);
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
                favouritesAdapter.setFavouriteMoviesList(movies);
                if (!(movies.size() > 0)) {
                    //To Prevent Favourite views overlapping with other sort options
                    if (sortBy.equals(mFavouritesOption)) {
                        //When List Is Empty
                        final Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
                        mFavconImage.setVisibility(View.VISIBLE);
                        mFavDefaultText.setVisibility(View.VISIBLE);
                        mFavDefaultText.setTypeface(custom_font);
                    }
                }
            }
        });
    }

    /**
     * @param sortBy sort option to set title toolbar text
     */
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
            setupSortedMovie();
        } else {
            setTitleToolbar(sortBy);
            mFavconImage.setVisibility(View.GONE);
            mFavDefaultText.setVisibility(View.GONE);
            mErrorMessageTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Methods to setup Recycler view and dynamic span count
     */
    void setupRecyclerView() {
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
        mMoviesRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), calculateNoOfColumns(this));
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
     * @param context Recycler view instance
     * @return number of columns for span count
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scaleFactor = 200;
        int noOfColumns = (int) (dpWidth / scaleFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
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

    private void setupSortedMovie() {
        setTitleToolbar(sortBy);
        setFavouriteViewsVisibility();
        getMovies(currentPage);
        setupClickHandling();
    }

    private void setupFavourites() {
        setupViewModel();
        setupFavouritesRecyclerViewAdapter();
        setupFavouritesObserver();
    }


}
