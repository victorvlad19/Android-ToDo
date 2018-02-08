package com.example.vves.workshop12;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.example.vves.workshop12.dev.ItemTouchHelperAdapter;
import com.example.vves.workshop12.dev.ItemTouchHelperViewHolder;

import static android.R.attr.padding;
import static android.R.attr.resource;
import static android.R.attr.width;

/**
 * Created by vves on 09.09.2017.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {



    private final ItemTouchHelperAdapter mAdapter;
    private RecyclerView rv;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, RecyclerView rv) {
        mAdapter = adapter;
        this.rv=rv;
    }



    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags =  ItemTouchHelper.END | ItemTouchHelper.START;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition(), rv);
        return true;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int i) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition(),rv);
    }


    private Context context;
    private Paint iconPaint;
    private Bitmap leftIcon = null;
    private int iconMarginPx;


    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 180;

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        Resources r = recyclerView.getResources();
        iconMarginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Get RecyclerView item from the ViewHolder
            View itemView = viewHolder.itemView;

            //Drawable drawable= ContextCompat.getDrawable(rv.getContext(),R.drawable.ic_cancel_black_24dp);

            Paint p = new Paint();
            p.setARGB(255, 255, 50, 50);

            iconPaint = new Paint();
            iconPaint.setColorFilter(new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN));

            //Bitmap bitmap = Bitmap.createBitmap(icon.getIntrinsicWidth(),icon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);


            RectF destination = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);

            //leftIcon = drawableToBitmap(drawable);
            //c.drawBitmap(leftIcon, itemView.getLeft() + iconMarginPx, itemView.getBottom() + itemView.getTop() - leftIcon.getHeight(), iconPaint);

            //Drawable vectorDrawable = ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.ic_cancel_black_24dp, null);
            Drawable d = rv.getResources().getDrawable(R.drawable.ic_cancel_black_24dp);
            Bitmap icon = drawableToBitmap(d);

            RectF icon_dest=new RectF(
                    (float)itemView.getRight()-2*width,
                    (float)itemView.getTop()+width,
                    (float)itemView.getRight() - width,
                    (float)itemView.getBottom() - width);
            c.drawBitmap(icon,null,icon_dest,iconPaint);
            c.drawText("Sterge", itemView.getLeft(), 2, iconPaint);

            if (dX > 0) {
            /* Set your color for positive displacement */



                // Draw Rect with varying right side, equal to displacement dX
                //c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), p);


            } else {
            /* Set your color for negative displacement */
                //icon = BitmapFactory.decodeResource(rv.getResources(), R.drawable.ic_cancel_black_24dp);
                //leftIcon = drawableToBitmap(drawable);

                //c.drawBitmap(leftIcon, itemView.getLeft() + iconMarginPx, itemView.getBottom() + itemView.getTop() - leftIcon.getHeight() >> 1, iconPaint);
                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                //c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom(), p);

            }






            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemSelected();
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
        itemViewHolder.onItemClear();
    }


    
}