package smoovie.apps.com.kayatech.smoovie.util;


import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * Created By blackcoder
 * On 05/04/19
 **/
public class SmoovieBackdropImageView extends AppCompatImageView {
    public SmoovieBackdropImageView(Context context) {
        super(context);
    }

    public SmoovieBackdropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmoovieBackdropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Unpack given dimens to new aspect ratio
        int threeTwoHeight = MeasureSpec.getSize(widthMeasureSpec) * 3 / 2;
        int threeTwoHeightSpec = MeasureSpec.makeMeasureSpec(threeTwoHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, threeTwoHeightSpec);
    }
}
