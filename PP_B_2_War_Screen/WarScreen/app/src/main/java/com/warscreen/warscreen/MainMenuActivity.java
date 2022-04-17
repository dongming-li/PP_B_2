package com.warscreen.warscreen;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.warscreen.warscreen.utils.Const;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;


public class MainMenuActivity extends Activity implements OnClickListener {

    public static final String EXTRA_MESSAGE = "com.warscreen.warscreen.MESSAGE";

    private Button btnNewGameReq;
    private Button btnChangeSettings;
    private TextView mainResponse;
    private ProgressDialog pDialog;
    private String TAG = MainMenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //get Intent that started activity
        Intent intent = getIntent();
        String message = intent.getStringExtra(loginActivity.EXTRA_MESSAGE);
        mainResponse = (TextView) findViewById(R.id.mainResponse);
        mainResponse.setText(message);

        btnNewGameReq = (Button) findViewById(R.id.btnNewGame);
        btnChangeSettings = (Button) findViewById(R.id.btnSettings);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        btnNewGameReq.setOnClickListener(this);
        btnChangeSettings.setOnClickListener(this);

    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    public void newGameReq() {

        showProgressDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        String message = response.toString();
                        if (message.contains("Match Making") || message.contains("Game Created")){
                            Const.gameID = message.substring(13);
                            mainResponse.setText(message);
                            Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                            intent.putExtra("message", message);
                            hideProgressDialog();
                            startActivity(intent);
                        }
                        else {
                            hideProgressDialog();
                            mainResponse.setText(message);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error.Response" + error.getMessage());
                        hideProgressDialog();
                        mainResponse.setText(error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reqType", "newGame");
                params.put("username", Const.username);
                params.put("password", "password");

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, "string_req");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNewGame:
                newGameReq();
                break;
            case R.id.btnSettings:
                break;
        }
    }

}
