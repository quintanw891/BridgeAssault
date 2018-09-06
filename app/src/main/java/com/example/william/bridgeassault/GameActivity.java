package com.example.william.bridgeassault;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.william.bridgeassault.bridgeAssault.*;
import com.example.william.bridgeassault.bridgeAssault.bridge.Space;
import com.example.william.bridgeassault.bridgeAssault.bridge.SpaceType;

import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class GameActivity extends AppCompatActivity implements GameOverDialogFragment.GameOverDialogListener {

    BridgeAssault game;
    ImageView[][] gameSpaces;
    int rows, numEnemies, columns;
    private Timer UITimer;
    private TimerTask UITimerTask;
    private Handler UIHandler;
    private Runnable updateInterface;
    private final int UI_DELAY = 100;
    private android.support.v4.app.DialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String specification = intent.getStringExtra(HomeActivity.EXTRA_SPECIFICATION);
        Log.d("PASSED_TO_GAME_ACTIVITY", '\''+specification+'\'');
        StringTokenizer tokenizer = new StringTokenizer(specification);
        rows = Integer.parseInt(tokenizer.nextToken());
        numEnemies = Integer.parseInt(tokenizer.nextToken());
        columns = Integer.parseInt(tokenizer.nextToken());
        gameSpaces = new ImageView[rows][columns];

        if(rows == 6 && columns == 3) {//if easy mode
            setContentView(R.layout.activity_6x3game);
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
            gameSpaces[5][0] = findViewById(R.id.imageView16);
            gameSpaces[5][1] = findViewById(R.id.imageView17);
            gameSpaces[5][2] = findViewById(R.id.imageView18);
        }
        //TODO set content view to different layouts

        updateInterface = new Runnable() {
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                Space[][] board = game.bridge.spaces;
                int rows = game.bridge.rows;
                int columns = game.bridge.columns;
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        Drawable imageDrawable = getDrawable(R.drawable.normal_space);
                        switch (board[i][j].getType()) {
                            case NORMAL:
                                //Log.d("SPACE","is normal");
                                imageDrawable = getDrawable(R.drawable.normal_space);
                                break;
                            case CRACKED:
                                //Log.d("SPACE","is cracked");
                                imageDrawable = getDrawable(R.drawable.cracked_space);
                                break;
                            case BROKEN:
                                //Log.d("SPACE","is broken");
                                imageDrawable = getDrawable(R.drawable.broken_space);
                                break;
                            case FILLED:
                                //Log.d("SPACE","is filled");
                                imageDrawable = getDrawable(R.drawable.filled_space);
                                break;
                            case FILLED_OCCUPIED:
                                //Log.d("SPACE","is filled_occupied");
                                imageDrawable = getDrawable(R.drawable.filled_occupied_space);
                                imageDrawable = imageDrawable.mutate().getConstantState().newDrawable();
                                imageDrawable.setColorFilter( board[i][j].getColor(), PorterDuff.Mode.DST_OVER );
                                break;
                            case OCCUPIED:
                                //Log.d("SPACE","is occupied");
                                imageDrawable = getDrawable(R.drawable.occupied_space);
                                imageDrawable = imageDrawable.mutate().getConstantState().newDrawable();
                                imageDrawable.setColorFilter( board[i][j].getColor(), PorterDuff.Mode.DST_OVER );
                                break;
                        }
                        gameSpaces[i][j].setImageDrawable(imageDrawable);
                    }
                }
            }
        };
        game = new BridgeAssault(this,numEnemies,rows,columns);

        UITimer = new Timer();
        UIHandler = new Handler();
        UITimerTask = new TimerTask() {
            @Override
            public void run() {
                UIHandler.post(updateInterface);
                checkGameOver();
            }
        };
        UITimer.schedule(UITimerTask,0,UI_DELAY);

        game.startGame();
    }

    public void tapSpace(View view) {
        //Log.d("TAPPED", "space at "+view.getTag().toString());
        StringTokenizer tokens = new StringTokenizer(view.getTag().toString());
        int row = Integer.parseInt(tokens.nextToken());
        int column = Integer.parseInt(tokens.nextToken());
        if(game.bridge.spaces[row][column].getType() == SpaceType.NORMAL)
            game.bridge.spaces[row][column].setType(SpaceType.CRACKED);
        if(game.bridge.spaces[row][column].getType() == SpaceType.BROKEN)
            game.bridge.spaces[row][column].setType(SpaceType.NORMAL);
    }

    private void checkGameOver(){
        if(game.gameOver && dialogFragment == null){
            dialogFragment = new GameOverDialogFragment();
            dialogFragment.setArguments(game.gameOverState);
            dialogFragment.show(getSupportFragmentManager(), "game over");
        }
    }

    @Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {
        //TODO restart game instead of entire activity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog) {
        finish();
    }
}
