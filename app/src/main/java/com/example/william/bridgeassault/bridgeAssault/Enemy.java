package com.example.william.bridgeassault.bridgeAssault;

/**
 * Created by William on 1/4/2018.
 */
public class Enemy implements Comparable<Enemy> {

    private int row, column;

    public Enemy() {
        row = -1;
        column = -1;
    }

    /**
     * @param bridge the bridge to move along
     * @return true if the enemy is no longer attacking as a result of the movement, false otherwise
     */
    public boolean move(Bridge bridge) {
        //Change the enemy's current space to the appropriate type assuming they will move
        if (bridge.spaces[row][column].getType() == SpaceType.OCCUPIED)
            bridge.spaces[row][column].setType(SpaceType.NORMAL);
        else//if the enemy's space is FILLED_OCCUPIED
            bridge.spaces[row][column].setType(SpaceType.FILLED);
        boolean enemyIsAttacking = true;
        if(row+1 == bridge.rows)//if enemy is about to finish crossing the bridge
            enemyIsAttacking = false;
        else {
            switch (bridge.spaces[row + 1][column].getType()) {
                case NORMAL:
                    row++;
                    bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                    break;
                case CRACKED:
                    row++;
                    enemyIsAttacking = false;
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
                    //TODO broken
                    if (row + 2 == bridge.rows) {//if broken space is last space
                        //enemy hangs on
                        bridge.spaces[row + 1][column].setType(SpaceType.FILLED);
                        enemyIsAttacking = false;
                    } else {
                        if (bridge.spaces[row + 2][column].getType() == SpaceType.NORMAL ||
                                bridge.spaces[row + 2][column].getType() == SpaceType.CRACKED ||
                                bridge.spaces[row + 2][column].getType() == SpaceType.OCCUPIED) {
                            //enemy hangs on
                            bridge.spaces[row + 1][column].setType(SpaceType.FILLED);
                            enemyIsAttacking = false;
                        } else {//enemy cant move down
                            //TODO try to go left or right randomly
                            //Can't move: reset current space to correct type.
                            if (bridge.spaces[row][column].getType() == SpaceType.NORMAL)
                                bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                            else//if the current space was changed to FILLED
                                bridge.spaces[row][column].setType(SpaceType.FILLED_OCCUPIED);
                        }
                    }
                    break;
                case FILLED:
                    row++;
                    bridge.spaces[row][column].setType(SpaceType.FILLED_OCCUPIED);
                    break;
                case FILLED_OCCUPIED:
                    //TODO try to go left or right randomly
                    //Can't move: reset current space to correct type.
                    if (bridge.spaces[row][column].getType() == SpaceType.NORMAL)
                        bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                    else//if the current space was changed to FILLED
                        bridge.spaces[row][column].setType(SpaceType.FILLED_OCCUPIED);
                    break;
                case OCCUPIED:
                    //TODO try to go left or right randomly
                    //Can't move: reset current space to correct type.
                    if (bridge.spaces[row][column].getType() == SpaceType.NORMAL)
                        bridge.spaces[row][column].setType(SpaceType.OCCUPIED);
                    else//if the current space was changed to FILLED
                        bridge.spaces[row][column].setType(SpaceType.FILLED_OCCUPIED);
                    break;
            }
        }
        return enemyIsAttacking;
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
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String toString() {
        return ("enemy at " + row + ", " + column);
    }
}