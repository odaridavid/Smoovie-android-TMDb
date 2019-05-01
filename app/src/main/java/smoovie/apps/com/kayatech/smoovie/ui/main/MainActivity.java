package smoovie.apps.com.kayatech.smoovie.ui.main;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import smoovie.apps.com.kayatech.smoovie.util.ViewUtils;

public class MainActivity extends AppCompatActivity implements MovieListCallBack, FavouriteMoviesCallback {

    private final String KEY_APPBAR_TITLE_PERSISTENCE = "movie_category";
    private final String KEY_MOVIE_LIST_PERSISTENCE = "movie_list";
    private List<Movie> mMovieList;
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
            setUpMovieView(mMovieList);
        } else if (NetworkUtils.isOnline(this)) {
            new MovieListAsync(mMainViewModel, Category.UPCOMING, this).execute();
        }
    }

    @Override
    public void loadFavouriteMovies() {
        mMainViewModel.getFavouriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null) {
                    mMovieList = movies;
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle(getString(R.string.action_sort_favourites));
                    if (!(movies.isEmpty())) {
                        setUpMovieView(mMovieList);
                    } else {
                        showDefaultFavMessage();
                    }
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

    void setUpMovieView(List<Movie> movies) {
        if (movies != null) {
            if (getSupportActionBar().getTitle().equals(getString(R.string.action_sort_favourites)) && movies.isEmpty()) {
                showDefaultFavMessage();
            } else {
                MoviesAdapter vMoviesAdapter = new MoviesAdapter(movies, null);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),
                        ViewUtils.calculateNoOfColumns(this));
                ViewUtils.setupRecyclerView(rvMovies, gridLayoutManager, this);
                rvMovies.setAdapter(vMoviesAdapter);
                pbLoadMovies.setVisibility(View.GONE);
            }
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
        outState.putParcelable(KEY_MOVIE_LIST_PERSISTENCE, Parcels.wrap(mMovieList));
        if (getSupportActionBar() != null)
            outState.putCharSequence(KEY_APPBAR_TITLE_PERSISTENCE, getSupportActionBar().getTitle());
    }

    @Override
    public void inProgress() {
        if (tvFavMessage.getVisibility() == View.VISIBLE)
            tvFavMessage.setVisibility(View.GONE);
        pbLoadMovies.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinished(List<Movie> movies, Category category) {
        setAppBarTitle(category);
        mMovieList = movies;
        setUpMovieView(mMovieList);
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
}
