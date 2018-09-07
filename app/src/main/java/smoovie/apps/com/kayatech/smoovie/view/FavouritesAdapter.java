package smoovie.apps.com.kayatech.smoovie.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Movie;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouriteMovieViewHolder> {

    //Adapter Class
    private Context context;
    private List<Movie> mMovieList;
    final private IFavMovieClickHandler iFavMovieClickHandler;

    FavouritesAdapter(Context context, IFavMovieClickHandler iFavMovieClickHandler1) {
        this.context = context;
        iFavMovieClickHandler = iFavMovieClickHandler1;

    }

    public void setmMovieReviewsList(List<Movie> movie) {
        this.mMovieList = movie;
        notifyDataSetChanged();
    }

    public interface IFavMovieClickHandler {
        void onItemClickListener(int movieId);
    }

    public void clearMovies() {
        mMovieList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouriteMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.favourite_movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new FavouriteMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteMovieViewHolder holder, int position) {
        holder.bind(mMovieList.get(position));
    }

    @Override
    public int getItemCount() {
        //check for null
        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    public List<Movie> getMovies() {
        return mMovieList;
    }

    public class FavouriteMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Movie movies;
        @BindView(R.id.tv_fav_movie_title)
        TextView mMovieTitleTextView;
        @BindView(R.id.tv_fav_movie_rating)
        TextView mMovieRatingTextView;

        FavouriteMovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        private void bind(Movie movie) {
            movies = movie;
            Context ctx = itemView.getContext();
            final Typeface custom_font = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Thin.ttf");
            String title = movie.getMovieTitle();
            float rating = movie.getVoterAverage();
            String ratingString = String.format(Locale.getDefault(), "%.2f", rating);

            mMovieRatingTextView.setText(ratingString);
            mMovieTitleTextView.setTypeface(custom_font);
            mMovieTitleTextView.setText(title);
            mMovieTitleTextView.setTypeface(custom_font);


        }

        @Override
        public void onClick(View v) {
            int movieId = mMovieList.get(getAdapterPosition()).getMovieId();
            iFavMovieClickHandler.onItemClickListener(movieId);
        }
    }

}
