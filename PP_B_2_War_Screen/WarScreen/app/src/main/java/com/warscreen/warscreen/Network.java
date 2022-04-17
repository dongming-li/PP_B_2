package com.warscreen.warscreen;

/**
 * Created by Kolton on 10/8/2017.
 */

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.warscreen.warscreen.utils.Const;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
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

import static android.support.v4.content.ContextCompat.startActivity;

public class Network extends Service {
    public static final String EXTRA_MESSAGE = "com.warscreen.warscreen.MESSAGE";
    private String TAG = Network.class.getSimpleName();
    static Thread background;


//    public static void ServerThread(View hexMap){
//
//        // Create Inner Thread Class
//        background = new Thread(new Runnable() {
//
//            // Define the Handler that receives messages from the thread and update the progress
//            private final Handler handler = new Handler() {
//
//                public void handleMessage(Message msg) {
//
//                    String aResponse = msg.getData().getString("message");
//
//
//                    if ((null != aResponse)) {
//
//                        // ALERT MESSAGE
//                        aResponse = messages.getText() + aResponse;
//                        messages.setText(aResponse);
//                    }
//                    else
//                    {
//
//                        // ALERT MESSAGE
//                        Toast.makeText(
//                                getBaseContext(),
//                                "Not Got Response From Server.",
//                                Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            };
//
//            // After call for background.start this run method call
//            public void run() {
//
//                if(!Thread.currentThread().isInterrupted()) {
//                    try {
//                        String message;
//
//                        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                                Const.URL_JSON_OBJECT, null,
//                                new Response.Listener<JSONObject>() {
//                                    @Override
//                                    public void onResponse(JSONObject response) {
//                                        // response
//                                        Log.d("Response", response.toString());
//
//                                        String message = response.toString();
//                                        if (message.contains("error")) {
//                                            //TODO
//                                        } else {
//                                            threadMsg(message);
//                                        }
//                                    }
//                                },
//                                new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//                                        // error
//                                        Log.d(TAG, "Error.Response" + error.getMessage());
//
//                                        //TODO
//                                    }
//                                }
//                        ) {
//                            @Override
//                            public Map<String, String> getHeaders() throws AuthFailureError {
//                                HashMap<String, String> headers = new HashMap<String, String>();
//                                headers.put("Content-Type", "application/json");
//                                return headers;
//                            }
//                            @Override
//                            protected Map<String, String> getParams() {
//                                Map<String, String> params = new HashMap<String, String>();
//                                params.put("username", Const.username);
//                                params.put("gameID", Const.gameID);
//
//                                int i = 0;
//
//                                while(true){
//
//                                    HexBoardRenderer r = new HexBoardRenderer(3,3,);
//                                }
//                                JSONObject json = new JSONObject();
//                                //while not at end of hex array
//                                Hex hex = new Hex(1,1,1,0,0);
//                                String hexNum = "hex" + String.valueOf(i);
//                                try {
//                                    json.put(hexNum, hex);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//
//
//                                params.put("json", json.toString());
//                                return params;
//                            }
//                        };
//                        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//                    } catch (Throwable t) {
//                        // just end the background thread
//                        Log.i("Animation", "Thread  exception " + t);
//                    }
//
//                    handler.postDelayed(this, 6000);
//                }
//            }
//
//            private void threadMsg(String msg) {
//
//                if (!msg.equals(null) && !msg.equals("")) {
//                    Message msgObj = handler.obtainMessage();
//                    Bundle b = new Bundle();
//                    b.putString("message", msg);
//                    msgObj.setData(b);
//                    handler.sendMessage(msgObj);
//                }
//            }
//
//        });
//
//        // Start Thread
//        background.start();  //After call start method thread called run Method
//    }



    private void hideProgressDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    public static void makePostReq(final String callback, final Map map) {

        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        switch (callback) {

                            case "leaveGame":
                                leaveGame(response);
                                break;
                            case "newGame":
                                newGame(response);
                                break;
                            default:
                                break;

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                       // Log.d(TAG, "Error.Response" + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = map;
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "string_req");

    }

    public static String leaveGame(String response){

        String message = response.toString();

        if (message.contains("Main Menu")){
            return "Main Menu";
        }

        return message;
    }

    public static String newGame(String response){

        String message = response.toString();

        if (message.contains("New Game")){
            return "New Game";
        }

        return message;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
