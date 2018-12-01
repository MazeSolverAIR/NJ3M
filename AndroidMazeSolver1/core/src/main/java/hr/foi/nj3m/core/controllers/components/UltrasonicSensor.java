package hr.foi.nj3m.core.controllers.components;

import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.IUltraSonic;

public class UltrasonicSensor implements IUltraSonic {

    private Sides SensorSide = null;
    private String CurrentValue = "";

    public UltrasonicSensor(Sides sensorSide) {
        this.SensorSide = sensorSide;
    }

    @Override
    public Sides getSensorSide() {
        return this.SensorSide;
    }

    @Override
    public String getFullValue() {
        //Poruka je oblika FUsS:#, tj. FrontUltrasonicSensor: broj

        return CurrentValue;
    }


    public double getNumericValue()
    {
        double returnValue = 0;
        String substring = CurrentValue.substring(CurrentValue.lastIndexOf(':'));

        try{

            returnValue = Double.parseDouble(substring);
        }
        catch (NumberFormatException ex)
        {
            returnValue = 0;
        }

        return returnValue;
    }

    @Override
    public boolean seesObstacle() {
        if(getNumericValue() < 5)
            return true;

        else return false;
    }


    @Override
    public void setCurrentValue(String currentVal) {
        this.CurrentValue = currentVal;
    }
}
