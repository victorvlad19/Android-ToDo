package com.example.vves.workshop12.money;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vves.workshop12.R;

/**
 * Created by vves on 20.11.2017.
 */

public class Buget extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.money_buget, container, false);
    }
}
