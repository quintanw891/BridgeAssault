package com.example.william.bridgeassault;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {
    public static final String EXTRA_SPECIFICATION = "com.example.bridgeAssault.SPECIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_SPECIFICATION, view.getTag().toString());
        startActivity(intent);
    }
}
