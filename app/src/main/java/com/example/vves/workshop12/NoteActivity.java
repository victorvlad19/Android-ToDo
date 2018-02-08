package com.example.vves.workshop12;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.zagum.switchicon.SwitchIconView;



import java.util.ArrayList;
import java.util.List;

import static android.R.id.input;


public class NoteActivity extends AppCompatActivity {

    public List<Customer> mycustomers = new ArrayList<Customer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        statsusBar();
        createBar();

        TextView titlebarTitle = (TextView) findViewById(R.id.titlebarTitleNote);
        titlebarTitle.setHint("Adauga titlu");

        EditText note = (EditText) findViewById(R.id.myTextImputNote);
        note.setMovementMethod(new ScrollingMovementMethod());
        note.requestFocus();


        Intent intent = getIntent();

        Bundle extras = getIntent().getExtras();
        String notename ="";
        String notedescp = "";
        String mode = "";


        if(extras !=null) {
            notename = extras.getString("notename");
            notedescp = extras.getString("nodedescp");
            //mode = extras.getString("mode");

            titlebarTitle.setText(notename);
            note.setText(notedescp);


        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contextual_menu,menu);

        return true;
    }


    public void statsusBar(){
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorVIntelStatusBar));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(NoteActivity.this, MainActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    long nr = 0;
    public void createBar (){

        // Icon Switch On
        SwitchIconView note = (SwitchIconView) findViewById(R.id.notetextview);
        note.setIconEnabled(true);

        // Create on Button Press
        ImageButton enter = (ImageButton) findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name  = "";
                String descp = "";

                EditText txtinput = (EditText) findViewById(R.id.myTextImputNote);
                descp = txtinput.getText().toString();

                TextView titlebarTitle = (TextView) findViewById(R.id.titlebarTitleNote);
                name = titlebarTitle.getText().toString();

                Intent i = new Intent(NoteActivity.this, MainActivity.class);
                i.putExtra("notename", name);
                i.putExtra("nodedescp", descp);
                startActivity(i);
                finish();


            }
        });

        SwitchIconView category = (SwitchIconView) findViewById(R.id.categorytext);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NoteActivity.this, MainActivity.class);
                String mode = "1";
                i.putExtra("mode", mode);
                startActivity(i);
                finish();
            }
        });

    }




}
