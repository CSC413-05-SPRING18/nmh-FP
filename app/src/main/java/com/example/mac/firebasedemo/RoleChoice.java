package com.example.mac.firebasedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RoleChoice extends AppCompatActivity {

    private Button djButton, viewerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_choice);

        // Initialize buttons
        djButton = (Button)findViewById(R.id.djButton);
        viewerButton = (Button)findViewById(R.id.viewerButton);

        // set click listeners
        djButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignDj();
            }
        });

        viewerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignViewer();
            }
        });
    }

    //Allow DJ to be assigned
    private void assignDj() {
        System.out.println("DJ Button");
        startActivity(new Intent(RoleChoice.this, MainActivity.class));
        finish();
    }

    //Allow Viewer to be assigned
    private void assignViewer() {
        System.out.println("Viewer Button");
        startActivity(new Intent(RoleChoice.this, VoteActivity.class));
        finish();
    }
}