package com.example.william.bridgeassault.bridgeAssault.bridge;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by William on 1/8/2018.
 *
 */

public class Bridge {
    public int rows, columns;
    public Space[][] spaces;

    public Bridge(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        spaces = new Space[rows][columns];
        for(int row=0; row<rows; row++){
            for(int column=0; column<columns; column++){
                spaces[row][column] = new Space();
            }
        }
    }

    /**
     * @return true if there exists a path of broken spaces from the left of the bridge to the right,
     * false otherwise
     */
    public boolean isBroken(){
        ArrayList<Space> modified = new ArrayList<Space>(); //modified space
        Stack<Point> toVisit = new Stack<Point>();          //indeces of spaces to be visited
        //Check if a path of broken spaces extends from any leftmost space the the rightmost column
        for(int i=rows-1; i>=0; i--){
            int row = i;
            int col = 0;
            Space curSpace = spaces[row][col];
            //schedule every broken space in the leftmost column to visit
            if(curSpace.getType() == SpaceType.BROKEN){
                curSpace.toVisit = true;
                toVisit.push(new Point(row,col));
                modified.add(curSpace);
            }
        }

        //for every space scheduled:
        //visit space and schedule visits for all adjacent unvisited broken spaces.
        boolean pathFound = false;  //refers to a path of broken spaces extending across the bridge
        while(!toVisit.empty() && !pathFound){
            Point curPoint = toVisit.pop();

            //check all 8 adjacent spaces for unvisited broken spaces. Schedule these to visit
            //Order: UpLeft, Left, DownLeft, Up, Down, DownRight, Right, UpRight
            Space spaceToCheck;
            if(curPoint.y > 0){
                if(curPoint.x > 0){
                    //UpLeft
                    spaceToCheck = spaces[curPoint.x-1][curPoint.y-1];
                    if(spaceToCheck.getType() == SpaceType.BROKEN && !spaceToCheck.toVisit){
                        spaceToCheck.toVisit = true;
                        toVisit.push(new Point(curPoint.x-1,curPoint.y-1));
                        modified.add(spaceToCheck);
                    }
                }
                //Left
                spaceToCheck = spaces[curPoint.x][curPoint.y-1];
                if(spaceToCheck.getType() == SpaceType.BROKEN && !spaceToCheck.toVisit){
                    spaceToCheck.toVisit = true;
                    toVisit.push(new Point(curPoint.x,curPoint.y-1));
                    modified.add(spaceToCheck);
                }
                if(curPoint.x < rows-1){
                    //DownLeft
                    spaceToCheck = spaces[curPoint.x+1][curPoint.y-1];
                    if(spaceToCheck.getType() == SpaceType.BROKEN && !spaceToCheck.toVisit){
                        spaceToCheck.toVisit = true;
                        toVisit.push(new Point(curPoint.x+1,curPoint.y-1));
                        modified.add(spaceToCheck);
                    }
                }
            }
            if(curPoint.x > 0){
                //Up
                spaceToCheck = spaces[curPoint.x-1][curPoint.y];
                if(spaceToCheck.getType() == SpaceType.BROKEN && !spaceToCheck.toVisit){
                    spaceToCheck.toVisit = true;
                    toVisit.push(new Point(curPoint.x-1,curPoint.y));
                    modified.add(spaceToCheck);
                }
            }
            if(curPoint.x < rows-1){
                //Down
                spaceToCheck = spaces[curPoint.x+1][curPoint.y];
                if(spaceToCheck.getType() == SpaceType.BROKEN && !spaceToCheck.toVisit){
                    spaceToCheck.toVisit = true;
                    toVisit.push(new Point(curPoint.x+1,curPoint.y));
                    modified.add(spaceToCheck);
                }
            }
            //If unvisited broken spaces are found to the right (UpRight, Right, DownRight), Check
            //if it is in last column. If so, this space completes a path of broken spaces across
            //the bridge. Thus, the bridge is broken.
            if(curPoint.y < columns-1){
                if(curPoint.x < rows-1){
                    //DownRight
                    spaceToCheck = spaces[curPoint.x+1][curPoint.y+1];
                    if(spaceToCheck.getType() == SpaceType.BROKEN && !spaceToCheck.toVisit){
                        spaceToCheck.toVisit = true;
                        toVisit.push(new Point(curPoint.x+1,curPoint.y+1));
                        modified.add(spaceToCheck);
                        if(curPoint.y+1 == columns-1){
                            pathFound = true;
                        }
                    }
                }
                //Right
                spaceToCheck = spaces[curPoint.x][curPoint.y+1];
                if(spaceToCheck.getType() == SpaceType.BROKEN && !spaceToCheck.toVisit){
                    spaceToCheck.toVisit = true;
                    toVisit.push(new Point(curPoint.x,curPoint.y+1));
                    modified.add(spaceToCheck);
                    if(curPoint.y+1 == columns-1){
                        pathFound = true;
                    }
                }
                if(curPoint.x > 0){
                    //UpRight
                    spaceToCheck = spaces[curPoint.x-1][curPoint.y+1];
                    if(spaceToCheck.getType() == SpaceType.BROKEN && !spaceToCheck.toVisit){
                        spaceToCheck.toVisit = true;
                        toVisit.push(new Point(curPoint.x-1,curPoint.y+1));
                        modified.add(spaceToCheck);
                        if(curPoint.y+1 == columns-1){
                            pathFound = true;
                        }
                    }
                }
            }
        }

        //reset modified values
        for (Space s : modified){
            s.toVisit = false;
        }

        return pathFound;
    }

}
