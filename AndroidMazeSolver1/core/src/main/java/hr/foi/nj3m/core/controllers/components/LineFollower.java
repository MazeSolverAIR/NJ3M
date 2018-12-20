package hr.foi.nj3m.core.controllers.components;

import hr.foi.nj3m.interfaces.ILineFollower;

public class LineFollower implements ILineFollower {

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

    @Override
    public void setCurrentValue(String currentVal) {

    }
}
