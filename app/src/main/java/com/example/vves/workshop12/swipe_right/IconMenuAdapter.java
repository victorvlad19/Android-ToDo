package com.example.vves.workshop12.swipe_right;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vves.workshop12.R;
import com.skydoves.powermenu.MenuBaseAdapter;

/**
 * Created by vves on 07.11.2017.
 */

public class IconMenuAdapter extends MenuBaseAdapter<IconPowerMenuItem> {

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_icon_menu, viewGroup, false);
        }

        IconPowerMenuItem item = (IconPowerMenuItem) getItem(index);
        final ImageView icon = view.findViewById(R.id.item_icon);
        icon.setImageDrawable(item.getIcon());
        final TextView title = view.findViewById(R.id.item_title);
        title.setText(item.getTitle());
        return view;
    }
}