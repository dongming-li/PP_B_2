package com.krottink.volleytest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.krottink.volleytest.utils.Const;

public class PutRequestActivity extends AppCompatActivity {

    private String TAG = StringRequestActivity.class.getSimpleName();
    private Button btnPutReq;
    private TextView msgResponse;
    private ProgressDialog pDialog;
    private String tag_string_req = "string_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_request);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.put_text_view);
        textView.setText(message);

        btnPutReq = (Button) findViewById(R.id.put_button);
        msgResponse = (TextView) findViewById(R.id.put_textBox);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        btnPutReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePutReq();
            }
        });
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
    private void makePutReq() {
        showProgressDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Const.URL_PUT_REQ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                msgResponse.setText(response.toString());
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
