package asa.org.bd.ammsma.customListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;


public class CustomNonScrollerListView extends ListView {

    public CustomNonScrollerListView(Context context) {
        super(context);
    }

    public CustomNonScrollerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNonScrollerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}