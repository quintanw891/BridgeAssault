package com.example.william.bridgeassault.bridgeAssault;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.william.bridgeassault.bridgeAssault.bridge.*;

import java.util.Random;

/**
 * Created by William on 1/4/2018.
 *
 */
public class Enemy implements Comparable<Enemy> {

    private int id, row, column;
    private static int numEnemies;

    Enemy() {
        id = ++numEnemies;
        row = -1;
        column = -1;
    }

    /**
     * Move the enemy along a bridge.
     *
     * @param bridge the bridge to move along
     * @return MoveResults object recording the status of the enemy after movement
     */
    MoveResults move(Bridge bridge) {
        //Change the enemy's current space to the appropriate type assuming they will move
        if (bridge.spaces[row][column].getType() == SpaceType.OCCUPIED)
            bridge.spaces[row][column].setType(SpaceType.NORMAL);
        else//if the enemy's space is FILLED_OCCUPIED
            bridge.spaces[row][column].setType(SpaceType.FILLED);
        MoveResults results = new MoveResults(true, true);
        if (row + 1 == bridge.rows) {//if enemy is about to finish crossing the bridge
            results.enemyIsAttacking = false;
            //Log.d("FINISH", "FINISH");
        } else {
            switch (bridge.spaces[row + 1][column].getType()) {
                case NORMAL:
                    row++;
                    bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                    break;
                case FILLED:
                    row++;
                    bridge.spaces[row][column].setType(SpaceType.FILLED_OCCUPIED);
                    break;
                case CRACKED:
                    row++;
                    results.enemyIsAttacking = false;
                    breakSpace(bridge);
                    break;
                case BROKEN:
                    if (canFill(bridge, row + 1, column)) {
                        bridge.spaces[row + 1][column].setType(SpaceType.FILLED);
                        results.enemyIsAttacking = false;
                    }
                    //This case falls through to next cases
                case OCCUPIED:
                case FILLED_OCCUPIED:
                    if(results.enemyIsAttacking){
                        //try to move left or right
                        results = moveLateral(bridge);
                        if (!results.movementSuccessful) {
                            //try to move back
                            results = moveBack(bridge);
                            if(!results.movementSuccessful) {
                                if(row == 0){
                                    //try to re-enter bridge at another column (top row only)
                                    results = enterBridge(bridge);
                                }
                                if(!results.movementSuccessful){//enemy can't move
                                    //reset current space to correct type.
                                    if (bridge.spaces[row][column].getType() == SpaceType.NORMAL)
                                        bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                                    else//if the current space was changed to FILLED
                                        bridge.spaces[row][column].setType(SpaceType.FILLED_OCCUPIED);
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return results;
    }

    /**
     * Move the enemy one space randomly to the left or right of its position on the bridge
     *
     * @param bridge the bridge to move along
     * @return MoveResults object recording the status of the enemy after movement
     */
    private MoveResults moveLateral(Bridge bridge) {
        MoveResults results;
        Random r = new Random();
        if (r.nextInt(2) > 0) {   //try left first
            results = moveLateral(bridge, -1);
            if (!results.movementSuccessful)
                results = moveLateral(bridge, 1);
        } else {                          //try right first
            results = moveLateral(bridge, 1);
            if (!results.movementSuccessful)
                results = moveLateral(bridge, -1);
        }
        return results;
    }

    /**
     * Move the enemy one space to the left or right of its position on the bridge.
     *
     * @param bridge the bridge to move along
     * @param direction the direction to move. Positive values and 0 = right, negative values = left.
     * @return MoveResults object recording the status of the enemy after movement
     */
    private MoveResults moveLateral(Bridge bridge, int direction) {
        MoveResults results = new MoveResults(true, true);
        int boundary;//the last column in the enemy's path that they can occupy.
        int toColumn;//the column to move to.
        if(direction >= 0){//moving right
            boundary = bridge.columns - 1;
            toColumn = column + 1;
            if(toColumn > boundary){
                results.movementSuccessful = false;
            }
            direction = 1;
        }else{//moving left
            boundary = 0;
            toColumn = column - 1;
            if(toColumn < boundary){
                results.movementSuccessful = false;
            }
            direction = -1;
        }
        if (results.movementSuccessful) {
            switch (bridge.spaces[row][column + direction].getType()) {
                case NORMAL:
                    column += direction;
                    bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                    break;
                case FILLED:
                    column += direction;
                    bridge.spaces[row][column].setType(SpaceType.FILLED_OCCUPIED);
                    break;
                case CRACKED:
                    column += direction;
                    results.enemyIsAttacking = false;
                    breakSpace(bridge);
                    break;
                case BROKEN:
                    if (canFill(bridge, row, column + direction)) {
                        //enemy hangs on
                        column += direction;
                        bridge.spaces[row][column].setType(SpaceType.FILLED);
                        results.enemyIsAttacking = false;
                    } else {
                        results.movementSuccessful = false;
                    }
                    break;
                case OCCUPIED:
                case FILLED_OCCUPIED:
                    results.movementSuccessful = false;
                    break;
            }
        }
        return results;
    }

    /**
     * Move the enemy one space above of its position on the bridge.
     *
     * @param bridge the bridge to move along
     * @return MoveResults object recording the status of the enemy after movement
     */
    private MoveResults moveBack(Bridge bridge){
        MoveResults results = new MoveResults(true, true);
        if(row == 0){
            results.movementSuccessful = false;
        }
        else{
            switch (bridge.spaces[row - 1][column].getType()) {
                case NORMAL:
                    Log.d("BACK","NORMAL");
                    row--;
                    bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                    break;
                case FILLED:
                    row--;
                    bridge.spaces[row][column].setType(SpaceType.FILLED_OCCUPIED);
                    break;
                case CRACKED:
                    row--;
                    results.enemyIsAttacking = false;
                    breakSpace(bridge);
                    break;
                case BROKEN:
                    if (canFill(bridge, row - 1, column)) {
                        //enemy hangs on
                        row--;
                        bridge.spaces[row][column].setType(SpaceType.FILLED);
                        results.enemyIsAttacking = false;
                    } else {//enemy can't move left
                        results.movementSuccessful = false;
                    }
                    break;
                case OCCUPIED:
                case FILLED_OCCUPIED:
                    results.movementSuccessful = false;
                    break;
            }
        }
        return results;
    }

    /**
     * Break the space that the enemy is positioned on. Either fall through the bridge or hang on
     * depending on the conditions of surrounding spaces.
     *
     * @param bridge the bridge where the enemy is positioned
     */
    private void breakSpace(Bridge bridge) {
        //assume enemy can hang on
        bridge.spaces[row][column].setType(SpaceType.FILLED);
        //if this space is not last space, check next space
        if (row + 1 != bridge.rows) {
            if (bridge.spaces[row + 1][column].getType() == SpaceType.FILLED ||
                    bridge.spaces[row + 1][column].getType() == SpaceType.FILLED_OCCUPIED ||
                    bridge.spaces[row + 1][column].getType() == SpaceType.BROKEN) {
                //enemy falls
                bridge.spaces[row][column].setType(SpaceType.BROKEN);
                bridge.spaces[row + 1][column].setType(SpaceType.BROKEN);
            }
        }
        //if this space is not first space, check previous space
        if (row != 0) {
            if (bridge.spaces[row - 1][column].getType() == SpaceType.FILLED ||
                    bridge.spaces[row - 1][column].getType() == SpaceType.FILLED_OCCUPIED ||
                    bridge.spaces[row - 1][column].getType() == SpaceType.BROKEN) {
                //enemy falls
                bridge.spaces[row][column].setType(SpaceType.BROKEN);
                bridge.spaces[row - 1][column].setType(SpaceType.BROKEN);
            }
        }
    }

    /**
     * Determines whether or not the specified space can be filled by the enemy.
     *
     * @param bridge the bridge where the space is.
     * @param row    the row that the space is on.
     * @param column the column that the space is on.
     * @return true if the enemy can fill the specified space, false otherwise.
     */
    private boolean canFill(Bridge bridge, int row, int column) {
        if (bridge.spaces[row][column].getType() != SpaceType.BROKEN) {
            return false;
        }
        //whether or not the enemy can grab the spaces below and above the broken space
        //Grabable spaces are NORMAL, CRACKED, OCCUPIED, or either end of the bridge.
        boolean canGrabAbove = false;
        boolean canGrabBelow = false;
        if (row == 0 ||
                bridge.spaces[row - 1][column].getType() == SpaceType.NORMAL ||
                bridge.spaces[row - 1][column].getType() == SpaceType.CRACKED ||
                bridge.spaces[row - 1][column].getType() == SpaceType.OCCUPIED) {
            canGrabAbove = true;
        }
        if (row + 1 == bridge.rows ||
                bridge.spaces[row + 1][column].getType() == SpaceType.NORMAL ||
                bridge.spaces[row + 1][column].getType() == SpaceType.CRACKED ||
                bridge.spaces[row + 1][column].getType() == SpaceType.OCCUPIED) {
            canGrabBelow = true;
        }
        return canGrabAbove && canGrabBelow;
    }

    /**
     * Enter a bridge by moving to one of the spaces on the bridge's top row.
     *
     * @param bridge the bridge to enter
     * @return MoveResults object recording the status of the enemy after movement
     */
    MoveResults enterBridge(Bridge bridge) {
        MoveResults results;
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
        } while (!results.movementSuccessful && i < attemptOrder.length);

        return results;
    }

    /**
     * Enter a bridge at a specified column by moving to the top row space of the bridge's
     * specified column.
     *
     * @param bridge      the bridge to enter.
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
                results = new MoveResults(true, true);
                bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                break;
            case FILLED:
                results = new MoveResults(true, true);
                bridge.spaces[row][column].setType(SpaceType.FILLED_OCCUPIED);
                break;
            case CRACKED:
                results = new MoveResults(true, false);
                breakSpace(bridge);
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
     * The results of a call to the Enemy class' move(), moveLateral(), moveBack(), moveColumn(), and
     * enterBridge() methods. This object holds boolean variables marking the status of the enemy
     * after calling these methods.
     */
    class MoveResults {
        /**
         * Flag marking whether or not the enemy was able to make the attempted movement.
         */
        boolean movementSuccessful;
        /**
         * Flag marking whether or not the enemy is still attacking. true if the enemy is still
         * attacking as a result of the movement, false otherwise.
         * Attacking is defined as standing on the bridge in an OCCUPIED or FILLED_OCCUPIED space
         * and therefore being subject to further movement. Not attacking is defined as either
         * hanging onto the bridge in a FILLED space or having fallen through the bridge in a BROKEN
         * space and therefore no longer being subject to further movement.
         **/
        boolean enemyIsAttacking;

        private MoveResults(boolean entered, boolean attacking) {
            movementSuccessful = entered;
            enemyIsAttacking = attacking;
        }
    }

    @Override
    public int compareTo(@NonNull Enemy o) {
        if (row > o.row)
            return 1;
        if (row < o.row)
            return -1;
        return 0;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    void setRow(int row) {
        this.row = row;
    }

    void setColumn(int column) {
        this.column = column;
    }

    public String toString() {
        return ("enemy " + id);
    }
}