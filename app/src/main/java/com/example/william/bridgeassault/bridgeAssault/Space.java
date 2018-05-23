package com.example.william.bridgeassault.bridgeAssault;

/**
 * Created by William on 1/8/2018.
 */

public class Space {
    public SpaceType type;

    public Space(){
        type = SpaceType.NORMAL;
    }
    public void setType(SpaceType type){
        this.type = type;
    }

    public SpaceType getType(){
        return type;
    }
}
