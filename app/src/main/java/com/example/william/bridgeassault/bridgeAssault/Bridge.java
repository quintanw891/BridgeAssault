package com.example.william.bridgeassault.bridgeAssault;

/**
 * Created by William on 1/8/2018.
 *
 */

public class Bridge {
    public int rows, columns;
    public Space[][] spaces;

    Bridge(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        spaces = new Space[rows][columns];
        for(int row=0; row<rows; row++){
            for(int column=0; column<columns; column++){
                spaces[row][column] = new Space();
            }
        }
    }

    public boolean isBroken(){
        //TODO isBroken
        return false;
    }
}
