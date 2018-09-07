package smoovie.apps.com.kayatech.smoovie.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.room_database.AppExecutors;
import smoovie.apps.com.kayatech.smoovie.room_database.MovieDatabase;
import smoovie.apps.com.kayatech.smoovie.viewmodel.FavouritesViewModel;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class FavouritesActivity extends AppCompatActivity implements FavouritesAdapter.IFavMovieClickHandler {

    private String TAG = FavouritesActivity.class.getSimpleName();
    private MovieDatabase movieDatabase;
    private FavouritesAdapter favouritesAdapter;
    RecyclerView mFavouriteMoviesRecyclerView;
    ImageView mFavconImage;
    TextView mFavDefaultText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        mFavouriteMoviesRecyclerView = findViewById(R.id.rv_favourite_movies);
        mFavconImage = findViewById(R.id.iv_favcon_view_default);
        mFavDefaultText = findViewById(R.id.tv_fav_default);
        Toolbar toolbarSettingsPage = findViewById(R.id.action_toolbar_fav);
        setSupportActionBar(toolbarSettingsPage);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Recycler View
        mFavouriteMoviesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerReview = new LinearLayoutManager(this);
        mFavouriteMoviesRecyclerView.setLayoutManager(linearLayoutManagerReview);
        favouritesAdapter = new FavouritesAdapter(this, this);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mFavouriteMoviesRecyclerView.addItemDecoration(decoration);
        mFavouriteMoviesRecyclerView.setAdapter(favouritesAdapter);

        movieDatabase = MovieDatabase.getMovieDatabaseInstance(getApplicationContext());
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
        setupViewModel();
    }

    private void setupViewModel() {
        FavouritesViewModel favouritesViewModel
                = ViewModelProviders
                .of(this)
                .get(FavouritesViewModel.class);

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

    @Override
    public void onItemClickListener(int movieId) {
        //Open Movie In Detail Activity
        Intent intent = new Intent(FavouritesActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_ID_DB, movieId);
        Log.d(TAG, "onItemClickListener: " + movieId);
        startActivity(intent);
    }
}
