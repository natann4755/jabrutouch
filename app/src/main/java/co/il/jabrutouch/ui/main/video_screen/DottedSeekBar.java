package co.il.jabrutouch.ui.main.video_screen;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import co.il.jabrutouch.R;



public class DottedSeekBar extends SeekBar {

    /** Int values which corresponds to dots */
    private List<Integer> mDotsPositions = null;
    /** Drawable for dot */
    private Bitmap mDotBitmap = null;
    private float topMargin = 0;

    public DottedSeekBar(final Context context) {
        super(context);
        init(null);
    }

    public DottedSeekBar(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DottedSeekBar(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * Initializes Seek bar extended attributes from xml
     *
     * @param attributeSet {@link AttributeSet}
     */
    private void init(final AttributeSet attributeSet) {
        final TypedArray attrsArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.DottedSeekBar, 0, 0);

        final int dotsArrayResource = attrsArray.getResourceId(R.styleable.DottedSeekBar_dots_positions, 0);

        if (0 != dotsArrayResource) {
//            mDotsPositions = getResources().getIntArray(dotsArrayResource);
        }

        final int dotDrawableId = attrsArray.getResourceId(R.styleable.DottedSeekBar_dots_drawable, 0);

        if (0 != dotDrawableId) {
            mDotBitmap = BitmapFactory.decodeResource(getResources(), dotDrawableId);
        }
    }

    /**
     * @param dots to be displayed on this SeekBar
     */
    public void setDots(final List<Integer> dots) {
        mDotsPositions = dots;
        invalidate();
    }

    /**
     * @param dotsResource resource id to be used for dots drawing
     */
    public void setDotsDrawable(final int dotsResource, int topMargin) {
        this.topMargin = topMargin;
        Drawable d = getResources().getDrawable(dotsResource, null);
        mDotBitmap = drawableToBitmap(d);
        invalidate();
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    protected synchronized void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        final int width = getMeasuredWidth();
        final int step = width / 100;

        if (null != mDotsPositions && 0 != mDotsPositions.size() && null != mDotBitmap) {
            // draw dots if we have ones
            for (int position : mDotsPositions) {

                Paint paint = new Paint();
                paint.setAlpha(100);

                double a = position * 100.0 / TimeUnit.MILLISECONDS.toSeconds(getMax());
                canvas.drawBitmap(mDotBitmap, (float) (step * a), topMargin, paint);
            }
        }
    }
}