package com.warscreen.warscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.warscreen.warscreen.utils.Const;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends Activity implements OnClickListener {

    public static final String EXTRA_MESSAGE = "com.warscreen.warscreen.MESSAGE";

    private Button btnSendChat;
    private Button btnBackToWar;
    private Button btnUpdateChat;
    private TextView messages;
    private TextView tempText;
    private EditText newMessage;
    private int var;
    Thread background;

    private String TAG = ChatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String message = intent.getStringExtra(WarScreen.EXTRA_MESSAGE);

        TextView textView = (TextView) findViewById(R.id.messages);
        textView.setText(message);

        btnSendChat = (Button) findViewById(R.id.btnSendChat);
        btnBackToWar = (Button) findViewById(R.id.btnBackToWar);
        btnUpdateChat = (Button) findViewById(R.id.btnUpdateChat);
        messages = (TextView) findViewById(R.id.messages);
        var = 1;
        tempText = (TextView) findViewById(R.id.tempText);
        newMessage = (EditText) findViewById(R.id.input_Chat);

        btnSendChat.setOnClickListener(this);
        btnBackToWar.setOnClickListener(this);
        btnUpdateChat.setOnClickListener(this);

        //ServerThread();
    }

    public void goToWarScreen(View v) {
        stopThread();
        Intent intent = new Intent(this, WarScreen.class);
        startActivity(intent);
    }

    public void stopThread() {
        background.currentThread().interrupt();
        background = null;
    }

    public void sendChat() {

        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        String message = response.toString();
                        if (message.contains("Chat Sent")) {
                            String updateMessages = messages.getText().toString() + Const.username + ": " + newMessage.getText().toString() + "\n\r";
                            messages.setText(updateMessages);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error.Response" + error.getMessage());

                        messages.setText(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reqType", "chatPost");
                params.put("username", Const.username);
                params.put("chatData", newMessage.getText().toString());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "string_req");
    }

    public void updateChat() {
        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        String message = response.toString();
                        if (message.contains("no chat logs")) {

                        } else {
                            message = messages.getText() + message;
                            messages.setText(message);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error.Response" + error.getMessage());

                        messages.setText(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reqType", "chatUpdate");
                params.put("username", Const.username);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "string_req");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackToWar:
                goToWarScreen(v);
                break;
            case R.id.btnSendChat:
                sendChat();
                break;
            case R.id.btnUpdateChat:
                updateChat();
                break;
        }
    }
}
//    public void ServerThread(){
//
//        Toast.makeText(getBaseContext(),
//                "Please wait, connecting to server.",
//                Toast.LENGTH_SHORT).show();
//
//        // Create Inner Thread Class
//        background = new Thread(new Runnable() {
//
//
//            // Define the Handler that receives messages from the thread and update the progress
//            private final Handler handler = new Handler() {
//
//                public void handleMessage(Message msg) {
//
//                    String aResponse = msg.getData().getString("message");
//
//                    if ((null != aResponse)) {
//
//                        // ALERT MESSAGE
//                        Toast.makeText(
//                                getBaseContext(),
//                                "Server Response: "+aResponse,
//                                Toast.LENGTH_SHORT).show();
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
//                        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
//                                new Response.Listener<String>() {
//                                    @Override
//                                    public void onResponse(String response) {
//                                        // response
//                                        Log.d("Response", response);
//
//                                        String message = response.toString();
//                                        if (message.contains("no chat logs")) {
//
//                                        } else {
//
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
//                                        messages.setText(error.getMessage());
//                                    }
//                                }
//                        ) {
//                            @Override
//                            protected Map<String, String> getParams() {
//                                Map<String, String> params = new HashMap<String, String>();
//                                params.put("reqType", "chatUpdate");
//                                params.put("username", Const.username);
//
//                                return params;
//                            }
//                        };
//                        AppController.getInstance().addToRequestQueue(strReq, "string_req");
//
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
//}

