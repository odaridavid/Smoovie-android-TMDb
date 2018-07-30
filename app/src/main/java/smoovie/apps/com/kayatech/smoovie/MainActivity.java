package smoovie.apps.com.kayatech.smoovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.Model.Movie;
import smoovie.apps.com.kayatech.smoovie.Network.OnMoviesCallback;
import smoovie.apps.com.kayatech.smoovie.Network.TMDBMovies;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    MoviesAdapter mMoviesAdapter;
    RecyclerView mMoviesRecyclerView;
    private boolean isFetchingMovies;
    private int currentPage = 1;
    GridLayoutManager gridLayoutManager;

    private TMDBMovies movieList;

    //TODO TITLEBAR TEXT
    //TODO SCROLL UP DISSAPEAR
    //TODO ON ROTATE MOVIE VOTE AVERAGE VOTE COUNT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = TMDBMovies.getInstance();
        //Reference
        mMoviesRecyclerView = findViewById(R.id.movie_recycler_view);
        mMoviesRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        getMovies(currentPage);
        //RecyclerViewScrollListener();
        mMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            // Scroll to half of the list we increment it by one which is the next page.
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //TODO 2.THESE METHODS
                //TODO SMOOTH SCROLLING
                int totalItemCount = gridLayoutManager.getItemCount();
                int visibleItemCount = gridLayoutManager.getChildCount();
                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });

    }


    //
    private void RecyclerViewScrollListener() {
        ;


    }

    private void getMovies(int page) {
        //checks if state is checking or not
        isFetchingMovies = true;
        movieList.getMovies(page, new OnMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {

                if (mMoviesAdapter == null) {
                    mMoviesAdapter = new MoviesAdapter(getApplicationContext(), movies);
                    mMoviesRecyclerView.setAdapter(mMoviesAdapter);
                } else {
                    //appends movie results to list and updates recycler view
                    mMoviesAdapter.setMovieList(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "getMovies Failure");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_sort_high_rating) {

//            //methods
//            ApiGetServices service = retrofit.create(ApiGetServices.class);
//            Call<MovieListResponse> movieCall = service.getHighRatedMovies();
//            movieCall.enqueue(new Callback<MovieListResponse>() {
//                @Override
//                public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
//                    mMoviesAdapter.setMovieList(response.body().getMoviesResult());
//                }
//
//                @Override
//                public void onFailure(Call<MovieListResponse> call, Throwable t) {
//                   Log.d(TAG,"Fail to retrieve high rated");
//                }
//            });
//
//           // Toast.makeText(MainActivity.this,"Highest Rating Movies",Toast.LENGTH_LONG).show();


        } else if (itemId == R.id.action_sort_most_popular) {

//            //methods
//            ApiGetServices service = retrofit.create(ApiGetServices.class);
//            Call<MovieListResponse> movieCall = service.getPopularMovies();
//            movieCall.enqueue(new Callback<MovieListResponse>() {
//                @Override
//                public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
//                    mMoviesAdapter.setMovieList(response.body().getMoviesResult());
//                }
//
//                @Override
//                public void onFailure(Call<MovieListResponse> call, Throwable t) {
//                    Log.d(TAG,"Fail to retrieve popular");
//                }
//            });
//
//          //  Toast.makeText(MainActivity.this,"Most Popualar Movies",Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }
}
