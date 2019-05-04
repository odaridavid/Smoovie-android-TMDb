package smoovie.apps.com.kayatech.smoovie.ui.detail.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Reviews;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MoviesReviewViewHolder> {

    private List<Reviews> mReviewsList;

    public ReviewsAdapter(List<Reviews> moviesReview) {
        mReviewsList = moviesReview;
    }

    @NonNull
    @Override
    public MoviesReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesReviewViewHolder holder, int position) {
        holder.bind(mReviewsList.get(position));
    }

    @Override
    public int getItemCount() {
        return (mReviewsList == null) ? 0 : mReviewsList.size();
    }

    public static class MoviesReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_reviews_author)
        TextView tvAuthor;
        @BindView(R.id.tv_reviews_content)
        TextView tvContent;

        MoviesReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Reviews movieReviews) {
            if (movieReviews != null) {
                String author = movieReviews.getAuthor();
                String reviews = movieReviews.getContent();
                tvAuthor.setText(author);
                tvContent.setText(reviews);
            }
        }
    }
}
