package com.example.vves.workshop12;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.vves.workshop12.dev.ItemTouchHelperAdapter;
import com.example.vves.workshop12.dev.ItemTouchHelperViewHolder;

import java.util.List;

/**
 * Created by vves on 12.10.2017.
 */

public class SimpleItemTouchHelperCallbackItem extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
    private RecyclerView rv1;
    static int count = 0;
    private Context touchContext;

    public SimpleItemTouchHelperCallbackItem(ItemTouchHelperAdapter adapter, RecyclerView rv1, Context context) {
        mAdapter = adapter;
        this.rv1 = rv1;
        touchContext = context;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    int start;
    static boolean makefolder = false;
    static int position_make_folder = 0;
    static String folder_name;
    int position_make_folder_temp = 0;
    Boolean folder = false;
    static int folder_pos;
    static int folder_curr;
    int folder_pos_temp;
    int folder_curr_temp;

    @Override
    public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
        count = 1;
        position_make_folder_temp = current.getAdapterPosition();
        if (target.getItemViewType() == 1) {
            folder = true;
            folder_pos_temp = target.getAdapterPosition();
            folder_curr_temp = current.getAdapterPosition();
        }
        else folder = false;
        return super.canDropOver(recyclerView, current, target);
    }

    Boolean sus = false;
    Boolean jos = false;


    @Override
    public RecyclerView.ViewHolder chooseDropTarget(RecyclerView.ViewHolder selected, List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
        if (count < 1) { start = curY; }
        return super.chooseDropTarget(selected, dropTargets, curX, curY);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (viewHolder.getItemViewType() == 0 && folder) {
            if (count == 1) {
                if (dY > 90 || dY < -90) {
                    if (dY > 90) { jos = true; sus = false; }
                    if (dY < - 90){ jos = false; sus = true; }

                    viewHolder.itemView.findViewById(R.id.folder_over).setVisibility(View.VISIBLE);
                    Log.i("start", dY + "ok");
                    makefolder = true;
                } else {
                    viewHolder.itemView.findViewById(R.id.folder_over).setVisibility(View.INVISIBLE);
                    count = 0;
                    Log.i("start", dY + "not ok");
                    makefolder = false;
                }
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition(), rv1);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemSelected();
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    Context context;
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        // Recreate intial state
        if (viewHolder.getItemViewType() == 0) {
            viewHolder.itemView.findViewById(R.id.folder_over).setVisibility(View.INVISIBLE);
        }
        count = 0;

        // Set Correct Position
        position_make_folder = position_make_folder_temp;
        // Set Correct Position
        if (jos == true) {
            folder_pos_temp= folder_pos_temp - 1;
            folder_pos = folder_pos_temp;
        }
        else folder_pos= folder_pos_temp;

        if (jos == true) {
            folder_curr_temp= folder_curr_temp - 1;
            folder_curr = folder_curr_temp;
        }
        else folder_curr= folder_curr_temp;

        // Add To Folder
        if (makefolder) {
            Button InboxButon = (Button) ((MainActivity) touchContext).findViewById(R.id.folder_trans);
            InboxButon.performClick();
        }
        makefolder = false;

        ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
        itemViewHolder.onItemClear();
    }
}
