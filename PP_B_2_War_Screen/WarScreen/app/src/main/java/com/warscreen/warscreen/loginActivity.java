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
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class loginActivity extends Activity implements OnClickListener {

    public static final String EXTRA_MESSAGE = "com.warscreen.warscreen.MESSAGE";

    private String TAG = loginActivity.class.getSimpleName();
    private Button btnLoginReq;
    private Button btnRegReq;
    private String message = "init";
    private EditText usernameEdit;
    private EditText passwordEdit;
    private TextView loginError;
    private ProgressDialog pDialog;
    private String tag_string_req = "string_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();

        btnLoginReq = (Button) findViewById(R.id.btn_login);
        btnRegReq = (Button) findViewById(R.id.btn_reg);
        usernameEdit = (EditText) findViewById(R.id.input_username);
        passwordEdit = (EditText) findViewById(R.id.input_password);
        loginError = (TextView) findViewById(R.id.loginError);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        btnLoginReq.setOnClickListener(this);
        btnRegReq.setOnClickListener(this);

    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /**
     * Making json object request
     */
    private void makeLoginReq() {
        showProgressDialog();
        Const.username = usernameEdit.getText().toString();
        Const.password = passwordEdit.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        message = response.toString();
                        //go straight to game
                        if(message.contains("Home")) {
                            loginError.setText("");
                            Const.gameID = message.substring(5);
                            Intent intent = new Intent(loginActivity.this, MainActivity.class);
                            intent.putExtra("message", message);
                            hideProgressDialog();
                            startActivity(intent);
                        }
                        //go to Game Options
                        else if(message.contains("Game Options")){
                            loginError.setText("");
                            Intent intent = new Intent(loginActivity.this, MainMenuActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, message);
                            hideProgressDialog();
                            startActivity(intent);
                        }
                        else {
                            loginError.setText(message);
                            hideProgressDialog();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d(TAG, "Error.Response" + error.getMessage());
                    }

                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("reqType", "login");
                params.put("username", Const.username);
                params.put("password", Const.password);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Making json object request
     */
    private void makeRegReq() {
        showProgressDialog();
        Const.username = usernameEdit.getText().toString();
        Const.password = passwordEdit.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, Const.URL_POST_REQ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        message = response.toString();
                        if(message.contains("Home")) {
                            loginError.setText("");
                            Intent intent = new Intent(loginActivity.this, MainMenuActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, message);
                            hideProgressDialog();
                            startActivity(intent);
                        }
                        else {
                            loginError.setText(message);
                            hideProgressDialog();
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
                params.put("reqType", "register");
                params.put("username", Const.username);
                params.put("password", Const.password);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                makeLoginReq();
                break;
            case R.id.btn_reg:
                makeRegReq();
                break;
        }
    }

}

