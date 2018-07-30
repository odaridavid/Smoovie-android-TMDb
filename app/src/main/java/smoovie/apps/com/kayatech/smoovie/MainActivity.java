package smoovie.apps.com.kayatech.smoovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import smoovie.apps.com.kayatech.smoovie.Model.Movie;
import smoovie.apps.com.kayatech.smoovie.Network.OnMoviesCallback;
import smoovie.apps.com.kayatech.smoovie.Network.TMDBMovies;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    MoviesAdapter mMoviesAdapter;
    RecyclerView mMoviesRecyclerView;

    private TMDBMovies movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieList = TMDBMovies.getInstance();

        //Reference
        mMoviesRecyclerView = findViewById(R.id.movie_recycler_view);
        mMoviesRecyclerView.setHasFixedSize(true);


        //Grid Layout For Main View
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        movieList.getMovies(new OnMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                // Loads Movie Data
                mMoviesAdapter = new MoviesAdapter(getApplicationContext(), movies);
                mMoviesRecyclerView.setAdapter(mMoviesAdapter);
            }

            @Override
            public void onFailure() {
                Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Fail to Load");
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
