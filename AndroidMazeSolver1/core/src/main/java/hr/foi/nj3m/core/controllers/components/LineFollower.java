package hr.foi.nj3m.core.controllers.components;

import hr.foi.nj3m.interfaces.sensors.ILineFollower;

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
        if(value.equals("OnLine")){
            left=true;
            right=true;
        }
        else if(value.equals("RightOut")){
            left=true;
            right=false;
        }
        else if(value.equals("LeftOut")){
            left=false;
            right=true;
        }
        else if(value.equals("BotbOut")){
            left=false;
            right=false;
        }
        //true predstavlja da je senzor na liniji, false da je van linije
    }
}
