package hr.foi.nj3m.virtualmbot;

import hr.foi.nj3m.interfaces.Enumerations.Sides;

public interface IVirtualUltrasonic extends IVirtualMeasure{

    void setNewSensorSide(Sides side);
    Sides getSensorSide();
}
