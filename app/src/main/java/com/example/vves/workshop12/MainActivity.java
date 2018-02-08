package com.example.vves.workshop12;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.vves.workshop12.calendar.CalendarHeader;
import com.example.vves.workshop12.calendar.CurrentDateDecorator;
import com.example.vves.workshop12.calendar.DecorateDay;
import com.example.vves.workshop12.dev.OnCustomerListChangedListener;
import com.example.vves.workshop12.dev.OnStartDragListener;
import com.example.vves.workshop12.money.Buget;
import com.example.vves.workshop12.money.Overview;
import com.github.zagum.switchicon.SwitchIconView;
import com.greysonparrelli.permiso.Permiso;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;
import com.jetradar.desertplaceholder.DesertPlaceholder;
import com.mancj.slideup.SlideUp;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.roughike.bottombar.TabSelectionInterceptor;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragListener;



import org.apache.commons.lang3.text.WordUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import io.paperdb.Paper;
import jp.wasabeef.richeditor.RichEditor;


import static android.widget.Toast.LENGTH_SHORT;
import static com.example.vves.workshop12.SideItemListAdapter.folder_curr_name;
import static com.example.vves.workshop12.SimpleItemTouchHelperCallbackItem.folder_pos;
import static com.example.vves.workshop12.SimpleItemTouchHelperCallbackItem.position_make_folder;




public class MainActivity extends AppCompatActivity
        implements OnCustomerListChangedListener,
        OnStartDragListener, com.philliphsu.bottomsheetpickers.date.DatePickerDialog.OnDateSetListener {


    static String group = "Inbox";
    static String group_item = "Proiecte";
    static List<Customer> customers = new ArrayList<Customer>();
    public static int picked_node;
    String curent_folder_item;
    List<String> brainstormItems = new ArrayList<String>();
    public static int[] toggledChoices = new int[100000];
    private HashSet<CalendarDay> dates =new HashSet<>();
    private ArrayList<String> dates_count = new ArrayList<>();
    private ArrayList<String> record_count = new ArrayList<>();

    public static int calendar_right_state = 0;
    SlideUp slideUp;

    private int i=0;

    // RECORDER
    Boolean recstate = false;
    Boolean playstate = false;
    Boolean pausestate = false;
    Boolean stop = false;
    private MediaRecorder recorder;
    private String externalStoragePath,externalOutputPath, storagePath;
    int mediaMax;
    int mediaPos = 0;
    Handler handler = new Handler();
    SeekBar seekBar = null;
    MediaPlayer mediaPlayer = null;
    Integer length = 0;
    TextView timepassed;
    int counter = 0;
    Chronometer cronos;
    // END RECORDER

    private RecyclerView mRecyclerView;
    private CustomerListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private  List<Customer> mCustomers;
    private long nr=0;
    private Integer modeint = 0;
    MaterialSearchView searchView;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private boolean calendar_switch_state = false;
    private boolean category_switch_state = false;
    private boolean note_switch_state = false;
    private boolean recorder_switch_state = false;
    private boolean search_switch_state = false;
    private Integer notestate = 0;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public static final String LIST_OF_SORTED_DATA_ID = "json_list_sorted_data_id";
    public final static String PREFERENCE_FILE = "preference_file";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private boolean state=false;
    public static boolean  calendar_global = false;
    BottomBar bottomBar;

    DatabaseHelper mDatabaseHelper;
    Context context;
    Bundle extras;
    MaterialCalendarView mcv;
    Toolbar mToolbar;
    private SlidingRootNav slidingRootNav;

    SlidingUpPanelLayout supl ;
    SlidingUpPanelLayout note_slide;

    private static final String TAG = "MainActivity";
    public String  picked_date;
    boolean date_remove_switch = false;

    // Notification area
    static int category_notification = 0;
    static int to_do_notification = 0;
    static int note_notification = 0;
    static int audio_notification = 0;
    static int camera_notification = 0;
    static TextView category_not;
    static TextView to_do_not;
    static TextView note_not;
    static TextView audio_not;
    static TextView camera_not;

    static RefreshLayout refreshLayout;
    Boolean isExpanded = false;
    Boolean isCollapsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);



        statsusBar();
        createBar();
        topslidingbar();
        notification_init();
        mDatabaseHelper = new DatabaseHelper(this);
        calendarCreate();
        populateListView();
        setupRecyclerView();
        recorderCreate();
        sideBar();
        populateListViewItem();
        populateListViewCH();
        updatePopUpMenuItemLayout();
        brainstormingview();
        calendarRightClick();

        this.setFinishOnTouchOutside(true);

        dates = Paper.book().read("Calendar_Dates", new HashSet<CalendarDay>());
        mcv.addDecorator(new DecorateDay(this,dates));

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Overview", Overview.class)
                .add("Buget", Buget.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);




    }

    private void brainstormingview () {
        ImageButton brainstormButton = (ImageButton) findViewById(R.id.brainstormButton);
        final AdvanceDrawerLayout drawer = (AdvanceDrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navigationView_notif = (NavigationView) findViewById(R.id.nav_view_notification);


        // Open Brainstorming Button
        brainstormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(navigationView_notif,true);
            }
        });

    }

    private void topslidingbar () {

        // Set no click
        supl = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        RelativeLayout drag = (RelativeLayout) findViewById(R.id.dragViewVves);
        supl.setDragView(drag);
        new ControllableSlidingLayout(this).setDragView(drag);
        supl.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener()
        {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // Title
                TextView title = (TextView) findViewById(R.id.titlebarTitle);
                ObjectAnimator animation = ObjectAnimator.ofFloat(title, "translationX", -slideOffset*120f);
                animation.setDuration(400);
                animation.start();
                // Drop Arrow
                ImageView arrow = (ImageView) findViewById(R.id.drop_arrow);
                ObjectAnimator animation3 = ObjectAnimator.ofFloat(arrow, "translationX", -slideOffset*120f);
                animation3.setDuration(400);
                animation3.start();
                ObjectAnimator animation4 = ObjectAnimator.ofFloat(arrow, "alpha", slideOffset);
                animation4.setDuration(400);
                animation4.start();

                // Drawer Button
                RelativeLayout drawerButton = (RelativeLayout) findViewById(R.id.drawerButton);
                ObjectAnimator animation1 = ObjectAnimator.ofFloat(drawerButton, "translationX", -slideOffset*120f);
                animation1.setDuration(400);
                animation1.start();
                // Icons
                RelativeLayout icons_top = (RelativeLayout) findViewById(R.id.icons_top);
                ObjectAnimator animation2 = ObjectAnimator.ofFloat(icons_top, "translationX", slideOffset*380f);
                animation2.setDuration(400);
                animation2.start();
                // Content Animation
                ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
                ObjectAnimator animation5 = ObjectAnimator.ofFloat(viewpager, "alpha", slideOffset);
                animation5.setDuration(400);
                animation5.start();

                // Set Text
                if (slideOffset > 0.5){ title.setText("Personal" ); }
                else { title.setText(group); }

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED)
                {
                    isExpanded = true;
                    isCollapsed = false;
                }
                else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                {
                    isExpanded = false;
                    isCollapsed = true;
                }
                else if (newState == SlidingUpPanelLayout.PanelState.ANCHORED)
                {
                    if (isExpanded )
                    {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                supl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            }
                        },100);
                    }else if (isCollapsed )
                    {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                supl.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                            }
                        },100);
                    }
                }
            }
        });



    }

    // 3. PLACEHOLDER
    private void show_placeholder () {
        DesertPlaceholder placeholder_gol = (DesertPlaceholder) findViewById(R.id.placeholder_gol);
        placeholder_gol.setVisibility(View.VISIBLE);
    }
    private void hide_placeholder () {
        DesertPlaceholder placeholder_gol = (DesertPlaceholder) findViewById(R.id.placeholder_gol);
        placeholder_gol.setVisibility(View.INVISIBLE);
    }
    private void check_placehoder (){
        // Show & Hide Placeholder
        if (customers.isEmpty()) show_placeholder();
        else hide_placeholder();

        Button checkplaceholder = (Button) findViewById(R.id.checkplaceholder);
        checkplaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customers.isEmpty()) show_placeholder();
                else hide_placeholder();
            }
        });
    }

    private void updatePopUpMenuItemLayout() {
        try {
            Field field = MenuPopupHelper.class.getDeclaredField("ITEM_LAYOUT");
            field.setAccessible(true);
            field.set(null, R.layout.popup_menu);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
    }




    @Override
    public void onResume(){
        super.onResume();
        addnote();
        Permiso.getInstance().setActivity(this);
        extras = null;
        TextView titlebarTitle = (TextView) findViewById(R.id.titlebarTitle);
        titlebarTitle.setText(group);
        slidingRootNav.closeMenu();

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.contextual_menu,menu);


        return true;
    }
    public String data_parser(String mydate){
        Date thedate = null;
        String database_date = "";
        Date date = null;

        Date date2 = new Date();

        if (!mydate.equals("")) {
            String dtStart = mydate;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                date = format.parse(dtStart);

                SimpleDateFormat sdf2 = new SimpleDateFormat("D");
                String date_number = sdf2.format(date);
                Log.i ("MyDate",date_number);

                int timeleft = Integer.parseInt(printDifference(date2,date)) +1;
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEE dd.MM "+timeleft);

                database_date = sdf1.format(date)+"z";



            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return database_date;
    }
    public void notification_init () {
        category_not = (TextView) findViewById(R.id.category_count);
        to_do_not = (TextView) findViewById(R.id.todo_count);
        note_not = (TextView) findViewById(R.id.note_count);
        audio_not = (TextView) findViewById(R.id.audio_count);
        camera_not  = (TextView) findViewById(R.id.camera_count);
    }
    public void populateListView(){
        // Notification counters
        int category_notification = 0;
        int to_do_notification = 0;
        int note_notification = 0;
        int audio_notification = 0;
        int camera_notification = 0;

        // Read from database
        customers.clear();
        Cursor data = mDatabaseHelper.getData(group);
        while (data.moveToNext()){
            customers.add(new Customer(nr++,data.getString(1),data.getString(2),data.getInt(3),data.getInt(4),data.getString(6),data_parser(data.getString(5)),data.getString(5),getImage(data.getBlob(7))));
            switch (data.getInt(3)) {
                case 0:
                    to_do_notification ++;
                    break;
                case 1:
                    category_notification ++;
                    break;
                case 2:
                    note_notification ++;
                    break;
                case 3:
                    audio_notification ++;
                    break;
                case 4:
                    camera_notification ++;
                    break;
            }
            //toastMessage(String.valueOf(data.getInt(4)));
        }
        category_not.setText(String.valueOf(category_notification));
        to_do_not.setText(String.valueOf(to_do_notification));
        note_not.setText(String.valueOf(note_notification));
        audio_not.setText(String.valueOf(audio_notification));
        camera_not.setText(String.valueOf(camera_notification));
        check_placehoder();
        mcv.addDecorator(new DecorateDay(this, dates));

    }

    public void populateListViewItem(){
        Cursor dataItem = mDatabaseHelper.getDataItem(group_item);
        while (dataItem.moveToNext()){
            sideItems.add(new SideItem(nr++,dataItem.getString(1),dataItem.getInt(2), dataItem.getInt(3), dataItem.getInt(4), dataItem.getString(5)));

        }
    }


    public void AddData (String newEntry, String Descp, Integer categ, Integer check, String data, String path, byte[] image){
        boolean insertData = mDatabaseHelper.addData(group,newEntry,Descp,categ,check,data,path,image);

    }
    public void AddDataItem (String nameitem, Integer ImagePath, Integer bage, Integer category, String folder_name){
        boolean insertDataItem = mDatabaseHelper.addDataItem(nameitem,ImagePath,bage,category,folder_name);
    }


    private void toastMessage (String message){
        Toast.makeText(this,message, LENGTH_SHORT).show();
    }


    public void addnote (){
        Intent intent = getIntent();

        extras = getIntent().getExtras();
        String notename ="";
        String notedescp = "";
        String mode = "";

        if(extras !=null) {
            notename = extras.getString("notename");
            notedescp = extras.getString("nodedescp");
            mode = extras.getString("mode");

            if (mode != null) {
                modeint = Integer.parseInt(mode);
            }

            if (modeint == 1) {
                notestate = 1;
                categorybuttonbarclick ();
            }

            if (notedescp != null){
                customers.add(new Customer((long)0,notename,notedescp,2,0,"","","",null));

                AddData(notename,notedescp,2,0,"","",new byte[0]);

                // Set Notification Counter
                note_notification = Integer.parseInt(note_not.getText().toString());
                note_notification++;
                note_not.setText(String.valueOf(note_notification));
            }

            if (mCustomers.size() != 0) {
                mRecyclerView.smoothScrollToPosition(mCustomers.size() - 1);
            }
            notename ="";
            notedescp = "";
            mode = null;
            extras = null;
        }
        check_placehoder();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.context3){
            Intent noteview = new Intent(MainActivity.this, NoteActivity.class);
            startActivity(noteview);
            finish();
        }

        if (item.getItemId() == R.id.action_search){
            Log.i ("A mers","A mers");
        }

        return super.onOptionsItemSelected(item);
    }

    private TextView getTitleTextView(){
        Field f = null;
        TextView titleTextView = null;
        try {
            f = mcv.getClass().getDeclaredField("title");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(mcv);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return titleTextView;
    }

    // 1.CALENDAR
    public void openCalendar () {
        calendar_switch_state = true;
        supl.setTouchEnabled(false);

        TextView titlebarTitle = (TextView) findViewById(R.id.titlebarTitle);
        titlebarTitle.setText(WordUtils.capitalize(getTitleTextView().getText().toString()));
        ToggleSwitch CalSwitch = (ToggleSwitch) findViewById(R.id.CalSwitch);
        CalSwitch.setVisibility(View.VISIBLE);
        TextView Cal1 = (TextView) findViewById(R.id.Cal1);
        Cal1.setVisibility(View.VISIBLE);
        TextView Cal2 = (TextView) findViewById(R.id.Cal2);
        Cal2.setVisibility(View.VISIBLE);
        CalSwitch.setCheckedTogglePosition(1);

        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                TextView textViewdate = (TextView) findViewById(R.id.titlebarTitle);
                if (date.getMonth() == 0) textViewdate.setText("Ianuarie " + Integer.toString(date.getYear()));
                if (date.getMonth() == 1) textViewdate.setText("Februarie " + Integer.toString(date.getYear()));
                if (date.getMonth() == 2) textViewdate.setText("Martie " + Integer.toString(date.getYear()));
                if (date.getMonth() == 3) textViewdate.setText("Aprilie " + Integer.toString(date.getYear()));
                if (date.getMonth() == 4) textViewdate.setText("Mai " + Integer.toString(date.getYear()));
                if (date.getMonth() == 5) textViewdate.setText("Iunie " + Integer.toString(date.getYear()));
                if (date.getMonth() == 6) textViewdate.setText("Iulie " + Integer.toString(date.getYear()));
                if (date.getMonth() == 7) textViewdate.setText("August " + Integer.toString(date.getYear()));
                if (date.getMonth() == 8) textViewdate.setText("Septembrie " + Integer.toString(date.getYear()));
                if (date.getMonth() == 9) textViewdate.setText("Octombrie " + Integer.toString(date.getYear()));
                if (date.getMonth() == 10) textViewdate.setText("Noiembrie " + Integer.toString(date.getYear()));
                if (date.getMonth() == 11) textViewdate.setText("Decembrie " + Integer.toString(date.getYear()));
            }
        });


        RelativeLayout rely = (RelativeLayout) findViewById(R.id.RecyclerViewContent);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 547, 0, 144);
        rely.setLayoutParams(params);
        RelativeLayout calendar = (RelativeLayout) findViewById(R.id.calendarViewContainer);
        calendar.setVisibility(View.VISIBLE);


    }
    public void closeCalendar () {
        calendar_switch_state = false;
        supl.setTouchEnabled(true);
        // Animate
        //StartSmartAnimation.startAnimation( findViewById(R.id.calendarViewContainer) , AnimationType.SlideOutUp , 550 , 0 , false );
        //StartSmartAnimation.startAnimation( findViewById(R.id.RecyclerViewContent) , AnimationType.SlideInUp, 300 , 0 , false );

        ToggleSwitch CalSwitch = (ToggleSwitch) findViewById(R.id.CalSwitch);
        CalSwitch.setVisibility(View.INVISIBLE);
        TextView Cal1 = (TextView) findViewById(R.id.Cal1);
        Cal1.setVisibility(View.INVISIBLE);
        TextView Cal2 = (TextView) findViewById(R.id.Cal2);
        Cal2.setVisibility(View.INVISIBLE);

        TextView titlebarTitle = (TextView) findViewById(R.id.titlebarTitle);
        titlebarTitle.setText(group);

        RelativeLayout rely = (RelativeLayout) findViewById(R.id.RecyclerViewContent);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 144);
        rely.setLayoutParams(params);
        RelativeLayout calendar = (RelativeLayout) findViewById(R.id.calendarViewContainer);
        calendar.setVisibility(View.INVISIBLE);

    }
    public void calendarCreate (){
        dates_count = Paper.book().read("Calnedar_Duplicates", new ArrayList<String>());


        RelativeLayout calendar = (RelativeLayout) findViewById(R.id.calendarViewContainer);
        calendar.setVisibility(View.INVISIBLE);

        mcv = (MaterialCalendarView) findViewById(R.id.materialCalendarView);
        mcv.setTopbarVisible(false);
        mcv.setDateTextAppearance(R.style.CustomTextAppearance);
        mcv.setWeekDayTextAppearance(R.style.CustomTextAppearance);
        mcv.addDecorator(new CurrentDateDecorator(this));

        ImageButton calendarbutton = (ImageButton) findViewById(R.id.calendarSwitchButton);
        calendarbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!calendar_switch_state) {
                    openCalendar();
                }
                else {
                    closeCalendar();
                }

            }
        });
    }
    public void calendarRightClick() {
        Button set_date = (Button) findViewById(R.id.set_date);
        set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date1 = null;
                String date_number = " ";
                if (calendar_right_state == 0) {
                    Calendar now = Calendar.getInstance();
                    com.philliphsu.bottomsheetpickers.date.DatePickerDialog date = new com.philliphsu.bottomsheetpickers.date.DatePickerDialog.Builder(
                            MainActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH))
                            .setHeaderColor(Color.parseColor("#0088fe"))
                            .build();
                    date.show(getSupportFragmentManager(), TAG);
                }
                if (calendar_right_state == 1){
                    date_remove_switch = true;
                    Calendar now = Calendar.getInstance();
                    com.philliphsu.bottomsheetpickers.date.DatePickerDialog date = new com.philliphsu.bottomsheetpickers.date.DatePickerDialog.Builder(
                            MainActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH))
                            .setHeaderColor(Color.parseColor("#0088fe"))
                            .build();
                    date.show(getSupportFragmentManager(), TAG);

                }
                if (calendar_right_state == 2){
                    String dbStart = customers.get(picked_node).getReal_data();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    try {
                        date1 = format.parse(dbStart);
                        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
                        date_number = sdf4.format(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    boolean blnExists = dates_count.contains(date_number);
                    Log.i ("DatesPick", String.valueOf(dbStart));
                    if (blnExists) {
                        customers.set(picked_node,new Customer(customers.get(picked_node).getId(),customers.get(picked_node).getName(),customers.get(picked_node).getEmailAddress(),
                                customers.get(picked_node).getCategory(), customers.get(picked_node).getCheck_state(),customers.get(picked_node).getPath(),"","",null));
                        dates_count.remove(date_number);
                        Paper.book().write("Calnedar_Duplicates", dates_count);
                        mDatabaseHelper.updateData(group,picked_node+1,"");
                        Log.i ("Dates2", "Ok");
                    }
                    else {
                        customers.set(picked_node,new Customer(customers.get(picked_node).getId(),customers.get(picked_node).getName(),customers.get(picked_node).getEmailAddress(),
                                customers.get(picked_node).getCategory(), customers.get(picked_node).getCheck_state(),customers.get(picked_node).getPath(),"","",null));
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        Log.i ("Dates2", "Moving");
                        try {
                            Date date = format1.parse(dbStart);
                            dates.remove(CalendarDay.from(date));
                            Paper.book().write("Calendar_Dates",dates);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mDatabaseHelper.updateData(group,picked_node+1,"");
                    }

                }
                mcv.removeDecorators();
                mcv.addDecorator(new CurrentDateDecorator(MainActivity.this));
                Log.i ("DatesDecorator", String.valueOf(dates));
                mcv.addDecorator(new DecorateDay(MainActivity.this, dates));
                mAdapter.notifyDataSetChanged ();
            }
        });

    }



    // Create Bar
    public void createBar (){
        final RelativeLayout textimput = (RelativeLayout) findViewById(R.id.textimput);
        textimput.setVisibility(View.INVISIBLE);


        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root);
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

                if (heightDiff > 400) {
                    Log.e("MyActivity", "keyboard opened");

                    RelativeLayout note_bottom = (RelativeLayout) findViewById(R.id.note_bottom);
                    note_bottom.setVisibility(View.INVISIBLE);
                    RelativeLayout note_tools = (RelativeLayout) findViewById(R.id.note_tools_bottom);
                    note_tools.setVisibility(View.VISIBLE);

                    if (!search_switch_state) {

                        mRecyclerView.setPadding(0, 0, 0, 126);
                        if (mCustomers.size() != 0) {
                            mRecyclerView.smoothScrollToPosition(mCustomers.size() - 1);
                        }

                    }

                    notestate = 0;

                } else {
                        Log.e("MyActivity", "keyboard closed");

                        Log.e ("Notestate", String.valueOf(notestate));
                        if (notestate == 0) {
                            RelativeLayout note_bottom = (RelativeLayout) findViewById(R.id.note_bottom);
                            note_bottom.setVisibility(View.VISIBLE);
                            RelativeLayout note_tools = (RelativeLayout) findViewById(R.id.note_tools_bottom);
                            note_tools.setVisibility(View.INVISIBLE);

                            RelativeLayout textimput = (RelativeLayout) findViewById(R.id.textimput);
                            textimput.setVisibility(View.INVISIBLE);
                            mRecyclerView.setPadding(0, 0, 0, 0);
                            bottomBar.setVisibility(View.VISIBLE);

                            ImageView ivVectorImage = (ImageView) findViewById(R.id.enter);
                            ivVectorImage.setColorFilter(getResources().getColor(R.color.colorBlack));

                            SwitchIconView category = (SwitchIconView) findViewById(R.id.categorytext);
                            SwitchIconView note = (SwitchIconView) findViewById(R.id.notetextview);
                            category.setIconEnabled(false, true);
                            note.setIconEnabled(false, true);

                            category_switch_state = false;
                            note_switch_state = false;

                            EditText txtinput = (EditText) findViewById(R.id.myTextImput);
                            txtinput.setHint("La ce to-do te gandesti ?");
                            txtinput.setText("");
                            notestate = 0;
                            check_placehoder ();

                        }
                }
            }
        });




        // Create on Enter Press
        EditText txtinput = (EditText) findViewById(R.id.myTextImput);
        txtinput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        txtinput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    EditText txtinput = (EditText) findViewById(R.id.myTextImput);
                    Log.i("Enter presss","Works");
                    String newItem = txtinput.getText().toString();

                    if (category_switch_state) {
                        Log.i("Victor",String.valueOf(category_switch_state));
                        customers.add(new Customer(nr++,newItem,"Categorie 1",1,0,"","","",null));

                    }
                    else {
                        Log.i("Victor",String.valueOf(category_switch_state));
                        String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        customers.add( new Customer(nr++,newItem,date,0,0,"","","",null));
                    }


                    mAdapter.notifyDataSetChanged();

                    txtinput.getText().clear();
                    return true;

                }
            check_placehoder();
            return true;
            }
        });

        // Button Color Change
        txtinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ImageView ivVectorImage = (ImageView) findViewById(R.id.enter);
                if (note_switch_state){
                    ivVectorImage.setColorFilter(getResources().getColor(R.color.colorNote));
                }
                else {
                    ivVectorImage.setColorFilter(getResources().getColor(R.color.colorPrimary));
                }
                EditText txtinput = (EditText) findViewById(R.id.myTextImput);
                    if (!(txtinput.getText().toString().trim().length() > 0)){
                        ivVectorImage.setColorFilter(getResources().getColor(R.color.colorBlack));
                    }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        // Create on Button Press
        ImageButton enter = (ImageButton) findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtinput = (EditText) findViewById(R.id.myTextImput);
                String newItem = txtinput.getText().toString();

                if (category_switch_state) {
                    Log.i("State",String.valueOf(category_switch_state));
                    customers.add(new Customer(nr++,newItem,"Categorie 1",1,0,"","","",null));
                    category_switch_state = true;
                    String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    AddData(newItem,date,1,0,"","",new byte[0]);

                    // Kill category view
                    SwitchIconView categ = (SwitchIconView) findViewById(R.id.categorytext);
                    categ.setIconEnabled(false,true);
                    SwitchIconView note = (SwitchIconView) findViewById(R.id.notetextview);
                    note.setIconEnabled(false,true);
                    category_switch_state= false;
                    txtinput.setHint("La ce to-do te gandesti ?");

                    // Set Notification Counter
                    category_notification = Integer.parseInt(category_not.getText().toString());
                    category_notification++;
                    category_not.setText(String.valueOf(category_notification));

                    // Light button
                    SwitchIconView todo = (SwitchIconView) findViewById(R.id.todotext);
                    todo.setIconEnabled(true,true);

                }
                else if (note_switch_state){
                    customers.add(new Customer(nr++,"New note",newItem,2,0,"","","",null));
                    note_switch_state = true;
                    AddData("New note",newItem,2,0,"","",new byte[0]);
                    mAdapter.notifyDataSetChanged();

                }
                else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
                    String date = sdf.format(new Date());

                    Log.i("State",String.valueOf(category_switch_state));
                    customers.add(new Customer(nr++,newItem,date,0,0,"","","",null));
                    // Set Notification Counter
                    to_do_notification = Integer.parseInt(to_do_not.getText().toString());
                    to_do_notification++;
                    to_do_not.setText(String.valueOf(to_do_notification));
                    // Set Category False
                    category_switch_state = false;
                    // Add Data to Database
                    AddData(newItem,date,0,0,"","",new byte[0]);
                }

                check_placehoder();
                mAdapter.notifyDataSetChanged();
                txtinput.getText().clear();
            }
        });

        // TO-DO BUTTON TEXTVIEW
        SwitchIconView todo = (SwitchIconView) findViewById(R.id.todotext);
        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_switch_state = false;
                note_switch_state = false;

                // Light button
                SwitchIconView todo = (SwitchIconView) findViewById(R.id.todotext);
                todo.setIconEnabled(true,true);

                // Close other icons
                SwitchIconView category = (SwitchIconView) findViewById(R.id.categorytext);
                category.setIconEnabled(false,true);
                SwitchIconView note = (SwitchIconView) findViewById(R.id.notetextview);
                note.setIconEnabled(false,true);

                EditText txtinput = (EditText) findViewById(R.id.myTextImput);
                txtinput.setHint("La ce to-do te gandesti ?");
            }
        });

        // CATEGORY BUTTON TEXTVIEW
        SwitchIconView category = (SwitchIconView) findViewById(R.id.categorytext);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchIconView category = (SwitchIconView) findViewById(R.id.categorytext);
                    Log.i("Switch state","True");
                    category.setIconEnabled(true,true);
                    category_switch_state = true;

                    // Set correct button color
                    ImageView ivVectorImage = (ImageView) findViewById(R.id.enter);
                    ivVectorImage.setColorFilter(getResources().getColor(R.color.colorPrimary));

                    // Close other icons
                    SwitchIconView todo = (SwitchIconView) findViewById(R.id.todotext);
                    todo.setIconEnabled(false,true);
                    SwitchIconView note = (SwitchIconView) findViewById(R.id.notetextview);
                    note.setIconEnabled(false,true);

                    note_switch_state = false;

                    EditText txtinput = (EditText) findViewById(R.id.myTextImput);
                    txtinput.setHint("La ce categorie te gandesti ?");

            }
        });

        // NOTE BUTTON TEXTVIEW
        SwitchIconView note = (SwitchIconView) findViewById(R.id.notetextview);
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchIconView note = (SwitchIconView) findViewById(R.id.notetextview);

                Intent noteview = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(noteview);
                finish();
            }
        });



        note_remake();

        // Bottom Bar
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                TextView titleTab = (TextView) findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_title);
                titleTab.setTextSize(12);
                titleTab.setPadding(0,8,0,0);
                AppCompatImageView icon = (AppCompatImageView) findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_icon);
                icon.setY(6);
                icon.setColorFilter(titleTab.getCurrentTextColor());





            }
        });

        BottomBarTab tab = bottomBar.getTabAtPosition(5);
        tab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

        bottomBar.setTabSelectionInterceptor(new TabSelectionInterceptor() {
            @Override
            public boolean shouldInterceptTabSelection(@IdRes int oldTabId, @IdRes int newTabId) {
                TextView titleTab = (TextView) findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_title);
                titleTab.setTextSize(12);
                titleTab.setPadding(0,8,0,0);

                AppCompatImageView icon = (AppCompatImageView) findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_icon);
                icon.setY(6);
                icon.setColorFilter(titleTab.getCurrentTextColor());



                if (newTabId == R.id.tab_todo){
                    Log.i("Works","Am ajuns aici");
                    bottomBar.setVisibility(View.INVISIBLE);

                    RelativeLayout textimput = (RelativeLayout) findViewById(R.id.textimput);
                    EditText myimputText = (EditText) findViewById(R.id.myTextImput);

                    textimput.setVisibility(View.VISIBLE);

                    // Light Icon
                    SwitchIconView todo = (SwitchIconView) findViewById(R.id.todotext);
                    todo.setIconEnabled(true,true);

                    // Close other icons
                    SwitchIconView category = (SwitchIconView) findViewById(R.id.categorytext);
                    category.setIconEnabled(false,true);
                    SwitchIconView note = (SwitchIconView) findViewById(R.id.notetextview);
                    note.setIconEnabled(false,true);

                    myimputText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(myimputText, InputMethodManager.SHOW_IMPLICIT);

                }

                if (newTabId == R.id.tab_categorie) {
                    bottomBar.setVisibility(View.INVISIBLE);
                    categorybuttonbarclick ();
                }

                if (newTabId == R.id.tab_nota){
                    hide_placeholder ();
                    note_slide.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

                    //Intent noteview = new Intent(MainActivity.this, NoteActivity.class);
                    //startActivity(noteview);
                    //finish();

                }

                if (newTabId == R.id.tab_audio){
                    if (!recorder_switch_state){ openRecorder(); }
                    else { closerRecorder();}
                }

                if (newTabId == R.id.tab_camera) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
                if (newTabId == R.id.tab_mindmap){
                    supl.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

                }


                return true;
            }
        });


    }
    // 7. NOTA
    public void note_remake (){
        // MAIN SETUP SI ANIMATII
        final int colorFrom = getResources().getColor(R.color.colorVIntelStatusBar);
        final int colorTo = getResources().getColor(R.color.colorBarDimed);
        note_slide = (SlidingUpPanelLayout) findViewById(R.id.sliding_note);
        note_slide.setScrollableViewHelper(new EditorScrollViewHelper(this));
        RelativeLayout drag1 = (RelativeLayout) findViewById(R.id.note_content);
        note_slide.setDragView(drag1);
        new ControllableSlidingLayout(this).setDragView(drag1);
        note_slide.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                ArgbEvaluator mArgbEvaluator = null;
                if (mArgbEvaluator == null) mArgbEvaluator = new ArgbEvaluator();
                int bgColor = (Integer) mArgbEvaluator.evaluate(slideOffset, colorFrom, colorTo);
                getWindow().setStatusBarColor(bgColor);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                // Save Note


                // No Click On Other Content
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED)
                {
                    isExpanded = true;
                    isCollapsed = false;
                }
                else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED)
                {
                    isExpanded = false;
                    isCollapsed = true;
                }
                else if (newState == SlidingUpPanelLayout.PanelState.ANCHORED)
                {
                    if (isExpanded )
                    {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                supl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            }
                        },100);
                    }else if (isCollapsed )
                    {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                supl.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                            }
                        },100);
                    }
                }
            }
        });

        // EXTRAGERE DATE
        final EditText note_title_remake = (EditText) findViewById(R.id.note_title_remake);
        final RichEditor note_desc_remake = (RichEditor) findViewById(R.id.editor);

        // Settings
        note_desc_remake.setPlaceholder("Continut");
        note_desc_remake.setEditorFontSize(16);
        note_desc_remake.setEditorFontColor(Color.parseColor("#4c4c4c"));
        note_desc_remake.isClickable();
        note_text_bar();

        //final EditText note_desc_remake = (EditText) findViewById(R.id.note_content_remake);
        ImageView note_button_remake = (ImageView) findViewById(R.id.enter_note_remake);
        note_button_remake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Nu exista titlu sau descriere
                if (TextUtils.isEmpty(note_desc_remake.getHtml()) || TextUtils.isEmpty(note_title_remake.getText().toString())){
                    if (TextUtils.isEmpty(note_title_remake.getText().toString())){
                        toastMessage("Nu ai adaugat un titlu !");
                    }
                    if (TextUtils.isEmpty(note_desc_remake.getHtml())){
                        toastMessage("Nu ai adugat o descriere !");
                    }
                }
                // Totul este ok... salvam si inchidem
                else {
                    // Adaugam
                    customers.add(new Customer((long)0,note_title_remake.getText().toString(),note_desc_remake.getHtml(),2,0,"","","",null));
                    AddData(note_title_remake.getText().toString(),note_desc_remake.getHtml(),2,0,"","",new byte[0]);
                    mAdapter.notifyDataSetChanged();
                    // Setam Counterul
                    note_notification = Integer.parseInt(note_not.getText().toString());
                    note_notification++;
                    note_not.setText(String.valueOf(note_notification));
                    // Inchidem
                    note_slide.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    // Curatam
                    note_title_remake.getText().clear();
                    note_desc_remake.setHtml("");
                    note_title_remake.requestFocus();
                    note_desc_remake.requestFocus();
                    note_title_remake.requestFocus();
                    note_title_remake.clearFocus();
                }
            }
        });

    }
    public void note_text_bar(){
        final RichEditor note_desc_remake = (RichEditor) findViewById(R.id.editor);
        // Undo
        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.undo();}});
        // Redo
        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.redo();}});
        // Bold
        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.setBold(); }});
        // Italic
        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.setItalic();}});
        // Strikethrough
        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.setStrikeThrough();}});
        // Underline
        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {note_desc_remake.setUnderline();}});
        // Color
        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override public void onClick(View v) {
                note_desc_remake.setTextColor(Color.RED);}});
        // BG Color
        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;
            @Override public void onClick(View v) {
                note_desc_remake.setTextBackgroundColor(Color.YELLOW);}});
        // Indent
        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.setIndent();}});
        // Outdent
        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.setOutdent();}});
        // Align Left
        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.setAlignLeft();}});
        // Align Center
        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.setAlignCenter();}});
        // Align Right
        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.setAlignRight();}});
        // Bullets List
        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.setBullets();}});
        // Number List
        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                note_desc_remake.setNumbers();}});


    }

    // 6. CAMERA
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            customers.add(new Customer(nr++,"Pic_1","date",4,0,"","","",imageBitmap));

            AddData("Pic 1","date",4,0,"","",getBytes(imageBitmap));

            mAdapter.notifyDataSetChanged();

        }
    }

    // 5. RECORDER
    public void openRecorder(){
        BottomBarTab tab = bottomBar.getTabAtPosition(3);
        TextView text = tab.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_title);
        AppCompatImageView icon1 = tab.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_icon);
        icon1.setColorFilter(Color.RED);
        text.setTextColor(Color.RED);

        RelativeLayout  recy = (RelativeLayout) findViewById(R.id.RecyclerViewContent);
        recy.setPadding(0,0,0,350);

        RelativeLayout rec = (RelativeLayout) findViewById(R.id.vicRecorded);
        rec.setVisibility(View.VISIBLE);


        if (mCustomers.size() != 0) {

            mRecyclerView.smoothScrollToPosition(1000000000);
        }

        recorder_switch_state = true;
    }


    public void closerRecorder(){
        BottomBarTab tab = bottomBar.getTabAtPosition(3);
        TextView text = tab.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_title);
        AppCompatImageView icon1 = tab.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_icon);
        icon1.setColorFilter(Color.parseColor("#606060"));
        text.setTextColor(Color.parseColor("#606060"));

        RelativeLayout  recy = (RelativeLayout) findViewById(R.id.RecyclerViewContent);
        recy.setPadding(0,0,0,0);

        RelativeLayout rec = (RelativeLayout) findViewById(R.id.vicRecorded);
        rec.setVisibility(View.INVISIBLE);


        recorder_switch_state = false;
    }
    public void playMedia(String path) {
        final ImageButton playrec = (ImageButton) findViewById(R.id.playrecButton);
        final TextView totaltime = (TextView) findViewById(R.id.totaltime);
        if (!playstate) {
            playstate = true;
            pausestate = false;
            stop = true;
            playrec.setBackgroundResource(R.drawable.ic_stop);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();

                seekBar.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });

                mediaPos = mediaPlayer.getCurrentPosition();
                mediaMax = mediaPlayer.getDuration();

                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mediaMax),
                        TimeUnit.MILLISECONDS.toMinutes(mediaMax) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mediaMax)),
                        TimeUnit.MILLISECONDS.toSeconds(mediaMax) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mediaMax)));


                totaltime.setText(hms);
                seekBar.setMax(mediaMax); // Set the Maximum range of the
                seekBar.setProgress(mediaPos);// set current progress to song's

                handler.removeCallbacks(moveSeekBarThread);
                handler.postDelayed(moveSeekBarThread, 100);


                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        seekBar.setProgress(0);
                        stop = false;
                        mediaPlayer.release();
                        playstate = false;

                        counter = 0;

                        String count = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(counter),
                                TimeUnit.MILLISECONDS.toMinutes(counter) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(counter)),
                                TimeUnit.MILLISECONDS.toSeconds(counter) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(counter)));
                        timepassed.setText(count);

                        playrec.setBackgroundResource(R.drawable.ic_play3);
                        seekBar.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (!pausestate) {
            playstate = false;
            stop = false;
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            seekBar.setProgress(0);
            pausestate = false;

            counter = 0;
            String count = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(counter),
                    TimeUnit.MILLISECONDS.toMinutes(counter) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(counter)),
                    TimeUnit.MILLISECONDS.toSeconds(counter) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(counter)));
            timepassed.setText(count);

            playrec.setBackgroundResource(R.drawable.ic_play3);
            seekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        if (pausestate){
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
            pausestate = false;
        }
        if (!pausestate && playstate){
            playrec.setBackgroundResource(R.drawable.ic_stop);
        }
    }
    public void startRecording (){
        recorder = new MediaRecorder();
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioEncodingBitRate(12200);
        recorder.setAudioSamplingRate(8000); // this is the MIME rate



        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            externalOutputPath = externalStoragePath + File.separator + "VIntel/Recordings/Rec_"+record_count.get(record_count.size()-1)+".mp3";
            recorder.setOutputFile(externalOutputPath);
        }
        else
        {
            storagePath = Environment.getDataDirectory().getAbsolutePath();
            recorder.setOutputFile(storagePath + "VIntel/Recordings/Rec_"+record_count.get(record_count.size()-1)+".mp3");
        }


        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void stopRecording() {
        if (null != recorder) {
            recorder.stop();
            recorder.reset();
            recorder.release();

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
            String date = sdf.format(new Date());

            customers.add( new Customer(nr++,"Rec_"+record_count.get(record_count.size()-1),date,3,0,externalOutputPath,"","",null));
            AddData("Rec_"+record_count.get(record_count.size()-1),date,3,0,"",externalOutputPath,new byte[0]);
            record_count.add(String.valueOf(Integer.parseInt(record_count.get(record_count.size()-1))+1));
            Paper.book().write("Recording_Number", record_count);

            mAdapter.notifyDataSetChanged();
            if (mCustomers.size() != 0) {
                mRecyclerView.smoothScrollToPosition(mCustomers.size() - 1);
            }


            recorder = null;
        }
    }

    private boolean makeDirRec() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/VIntel/Recordings");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            return true;
        } else {
            return false;
        }
    }
    private boolean makeDirPic() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/VIntel/Pictures");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            return true;
        } else {
            return false;
        }
    }

    public void recorderCreate(){
        RelativeLayout rec = (RelativeLayout) findViewById(R.id.vicRecorded);
        rec.setVisibility(View.INVISIBLE);

        record_count = Paper.book().read("Recording_Number", new ArrayList<String>());

        if (record_count.isEmpty()) {
            record_count.add("1");
            Paper.book().write("Recording_Number", record_count);
        }

        final FloatingActionButton recbutton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        final ImageButton playrec = (ImageButton) findViewById(R.id.playrecButton);
        final ImageButton pauserec = (ImageButton) findViewById(R.id.pauserecButton);
        final ImageButton backwardrec = (ImageButton) findViewById(R.id.backwardrecButton);
        final ImageButton forwardrec = (ImageButton) findViewById(R.id.forwardrecButton);
        timepassed = (TextView) findViewById(R.id.timepassed);
        final TextView totaltime = (TextView) findViewById(R.id.totaltime);
        cronos = (Chronometer) findViewById(R.id.chronometer2);
        cronos.setVisibility(View.INVISIBLE);

        makeDirRec();
        makeDirPic();

        Button public_play = (Button) findViewById(R.id.public_play);
        public_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecorder();
                Log.i ("Pathto", String.valueOf(customers.get(picked_node).getCategory()));
                playMedia(customers.get(picked_node).getPath());
            }
        });

        cronos.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
            }
        });
        cronos.setBase(SystemClock.elapsedRealtime());

        // Play last recording
        externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        externalOutputPath = externalStoragePath + File.separator + "VIntel/Recordings/Rec_"+record_count.get(record_count.size()-1)+".mp3";


        Permiso.getInstance().setActivity(this);

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {

                }

                if (resultSet.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Calendar permission granted!
                }

                if (resultSet.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){

                }

            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog("Title", "Message", null, callback);
            }
        }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

        cronos.setBase(SystemClock.elapsedRealtime());
        cronos.stop();

        recbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recstate){
                    recbutton.setImageResource(R.drawable.ic_stop2);
                    seekBar.setVisibility(View.INVISIBLE);
                    playrec.setVisibility(View.INVISIBLE);
                    pauserec.setVisibility(View.INVISIBLE);
                    backwardrec.setVisibility(View.INVISIBLE);
                    forwardrec.setVisibility(View.INVISIBLE);
                    totaltime.setVisibility(View.INVISIBLE);
                    timepassed.setVisibility(View.INVISIBLE);
                    cronos.setVisibility(View.VISIBLE);
                    recstate = true;
                    cronos.setBase(SystemClock.elapsedRealtime());
                    cronos.start();
                    startRecording();
                }
                else {
                    recbutton.setImageResource(R.drawable.ic_mic_black_24dp);
                    seekBar.setVisibility(View.VISIBLE);
                    playrec.setVisibility(View.VISIBLE);
                    pauserec.setVisibility(View.VISIBLE);
                    backwardrec.setVisibility(View.VISIBLE);
                    forwardrec.setVisibility(View.VISIBLE);
                    totaltime.setVisibility(View.VISIBLE);
                    timepassed.setVisibility(View.VISIBLE);
                    cronos.setVisibility(View.INVISIBLE);
                    recstate = false;
                    cronos.setBase(SystemClock.elapsedRealtime());
                    cronos.stop();
                    stopRecording();
                }
            }
        });

        seekBar.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        playrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               playMedia(externalOutputPath);
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                    if (pausestate){
                        length = progress;
                    }
                }
            }
        });

        backwardrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playstate){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-2000);
                }
            }
        });

        forwardrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playstate){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+2000);
                }
            }
        });


    }


    private Runnable moveSeekBarThread = new Runnable() {
        public void run() {
            if(stop){
                counter = mediaPlayer.getCurrentPosition();

                String count = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(counter),
                        TimeUnit.MILLISECONDS.toMinutes(counter) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(counter)),
                        TimeUnit.MILLISECONDS.toSeconds(counter) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(counter)));

                timepassed.setText(count);

                int mediaPos_new = mediaPlayer.getCurrentPosition();
                int mediaMax_new = mediaPlayer.getDuration();
                seekBar.setMax(mediaMax_new);
                seekBar.setProgress(mediaPos_new);

                handler.postDelayed(this, 0); //Looping the thread after 0.1 second
                // seconds
            }
            else {
                counter = 0;
            }


        }
    };



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private RecyclerView mRecyclerViewSideItem;
    private SideItemListAdapter mAdapterSideItem;
    private List<SideItem> mSideItems;
    private RecyclerView.LayoutManager mLayoutManagerSideItem;
    public List<SideItem> sideItems = new ArrayList<SideItem>();

    @Override
    public void onBackPressed(){
        if (recorder_switch_state){
            closerRecorder();
        }
        else {
            super.onBackPressed();
            return;
        }
    }

    public void categorybuttonbarclick (){
        RelativeLayout textimput = (RelativeLayout) findViewById(R.id.textimput);
        EditText myimputText = (EditText) findViewById(R.id.myTextImput);

        textimput.setVisibility(View.VISIBLE);

        myimputText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(myimputText, InputMethodManager.SHOW_IMPLICIT);


        // Light Icon
        SwitchIconView category = (SwitchIconView) findViewById(R.id.categorytext);
        category.setIconEnabled(true,true);

        // Close other icons
        SwitchIconView todo = (SwitchIconView) findViewById(R.id.todotext);
        todo.setIconEnabled(false,true);
        SwitchIconView note = (SwitchIconView) findViewById(R.id.notetextview);
        note.setIconEnabled(false,true);

        category_switch_state = true;

        EditText txtinput = (EditText) findViewById(R.id.myTextImput);
        txtinput.setHint("La ce categorie te gandesti ?");
    }

    public void slidefromRightToLeft(View view) {

    }



    private ItemTouchHelper mItemTouchHelperSideItem;
    public void sideBar(){

        mToolbar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        final int colorFrom = getResources().getColor(R.color.colorVIntelStatusBar);
        final int colorTo = getResources().getColor(R.color.colorWhite);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(220) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.93f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
                .withRootViewYTranslation(-23) //Content view's translationY will be interpolated between 0 and 4. Default == 0
                .withMenuOpened(false)
                .withToolbarMenuToggle(mToolbar)
                .withMenuLayout(R.layout.menu_left_drawer).addDragListener(new DragListener() {
                    @Override
                    public void onDrag(float progress) {
                        ArgbEvaluator mArgbEvaluator = null;
                        if (mArgbEvaluator == null) mArgbEvaluator = new ArgbEvaluator();
                        int bgColor = (Integer) mArgbEvaluator.evaluate(progress, colorFrom, colorTo);
                        getWindow().setStatusBarColor(bgColor);

                        // Dock
                        RelativeLayout dock = (RelativeLayout) findViewById(R.id.dock);
                        ObjectAnimator animation = ObjectAnimator.ofFloat(dock, "translationX", progress*178f);
                        animation.setDuration(400);
                        animation.start();

                        // Proiecte Content
                        RelativeLayout proj_content = (RelativeLayout) findViewById(R.id.proiecte_content);

                        ObjectAnimator animation_proj_content = ObjectAnimator.ofFloat(proj_content, "translationX", progress*60f);
                        ObjectAnimator animation_proj_content_alpha = ObjectAnimator.ofFloat(proj_content, "alpha", progress*2f);
                        animation_proj_content.setDuration(400);
                        animation_proj_content_alpha.setDuration(400);
                        animation_proj_content.start();
                        animation_proj_content_alpha.start();


                    }
                })
                .inject();


        mRecyclerViewSideItem = (RecyclerView) findViewById(R.id.side_recycler_view);
        mRecyclerViewSideItem.setHasFixedSize(true);
        mLayoutManagerSideItem = new LinearLayoutManager(this);
        mRecyclerViewSideItem.setLayoutManager(mLayoutManagerSideItem);

        mSideItems = sideItems;

        mAdapterSideItem = new SideItemListAdapter(mSideItems, this, this, this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallbackItem(mAdapterSideItem, mRecyclerViewSideItem, this);
        mItemTouchHelperSideItem = new ItemTouchHelper(callback);
        mItemTouchHelperSideItem.attachToRecyclerView(mRecyclerViewSideItem);


        mRecyclerViewSideItem.setAdapter(mAdapterSideItem);

        final RelativeLayout header_nav = (RelativeLayout) findViewById(R.id.header_nav);
        final RelativeLayout header_side = (RelativeLayout) findViewById(R.id.header_side);
        final TextView side_text_nav = (TextView) findViewById(R.id.side_text_nav);
        ImageView side_back_header = (ImageView) findViewById(R.id.side_back_header);
        final List<String> stack = new ArrayList<String>();
        stack.add(group_item);


        mRecyclerViewSideItem.addOnItemTouchListener(
                new RecyclerItemClickListener(context, mRecyclerViewSideItem ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(final View view, final int position) {

                        if (mAdapterSideItem.getItemViewType(position) == 0) {
                            view.findViewById(R.id.sidebar_item_project_name).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    slidingRootNav.closeMenu();
                                    group = sideItems.get(position).getNameitem();
                                    mCustomers.clear();
                                    populateListView();
                                    // Restet all to unchecked
                                    for (int i = 0; i < toggledChoices.length; i++) {
                                        toggledChoices[i] = 0;
                                    }
                                    // Repopulate
                                    populateListViewCH();
                                    mAdapter.notifyDataSetChanged();
                                    TextView titlebarTitle = (TextView) findViewById(R.id.titlebarTitle);
                                    titlebarTitle.setText(sideItems.get(position).getNameitem());

                                }

                            });
                        }
                        if (mAdapterSideItem.getItemViewType(position) == 1) {
                            // Folder SetUp
                            group_item = sideItems.get(position).getNameitem();
                            stack.add(group_item);
                            mSideItems.clear();
                            populateListViewItem();
                            mAdapterSideItem.notifyDataSetChanged();

                            // Close Folder
                            header_side.setVisibility(View.GONE);
                            header_nav.setVisibility(View.VISIBLE);
                            side_text_nav.setText(group_item);

                        }

                    }

                })
        );

        side_back_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stack.remove(stack.size()-1);
                curent_folder_item = stack.get(stack.size()-1);
                group_item = curent_folder_item;
                if (curent_folder_item.equals("Proiecte")) {
                    header_side.setVisibility(View.VISIBLE);
                    header_nav.setVisibility(View.GONE);
                    mSideItems.clear();
                    populateListViewItem();
                    mAdapterSideItem.notifyDataSetChanged();
                }
                else {
                    side_text_nav.setText(curent_folder_item);
                    mSideItems.clear();
                    populateListViewItem();
                    mAdapterSideItem.notifyDataSetChanged();
                }

            }
        });

        Button inboxbuton = (Button) findViewById(R.id.InboxButon);
        inboxbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group = "Inbox";
                mCustomers.clear();
                populateListView();
                // Restet all to unchecked
                for (int i = 0; i<toggledChoices.length; i++){
                    toggledChoices[i] = 0;
                }
                // Repopulate
                populateListViewCH();
                mAdapter.notifyDataSetChanged();

                TextView titlebarTitle = (TextView) findViewById(R.id.titlebarTitle);
                titlebarTitle.setText(group);
                slidingRootNav.closeMenu();
            }
        });

        Button inboxtransparent = (Button) findViewById(R.id.InboxTrasparent);
        inboxtransparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group = "Inbox";
                mCustomers.clear();
                populateListView();
                // Restet all to unchecked
                for (int i = 0; i<toggledChoices.length; i++){
                    toggledChoices[i] = 0;
                }
                // Repopulate
                populateListViewCH();
                mAdapter.notifyDataSetChanged();

                TextView titlebarTitle = (TextView) findViewById(R.id.titlebarTitle);
                titlebarTitle.setText(group);
            }
        });




        Button FolderTransparent = (Button) findViewById(R.id.folder_trans);
        FolderTransparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove Item
                mSideItems.remove(position_make_folder);
                mAdapterSideItem.notifyDataSetChanged();
                // Set Item New Location
                toastMessage(folder_curr_name + " mutat in "+ sideItems.get(folder_pos).getNameitem());
                mDatabaseHelper.updateDataItem(folder_curr_name,sideItems.get(folder_pos).getNameitem());
                //toastMessage(folder_curr_name);
                //toastMessage(sideItems.get(folder_curr1).getNameitem());
                // Set Database Folder


            }
        });

        Button newProj = (Button) findViewById(R.id.buttonProiectNou);
        Button newFolder = (Button) findViewById(R.id.buttonDosarNou);
        newProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
        });
        newFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFolder();
            }
        });

    }

    public void populateListViewCH(){
        i = 0;
        Cursor data = mDatabaseHelper.getData(MainActivity.group);
        while (data.moveToNext()){
            toggledChoices[i++] = data.getInt(4);
        }
    }

    public void Dialog (){
        boolean wrapInScrollView = false;
        new MaterialDialog.Builder(this)
                .positiveText("Gata")
                .positiveColorRes(R.color.colorShadow)
                .neutralColorRes(R.color.colorShadow)
                .negativeColorRes(R.color.colorShadow)
                .widgetColorRes(R.color.colorShadow)
                .contentColor(getResources().getColor(R.color.colorShadow))

                .content("Creaza un proiect nou")
                .negativeText("Inchide")
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
                .input("La ce te gandesti ?", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (!TextUtils.isEmpty(input)) {
                            mDatabaseHelper.createTable(input.toString());
                            if (sideItems.size() == 4) {
                                sideItems.add(new SideItem(nr++, input.toString(), R.drawable.ic_layers_black_24dp, 0, 0, group_item));
                                AddDataItem(input.toString(), R.drawable.ic_layers_black_24dp, 0, 2, group_item);
                            }
                            else {
                                sideItems.add(new SideItem(nr++, input.toString(), R.drawable.ic_layers_black_24dp, 0, 0, group_item));
                                AddDataItem(input.toString(), R.drawable.ic_layers_black_24dp, 0, 0,group_item);
                            }
                        }
                    }
                })
                .show();
    }
    public void DialogFolder () {
        boolean wrapInScrollView = false;
        new MaterialDialog.Builder(this)
                .positiveText("Gata")
                .positiveColorRes(R.color.colorShadow)
                .neutralColorRes(R.color.colorShadow)
                .negativeColorRes(R.color.colorShadow)
                .widgetColorRes(R.color.colorShadow)
                .contentColor(getResources().getColor(R.color.colorShadow))

                .content("Creaza un dosar nou")
                .negativeText("Inchide")
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)

                .show();
    }

    public void statsusBar(){
        Window window = this.getWindow();
        View decor = getWindow().getDecorView();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (search_switch_state){
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorVIntelStatusBar));
            
        }
        else {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorVIntelStatusBar));
            decor.setSystemUiVisibility(0);
        }


    }

    private void setupRecyclerView(){


        mRecyclerView = (RecyclerView) findViewById(R.id.note_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RelativeLayout rely = (RelativeLayout) findViewById(R.id.RecyclerViewContent);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 144);
        rely.setLayoutParams(params);

        mCustomers =  customers;

        //setup the adapter with empty list
        mAdapter = new CustomerListAdapter(mCustomers, this, this, this);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter, mRecyclerView);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.getRecycledViewPool().clear();

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.i ("Recy", "Down");
                    case MotionEvent.ACTION_UP:
                        MainActivity.refreshLayout.setEnableRefresh(true);
                        MainActivity.refreshLayout.setEnableLoadmore(true);
                        MainActivity.refreshLayout.setEnableNestedScroll(true);
                        MainActivity.refreshLayout.setEnableAutoLoadmore(true);
                        MainActivity.refreshLayout.setEnableOverScrollDrag(true);
                        Log.i ("Recy", "Up");
                }
                return false;
            }
        });

        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new CalendarHeader(this));
        refreshLayout.setFooterHeight(1);
        //refreshLayout.setFooterMaxDragRate(0);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (!calendar_global) {
                    openCalendar();
                    calendar_global = true;
                }
                else {
                    closeCalendar();
                    calendar_global = false;
                }
                refreshlayout.finishRefresh(0);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(0);
            }
        });

    }
    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;

        return String.valueOf(elapsedDays);
        //toastMessage(String.valueOf(elapsedDays));
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

    }

    @Override
    public void onNoteListChanged(List<Customer> customers) {

    }
    @Override
    public void onNoteListChangedSide(List<SideItem> sideItems) {

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
        mItemTouchHelperSideItem.startDrag(viewHolder);
    }

    @Override
    public void onDateSet(com.philliphsu.bottomsheetpickers.date.DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = new java.util.GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        Date date1 = cal.getTime();
        Date date2 = new Date();
        picked_date = DateFormat.getDateFormat(this).format(cal.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd.MM "+ printDifference(date2,date1));
        String date = sdf.format(cal.getTime())+"z";



        // Add Date To Database
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String database_date = sdf1.format(cal.getTime());

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String database_date_change = sdf2.format(cal.getTime());

        if (date_remove_switch){
            String dbStart = customers.get(picked_node).getReal_data();
            boolean blnExists = dates_count.contains(database_date_change);
            Log.i ("DatesPick", String.valueOf(dbStart));
            if (blnExists) {
                customers.set(picked_node,new Customer(customers.get(picked_node).getId(),customers.get(picked_node).getName(),customers.get(picked_node).getEmailAddress(),
                        customers.get(picked_node).getCategory(), customers.get(picked_node).getCheck_state(),customers.get(picked_node).getPath(),"","",null));
                dates_count.remove(database_date_change);
                Paper.book().write("Calnedar_Duplicates", dates_count);
                Log.i ("Dates2", "Ok");
            }
            else {
                customers.set(picked_node,new Customer(customers.get(picked_node).getId(),customers.get(picked_node).getName(),customers.get(picked_node).getEmailAddress(),
                        customers.get(picked_node).getCategory(), customers.get(picked_node).getCheck_state(),customers.get(picked_node).getPath(),"","",null));
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Log.i ("Dates2", "Moving");
                try {
                    Date date10 = format1.parse(dbStart);
                    dates.remove(CalendarDay.from(date10));
                    Paper.book().write("Calendar_Dates",dates);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        mcv.removeDecorators();
        Log.i ("DatesDecorator", String.valueOf(dates));
            date_remove_switch = false;
        }


        if (!dates.isEmpty()) {
            boolean blnExists = dates.contains(CalendarDay.from(date1));
            if (blnExists) {
                dates_count.add(database_date_change);
                Paper.book().write("Calnedar_Duplicates", dates_count);
            }
        }
        dates.add(CalendarDay.from(date1));
        mcv.addDecorator(new DecorateDay(this,dates));
        Paper.book().write("Calendar_Dates",dates);

        Log.i ("Dates1", String.valueOf(dates));
        Log.i ("Dates", String.valueOf(dates_count));

        customers.set(picked_node,new Customer(customers.get(picked_node).getId(),customers.get(picked_node).getName(),customers.get(picked_node).getEmailAddress(),
                customers.get(picked_node).getCategory(), customers.get(picked_node).getCheck_state(),customers.get(picked_node).getPath(),date,database_date,null));

        mDatabaseHelper.updateData(group,picked_node+1,database_date);

        mAdapter.notifyDataSetChanged();
        //toastMessage(String.valueOf(picked_node));
        //toastMessage("Date set: " + DateFormat.getDateFormat(this).format(cal.getTime()));
    }
}
