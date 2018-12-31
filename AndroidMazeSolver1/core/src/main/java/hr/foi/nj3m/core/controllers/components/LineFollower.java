package hr.foi.nj3m.core.controllers.components;

import hr.foi.nj3m.core.controllers.algorithms.MBotInfoCutter;
import hr.foi.nj3m.interfaces.ILineFollower;

public class LineFollower implements ILineFollower {

    public boolean left, right;

    public LineFollower()
    {

    }

    @Override
    public boolean isOnRightSide() {
        return false;
    }

    @Override
    public boolean isOnLeftSide() {
        return false;
    }

    @Override
    public boolean isOnCrossroad() {
        return false;
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
