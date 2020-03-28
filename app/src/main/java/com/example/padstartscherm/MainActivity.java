package com.example.padstartscherm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private RelativeLayout buttonContainer;
    public EditText pas, usr;
    private final String API_URL = "https://PADAPI.000webhostapp.com/Login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usr = findViewById(R.id.Username);
        pas = findViewById(R.id.UserPassword);
        button = findViewById(R.id.buttonLogin);

        // Make an attempt to login user
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = usr.getText().toString();
                final String pass = pas.getText().toString();

                Map<String, String> dataList = new HashMap<>(); // Reserve a place for data

                dataList.put("license_plate", user);
                dataList.put("password", pass);

                Request.makeRequest(dataList, API_URL, MainActivity.this, user, "Gebruiker is ingelogd");
            }
        });

        buttonContainer = findViewById(R.id.buttonContainer);

        buttonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.performClick();
            }
        });
    }

    // Prevents users pressing the back button when they logged out
    @Override
    public void onBackPressed(){
    }

    @Override
    protected void onStop() {
        super.onStop();

        button.setOnClickListener(null);
        buttonContainer.setOnClickListener(null);
}
}


