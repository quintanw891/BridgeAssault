package com.example.william.bridgeassault;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.william.bridgeassault.bridgeAssault.*;

import java.util.StringTokenizer;
import java.util.Timer;

public class GameActivity extends AppCompatActivity {

    BridgeAssault game;
    ImageView[][] gameSpaces;
    int rows, numEnemies, columns;
    Timer updateTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String specification = intent.getStringExtra(HomeActivity.EXTRA_SPECIFICATION);
        //Log.d("PASSED_TO_GAME_ACTIVITY", '\''+specification+'\'');
        StringTokenizer tokenizer = new StringTokenizer(specification);
        rows = Integer.parseInt(tokenizer.nextToken());
        numEnemies = Integer.parseInt(tokenizer.nextToken());
        columns = Integer.parseInt(tokenizer.nextToken());
        gameSpaces = new ImageView[rows][columns];

        if(rows == 5 && columns == 3) {//if easy mode
            setContentView(R.layout.activity_5x3game);
            gameSpaces[0][0] = findViewById(R.id.imageView);
            gameSpaces[0][1] = findViewById(R.id.imageView2);
            gameSpaces[0][2] = findViewById(R.id.imageView3);
            gameSpaces[1][0] = findViewById(R.id.imageView4);
            gameSpaces[1][1] = findViewById(R.id.imageView5);
            gameSpaces[1][2] = findViewById(R.id.imageView6);
            gameSpaces[2][0] = findViewById(R.id.imageView7);
            gameSpaces[2][1] = findViewById(R.id.imageView8);
            gameSpaces[2][2] = findViewById(R.id.imageView9);
            gameSpaces[3][0] = findViewById(R.id.imageView10);
            gameSpaces[3][1] = findViewById(R.id.imageView11);
            gameSpaces[3][2] = findViewById(R.id.imageView12);
            gameSpaces[4][0] = findViewById(R.id.imageView13);
            gameSpaces[4][1] = findViewById(R.id.imageView14);
            gameSpaces[4][2] = findViewById(R.id.imageView15);
        }
        //TODO set content view to different layouts

            game = new BridgeAssault(numEnemies,rows,columns);

        game.startGame();
        updateTimer = new Timer();
        //updateTimer.schedule();
    }

    public void updateInterface(){
        Space[][] board = game.bridge.spaces;
        int rows = game.bridge.rows;
        int columns = game.bridge.columns;
        Drawable imageDrawable = getDrawable(R.drawable.normal_space);
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(board[i][j].getType() == SpaceType.NORMAL){
                    imageDrawable = getDrawable(R.drawable.normal_space);
                }
                //TODO check for other types of spaces
                gameSpaces[i][j].setImageDrawable(imageDrawable);
            }
        }
    }
}
