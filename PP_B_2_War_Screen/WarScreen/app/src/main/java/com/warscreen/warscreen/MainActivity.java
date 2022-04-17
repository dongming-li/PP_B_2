package com.warscreen.warscreen;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.warscreen.warscreen.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    public static final String EXTRA_MESSAGE = "com.warscreen.warscreen.MESSAGE";


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
    PopupWindow pw2;
    int hexAction=-1;
    int mainAction=-1;
    int numHexes = 3;
    Thread background;

    int numGameHexes = 9;



    private String TAG = MainActivity.class.getSimpleName();
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HexMap = (HexBoardView) findViewById(R.id.HexMap);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");

        Toast.makeText(
                getBaseContext(),
                message,
                Toast.LENGTH_SHORT).show();

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
                        LayoutInflater inflater = (LayoutInflater) HexMap.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View reconInfo = inflater.inflate(R.layout.popup_menu, null);
                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        boolean focusable = true;
                        pw1 = new PopupWindow(reconInfo, width, height, focusable);
                        ListView lv = (ListView) reconInfo.findViewById(R.id.HexList);
                        String[] info = {"Owner: ","Terrain: ","Manpower: ","Gold: ","Army: "};
                        if(firstTouchedHex.getHexData().get_User()==null){
                            info[0]+="None";
                        }
                        else{
                            info[0]+=firstTouchedHex.getHexData().get_User().name;
                        }
                        info[1]+="grasslands";
                        info[2]+=Integer.toString(firstTouchedHex.getHexData().get_MPT());
                        info[3]+=Double.toString(firstTouchedHex.getHexData().get_GPT());
                        if(firstTouchedHex.getHexData().get_hex_army()!=null) {
                            info[4] += "Archers: " + Integer.toString(firstTouchedHex.getHexData().get_archers().size()) + "\n Infantry: " + Integer.toString(firstTouchedHex.getHexData().get_swords().size()) + " ,Horses: " + Integer.toString(firstTouchedHex.getHexData().get_horses().size());
                        }
                        else{
                            info[4]+= "None";

                            System.out.println(info[4]);
                        }

                        ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.name,info);
                        lv.setAdapter(aa);
                        pw1.showAtLocation(HexMap,Gravity.NO_GRAVITY,HexMap.getWidth(),HexMap.getHeight());
                        break;
                    case 1: //Move all
                        hexAction=1;
                        break;
                    case 2: //Build Unit
                        ArrayList<units> ar=new ArrayList<units>(2);
                        ArrayList<units> s=new ArrayList<units>(2);
                        ArrayList<units> h=new ArrayList<units>(2);
                        ar.add(new units("sword","Bob",0,0,0,0,0,0));
                        s.add(new units("archer","Bob1",0,0,0,0,0,0));
                        h.add(new units("horse","Bob2",0,0,0,0,0,0));
                        army a = new army(ar,s,h,firstTouchedHex.getHexData().get_ID(),0);
                        Hex_data newData=firstTouchedHex.getHexData();
                        newData.add_army(a);
                        firstTouchedHex.setHexData(newData);
                        //System.out.println(Integer.toString(firstTouchedHex.getHexData().get_archers().size()));
                        buildToServer(newData); //update server
                        break;
                    case 3: //Build Building

                        break;
                }
                pw.dismiss();
            }
        });

        //Network.ServerThread(HexMap);
        ServerThread();

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
                        mainAction=0;
                        break;
                    case 1:
                        mainAction=1;
                        break;
                    case 2:
                        mainAction=2;
                        break;
                    case 3:
                        mainAction=3;
                        break;
                    case 4:
                        mainAction=4;
                        break;
                    case 5:
                        mainAction=5;
                        break;
                    case 6:
                        leaveGame();
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
                if (inHex == true && hexAction==-1) {
                    firstTouchedHex = HexMap.contextInfo.touchedHex;
                    // int a=HexMap.getRenderer().HexBoard.length;
                    pw.showAtLocation(HexMap, Gravity.NO_GRAVITY, (int) event.getX(), (int) event.getY());
                }
                else if(inHex==true && hexAction==1){
                    secondTouchedHex = HexMap.contextInfo.touchedHex;
                    if(hexAction==1){
//                        army a =firstTouchedHex.getHexData().get_hex_army();
//                        secondTouchedHex.context=1;
//                        firstTouchedHex.context=1;
//                        secondTouchedHex.getHexData().add_army(a);
//                        firstTouchedHex.getHexData().remove_Army();
                        //HexMap.requestRender();
                        //show progress dialog until response and then toast move successful
                        moveToServer(firstTouchedHex, secondTouchedHex);
                    }
                    hexAction=-1;
                }
                return inHex;
            }
        });
    }

    public void buildToServer(Hex_data hexData)
    {

        String hexNumber = Integer.toString(hexData.get_ID());

        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("hexNumber", hexNumber);
            jsonData.put(hexNumber +"hexU1", Integer.toString(hexData.get_swords().size()));
            jsonData.put(hexNumber +"hexU2", Integer.toString(hexData.get_archers().size()));
            jsonData.put(hexNumber +"hexU3", Integer.toString(hexData.get_horses().size()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String jsonString = jsonData.toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        //TODO
                        String message = response.toString();
                        Toast.makeText(
                                getBaseContext(),
                                message,
                                Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error.Response" + error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reqType", "buildUnits");
                params.put("username", Const.username);
                params.put("gameID", Const.gameID);
                params.put("data", jsonString);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "string_req");


    }

    public void moveToServer(final Hex firstTouched, final Hex secondTouched)
    {

        final Hex_data fromHex = firstTouched.getHexData();
        final Hex_data toHex = secondTouched.getHexData();

        String fromHexNumber = Integer.toString(fromHex.get_ID());
        String toHexNumber = Integer.toString(toHex.get_ID());

        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("fromHexNumber", fromHexNumber);
            jsonData.put("toHexNumber", toHexNumber);
            jsonData.put(fromHexNumber +"hexU1", Integer.toString(fromHex.get_swords().size()));
            jsonData.put(fromHexNumber +"hexU2", Integer.toString(fromHex.get_archers().size()));
            jsonData.put(fromHexNumber +"hexU3", Integer.toString(fromHex.get_horses().size()));
            jsonData.put(toHexNumber +"hexU1", Integer.toString(toHex.get_swords().size()));
            jsonData.put(toHexNumber +"hexU2", Integer.toString(toHex.get_archers().size()));
            jsonData.put(toHexNumber +"hexU3", Integer.toString(toHex.get_horses().size()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String jsonString = jsonData.toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        //TODO
                        String message = response.toString();


                        if (message.contains("Units Moved"))
                        {
                            Toast.makeText(
                                    getBaseContext(),
                                    message,
                                    Toast.LENGTH_SHORT).show();

                            army a = firstTouched.getHexData().get_hex_army();
                            secondTouched.context=1;
                            firstTouched.context=1;
                            secondTouched.getHexData().add_army(a);
                            firstTouched.getHexData().remove_Army();
                            HexMap.requestRender();
                        }
                        else{
                            Toast.makeText(
                                    getBaseContext(),
                                    message,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error.Response" + error.getMessage());

                        Toast.makeText(
                                getBaseContext(),
                                error.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reqType", "moveUnits");
                params.put("username", Const.username);
                params.put("gameID", Const.gameID);
                params.put("data", jsonString);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "string_req");


    }

    public void leaveGame(){

        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        String message = response.toString();
                        if (message.contains("Main Menu")){
                            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, message);
                            startActivity(intent);
                        }
                        else {
                            //do something
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error.Response" + error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reqType", "leaveGame");
                params.put("username", Const.username);
                params.put("gameID", Const.gameID);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "string_req");
    }

    public void updateData(JSONObject json)
    {
        System.out.println(json.toString());

        String [] hexdataU1 = new String[numGameHexes];
        String [] hexdataU2 = new String[numGameHexes];
        String [] hexdataU3 = new String[numGameHexes];
        String [] hexTerrain = new String[numGameHexes];
        String [] hexStructure = new String[numGameHexes];
        String [] hexOwner = new String [numGameHexes];
        String [] hexResource = new String[numGameHexes];
        String playerIncome;
        String playerTCash;
        String playerUnitTotal;
        String  playerResourceA;
        String [] Username = new String[2];
        ArrayList<units> swords = new ArrayList<units>(2);
        ArrayList<units> archers = new ArrayList<units>(2);
        ArrayList<units> horses = new ArrayList<units>(2);
        double slow=0.0;
        User[] user=new User[2];
        //System.out.println(json.toString());

        try {

            if(Integer.parseInt(json.get(Const.username).toString())==1) {//1 or 2
                playerIncome = json.get(Integer.toString(1) + "playerIncome").toString(); //update User in Hexdata I'd suppose
                playerTCash = json.get(Integer.toString(1) + "playerTCash").toString();
                playerUnitTotal = json.get(Integer.toString(1) + "playerUnitTotal").toString();
                playerResourceA = json.get(Integer.toString(1) + "playerResourceA").toString();
                user[0] = new User(Const.username, 1, Double.parseDouble(playerTCash), 10000, null);
                playerIncome = json.get(Integer.toString(2) + "playerIncome").toString(); //update User in Hexdata I'd suppose
                playerTCash = json.get(Integer.toString(2) + "playerTCash").toString();
                playerUnitTotal = json.get(Integer.toString(2) + "playerUnitTotal").toString();
                playerResourceA = json.get(Integer.toString(2) + "playerResourceA").toString();
                user[1] = new User("Player 2",2,Double.parseDouble(playerTCash),100000,null);
            }
            else {
                playerIncome = json.get(Integer.toString(2) + "playerIncome").toString(); //update User in Hexdata I'd suppose
                playerTCash = json.get(Integer.toString(2) + "playerTCash").toString();
                playerUnitTotal = json.get(Integer.toString(2) + "playerUnitTotal").toString();
                playerResourceA = json.get(Integer.toString(2) + "playerResourceA").toString();
                user[1] = new User(Const.username, 2, Double.parseDouble(playerTCash), 100000, null);
                playerIncome = json.get(Integer.toString(1) + "playerIncome").toString(); //update User in Hexdata I'd suppose
                playerTCash = json.get(Integer.toString(1) + "playerTCash").toString();
                playerUnitTotal = json.get(Integer.toString(1) + "playerUnitTotal").toString();
                playerResourceA = json.get(Integer.toString(1) + "playerResourceA").toString();
                user[0] = new User("Player 1", 1, Double.parseDouble(playerTCash), 10000, null);
            }

            for(int i=0;i<9;i++){
                hexdataU1[i]=json.get(Integer.toString(i)+"hexU1").toString(); //swords
                hexdataU2[i]=json.get(Integer.toString(i)+"hexU2").toString(); //archers
                hexdataU3[i]=json.get(Integer.toString(i)+"hexU3").toString(); //horses
                hexTerrain[i]=json.get(Integer.toString(i)+"hexTerrain").toString(); //use to make a double for slow
                hexStructure[i]=json.get(Integer.toString(i)+"hexStructure").toString(); //should be null
                hexOwner[i]=json.get(Integer.toString(i)+"hexOwner").toString(); //none for now
                hexResource[i]=json.get(Integer.toString(i)+"hexResource").toString(); //none for now
                for(int j=0;j<Integer.parseInt(hexdataU1[i]);j++){
                    swords.add(new units("sword","Generic",0,0,0,0,0,0));
                }
                for(int j=0;j<Integer.parseInt(hexdataU2[i]);j++){
                    archers.add(new units("archer","Slightly less generic",0,0,0,0,0,0));
                }
                for(int j=0;j<Integer.parseInt(hexdataU3[i]);j++){
                    horses.add(new units("horse","Lances couched",0,0,0,0,0,0));
                }
                if(Integer.parseInt(hexTerrain[i])==0){
                    slow=1.0;
                }

                if (Integer.parseInt(hexOwner[i]) == user[0].get_ID()) {
                        Hex_data newData = new Hex_data(i, new army(swords, archers, horses, i, 0), slow, 1.0, 1000, null, user[0]);
                        HexMap.getRenderer().HexBoard[i].setHexData(newData);
                }

                else if (Integer.parseInt(hexOwner[i]) == user[1].get_ID()) {
                        Hex_data newData = new Hex_data(i, new army(swords, archers, horses, i, 0), slow, 1.0, 1000, null, user[1]);
                        HexMap.getRenderer().HexBoard[i].setHexData(newData);
                }

                else{
                    Hex_data newData = new Hex_data(i, new army(swords, archers, horses, i, 0), slow, 1.0, 1000, null, null);
                    HexMap.getRenderer().HexBoard[i].setHexData(newData);
                }

            }

            System.out.println(user[1]);

        } catch (JSONException e) {
            System.out.println("failure to parse");
            e.printStackTrace();
        }

    }

    public void ServerThread(){

        Toast.makeText(getBaseContext(),
                "Please wait, connecting to server.",
                Toast.LENGTH_SHORT).show();

        // Create Inner Thread Class
        background = new Thread(new Runnable() {


            // Define the Handler that receives messages from the thread and update the progress
            private final Handler handler = new Handler() {

                public void handleMessage(Message msg) {

                    String aResponse = msg.getData().getString("message");
                    JSONObject json = null;
                    try {
                        json = new JSONObject(aResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    updateData(json); //pass the data to function to update hexes

                    if ((aResponse != null)) {

                        String res = null;
                        try {
                            res = json.get("gameID").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    else
                    {

                        // ALERT MESSAGE
                        Toast.makeText(getBaseContext(), "Not Got Response From Server.", Toast.LENGTH_SHORT).show();
                    }

                }
            };

            // After call for background.start this run method call
            public void run() {

                if(!Thread.currentThread().isInterrupted()) {
                    try {
                        String message;

                        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                        Log.d("Response", response);

                                        String message = response;

                                        if(message.contains("Match Making"))
                                        {
                                            Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            threadMsg(message);
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        System.out.println("error from server");
                                        Log.d(TAG, "Error.Response" + error.getMessage());
                                        if (error instanceof TimeoutError) {
                                            Log.e("Volley", "TimeoutError");
                                        }else if(error instanceof NoConnectionError){
                                            Log.e("Volley", "NoConnectionError");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.e("Volley", "AuthFailureError");
                                        } else if (error instanceof ServerError) {
                                            Log.e("Volley", "ServerError");
                                        } else if (error instanceof NetworkError) {
                                            Log.e("Volley", "NetworkError");
                                        } else if (error instanceof ParseError) {
                                            Log.e("Volley", "ParseError");
                                        }

                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("reqType", "updateGame");
                                params.put("username", Const.username);
                                params.put("gameID", Const.gameID);

                                return params;
                            }
                        };
                        AppController.getInstance().addToRequestQueue(strReq, "string_req");

                    } catch (Throwable t) {
                        // just end the background thread
                        Log.i("Animation", "Thread  exception " + t);
                    }

                    handler.postDelayed(this, 6000);
                }
            }

            private void threadMsg(String msg) {

                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                }
            }

        });

        // Start Thread
        background.start();  //After call start method thread called run Method
    }
}
