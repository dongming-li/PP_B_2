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
import android.support.v7.widget.PopupMenu;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    //SlidingDrawer from http://abhiandroid.com/ui/slidingdrawer
    String[] nameArray = {"All Attack", "All Fallback", "Chat", "Building Wiki", "Unit Wiki", "Options", "Quit/Surrender"};
    String[] hexArray = {"Recon", "All Move", "Build Unit", "Build Building"};
    //    String[] buildUnitArray = {"A","B","C"};
//    String[] buildBuildingArray = {"BA","BB","BC"};
    Hex firstTouchedHex = null;
    Hex secondTouchedHex = null;
    HexBoardView HexMap;
    PopupWindow pw;
    PopupWindow pw1;
    int hexAction=-1;
    int mainAction=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HexMap = (HexBoardView) findViewById(R.id.HexMap);

        LayoutInflater inflater = (LayoutInflater) HexMap.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupMenu = inflater.inflate(R.layout.popup_menu, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        pw = new PopupWindow(popupMenu, width, height, focusable);
        ListView lv = (ListView) popupMenu.findViewById(R.id.HexList);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.name, hexArray);
        lv.setAdapter(aa);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos=position;
                switch(pos){
                    case 0: //Recon
                        hexAction=0;
                        break;
                    case 1: //Move all
                        hexAction=1;
                        break;
                    case 2: //Build Unit
                        hexAction=2;
                        break;
                    case 3: //Build Building
                        hexAction=3;
                        break;
                }
                pw.dismiss();
            }
        });

        SlidingDrawer simpleSlidingDrawer = (SlidingDrawer) findViewById(R.id.simpleSlidingDrawer); // initiate the SlidingDrawer
        final ImageButton handleButton = (ImageButton) findViewById(R.id.handle); // inititate a Button which is used for handling the content of SlidingDrawer
        ListView simpleListView = (ListView) findViewById(R.id.simpleListView); // initiate the ListView that is used for content of Sl.idingDrawer
        // adapter for the list view
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.name, nameArray);
        // set an adapter to fill the data in the ListView
        simpleListView.setAdapter(arrayAdapter);
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        finish();
                        break;
                }
            }
        });

        HexMap.setZOrderMediaOverlay(true);
        HexMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean inHex = HexMap.onTouchEvent(event);
                if (inHex == true && hexAction==1) {
                    firstTouchedHex = HexMap.contextInfo.touchedHex;
                  // int a=HexMap.getRenderer().HexBoard.length;
                    pw.showAtLocation(HexMap, Gravity.NO_GRAVITY, (int) event.getX(), (int) event.getY());
                }
                else if(inHex==true && hexAction==0 || hexAction==1){
                    secondTouchedHex = HexMap.contextInfo.touchedHex;
                    if(hexAction==1){

                    }
                    else{

                    }
                    hexAction=-1;
                }
                return inHex;
            }
        });

    }
}
