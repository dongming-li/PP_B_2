package com.example.myfirstapp;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SlidingDrawer;

public class MainActivity extends AppCompatActivity {
    //SlidingDrawer from http://abhiandroid.com/ui/slidingdrawer
    String[] nameArray = {"All Attack","All Fallback","Chat","Building Wiki","Unit Wiki","Options","Quit/Surrender"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        final HexBoardView HexMap = (HexBoardView) findViewById(R.id.HexMap);
        SlidingDrawer simpleSlidingDrawer = (SlidingDrawer) findViewById(R.id.simpleSlidingDrawer); // initiate the SlidingDrawer
        final ImageButton handleButton = (ImageButton) findViewById(R.id.handle); // inititate a Button which is used for handling the content of SlidingDrawer
        ListView simpleListView = (ListView) findViewById(R.id.simpleListView); // initiate the ListView that is used for content of Sl.idingDrawer
        // adapter for the list view
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.name, nameArray);
        // set an adapter to fill the data in the ListView
        simpleListView.setAdapter(arrayAdapter);
        // implement setOnDrawerOpenListener event
        simpleSlidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
             //   handleButton.setText("Close");
            }
        });
        // implement setOnDrawerCloseListener event
        simpleSlidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                // change the handle button text
               // handleButton.setText("Open");
            }
        });

        HexMap.setZOrderMediaOverlay(true);

    }
}
