package smoovie.apps.com.kayatech.smoovie.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.MovieReviews;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MoviesReviewViewHolder> {

    //Adapter Class
    private List<MovieReviews> mMovieReviewsList;

    ReviewsAdapter(List<MovieReviews> moviesReview) {
        mMovieReviewsList = moviesReview;
    }

    public void setmMovieReviewsList(List<MovieReviews> movieReviews) {
        this.mMovieReviewsList = movieReviews;
        notifyDataSetChanged();
    }

    public void clearMovies() {
        mMovieReviewsList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoviesReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesReviewViewHolder holder, int position) {
        holder.bind(mMovieReviewsList.get(position));
    }

    @Override
    public int getItemCount() {
        //check for null
        return (mMovieReviewsList == null) ? 0 : mMovieReviewsList.size();
    }

    public static class MoviesReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_reviews_author)
        TextView mMovieReviewsAuthorTextView;
        @BindView(R.id.tv_reviews_content)
        TextView mMovieReviewsContentTextView;

        MoviesReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(MovieReviews movieReviews) {

            if (movieReviews != null) {
                String author = movieReviews.getAuthor();
                String reviews = movieReviews.getContent();
                mMovieReviewsAuthorTextView.setText(author);
                mMovieReviewsContentTextView.setText(reviews);
                Log.d("MOVIES REVIEWS", author + " THE BODY " + reviews);
            }
        }
    }

}
