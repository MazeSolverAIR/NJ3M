package hr.foi.nj3m.core.controllers.components;

import hr.foi.nj3m.interfaces.sensors.ILineFollower;

public class LineFollower implements ILineFollower {

    public static boolean left, right;
    private static String CurrentVal;

    public LineFollower()
    {

    }

    /**
     * @return true ukoliko je desni senzor van linije, a lijevi u liniji
     */
    @Override
    public boolean rightSideOut() {
        return !right && left;
    }

    /**
     * @return true ukoliko je lijevi senzor van linije, a desni u liniji
     */
    @Override
    public boolean leftSideOut() {
        return right && !left;
    }


    /**
     * @return true ukoliko nijedan senzor nije na liniji
     */
    @Override
    public boolean isOnCrossroad() {
        return !right && !left;
    }


    /**
     * @return vraca punu vrijednost primljene poruke
     */
    @Override
    public String getFullValue() {
        return CurrentVal;
    }


    /**
     * @param value primljena poruka od mBota
     */
    @Override
    public void setCurrentValue(String value) {
        CurrentVal = value;

        switch (value) {
            case "OL":
                left = true;
                right = true;
                break;
            case "RO":
                left = true;
                right = false;
                break;
            case "LO":
                left = false;
                right = true;
                break;
            case "BO":
                left = false;
                right = false;
                break;
        }
        //true predstavlja da je senzor na liniji, false da je van linije
    }
}
