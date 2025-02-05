package com.example.eggtimer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eggtimer.hbactivities.HardActivity;
import com.example.eggtimer.hbactivities.MediumActivity;
import com.example.eggtimer.hbactivities.RunnyActivity;
import com.example.eggtimer.hbactivities.SoftActivity;

public class HBActivity extends AppCompatActivity {

    Button sixmin;
    Button eightmin;
    Button tenmin;
    Button twelvemin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hbactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        sixmin=findViewById(R.id.runny);
        eightmin=findViewById(R.id.soft);
        tenmin=findViewById(R.id.medium);
        twelvemin=findViewById(R.id.hard);



        sixmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HBActivity.this, RunnyActivity.class);
                startActivity(intent);
            }
        });

        eightmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HBActivity.this, SoftActivity.class);
                startActivity(intent);
            }
        });

        tenmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HBActivity.this, MediumActivity.class);
                startActivity(intent);
            }
        });

        twelvemin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HBActivity.this, HardActivity.class);
                startActivity(intent);
            }
        });
    }
}