package com.example.william.bridgeassault.bridgeAssault;

import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by William on 1/8/2018.
 */

public class BridgeAssault {

    private final int MIN_BRIDGE_ROWS = 5;
    private final int MIN_BRIDGE_COLUMNS = 3;
    private final int SEND_ENEMY_DELAY = 1000;

    private int numEnemies;
    public Bridge bridge;
    private Enemy[] enemies;
    private int nextEnemyIndex;
    private ArrayList<Enemy> attackingEnemies;
    private Timer sendEnemyTimer, enemyMovementTimer;
    private TimerTask sendEnemy, moveEnemies;

    public BridgeAssault(int n, int rows, int columns) {
        if (rows < MIN_BRIDGE_ROWS || columns < MIN_BRIDGE_COLUMNS) {
            rows = MIN_BRIDGE_ROWS;
            columns = MIN_BRIDGE_COLUMNS;
        }
        numEnemies = n;
        bridge = new Bridge(rows,columns);
        enemies = new Enemy[numEnemies];
        for (int i = 0; i < numEnemies; i++)
            enemies[i] = new Enemy();
        nextEnemyIndex = 0;
        attackingEnemies = new ArrayList<Enemy>();
        sendEnemyTimer = new Timer();
        sendEnemy = new TimerTask() {
            public void run() {
                Enemy nextEnemy = enemies[nextEnemyIndex];
                nextEnemy.setRow(0);
                nextEnemy.setColumn(1);//TODO have enemy choose a column
                attackingEnemies.add(nextEnemy);
                Log.d("SENDING",nextEnemy.toString());
                bridge.spaces[nextEnemy.getRow()][nextEnemy.getColumn()].setType(SpaceType.OCCUPIED);
                nextEnemyIndex++;
                if (nextEnemyIndex >= numEnemies)
                    sendEnemyTimer.cancel();
            }
        };
        moveEnemies = new TimerTask() {
            public void run() {
                if(attackingEnemies.size() == 0){
                    return;
                }
                for(int i=0; i<attackingEnemies.size(); i++){
                    attackingEnemies.get(i).move(bridge);
                }
            }
        };

    }

    public void startGame(){
        sendEnemyTimer.schedule(sendEnemy, 0, SEND_ENEMY_DELAY);
    }

    public void endGame(){
        //TODO ???
    }
}
