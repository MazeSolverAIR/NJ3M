package hr.foi.nj3m.core.controllers.components;

import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.sensors.IUltraSonic;

public class UltrasonicSensor implements IUltraSonic {

    private Sides SensorSide = null;
    private String CurrentValue = "";

    public UltrasonicSensor(Sides sensorSide) {
        this.SensorSide = sensorSide;
    }

    /**
     * @return strana na kojoj se ovaj senzor nalazi
     */
    @Override
    public Sides getSensorSide() {
        return this.SensorSide;
    }

    /**
     * @return puna vrijednost poruke od mBota
     */
    @Override
    public String getFullValue() {
        //Poruka je oblika FUsS:#, tj. FrontUltrasonicSensor: broj

        return CurrentValue;
    }


    /**
     * @return pretvara poruku od mBota u udaljenost
     */
    public double getNumericValue()
    {
        double returnValue = 0;

        try{

            returnValue = Double.parseDouble(this.CurrentValue);
        }
        catch (NumberFormatException ex)
        {
            returnValue = 0;
        }

        return returnValue;
    }

    /**
     * @return true ukoliko senzor ocitava vrijednost manju od 28cm
     */
    @Override
    public boolean seesObstacle() {
        if(getNumericValue() < 28)
            return true;

        else return false;
    }


    /**
     * @param value vrijednost primljena od mBota
     */
    @Override
        public void setCurrentValue(String value) {
        this.CurrentValue = value;
    }
}
