package com.example.vves.workshop12.dev;

import com.example.vves.workshop12.Customer;
import com.example.vves.workshop12.SideItem;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.util.List;

public interface OnCustomerListChangedListener {


    void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second);

    void onNoteListChanged(List<Customer> customers);
    void onNoteListChangedSide(List<SideItem> sideItems);
}
