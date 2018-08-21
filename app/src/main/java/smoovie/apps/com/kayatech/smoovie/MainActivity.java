package smoovie.apps.com.kayatech.smoovie;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.Model.Movie;
import smoovie.apps.com.kayatech.smoovie.Network.OnMoviesCallback;
import smoovie.apps.com.kayatech.smoovie.Network.TMDBMovies;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isFetchingMovies = false;
    private int currentPage = 1;
    private TMDBMovies movieList;
    private String sortBy = TMDBMovies.UPCOMING;
    MoviesAdapter mMoviesAdapter;
    GridLayoutManager gridLayoutManager;
    MovieClickHandler movieClickHandler;


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
        mMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //on ui and cached
                int totalItemCount = gridLayoutManager.getItemCount();

                //in cache
                int visibleItemCount = gridLayoutManager.getChildCount();

                //on ui
                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    //if reached the end fetch more movies move to next page
                    if (!isFetchingMovies) {
                        mProgressBar.setVisibility(View.GONE);
                        getMovies(currentPage + 1);
                    }
                }
            }
        });

    }

    private void setUpRecyclerView() {

        //Reference
        mMoviesRecyclerView.setHasFixedSize(true);

        //Grid Layout Setup
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
        movieClickHandler = new MovieClickHandler() {
            @Override
            public void onClick(Movie movie) {
                Intent openDetailsActivity = new Intent(MainActivity.this, DetailActivity.class);
                openDetailsActivity.putExtra(DetailActivity.MOVIE_ID, Parcels.wrap(movie.getMovieId()));
                startActivity(openDetailsActivity);
            }
        };


        movieList.getMovies(page, sortBy, new OnMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {

                if (mMoviesAdapter == null) {
                    mMoviesAdapter = new MoviesAdapter(getApplicationContext(), movies, movieClickHandler);
                    mMoviesRecyclerView.setAdapter(mMoviesAdapter);

                } else {
                    if (page == 1) {
                        mMoviesAdapter.clearMovies();
                    }
                    mProgressBar.setVisibility(View.GONE);
                    //appends movie results to list and updates recycler view
                    mMoviesAdapter.setMovieList(movies);

                }
                currentPage = page;
                isFetchingMovies = false;


                setTitleBar();
            }

            @Override
            public void onFailure() {
                Log.d(TAG, getString(R.string.error_network));

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
        } else {
            return super.onOptionsItemSelected(item);
        }

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
     * @return
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
            mErrorMessageTextView.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }
}
