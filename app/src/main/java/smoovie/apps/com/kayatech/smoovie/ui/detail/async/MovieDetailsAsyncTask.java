package smoovie.apps.com.kayatech.smoovie.ui.detail.async;

import android.os.AsyncTask;
import android.util.Log;

import smoovie.apps.com.kayatech.smoovie.model.Movie;
import smoovie.apps.com.kayatech.smoovie.ui.detail.viewmodel.DetailViewModel;

/**
 * Created By blackcoder
 * On 03/05/19
 **/
public final class MovieDetailsAsyncTask extends AsyncTask<Integer, Void, Movie> {

    private DetailViewModel mDetailViewModel;
    private String lang;
    private MovieDetailsCallBack mMovieDetailsCallBack;

    public MovieDetailsAsyncTask(DetailViewModel detailViewModel, String language, MovieDetailsCallBack movieDetailsCallBack) {
        mDetailViewModel = detailViewModel;
        lang = language;
        mMovieDetailsCallBack = movieDetailsCallBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mMovieDetailsCallBack.loading();
    }

    @Override
    protected Movie doInBackground(Integer... integers) {
        Log.d(MovieDetailsCallBack.class.getSimpleName(), String.valueOf(integers[0]));
        return mDetailViewModel.getMovieDetails(integers[0], lang);
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        mMovieDetailsCallBack.complete(movie);
        Log.d("Async", "DOne");
    }
}
