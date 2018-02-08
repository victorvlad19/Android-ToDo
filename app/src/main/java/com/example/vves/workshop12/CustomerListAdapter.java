package com.example.vves.workshop12;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.androidadvance.topsnackbar.TSnackbar;
import com.example.vves.workshop12.dev.ItemTouchHelperAdapter;
import com.example.vves.workshop12.dev.ItemTouchHelperViewHolder;
import com.example.vves.workshop12.dev.OnCustomerListChangedListener;
import com.example.vves.workshop12.dev.OnStartDragListener;
import com.example.vves.workshop12.swipe_right.IconMenuAdapter;
import com.example.vves.workshop12.swipe_right.IconPowerMenuItem;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;

import org.jsoup.Jsoup;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cn.refactor.library.SmoothCheckBox;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.richeditor.RichEditor;
import me.biubiubiu.justifytext.library.JustifyTextView;
import ru.rambler.libs.swipe_layout.SwipeLayout;

import static com.example.vves.workshop12.MainActivity.category_not;
import static com.example.vves.workshop12.MainActivity.customers;
import static com.example.vves.workshop12.MainActivity.note_not;
import static com.example.vves.workshop12.MainActivity.to_do_not;
import static com.example.vves.workshop12.MainActivity.to_do_notification;

/**
 * Created by vves on 09.09.2017.
 */

public class  CustomerListAdapter extends
        RecyclerView.Adapter<CustomerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private List<Customer> mCustomers;
    private Context mContext;
    private OnStartDragListener mDragStartListener;
    private OnCustomerListChangedListener mListChangedListener;
    private DatabaseHelper mDatabaseHelper;
    private SwipeLayout swiped;
    List<IconPowerMenuItem> list_right = new ArrayList<IconPowerMenuItem>();
    HashMap<Integer,Integer> expand_list = new HashMap<>();
    private boolean calendar_data_set = false;

    private final int USER = 0, CATEGORY = 1, NOTE = 2, AUDIO = 3, CAMERA = 4;

    public CustomerListAdapter(List<Customer> customers, Context context,
                               OnStartDragListener dragLlistener,
                               OnCustomerListChangedListener listChangedListener){
        mCustomers = customers;
        mContext = context;
        mDragStartListener = dragLlistener;
        mListChangedListener = listChangedListener;
        mDatabaseHelper = new DatabaseHelper(context);
        right_swipe_set();

    }

    @Override
    public int getItemViewType(int position) {
        if (mCustomers.get(position).getCategory() == 1) { return CATEGORY; }
        if (mCustomers.get(position).getCategory() == 2) { return NOTE; }
        if (mCustomers.get(position).getCategory() == 3) { return AUDIO; }
        if (mCustomers.get(position).getCategory() == 4) {return  CAMERA;}
        else  { return USER;}

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder viewHolder = null;
        switch (viewType) {
            case USER:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_list, parent, false);
                viewHolder = new ItemViewHolder(v1);
                break;
            case CATEGORY:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_header, parent, false);
                viewHolder = new ItemViewHolder(v2);
                break;
            case NOTE:
                View v3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_note_list, parent, false);
                viewHolder = new ItemViewHolder(v3);
                break;
            case AUDIO:
                View v4 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_audio_list, parent, false);
                viewHolder = new ItemViewHolder(v4);
                break;
            case CAMERA:
                View v5 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_camera_list, parent, false);
                viewHolder = new ItemViewHolder(v5);
                break;
        }

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        final Customer selectedCustomer = mCustomers.get(position);
        final int public_pos = position;




        switch (holder.getItemViewType()) {
            case USER:
                holder.customerName.setText(selectedCustomer.getName());
                holder.customerEmail.setText(selectedCustomer.getEmailAddress());

                if (MainActivity.toggledChoices[position] == 1) {
                    mDatabaseHelper.makeTrue(MainActivity.group,position+1);
                    holder.vChecked.setChecked(true,false);
                    holder.customerName.setPaintFlags(holder.customerName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.customerEmail.setPaintFlags(holder.customerEmail.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.customerName.setTextColor(Color.parseColor("#878585"));
                    holder.customerEmail.setTextColor(Color.parseColor("#878585"));
                } else {
                    Log.i("CheckBox","False");
                    mDatabaseHelper.makeFalse(MainActivity.group,position+1);
                    holder.vChecked.setChecked(false,false);
                    holder.customerName.setPaintFlags(0);
                    holder.customerEmail.setPaintFlags(0);
                    holder.customerName.setTextColor(Color.parseColor("#000000"));
                    holder.customerEmail.setTextColor(Color.parseColor("#7d7d7d"));
                }

                holder.vChecked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MainActivity.toggledChoices[holder.getAdapterPosition()] == 0) {
                            MainActivity.toggledChoices[holder.getAdapterPosition()] = 1;
                        } else {
                            MainActivity.toggledChoices[holder.getAdapterPosition()] = 0;
                        }
                        notifyDataSetChanged();
                    }
                });

                if (selectedCustomer.getDate_pick().equals("")) {
                    holder.picked_calendar.setVisibility(View.GONE);
                }
                else {
                    holder.picked_calendar.setVisibility(View.VISIBLE);
                    holder.picked_calendar.setText(selectedCustomer.getDate_pick());
                }
                holder.picked_money.setVisibility(View.GONE);


                break;
            case CATEGORY:
                holder.category_name.setText(selectedCustomer.getName());
                // De scris cod
                break;
            case NOTE:

                holder.note_name_title.setText(selectedCustomer.getName());
                //holder.expTv1.setText(selectedCustomer.getEmailAddress());
                holder.note_content_remake.setHtml(selectedCustomer.getEmailAddress());
                holder.note_content_remake.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        return true;
                    }
                });
                holder.note_content_remake.setEditorFontSize(15);
                holder.note_content_remake.setOnClickListener(null);
                holder.note_content_remake.setOnLongClickListener(null);
                holder.note_content_remake.setClickable(false);
                holder.note_content_remake.setFocusable(false);
                holder.note_content_remake.setLongClickable(false);
                Log.i ("Contetent Height", String.valueOf(Jsoup.parse(holder.note_content_remake.getHtml()).text().length()) );

                if (Jsoup.parse(holder.note_content_remake.getHtml()).text().length() <= 156) {
                    holder.expand.setVisibility(View.GONE);
                    holder.invisible_toggle.setVisibility(View.GONE);
                }

                holder.invisible_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (customers.get(holder.getAdapterPosition()).getCheck_state() == 0) {
                            holder.expand.setRotation(-90);
                            holder.note_container.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                            customers.get(holder.getAdapterPosition()).setCheck_state(1);
                        }
                        else {
                            holder.note_container.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 400));
                            holder.expand.setRotation(0);
                            customers.get(holder.getAdapterPosition()).setCheck_state(0);
                        }
                    }
                });

                holder.note_name_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent noteview = new Intent(mContext, NoteActivity.class);
                        noteview.putExtra("notename", selectedCustomer.getName());
                        noteview.putExtra("nodedescp", selectedCustomer.getEmailAddress());
                        mContext.startActivity(noteview);
                        ((Activity)mContext).finish();
                    }
                });

                break;

            case AUDIO:
                holder.audio_name.setText(selectedCustomer.getName());
                holder.audio_descp.setText(selectedCustomer.getEmailAddress());


                    holder.play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainActivity.picked_node = holder.getAdapterPosition();
                            Button InboxButon = (Button) ((MainActivity) mContext).findViewById(R.id.public_play);
                            InboxButon.performClick();
                        }
                    });


                break;
            case CAMERA:
                holder.circle.setImageBitmap(selectedCustomer.getImage());
                // DE SCRIS COD
                break;
        }

        if (holder.getItemViewType() == USER || holder.getItemViewType() == CATEGORY || holder.getItemViewType() == NOTE || holder.getItemViewType() == AUDIO) {
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
                    if (!moveToRight) {
                        if (holder.getItemViewType() == USER) {
                            holder.content_view.setBackgroundColor(Color.parseColor("#f5f5f5"));

                            if (selectedCustomer.getDate_pick().equals("")){
                                right_swipe_set();
                                calendar_data_set = false;
                            }
                            else {
                                calendar_data_set = true;
                                right_swipe_set_nocal();
                            }

                            final CustomPowerMenu customPowerMenu = new CustomPowerMenu.Builder<>(mContext, new IconMenuAdapter())
                                    .addItemList(list_right)
                                    .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
                                    .setMenuRadius(0f)
                                    .setWith(700)
                                    .setBackgroundAlpha(0f)
                                    .setMenuShadow(15f)
                                    .build();
                            customPowerMenu.showAsDropDown(holder.itemView,0,-20);

                            customPowerMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                                @Override
                                public void onItemClick(int position, Object item) {
                                    customPowerMenu.dismiss();
                                    if (position == 3 && !calendar_data_set){
                                        MainActivity.picked_node = holder.getAdapterPosition();
                                        MainActivity.calendar_right_state = 0;
                                        Button InboxButon = (Button) ((MainActivity) mContext).findViewById(R.id.set_date);
                                        InboxButon.performClick();

                                    }
                                    if (position == 3 && calendar_data_set){
                                        MainActivity.picked_node = holder.getAdapterPosition();
                                        MainActivity.calendar_right_state = 1;
                                        Button InboxButon = (Button) ((MainActivity) mContext).findViewById(R.id.set_date);
                                        InboxButon.performClick();

                                    }
                                    if (position == 4 && calendar_data_set){
                                        MainActivity.picked_node = holder.getAdapterPosition();
                                        MainActivity.calendar_right_state = 2;
                                        Button InboxButon = (Button) ((MainActivity) mContext).findViewById(R.id.set_date);
                                        InboxButon.performClick();

                                    }


                                    holder.content_view.setBackgroundColor(Color.WHITE);
                                }
                            });

                            customPowerMenu.setOnBackgroundClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    customPowerMenu.dismiss();
                                    holder.content_view.setBackgroundColor(Color.WHITE);
                                }
                            });

                        }
                        swiped.animateReset();
                    }
                }

                @Override
                public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {

                }

                @Override
                public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {

                }
            });

            if (holder.getItemViewType() == USER || holder.getItemViewType() == CATEGORY || holder.getItemViewType() == NOTE || holder.getItemViewType() == AUDIO) {
                holder.sterge_trans.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sterge(holder, position);
                    }
                });
            }

        }


        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (MotionEventCompat.getActionMasked(event)){
                    case MotionEvent.ACTION_DOWN:
                        MainActivity.refreshLayout.setEnableRefresh(false);
                        MainActivity.refreshLayout.setEnableLoadmore(false);
                        MainActivity.refreshLayout.setEnableNestedScroll(false);
                        MainActivity.refreshLayout.setEnableAutoLoadmore(false);
                        MainActivity.refreshLayout.setEnableOverScrollDrag(false);
                        mDragStartListener.onStartDrag(holder);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });

    }
    public void sterge (final ItemViewHolder holder, final int position){
        final Customer lastRemovedItem = mCustomers.get(position);
        final int lastindex = position;
        mCustomers.remove(position);
        swiped.animateReset();
        notifyDataSetChanged();
        final boolean[] ok = {false};
        String text = "";

        if (holder.getItemViewType() == 0){
            text = " To-do sters ";
            to_do_notification = Integer.parseInt(to_do_not.getText().toString());
            to_do_notification--;
            MainActivity.to_do_not.setText(String.valueOf(to_do_notification));
        }
        if (holder.getItemViewType() == 1) {
            text = " Categorie stearsa ";
            MainActivity.category_notification = Integer.parseInt(category_not.getText().toString());
            MainActivity.category_notification --;
            MainActivity.category_not.setText(String.valueOf(MainActivity.category_notification));
        }
        if (holder.getItemViewType() == 2) {
            text = " Nota stearsa ";
            MainActivity.note_notification = Integer.parseInt(note_not.getText().toString());
            MainActivity.note_notification --;
            MainActivity.note_not.setText(String.valueOf(MainActivity.note_notification));
        }
        if (holder.getItemViewType() == 3){
            text = " Inregistrare stearsa ";
        }


        TSnackbar snackbar1 = TSnackbar.make(holder.itemView, text, TSnackbar.LENGTH_LONG);
        snackbar1.setActionTextColor(Color.WHITE);
        snackbar1.setIconLeft(R.drawable.ic_delete_forever_black_24dp, 24);

        Button InboxButon = (Button) ((MainActivity) mContext).findViewById(R.id.checkplaceholder);
        InboxButon.performClick();

        snackbar1.setAction("Anuleaza", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomers.add(lastindex, lastRemovedItem);
                ok[0] = true;
                notifyDataSetChanged();

                Button InboxButon = (Button) ((MainActivity) mContext).findViewById(R.id.checkplaceholder);
                InboxButon.performClick();
                if (holder.getItemViewType() == 0) {
                    to_do_notification++;
                    MainActivity.to_do_not.setText(String.valueOf(to_do_notification));
                }
                if (holder.getItemViewType() == 1) {
                    MainActivity.category_notification++;
                    MainActivity.category_not.setText(String.valueOf(MainActivity.category_notification));
                }
                if (holder.getItemViewType() == 2) {
                    MainActivity.note_notification++;
                    MainActivity.note_not.setText(String.valueOf(MainActivity.note_notification));
                }
            }
        });
        View snackbarView = snackbar1.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#006ecf"));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar1.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ok[0] == false) {
                    mDatabaseHelper.deleteData(MainActivity.group, position + 1);
                } else ok[0] = false;
                notifyDataSetChanged();
            }
        }, 2700);
    }


    @Override
    public int getItemCount() {
        return mCustomers.size();
    }
    public void right_swipe_set (){
        // Add Items To Right Click
        list_right.clear();
        list_right.add(new IconPowerMenuItem(mContext.getResources().getDrawable(R.drawable.ic_arrow_forward_black_24dp), "Muta in alt proiect"));
        list_right.add(new IconPowerMenuItem(mContext.getResources().getDrawable(R.drawable.ic_content_copy_black_24dp), "Copiza titlul"));
        list_right.add(new IconPowerMenuItem(mContext.getResources().getDrawable(R.drawable.ic_priority_high_black_24dp), "Seteaza prioritatea"));
        list_right.add(new IconPowerMenuItem(mContext.getResources().getDrawable(R.drawable.ic_access_alarm_black_24dp), "Seteaza data in calendar"));
        list_right.add(new IconPowerMenuItem(mContext.getResources().getDrawable(R.drawable.ic_monetization_on_black_24dp), "Steaza pretul"));
    }
    public void right_swipe_set_nocal (){
        // Add Items To Right Click
        list_right.clear();
        list_right.add(new IconPowerMenuItem(mContext.getResources().getDrawable(R.drawable.ic_arrow_forward_black_24dp), "Muta in alt proiect"));
        list_right.add(new IconPowerMenuItem(mContext.getResources().getDrawable(R.drawable.ic_content_copy_black_24dp), "Copiza titlul"));
        list_right.add(new IconPowerMenuItem(mContext.getResources().getDrawable(R.drawable.ic_priority_high_black_24dp), "Seteaza prioritatea"));
        list_right.add(new IconPowerMenuItem(mContext.getResources().getDrawable(R.drawable.ic_access_alarm_black_24dp), "Schimba data din calendar"));
        list_right.add(new IconPowerMenuItem(mContext.getResources().getDrawable(R.drawable.ic_access_alarm_black_24dp), "Sterge data din calendar"));
        list_right.add(new IconPowerMenuItem(mContext.getResources().getDrawable(R.drawable.ic_monetization_on_black_24dp), "Steaza pretul"));
    }

    public static void swap(boolean[] arr, int x, int z){
        boolean temp = arr[x];
        arr[x]=arr[z];
        arr[z]=temp;
    }

    public static void swapcheck(int[] arr, int x, int z){
        int temp = arr[x];
        arr[x]=arr[z];
        arr[z]=temp;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition, RecyclerView rv1) {
        mDatabaseHelper.swapData(MainActivity.group,fromPosition+1, toPosition+1);

        Collections.swap(mCustomers, fromPosition, toPosition);
        swapcheck(MainActivity.toggledChoices,fromPosition,toPosition);
        mListChangedListener.onNoteListChanged(mCustomers);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(final int position, final RecyclerView rv) {
        final Customer lastRemovedItem = mCustomers.get(position);
        final int lastindex = position;
        mCustomers.remove(position);
        final boolean[] ok = {false};
        notifyDataSetChanged();

        Snackbar snackbar = Snackbar
                .make(rv, "Element sters " , Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCustomers.add(lastindex,lastRemovedItem);
                        ok[0] = true;
                        notifyDataSetChanged();
                    }
                });


        snackbar.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ok[0] == false) {
                    mDatabaseHelper.deleteData(MainActivity.group, position + 1);
                }
                else ok[0] = false;
                notifyDataSetChanged();
            }
        }, 2700);
    }

    @Override
    public void onItemDismiss(int position) {

    }


    public final class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        public final TextView customerName;
        public final TextView customerEmail;
        public final TextView  picked_money,picked_calendar;
        public TextView category_name, note_name_title;
        public final ImageView handleView,play,expand;
        public final SmoothCheckBox vChecked;
        public ExpandableTextView expTv1;
        JustifyTextView note_name;
        public final TextView audio_name, audio_descp;
        public final SwipeLayout swipeLayout;
        public final Button sterge_trans, invisible_toggle;
        public final RelativeLayout content_view, note_container;
        public final CircleImageView circle;
        public final RichEditor note_content_remake;

        public ItemViewHolder(View itemView) {
            super(itemView);
            customerName = (TextView)itemView.findViewById(R.id.sidebar_item_name);
            customerEmail = (TextView)itemView.findViewById(R.id.mytododesc);
            picked_calendar = (TextView) itemView.findViewById(R.id.date_pick);
            picked_money = (TextView)itemView.findViewById(R.id.money_pick);
            handleView = (ImageView)itemView.findViewById(R.id.handle);
            vChecked = (SmoothCheckBox)itemView.findViewById(R.id.checkBox);
            category_name = (TextView) itemView.findViewById(R.id.category_name);
            note_name = (JustifyTextView) itemView.findViewById(R.id.expandable_text);
            note_name_title = (TextView) itemView.findViewById(R.id.titlu_nota);

            note_content_remake = (RichEditor) itemView.findViewById(R.id.note_content_remake);
            note_container = (RelativeLayout) itemView.findViewById(R.id.note_container);
            expand = (ImageView) itemView.findViewById(R.id.expand_button);
            invisible_toggle = (Button) itemView.findViewById(R.id.invisible_toggle);

            audio_name = (TextView) itemView.findViewById(R.id.sidebar_item_name);
            audio_descp  = (TextView) itemView.findViewById(R.id.audio_desc_1);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swiped_layout);
            sterge_trans = (Button) itemView.findViewById(R.id.sterge_trans);
            play = (ImageView)  itemView.findViewById(R.id.iconItem_play);
            content_view = (RelativeLayout) itemView.findViewById(R.id.content_view);
            circle = (CircleImageView) itemView.findViewById(R.id.profile_image);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.parseColor("#f5f5f5"));
            if (getItemViewType() == NOTE) {
                itemView.findViewById(R.id.notefooter).setBackgroundColor(Color.parseColor("#f5f5f5"));
            }
        }

        @Override
        public void onItemClear() {

            itemView.setBackgroundColor(Color.WHITE);
            if (getItemViewType() == NOTE) {
                itemView.findViewById(R.id.notefooter).setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }




    }




}
