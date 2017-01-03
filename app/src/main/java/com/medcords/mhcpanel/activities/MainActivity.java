package com.medcords.mhcpanel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.medcords.mhcpanel.R;

public class MainActivity extends AppCompatActivity {

    private ImageButton addUserButton, searchUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addUserButton = (ImageButton) findViewById(R.id.add_user_button);
        searchUserButton = (ImageButton) findViewById(R.id.search_user_button);

        addUserButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                addUserButton.setImageResource(R.mipmap.add_user_green);
                searchUserButton.setImageResource(R.mipmap.search_user_grey);
                return false;
            }
        });

        searchUserButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                addUserButton.setImageResource(R.mipmap.add_user_grey);
                searchUserButton.setImageResource(R.mipmap.search_user_green);
                return false;
            }
        });

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UserSignupActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        addUserButton.setImageResource(R.mipmap.add_user_grey);
        searchUserButton.setImageResource(R.mipmap.search_user_grey);
    }
}
