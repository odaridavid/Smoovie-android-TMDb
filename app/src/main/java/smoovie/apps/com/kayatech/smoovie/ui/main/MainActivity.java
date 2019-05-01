package smoovie.apps.com.kayatech.smoovie.ui.main;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import smoovie.apps.com.kayatech.smoovie.ui.main.callbacks.MovieListCallBack;
import smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel.MainViewModel;
import smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel.MainViewModelFactory;
import smoovie.apps.com.kayatech.smoovie.util.InjectorUtils;
import smoovie.apps.com.kayatech.smoovie.util.NetworkUtils;
import smoovie.apps.com.kayatech.smoovie.util.ViewUtils;

public class MainActivity extends AppCompatActivity implements MovieListCallBack {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final String KEY_APPBAR_TITLE_PERSISTENCE = "movie_category";
    private final String KEY_MOVIE_LIST_PERSISTENCE = "movie_list";
    //    @BindView(R.id.imageview_fav_default)
//    ImageView mFavconImage;
//    @BindView(R.id.textview_fav_default)
//    TextView mFavDefaultText;
    @BindView(R.id.pb_movies_loading)
    ProgressBar pbLoadMovies;
    @BindView(R.id.recycler_view_movies)
    RecyclerView rvMovies;
    @BindView(R.id.textview_error_message)
    TextView tvNetworkError;

    private List<Movie> mMovieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar vTbMain = findViewById(R.id.toolbar_main);
        setSupportActionBar(vTbMain);
        MainViewModelFactory vMainViewModelFactory = InjectorUtils.provideMainViewModelFactory(this);
        MainViewModel vMainViewModel = ViewModelProviders.of(this, vMainViewModelFactory).get(MainViewModel.class);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIE_LIST_PERSISTENCE)) {
            mMovieList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE_LIST_PERSISTENCE));
            setUpMovieView(mMovieList);
            setTitle(savedInstanceState.getCharSequence(KEY_APPBAR_TITLE_PERSISTENCE));
        } else {
            if (NetworkUtils.isOnline(this)) {
                new MovieListAsync(vMainViewModel, Category.UPCOMING, this).execute();
            }
        }
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
            MoviesAdapter vMoviesAdapter = new MoviesAdapter(movies, null);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),
                    ViewUtils.calculateNoOfColumns(this));
            ViewUtils.setupRecyclerView(rvMovies, gridLayoutManager, this);
            rvMovies.setAdapter(vMoviesAdapter);
            pbLoadMovies.setVisibility(View.GONE);
        } else {
            rvMovies.setVisibility(View.GONE);
            tvNetworkError.setVisibility(View.VISIBLE);
            pbLoadMovies.setVisibility(View.GONE);
        }
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
        pbLoadMovies.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFinished(List<Movie> movies, Category category) {
        setAppBarTitle(category);
        mMovieList = movies;
        setUpMovieView(mMovieList);
    }
}
