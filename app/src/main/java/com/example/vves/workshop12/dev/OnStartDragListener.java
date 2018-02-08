package com.example.vves.workshop12.dev;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by vves on 09.09.2017.
 */

public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}

