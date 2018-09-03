package com.example.william.bridgeassault.bridgeAssault.bridge;

/**
 * Created by William on 1/8/2018.
 */

public class Space {
    private SpaceType type;
    private int enemyColor;
    boolean toVisit;//used by Bridge class' isBroken() method.

    Space(){
        type = SpaceType.NORMAL;
        toVisit = false;
    }
    public void setType(SpaceType type){
        this.type = type;
    }

    public SpaceType getType(){
        return type;
    }

    public void setColor(int color){
        enemyColor = color;
    }

    public int getColor(){
        return enemyColor;
    }
}
