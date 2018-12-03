package hr.foi.nj3m.interfaces;

import hr.foi.nj3m.interfaces.Enumerations.Sides;

public interface IUltraSonic extends ISensors {

    Sides getSensorSide();

    double getNumericValue();

    boolean seesObstacle();
}
