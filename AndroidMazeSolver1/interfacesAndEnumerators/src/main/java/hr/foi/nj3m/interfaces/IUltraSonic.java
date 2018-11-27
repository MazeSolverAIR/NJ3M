package hr.foi.nj3m.interfaces;

import hr.foi.nj3m.interfaces.Enumerations.SensorSide;

public interface IUltraSonic extends ISensors {

    SensorSide getSensorSide();

    int getNumericValue();
}
