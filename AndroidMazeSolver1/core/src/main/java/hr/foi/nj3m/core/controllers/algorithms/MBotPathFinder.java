package hr.foi.nj3m.core.controllers.algorithms;


import java.util.List;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
import hr.foi.nj3m.interfaces.Enumerations.SensorSide;
import hr.foi.nj3m.interfaces.IUltraSonic;

import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.Kreni;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.ZaustaviSe;
import static hr.foi.nj3m.interfaces.Enumerations.SensorSide.Front;
import static hr.foi.nj3m.interfaces.Enumerations.SensorSide.Left;
import static hr.foi.nj3m.interfaces.Enumerations.SensorSide.Right;

public class MBotPathFinder {

    List<IUltraSonic> Sensors = null;

    IUltraSonic LeftSensor = null;
    IUltraSonic RightSensor = null;
    IUltraSonic FrontSensor = null;

    public MBotPathFinder(List<IUltraSonic> sensors)
    {
        this.Sensors = sensors;

        setSensorVariables();
    }

    private void setSensorVariables() {
        for (IUltraSonic sensor : Sensors) {
            SensorSide sensorSide = sensor.getSensorSide();
            if (sensorSide == Left)
                this.LeftSensor = sensor;
            else if (sensorSide == Right)
                this.RightSensor = sensor;
            else if (sensorSide == Front)
                this.FrontSensor = sensor;
        }
    }

    public List<CommandsToMBot> FrontUltrasonicSearcher()
    {
        List<CommandsToMBot> finalCommandList = null;

        if(FrontSensor.getNumericValue() > 10)
            finalCommandList.add(Kreni);
        else
            finalCommandList.add(ZaustaviSe);
        return finalCommandList;
    }
}
