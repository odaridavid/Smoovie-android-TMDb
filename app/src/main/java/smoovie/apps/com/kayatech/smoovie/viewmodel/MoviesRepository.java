package smoovie.apps.com.kayatech.smoovie.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.network.TMDBMovies;


public class MoviesRepository implements IMoviePresenter {
    //Fetches The Data From Api

    //Details
    @Override
    public LiveData<Movie> getMovie(int movieId, final MovieDetailsCallback movieDetailsCallback) {

        final MutableLiveData<Movie> movieDetailLiveData = new MutableLiveData<>();
        //TODO To Be Checked
        TMDBMovies.tmdbApiService
                .getMovie(movieId,TMDBMovies.API_KEY,TMDBMovies.LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie mv = response.body();
                            if (mv != null) {
                                movieDetailsCallback.onSuccess(mv);
                                movieDetailLiveData.setValue(mv);
                                Log.d("RESPONSE", movieDetailLiveData.toString());
                            } else {
                                movieDetailsCallback.onError();
                                Log.d("MOVIE ERRROR RESPONSE", "NULL");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {

                    }
                });
        return movieDetailLiveData;
    }


}
