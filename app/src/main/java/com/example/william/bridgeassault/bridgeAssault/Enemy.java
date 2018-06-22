package com.example.william.bridgeassault.bridgeAssault;

/**
 * Created by William on 1/4/2018.
 */
public class Enemy implements Comparable<Enemy> {

    private int row,column;

    public Enemy(){
        row = -1;
        column = -1;
    }

    /**
     *
     * @param bridge the bridge to move along
     * @return true if the enemy is no longer attacking as a result of the movement, false otherwise;
     */
    public boolean move(Bridge bridge){
        boolean doneCrossingBridge = false;
        if(++row > bridge.rows-1){
            doneCrossingBridge = true;
        }
        return doneCrossingBridge;
    }

    @Override
    public int compareTo(Enemy o) {
        if(row > o.row)
            return 1;
        if(row < o.row)
            return -1;
        return 0;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return row;
    }

    public void setRow(int row){
        this.row = row;
    }

    public void setColumn(int column){
        this.column = column;
    }

    public String toString(){
        return("enemy at " + row + ", " + column);
    }
}