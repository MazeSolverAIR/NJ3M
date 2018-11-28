package hr.foi.nj3m.core.controllers.sensors;

import hr.foi.nj3m.interfaces.Enumerations.SensorSide;
import hr.foi.nj3m.interfaces.IUltraSonic;

public class UltrasonicSensor implements IUltraSonic {

    private SensorSide SensorSide = null;
    private String CurrentValue = "";

    public UltrasonicSensor(SensorSide sensorSide) {
        this.SensorSide = sensorSide;
    }

    @Override
    public SensorSide getSensorSide() {
        return this.SensorSide;
    }

    @Override
    public String getFullValue() {
        //Poruka je oblika FUsS:#, tj. FrontUltrasonicSensor: broj

        return CurrentValue;
    }


    public int getNumericValue()
    {
        int returnValue = 0;
        String substring = CurrentValue.substring(CurrentValue.lastIndexOf(':'));

        try{

            returnValue = Integer.parseInt(substring);
        }
        catch (NumberFormatException ex)
        {
            returnValue = 0;
        }

        return returnValue;
    }



    @Override
    public void setCurrentValue(String currentVal) {
        this.CurrentValue = currentVal;
    }
}
