package hr.foi.nj3m.core.controllers.components;

import hr.foi.nj3m.core.controllers.algorithms.MBotInfoCutter;
import hr.foi.nj3m.interfaces.ILineFollower;

public class LineFollower implements ILineFollower {

    public static boolean left, right;

    public LineFollower()
    {

    }

    @Override
    public boolean rightSideOut() {
        return !right && left;
    }

    @Override
    public boolean leftSideOut() {
        return right && !left;
    }

    @Override
    public boolean isOnCrossroad() {
        return !right && !left;
    }

    @Override
    public String getFullValue() {
        return null;
    }


    //neka se ova metoda kod ove klase poziva kao setCurrentValue("")
    @Override
    public void setCurrentValue(String value) {
        left = MBotInfoCutter.LineFollowerLeft;
        right = MBotInfoCutter.LineFollowerRight;
        /*if(currentVal=="OnLine"){
            left=true;
            right=true;
        }
        else if(currentVal=="RightOut"){
            left=true;
            right=false;
        }
        else if(currentVal=="LeftOut"){
            left=false;
            right=true;
        }
        else if(currentVal=="BotbOut"){
            left=false;
            right=false;
        }*/
        //true predstavlja da je senzor na liniji, false da je van linije
    }
}
