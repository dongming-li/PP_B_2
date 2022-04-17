package com.warscreen.warscreen;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.warscreen.warscreen.utils.Const;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;


public class WarScreen extends Activity implements OnClickListener {

    public static final String EXTRA_MESSAGE = "com.warscreen.warscreen.MESSAGE";

    private Button btnOpenChat;
    private Button btnNewStringReq;
    private Button btnLeaveGame;
    private TextView warResponse;
    private ProgressDialog pDialog;
    private String TAG = WarScreen.class.getSimpleName();
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    Thread background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_war_screen);

        //get Intent that started activity
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        warResponse = (TextView) findViewById(R.id.warResponse);
        warResponse.setText(message);

        btnOpenChat = (Button) findViewById(R.id.btnOpenChat);
        btnNewStringReq = (Button) findViewById(R.id.btnStringReq);
        btnLeaveGame = (Button) findViewById(R.id.btnLeaveGame);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        btnLeaveGame.setOnClickListener(this);
        btnOpenChat.setOnClickListener(this);
        btnNewStringReq.setOnClickListener(this);

        //start background thread to update game
        ServerThread();
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /** Called when the user taps the String Request button */
    public void goToChatActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToStringRequestActivity(View view) {
        Intent intent = new Intent(this, StringRequestActivity.class);
        startActivity(intent);
    }

    public void leaveGame(){

        showProgressDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        String message = response.toString();
                        if (message.contains("Main Menu")){
                            Intent intent = new Intent(WarScreen.this, MainMenuActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, message);
                            hideProgressDialog();
                            startActivity(intent);
                        }
                        else {
                            hideProgressDialog();
                            warResponse.setText(message);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error.Response" + error.getMessage());
                        hideProgressDialog();
                        warResponse.setText(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reqType", "leaveGame");
                params.put("username", Const.username);
                params.put("password", Const.password);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "string_req");
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

                    if ((null != aResponse)) {
                        // ALERT MESSAGE
                        Toast.makeText(
                                getBaseContext(),
                                "Server Response: "+aResponse,
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // ALERT MESSAGE
                        Toast.makeText(
                                getBaseContext(),
                                "Not Got Response From Server.",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            };

            // After call for background.start this run method call
            public void run() {

                if(!Thread.currentThread().isInterrupted()) {
                    try {
                        String message;

                        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                                Const.URL_JSON_OBJECT, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // response
                                        Log.d("Response", response.toString());

                                        String message = response.toString();
                                        if (message.contains("error")) {
                                            //TODO
                                        } else {
                                            threadMsg(message);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        Log.d(TAG, "Error.Response" + error.getMessage());

                                        //TODO
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json");
                                return headers;
                            }
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("username", Const.username);
                                params.put("gameID", Const.gameID);

                                int i = 0;

                                JSONObject json = new JSONObject();
                                //while not at end of hex array
                                Hex hex = new Hex(1,1,1,0,0);
                                String hexNum = "hex" + String.valueOf(i);
                                try {
                                    json.put(hexNum, hex);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                                params.put("json", json.toString());
                                return params;
                            }
                        };
                        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOpenChat:
                goToChatActivity(v);
                break;
            case R.id.btnStringReq:
                goToStringRequestActivity(v);
                break;
            case R.id.btnLeaveGame:
                leaveGame();
                break;
        }
    }

}
