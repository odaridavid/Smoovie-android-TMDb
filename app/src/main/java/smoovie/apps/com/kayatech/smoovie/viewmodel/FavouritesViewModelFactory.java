package smoovie.apps.com.kayatech.smoovie.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import smoovie.apps.com.kayatech.smoovie.room_database.MovieDatabase;

public class FavouritesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieDatabase movieDatabase;
    private  final int movieId;

    public FavouritesViewModelFactory(MovieDatabase movieDatabase,int movieId){
        this.movieDatabase = movieDatabase;
        this.movieId = movieId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(movieDatabase,movieId);
    }
}
