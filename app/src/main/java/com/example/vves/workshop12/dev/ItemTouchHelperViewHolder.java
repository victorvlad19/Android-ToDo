package com.example.vves.workshop12.dev;

/**
 * Created by vves on 09.09.2017.
 */

public interface ItemTouchHelperViewHolder {
    /**
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();


    /**
     * state should be cleared.
     */
    void onItemClear();
}
