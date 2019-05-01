package smoovie.apps.com.kayatech.smoovie.ui.main;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Category;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel.MainViewModel;
import smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel.MainViewModelFactory;
import smoovie.apps.com.kayatech.smoovie.util.InjectorUtils;
import smoovie.apps.com.kayatech.smoovie.util.NetworkUtils;
import smoovie.apps.com.kayatech.smoovie.util.ViewUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainViewModel mMainViewModel;

    @BindView(R.id.imageview_fav_default)
    ImageView mFavconImage;
    @BindView(R.id.textview_fav_default)
    TextView mFavDefaultText;
    @BindView(R.id.pb_movies_loading)
    ProgressBar mProgressBar;
    @BindView(R.id.recycler_view_movies)
    RecyclerView mMoviesRecyclerView;
    @BindView(R.id.textview_error_message)
    TextView mErrorMessageTextView;
    @BindView(R.id.toolbar_main)
    Toolbar toolbarMainPage;
    private List<Movie> mMovieList;
    private final String KEY_MOVIE_LIST_PERSISTENCE = "movie_list";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MainViewModelFactory vMainViewModelFactory = InjectorUtils.provideMainViewModelFactory(this);
        mMainViewModel = ViewModelProviders.of(this, vMainViewModelFactory).get(MainViewModel.class);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIE_LIST_PERSISTENCE)) {
            mMovieList = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE_LIST_PERSISTENCE));
        } else {
            if (NetworkUtils.isOnline(this)) {
                mMovieList = mMainViewModel.getMovies(Category.POPULAR, "en-US", 1);
            }
        }
        setUpMovieView(mMovieList);
    }

    void setUpMovieView(List<Movie> movies) {
        if (movies != null) {
            MoviesAdapter vMoviesAdapter = new MoviesAdapter(movies, null);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),
                    ViewUtils.calculateNoOfColumns(this));
            ViewUtils.setupRecyclerView(mMoviesRecyclerView, gridLayoutManager, this);
            mMoviesRecyclerView.setAdapter(vMoviesAdapter);
            mProgressBar.setVisibility(View.GONE);
        } else {
            mMoviesRecyclerView.setVisibility(View.GONE);
            mErrorMessageTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_MOVIE_LIST_PERSISTENCE, Parcels.wrap(mMovieList));
    }

}
