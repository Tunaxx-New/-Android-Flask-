package com.nik.someproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class IPActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ip);

        Button submitIp = findViewById(R.id.set_ip_submit);
        submitIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText data = findViewById(R.id.set_ip_data);
                //"192.168.0.103"
                Constants.host_ip = data.getText().toString();
                Constants.socket_ip = Constants.host_ip;
                Constants.host = "http://" + Constants.host_ip + ":" + Constants.host_port;
                setResult(1);
                finish();
            }
        });
    }
}
