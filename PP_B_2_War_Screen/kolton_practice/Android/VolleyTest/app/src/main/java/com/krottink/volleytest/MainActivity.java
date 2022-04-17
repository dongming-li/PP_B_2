package com.krottink.volleytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;

public class MainActivity extends AppCompatActivity{

    public static final String EXTRA_MESSAGE = "com.krottink.volleytest.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the String Request button */
    public void goToStringRequestActivity(View view) {
        Intent intent = new Intent(this, StringRequestActivity.class);
        startActivity(intent);
    }

    public void goToJsonRequestActivity(View view) {
        Intent intent = new Intent(this, JsonRequestActivity.class);
        startActivity(intent);
    }

    public void goToImageRequestActivity(View view) {
        Intent intent = new Intent(this, ImageRequestActivity.class);
        startActivity(intent);
    }


}
