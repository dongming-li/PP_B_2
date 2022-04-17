package com.warscreen.warscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.warscreen.warscreen.Tutorial;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonObjectRequestActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String REQUEST_TAG = "JSON_OBJECT_REQUEST_TAG";
    public static final String POST_REQUEST_TAG = "JSON_OBJECT_POST_REQUEST_TAG";
    public static final String JSON_URL = "https://tutorialwing.com/api/tutorialwing_details.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_json_object_request);

        View view = findViewById(R.id.get_request);
        if (view != null)
            view.setOnClickListener(this);

        view = findViewById(R.id.post_request);
        if (view != null)
            view.setOnClickListener(this);
    }

    private void sendRequest() {
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showResponse(response, "Showing GET request response...");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        jsonObjectReq.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding JsonObject request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectReq, REQUEST_TAG);
    }

    private void sendPostRequest() {

        // Change the url of the post request as per your need...This url is just for demo purposes and
        // actually it does not post data...i.e. it will return same response irrespective of the value you
        // as parameters.

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, JSON_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        showResponse(response, "Showing POST request response...");
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(JsonObjectRequestActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            // You can send parameters as well with POST request....
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", "Tutorialwing");
                params.put("email", "tutorialwing@gmail.com");
                params.put("password", "1234567");
                return params;
            }
        };

        // Increase Timeout to 15 secs.
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, POST_REQUEST_TAG);
    }

    private void sendPostRequestWithHeaders() {

        // Change the url of the post request as per your need...This url is just for demo purposes and
        // actually it does not post data...i.e. it will return same response irrespective of the value you
        // as parameters.

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, JSON_URL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        showResponse(response, "Showing POST request response...");
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//						VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }) {

            // You can send parameters as header with POST request....
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, POST_REQUEST_TAG);
    }

    private void showResponse(JSONObject response, String extra_info) {
        Tutorial tutorial = new Tutorial(response);
        String text = extra_info
                + "\n\n Website: " + tutorial.getWebsite()
                + "\n\n Topics: " + tutorial.getTopics()
                + "\n\n Facebook: " + tutorial.getFacebook()
                + "\n\n Twitter: " + tutorial.getTwitter()
                + "\n\n Pinterest: " + tutorial.getPinterest()
                + "\n\n Youtube: " + tutorial.getYoutube()
                + "\n\n Message: " + tutorial.getMessage();

        TextView txvResponse = (TextView) findViewById(R.id.request_response);
        if (txvResponse != null) {
            txvResponse.setText(text);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_request:
                sendRequest();
                break;
            case R.id.post_request:
                sendPostRequest();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppController.getInstance().getRequestQueue().cancelAll(REQUEST_TAG);
    }
}
