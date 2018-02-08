package com.example.vves.workshop12;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.Button;

import com.sothree.slidinguppanel.ScrollableViewHelper;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by vves on 26.11.2017.
 */

public class EditorScrollViewHelper extends ScrollableViewHelper {
    private Context mContext;

    EditorScrollViewHelper(Context context){
        mContext = context;
    }

    public int getScrollableViewScrollPosition(View scrollableView, boolean isSlidingUp) {

        View mScrollableView = ((MainActivity) mContext).findViewById(R.id.editor);

        if (mScrollableView instanceof RichEditor) {
            if(isSlidingUp){
                return mScrollableView.getScrollY();
            } else {
                RichEditor nsv = ((RichEditor) mScrollableView);
                View child = nsv.getChildAt(0);
                return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
            }
        } else {
            return 0;
        }
    }
}
