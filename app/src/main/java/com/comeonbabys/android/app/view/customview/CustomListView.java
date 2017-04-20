package com.comeonbabys.android.app.view.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Server on 23.03.2017.
 */

public class CustomListView extends ListView {

    private android.view.ViewGroup.LayoutParams params;
    private int old_count = 0;

    public CustomListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//
//        if (getCount() != old_count) {
//            params = getLayoutParams();
//            old_count = getCount();
//            int totalHeight = 0;
//            for (int i = 0; i < getCount(); i++) {
//                this.measure(0, 0);
//                totalHeight += getMeasuredHeight();
//            }
//
//            params = getLayoutParams();
//            params.height = totalHeight + (getDividerHeight() * (getCount() - 1));
//            setLayoutParams(params);
//        }
//
//        super.onDraw(canvas);
//    }

}