package com.example.william.bridgeassault.bridgeAssault;

import android.util.Log;

import java.util.Random;

/**
 * Created by William on 1/4/2018.
 */
public class Enemy implements Comparable<Enemy> {

    private int id, row, column;
    private static int numEnemies;

    public Enemy() {
        id = ++numEnemies;
        row = -1;
        column = -1;
    }

    /**
     * Move the enemy along a bridge.
     * @param bridge the bridge to move along
     * @return MoveResults object recording the status of the enemy after movement
     */
    public MoveResults move(Bridge bridge) {
        //Change the enemy's current space to the appropriate type assuming they will move
        if (bridge.spaces[row][column].getType() == SpaceType.OCCUPIED)
            bridge.spaces[row][column].setType(SpaceType.NORMAL);
        else//if the enemy's space is FILLED_OCCUPIED
            bridge.spaces[row][column].setType(SpaceType.FILLED);
        MoveResults results = new MoveResults(true, true);
        results.enemyIsAttacking = true;
        if (row + 1 == bridge.rows) {//if enemy is about to finish crossing the bridge
            results.enemyIsAttacking = false;
            Log.d("FINISH", "");
        } else {
            switch (bridge.spaces[row + 1][column].getType()) {
                case NORMAL:
                case FILLED:
                    row++;
                    bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                    break;
                case CRACKED:
                    row++;
                    results.enemyIsAttacking = false;
                    //if cracked space is not last space, check next space
                    if (row + 1 != bridge.rows) {
                        if (bridge.spaces[row + 1][column].getType() == SpaceType.FILLED ||
                                bridge.spaces[row + 1][column].getType() == SpaceType.FILLED_OCCUPIED ||
                                bridge.spaces[row + 1][column].getType() == SpaceType.BROKEN) {
                            //enemy falls
                            bridge.spaces[row][column].setType(SpaceType.BROKEN);
                            bridge.spaces[row + 1][column].setType(SpaceType.BROKEN);
                        }
                    }
                    //if cracked space is not first space, check previous space
                    if (row != 0) {
                        if (bridge.spaces[row - 1][column].getType() == SpaceType.FILLED ||
                                bridge.spaces[row - 1][column].getType() == SpaceType.FILLED_OCCUPIED ||
                                bridge.spaces[row - 1][column].getType() == SpaceType.BROKEN) {
                            //enemy falls
                            bridge.spaces[row][column].setType(SpaceType.BROKEN);
                            bridge.spaces[row - 1][column].setType(SpaceType.BROKEN);
                        }
                    }
                    if (bridge.spaces[row][column].getType() != SpaceType.BROKEN) {
                        //enemy hangs on
                        bridge.spaces[row][column].setType(SpaceType.FILLED);
                    }
                    break;
                case BROKEN:
                    if (row + 2 == bridge.rows) {//if broken space is last space
                        //enemy hangs on
                        bridge.spaces[row + 1][column].setType(SpaceType.FILLED);
                        results.enemyIsAttacking = false;
                    } else {
                        if (bridge.spaces[row + 2][column].getType() == SpaceType.NORMAL ||
                                bridge.spaces[row + 2][column].getType() == SpaceType.CRACKED ||
                                bridge.spaces[row + 2][column].getType() == SpaceType.OCCUPIED) {
                            //enemy hangs on
                            bridge.spaces[row + 1][column].setType(SpaceType.FILLED);
                            results.enemyIsAttacking = false;
                        } else {//enemy can't move down
                            //try to move left or right
                            results = moveLateral(bridge);
                            if (!results.movementSuccessful) {//enemy can't move
                                //reset current space to correct type.
                                if (bridge.spaces[row][column].getType() == SpaceType.NORMAL)
                                    bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                                else//if the current space was changed to FILLED
                                    bridge.spaces[row][column].setType(SpaceType.FILLED_OCCUPIED);
                            }
                        }
                    }
                    break;
                case OCCUPIED:
                case FILLED_OCCUPIED:
                    //try to move left or right
                    results = moveLateral(bridge);
                    if (!results.movementSuccessful) {//enemy can't move
                        //reset current space to correct type.
                        if (bridge.spaces[row][column].getType() == SpaceType.NORMAL)
                            bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                        else//if the current space was changed to FILLED
                            bridge.spaces[row][column].setType(SpaceType.FILLED_OCCUPIED);
                    }
                    break;
            }
        }
        return results;
    }

    /**
     * Move the enemy to the left or right of its position on the bridge
     * @param bridge the bridge to move along
     * @return MoveResults object recording the status of the enemy after movement
     */
    private MoveResults moveLateral(Bridge bridge) {
        MoveResults results;
        Random r = new Random();
        if (r.nextInt(1) > 0) {   //try left first
            results = moveLeft(bridge);
            if (!results.movementSuccessful)
                results = moveRight(bridge);
        } else {                          //try right first
            results = moveRight(bridge);
            if (!results.movementSuccessful)
                results = moveLeft(bridge);
        }
        return results;
    }

    /**
     * Move the enemy to the left of its position on the bridge
     * @param bridge the bridge to move along
     * @return MoveResults object recording the status of the enemy after movement
     */
    private MoveResults moveLeft(Bridge bridge) {
        MoveResults results = new MoveResults(false, false);
        return results;
    }

    /**
     * Move the enemy to the right of its position on the bridge
     * @param bridge the bridge to move along
     * @return MoveResults object recording the status of the enemy after movement
     */
    private MoveResults moveRight(Bridge bridge) {
        MoveResults results = new MoveResults(false, false);
        return results;
    }

    /**
     * Enter a bridge by moving to one of the spaces on the bridge's top row.
     * @param bridge the bridge to enter
     * @return MoveResults object recording the status of the enemy after movement
     */
    public MoveResults enterBridge(Bridge bridge) {
        MoveResults results = new MoveResults(false, false);
        //try to enter the bridge at each of its columns in random order
        int[] attemptOrder = new int[bridge.columns];
        for (int i = 0; i < attemptOrder.length; i++) {
            attemptOrder[i] = i;
        }
        //randomize column attempt order
        Random r = new Random();
        for (int i = 0; i < attemptOrder.length; i++) {
            int newIndex = r.nextInt(attemptOrder.length);
            int temp = attemptOrder[newIndex];
            attemptOrder[newIndex] = attemptOrder[i];
            attemptOrder[i] = temp;
        }
        //attempt to enter bridge at each column in the established random order
        int i = 0;
        do {
            results = moveColumn(attemptOrder[i], bridge);
            i++;
        } while (results.movementSuccessful == false && i < attemptOrder.length);

        return results;
    }

    /**
     * Enter a bridge at a specified column by moving to the top row space of the bridge's
     * specified column.
     * @param bridge the bridge to enter.
     * @param columnToTry the column to enter the bridge at
     * @return MoveResults object recording the status of the enemy after movement
     */
    private MoveResults moveColumn(int columnToTry, Bridge bridge) {
        //Log.d("TRYING","column "+columnToTry);
        MoveResults results = new MoveResults(false, false);
        //set enemy's position to potential starting point
        row = 0;
        column = columnToTry;
        switch (bridge.spaces[0][columnToTry].getType()) {
            case NORMAL:
            case FILLED:
                results = new MoveResults(true, true);
                bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                break;
            case CRACKED:
                results = new MoveResults(true, false);
                //check the next space, to see if enemy falls
                if (bridge.spaces[row + 1][column].getType() == SpaceType.FILLED ||
                        bridge.spaces[row + 1][column].getType() == SpaceType.FILLED_OCCUPIED ||
                        bridge.spaces[row + 1][column].getType() == SpaceType.BROKEN) {
                    //enemy falls
                    bridge.spaces[row][column].setType(SpaceType.BROKEN);
                    bridge.spaces[row + 1][column].setType(SpaceType.BROKEN);
                } else {
                    //enemy hangs on
                    bridge.spaces[row][column].setType(SpaceType.FILLED);
                }
                break;
            case BROKEN:
                if (bridge.spaces[row + 1][column].getType() == SpaceType.NORMAL ||
                        bridge.spaces[row + 1][column].getType() == SpaceType.CRACKED ||
                        bridge.spaces[row + 1][column].getType() == SpaceType.OCCUPIED) {
                    //enemy hangs on
                    bridge.spaces[row][column].setType(SpaceType.FILLED);
                    results = new MoveResults(true, false);
                } else {//enemy cant enter at this column
                    results = new MoveResults(false, false);
                }
                break;
            case OCCUPIED:
            case FILLED_OCCUPIED:
                results = new MoveResults(false, false);
                break;
        }
        return results;
    }

    /**
     * The results of a call to the Enemy class' move(), moveLateral(), moveLeft(), moveRight(), and
     * enterBridge() methods. This object holds boolean variables marking the status of the enemy
     * after calling these methods.
     */
    public class MoveResults {
        /**
         * Flag marking whether or not the enemy was able to make the attempted movement.
         */
        public boolean movementSuccessful;
        /**
         * Flag marking whether or not the enemy is still attacking. true if the enemy is still
         * attacking as a result of the movement, false otherwise.
         * Attacking is defined as standing on the bridge in an OCCUPIED or FILLED_OCCUPIED space
         * and therefore being subject to further movement. Not attacking is defined as either
         * hanging onto the bridge in a FILLED space or having fallen through the bridge in a BROKEN
         * space and therefore no longer being subject to further movement.
         **/
        public boolean enemyIsAttacking;

        private MoveResults(boolean entered, boolean attacking) {
            movementSuccessful = entered;
            enemyIsAttacking = attacking;
        }
    }

    @Override
    public int compareTo(Enemy o) {
        if (row > o.row)
            return 1;
        if (row < o.row)
            return -1;
        return 0;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String toString() {
        return ("enemy " + id);
    }
}