package smoovie.apps.com.kayatech.smoovie.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;

import smoovie.apps.com.kayatech.smoovie.R;

/**
 * Created By blackcoder
 * On 30/04/19
 **/
public final class ViewUtils {
    /**
     * @param context Recycler view instance
     * @return number of columns for span count
     */
    public static int calculateNoOfColumns(@NonNull Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scaleFactor = 200;
        int noOfColumns = (int) (dpWidth / scaleFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    /**
     * Methods to setup Recycler view and dynamic span count
     */
    public static void setupRecyclerView(@NonNull final RecyclerView recyclerView, @NonNull final GridLayoutManager gridLayoutManager, @NonNull final Context context) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        //Gets Recycler width and card width and arranges elements in layout as per screen size
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int viewWidth = recyclerView.getMeasuredWidth();
                float cardViewWidth = context.getResources().getDimension(R.dimen.size_layout);
                int newSpanCunt = (int) Math.floor(viewWidth / cardViewWidth);
                gridLayoutManager.setSpanCount(newSpanCunt);
                gridLayoutManager.requestLayout();
            }
        });
    }
}
