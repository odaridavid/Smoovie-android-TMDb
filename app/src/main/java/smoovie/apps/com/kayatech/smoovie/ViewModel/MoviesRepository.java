package smoovie.apps.com.kayatech.smoovie.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import smoovie.apps.com.kayatech.smoovie.Model.Movie;
import smoovie.apps.com.kayatech.smoovie.Network.TMDBMovies;
import smoovie.apps.com.kayatech.smoovie.Presenter.IMoviePresenter;
import smoovie.apps.com.kayatech.smoovie.Presenter.MovieDetailsCallback;


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
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
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
                    public void onFailure(Call<Movie> call, Throwable t) {

                    }
                });
        return movieDetailLiveData;
    }


}
