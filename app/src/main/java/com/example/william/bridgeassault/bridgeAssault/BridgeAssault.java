package com.example.william.bridgeassault.bridgeAssault;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Created by William on 1/8/2018.
 */

public class BridgeAssault {

    private final int MIN_BRIDGE_ROWS = 6;
    private final int MIN_BRIDGE_COLUMNS = 3;
    private final int SEND_ENEMY_DELAY = 3000;
    private final int MOVE_ENEMY_DELAY = 1000;

    private int numEnemies;
    public Bridge bridge;
    private Enemy[] enemies;
    private int nextEnemyIndex;
    private Vector<Enemy> attackingEnemies;
    private Timer sendEnemyTimer, moveEnemyTimer;
    private TimerTask sendEnemy, moveEnemies;

    public BridgeAssault(int n, int rows, int columns) {
        if (rows < MIN_BRIDGE_ROWS || columns < MIN_BRIDGE_COLUMNS) {
            rows = MIN_BRIDGE_ROWS;
            columns = MIN_BRIDGE_COLUMNS;
        }
        numEnemies = n;
        bridge = new Bridge(rows, columns);
        enemies = new Enemy[numEnemies];
        for (int i = 0; i < numEnemies; i++)
            enemies[i] = new Enemy();
        nextEnemyIndex = 0;
        attackingEnemies = new Vector<Enemy>();
        sendEnemyTimer = new Timer();
        sendEnemy = new TimerTask() {
            public void run() {
                Enemy nextEnemy = enemies[nextEnemyIndex];
                nextEnemy.setRow(0);
                nextEnemy.setColumn(1);//TODO have enemy choose a column
                //Log.d("SENDING", nextEnemy.toString());
                Enemy.MoveResults results = nextEnemy.enterBridge(bridge);
                if (results.movementSuccessful) {
                    if (results.enemyIsAttacking) {
                        attackingEnemies.add(nextEnemy);
                        //check if this movement caused elimination of any other attacking enemies
                        checkForEliminations(bridge);
                    }
                    nextEnemyIndex++;
                }
                if (nextEnemyIndex >= numEnemies)
                    sendEnemyTimer.cancel();
            }
        };
        moveEnemyTimer = new Timer();
        moveEnemies = new TimerTask() {
            public void run() {
                for (int i = 0; i < attackingEnemies.size(); i++) {
                    Enemy enemyToMove = attackingEnemies.get(i);
                    //Log.d("MOVING",enemyToMove.toString());
                    if (enemyToMove.getRow() + 1 == bridge.rows) {
                        //TODO hit player
                    }
                    if (!enemyToMove.move(bridge).enemyIsAttacking)
                        attackingEnemies.remove(enemyToMove);
                    //check if this movement caused elimination of any other attacking enemies
                    checkForEliminations(bridge);
                }
            }
        };

    }

    private void checkForEliminations(Bridge bridge) {
        for (int i = 0; i < attackingEnemies.size(); i++) {
            Enemy enemyToCheck = attackingEnemies.get(i);
            Space spaceOfEnemy = bridge.spaces[enemyToCheck.getRow()][enemyToCheck.getColumn()];
            if (spaceOfEnemy.getType() == SpaceType.BROKEN ||
                    spaceOfEnemy.getType() == SpaceType.FILLED) {
                attackingEnemies.remove(enemyToCheck);
            }
        }
    }

    public void startGame() {
        sendEnemyTimer.schedule(sendEnemy, 500, SEND_ENEMY_DELAY);
        moveEnemyTimer.schedule(moveEnemies, 0, MOVE_ENEMY_DELAY);
    }

    public void endGame() {
        //TODO ???
    }
}
