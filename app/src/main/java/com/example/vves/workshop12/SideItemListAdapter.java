package com.example.vves.workshop12;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidadvance.topsnackbar.TSnackbar;
import com.example.vves.workshop12.dev.ChildItemClickListener;
import com.example.vves.workshop12.dev.ItemTouchHelperAdapter;
import com.example.vves.workshop12.dev.ItemTouchHelperViewHolder;
import com.example.vves.workshop12.dev.OnCustomerListChangedListener;
import com.example.vves.workshop12.dev.OnStartDragListener;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
        import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;

import static com.example.vves.workshop12.MainActivity.group;
import static com.example.vves.workshop12.MainActivity.group_item;


public class SideItemListAdapter extends
        RecyclerView.Adapter<SideItemListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private List<SideItem> mSideItem;
    private Context mContext;
    private OnStartDragListener mDragStartListener;
    private OnCustomerListChangedListener mListChangedListener;
    private ChildItemClickListener listener;
    private DatabaseHelper mDatabaseHelper;
    private SwipeLayout swiped;


    private final int NORMAL = 0, FOLDER = 1;

    public SideItemListAdapter(List<SideItem> customers, Context context,
                               OnStartDragListener dragLlistener,
                               OnCustomerListChangedListener listChangedListener) {
        mSideItem = customers;
        mContext = context;
        mDragStartListener = dragLlistener;
        mListChangedListener = listChangedListener;
        mDatabaseHelper = new DatabaseHelper(context);
    }

    @Override
    public int getItemViewType(int position) {
        Log.i("Here","Ok");
        if (mSideItem.get(position).getCategory() == 1) { return FOLDER; }
        else  { return NORMAL;}

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder viewHolder = null;
        switch (viewType) {
            case NORMAL:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.sideitem_proiect, parent, false);
                viewHolder = new ItemViewHolder(v1);
                break;
            case FOLDER:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.sideitem_folder, parent, false);
                viewHolder = new ItemViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    boolean okstate = false;
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        final SideItem selectedCustomer = mSideItem.get(position);

        switch (holder.getItemViewType()) {
            case NORMAL:
                holder.itemNameProject.setText(selectedCustomer.getNameitem());

                // Edit
                holder.edit_side.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialDialog.Builder(mContext)
                                .positiveText("Gata")
                                .positiveColorRes(R.color.colorShadow)
                                .neutralColorRes(R.color.colorShadow)
                                .negativeColorRes(R.color.colorShadow)
                                .widgetColorRes(R.color.colorShadow)
                                .contentColor(mContext.getResources().getColor(R.color.colorShadow))
                                .content("Schimba numele proiectului")
                                .negativeText("Inchide")
                                .inputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                                .input("La ce te gandesti ?", selectedCustomer.getNameitem(), new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                        if (!TextUtils.isEmpty(input) && !input.toString().equals(selectedCustomer.getNameitem()) ) {
                                            if (MainActivity.group.equals(selectedCustomer.getNameitem())) {
                                                TextView titlebarTitle = (TextView)  ((MainActivity) mContext).findViewById(R.id.titlebarTitle);
                                                titlebarTitle.setText(input.toString());
                                                MainActivity.group = selectedCustomer.getNameitem();
                                            }
                                            mSideItem.set(position, new SideItem((long) 0, input.toString(), R.drawable.ic_layers_black_24dp, 0, 2, group_item));
                                            mDatabaseHelper.renameRow(input.toString(), position+1);
                                            mDatabaseHelper.renameTable(selectedCustomer.getNameitem(), input.toString());
                                            notifyDataSetChanged();
                                        }
                                    }
                                })
                                .show();
                        holder.swipeLayout.animateReset();
                    }
                });

                // Remove
                holder.sterge_side.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final SideItem lastRemovedItem = mSideItem.get(position);
                        final int lastindex = position;
                        mSideItem.remove(position);
                        okstate = false;

                        if (selectedCustomer.getNameitem()  == group) {
                            Button InboxButon = (Button) ((MainActivity) mContext).findViewById(R.id.InboxTrasparent);
                            InboxButon.performClick();
                        }

                        notifyDataSetChanged();

                        TSnackbar snackbar1 = TSnackbar.make(holder.itemView, "Proiect sters ", TSnackbar.LENGTH_LONG);
                        snackbar1.setActionTextColor(Color.BLACK);
                        snackbar1.setIconLeft(R.drawable.ic_delete_forever_black_24dp, 24);

                        Button InboxButon = (Button) ((MainActivity) mContext).findViewById(R.id.checkplaceholder);
                        InboxButon.performClick();

                        snackbar1.setAction("Anuleaza", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mSideItem.add(lastindex,lastRemovedItem);
                                okstate = true;
                                notifyDataSetChanged();
                            }
                        });
                        View snackbarView = snackbar1.getView();
                        snackbarView.setBackgroundColor(Color.WHITE);
                        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                        textView.setTextColor(Color.BLACK);
                        snackbar1.show();


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (okstate == false) {
                                    Log.i ("AM False","Am ajuns aici");
                                    mDatabaseHelper.deleteDataID(selectedCustomer.getNameitem());
                                    mDatabaseHelper.removeTable(selectedCustomer.getNameitem());
                                }
                                else okstate = false;
                                notifyDataSetChanged();
                            }
                        }, 2700);
                        holder.swipeLayout.animateReset();
                    }
                });

                holder.swipeLayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
                    @Override
                    public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {
                        if (swiped != null && swiped != swipeLayout) {
                            swiped.animateReset();
                        }
                        swiped = swipeLayout;
                    }

                    @Override
                    public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {

                    }

                    @Override
                    public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {

                    }

                    @Override
                    public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {

                    }
                });


                break;

            case FOLDER:
                // Scrie cod
                holder.folderNameProject.setText(selectedCustomer.getNameitem());
                Log.i ("Folder", "Am ajuns aici");
                break;
        }

        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                    folder_curr_name = mSideItem.get(holder.getAdapterPosition()).getNameitem();
                    Log.i ("Handle", mSideItem.get(holder.getAdapterPosition()).getNameitem());
                }

                return true;
            }
        });
    }

    public static String folder_curr_name;

    public void setChildItemClickListener(ChildItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mSideItem.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition, RecyclerView rv1) {
        SimpleItemTouchHelperCallbackItem.count = 0;
        SimpleItemTouchHelperCallbackItem.makefolder = false;
        Log.i("Trecere","True");
        if (rv1.findViewHolderForAdapterPosition(fromPosition).getItemViewType() == 0) {
            rv1.findViewHolderForAdapterPosition(fromPosition).itemView.findViewById(R.id.folder_over).setVisibility(View.INVISIBLE);
        }
        mDatabaseHelper.swapData("item_table",fromPosition+1, toPosition+1);
        Collections.swap(mSideItem, fromPosition, toPosition);
        mListChangedListener.onNoteListChangedSide(mSideItem);

        notifyItemMoved(fromPosition, toPosition);
    }




    @Override
    public void onItemDismiss(int position, RecyclerView rv) {

    }

    @Override
    public void onItemDismiss(int position) {

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        //public final TextView customerName, customerEmail;
        //public final ImageView handleView, profileImage;
        public  final  TextView itemName, itemCount;
        public final Button itemNameProject, folderNameProject;
        public final ImageView iconItem;
        public final ImageView handleView;
        SwipeLayout swipeLayout;
        public final Button InboxButon, edit_side, sterge_side;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemName = (TextView) itemView.findViewById(R.id.sidebar_item_name);
            iconItem = (ImageView) itemView.findViewById(R.id.iconItem_play);
            itemCount = (TextView) itemView.findViewById(R.id.bageItem);
            itemNameProject = (Button) itemView.findViewById(R.id.sidebar_item_project_name);
            folderNameProject = (Button) itemView.findViewById(R.id.folder_name);
            handleView = (ImageView)itemView.findViewById(R.id.handleside);
            InboxButon = (Button) itemView.findViewById(R.id.InboxButon);
            edit_side = (Button) itemView.findViewById(R.id.edit_side);
            sterge_side = (Button) itemView.findViewById(R.id.sterge_side);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swiped_layout_side);

        }

        @Override
        public void onItemSelected() {
            if (getItemViewType() == 0){ itemView.findViewById(R.id.projcontent).setBackgroundColor(Color.parseColor("#FFF5F5F5")); }
            if (getItemViewType() == 1) { itemView.findViewById(R.id.folder_new).setBackgroundColor(Color.parseColor("#FFF5F5F5")); }
        }

        @Override
        public void onItemClear() {
            if (getItemViewType() == 0){ itemView.findViewById(R.id.projcontent).setBackgroundColor(Color.parseColor("#ffffff")); }
            if (getItemViewType() == 1) { itemView.findViewById(R.id.folder_new).setBackgroundColor(Color.parseColor("#eeeeee")); }
        }
    }
}