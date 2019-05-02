package smoovie.apps.com.kayatech.smoovie.ui.main;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Category;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.model.MovieNetworkLite;
import smoovie.apps.com.kayatech.smoovie.ui.detail.MovieDetailActivity;
import smoovie.apps.com.kayatech.smoovie.ui.main.adapters.MoviesAdapter;
import smoovie.apps.com.kayatech.smoovie.ui.main.async.MovieListAsync;
import smoovie.apps.com.kayatech.smoovie.ui.main.callbacks.FavouriteMoviesCallback;
import smoovie.apps.com.kayatech.smoovie.ui.main.callbacks.MovieListCallBack;
import smoovie.apps.com.kayatech.smoovie.ui.main.menu.CategoryMenuListener;
import smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel.MainViewModel;
import smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel.MainViewModelFactory;
import smoovie.apps.com.kayatech.smoovie.ui.settings.SettingsActivity;
import smoovie.apps.com.kayatech.smoovie.util.InjectorUtils;
import smoovie.apps.com.kayatech.smoovie.util.NetworkUtils;
import smoovie.apps.com.kayatech.smoovie.util.SmooviePosterImageView;
import smoovie.apps.com.kayatech.smoovie.util.ViewUtils;

import static smoovie.apps.com.kayatech.smoovie.util.Constants.KEY_MOVIE_ID;
import static smoovie.apps.com.kayatech.smoovie.util.Constants.KEY_MOVIE_POSTER;

public class MainActivity extends AppCompatActivity implements MovieListCallBack, FavouriteMoviesCallback, MoviesAdapter.IMovieClickHandler {

//    TODO 1.(Main Activity) - Shared Preferences Sync with language Selection
//    TODO 2.(Main Activity) - Endless Scrolling,Pagination
//    TODO 3.(Main Activity) - Navigate To Settings Smoothly

    private final String KEY_APPBAR_TITLE_PERSISTENCE = "movie_category";
    private final String KEY_MOVIE_LIST_PERSISTENCE = "movie_list";
    private final String KEY_FAV_MOVIE_LIST_PERSISTENCE = "fav_movie_list";
    private List<MovieNetworkLite> mMovieList;
    private List<Movie> favouriteMovies;
    private MainViewModel mMainViewModel;

    @BindView(R.id.progressbar_movies_loading)
    ProgressBar pbLoadMovies;
    @BindView(R.id.recycler_view_movies)
    RecyclerView rvMovies;
    @BindView(R.id.text_view_info_message)
    TextView tvInfoMessage;
    @BindView(R.id.text_view_favourites_message)
    TextView tvFavMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar vTbMain = findViewById(R.id.toolbar_main);
        setSupportActionBar(vTbMain);
        MainViewModelFactory vMainViewModelFactory = InjectorUtils.provideMainViewModelFactory(this);
        mMainViewModel = ViewModelProviders.of(this, vMainViewModelFactory).get(MainViewModel.class);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIE_LIST_PERSISTENCE)) {
            mMovieList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE_LIST_PERSISTENCE));
            setTitle(savedInstanceState.getCharSequence(KEY_APPBAR_TITLE_PERSISTENCE));
            bindNetworkMovies(mMovieList);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(KEY_FAV_MOVIE_LIST_PERSISTENCE)) {
            favouriteMovies = Parcels.unwrap(savedInstanceState.getParcelable(KEY_FAV_MOVIE_LIST_PERSISTENCE));
            setTitle(savedInstanceState.getCharSequence(KEY_APPBAR_TITLE_PERSISTENCE));
            bindFavouriteMovies(favouriteMovies);
        } else if (NetworkUtils.isOnline(this)) {
            removeMessageInfo(tvInfoMessage);
            new MovieListAsync(mMainViewModel, Category.UPCOMING, this).execute();
        }
    }

    private void bindFavouriteMovies(List<Movie> favouriteMovies) {
        if (favouriteMovies.isEmpty()) {
            removeMessageInfo(tvInfoMessage);
            showDefaultFavMessage();
        } else {
            MoviesAdapter vMoviesAdapter = new MoviesAdapter(null, favouriteMovies, this);
            setupRecyclerView(vMoviesAdapter);
        }
    }

    private void setupRecyclerView(MoviesAdapter moviesAdapter) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),
                ViewUtils.calculateNoOfColumns(this));
        ViewUtils.setupRecyclerView(rvMovies, gridLayoutManager, this);
        rvMovies.setAdapter(moviesAdapter);
        pbLoadMovies.setVisibility(View.GONE);
    }

    private void removeMessageInfo(TextView tvInfoMessage) {
        if (tvInfoMessage.getVisibility() == View.VISIBLE)
            tvInfoMessage.setVisibility(View.GONE);
    }

    @Override
    public void loadFavouriteMovies() {
        mMainViewModel.getFavouriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieList = null;
                if (movies != null) {
                    favouriteMovies = movies;
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle(getString(R.string.action_sort_favourites));
                    bindFavouriteMovies(favouriteMovies);
                }
            }
        });
    }

    private void setAppBarTitle(Category category) {
        if (getSupportActionBar() != null)
            switch (category) {
                case TOP_RATED:
                    getSupportActionBar().setTitle(getString(R.string.action_sort_top_rated));
                    break;
                case UPCOMING:
                    getSupportActionBar().setTitle(getString(R.string.action_sort_upcoming));
                    break;
                case POPULAR:
                    getSupportActionBar().setTitle(getString(R.string.action_sort_most_popular));
                    break;
            }
    }

    void bindNetworkMovies(List<MovieNetworkLite> movies) {
        if (movies != null) {
            MoviesAdapter vMoviesAdapter = new MoviesAdapter(movies, null, this);
            setupRecyclerView(vMoviesAdapter);
        } else {
            showNoConnectionMessage();
        }
    }

    private void showNoConnectionMessage() {
        rvMovies.setVisibility(View.GONE);
        tvInfoMessage.setVisibility(View.VISIBLE);
        pbLoadMovies.setVisibility(View.GONE);
    }

    private void showDefaultFavMessage() {
        rvMovies.setVisibility(View.GONE);
        pbLoadMovies.setVisibility(View.GONE);
        tvFavMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovieList != null)
            outState.putParcelable(KEY_MOVIE_LIST_PERSISTENCE, Parcels.wrap(mMovieList));
        if (favouriteMovies != null)
            outState.putParcelable(KEY_FAV_MOVIE_LIST_PERSISTENCE, Parcels.wrap(favouriteMovies));
        if (getSupportActionBar() != null)
            outState.putCharSequence(KEY_APPBAR_TITLE_PERSISTENCE, getSupportActionBar().getTitle());
    }

    @Override
    public void inProgress() {
        removeMessageInfo(tvFavMessage);
        pbLoadMovies.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinished(List<MovieNetworkLite> movies, Category category) {
        setAppBarTitle(category);
        mMovieList = movies;
        bindNetworkMovies(mMovieList);
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
        sortMenu.setOnMenuItemClickListener(new CategoryMenuListener(mMainViewModel, this, this));
        sortMenu.inflate(R.menu.category_menu);
        sortMenu.show();
    }

    @Override
    public void viewMovieDetails(MovieNetworkLite movie, SmooviePosterImageView view) {
        Intent vIntent = new Intent(this, MovieDetailActivity.class);
        vIntent.putExtra(KEY_MOVIE_ID, movie.getMovieId());
        vIntent.putExtra(KEY_MOVIE_POSTER, movie.getMoviePoster());
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        startActivity(vIntent, options.toBundle());
    }
}
