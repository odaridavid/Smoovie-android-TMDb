package smoovie.apps.com.kayatech.smoovie.ui.main.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import smoovie.apps.com.kayatech.smoovie.MoviesRepository;

/**
 * Created By blackcoder
 * On 30/04/19
 **/
public final class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MoviesRepository mRepository;

    public MainViewModelFactory(MoviesRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked cast")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(mRepository);
    }
}
