package smoovie.apps.com.kayatech.smoovie.ui.detail.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smoovie.apps.com.kayatech.smoovie.R;
import smoovie.apps.com.kayatech.smoovie.model.Trailers;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MoviesVideoViewHolder> {

    private List<Trailers> mTrailersList;
    private static IWatchTrailerClickHandler sMIWatchTrailerClickHandler;
    private static IShareTrailerHandler sMIShareTrailerHandler;


    public TrailersAdapter(List<Trailers> mMovieTrailerList, IWatchTrailerClickHandler iWatchTrailerClickHandler, IShareTrailerHandler iShareTrailerHandler) {
        mTrailersList = mMovieTrailerList;
        sMIWatchTrailerClickHandler = iWatchTrailerClickHandler;
        sMIShareTrailerHandler = iShareTrailerHandler;
    }

    @NonNull
    @Override
    public MoviesVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MoviesVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesVideoViewHolder holder, int position) {
        holder.bind(mTrailersList.get(position));
    }

    @Override
    public int getItemCount() {
        return (mTrailersList == null) ? 0 : mTrailersList.size();
    }

    public class MoviesVideoViewHolder extends RecyclerView.ViewHolder {

        private String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";
        Trailers mTrailers;
        private Context ctx;
        @BindView(R.id.iv_trailer)
        ImageView mMovieTrailerThumbnailImageView;
        @BindView(R.id.iv_icon_share)
        ImageView mMovieTrailerShareImageView;
        @BindView(R.id.iv_trailer_play)
        ImageView mMovieTrailerPlay;

        MoviesVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mMovieTrailerPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTrailers = mTrailersList.get(getAdapterPosition());
                    sMIWatchTrailerClickHandler.onClick(mTrailers);
                }
            });
            mMovieTrailerShareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTrailers = mTrailersList.get(getAdapterPosition());
                    sMIShareTrailerHandler.onClick(mTrailers);
                }
            });
        }

        private void bind(Trailers trailers) {
            ctx = itemView.getContext();
            Picasso.with(ctx)
                    .load(String.format(YOUTUBE_THUMBNAIL_URL, trailers.getKey()))
                    .placeholder(R.color.colorAlternate)
                    .into(mMovieTrailerThumbnailImageView);
        }
    }

}
