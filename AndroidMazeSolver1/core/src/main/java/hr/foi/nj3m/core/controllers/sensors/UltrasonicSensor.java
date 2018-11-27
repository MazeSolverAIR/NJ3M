package hr.foi.nj3m.core.controllers.sensors;

import hr.foi.nj3m.interfaces.Enumerations.SensorSide;
import hr.foi.nj3m.interfaces.IUltraSonic;

public class UltrasonicSensor implements IUltraSonic {

    SensorSide SensorSide = null;
    String CurrentValue = "";

    public UltrasonicSensor(SensorSide sensorSide) {
        this.SensorSide = sensorSide;
    }

    @Override
    public SensorSide getSensorSide() {
        return this.SensorSide;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public void setCurrentValue(String currentVal) {
        this.CurrentValue = currentVal;
    }
}
