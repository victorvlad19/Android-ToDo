package com.example.vves.workshop12.dev;

import android.support.v7.widget.RecyclerView;

/**
 * Created by vves on 09.09.2017.
 */

public interface ItemTouchHelperAdapter {
    /**
     * Called when an item has been dragged far enough to trigger a move. This is called every time
     * an item is shifted, and not at the end of a "drop" event.
     *  @param fromPosition The start position of the moved item.
     * @param toPosition   Then end position of the moved item.
     * @param rv1

     */
    void onItemMove(int fromPosition, int toPosition, RecyclerView rv1);


    /**
     * Called when an item has been dismissed by a swipe.
     *
     * @param position The position of the item dismissed.

     */
    void onItemDismiss(int position, RecyclerView rv);

    void onItemDismiss(int position);

}