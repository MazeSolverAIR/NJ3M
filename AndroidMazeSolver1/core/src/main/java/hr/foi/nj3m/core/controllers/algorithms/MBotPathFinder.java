package hr.foi.nj3m.core.controllers.algorithms;


import java.util.List;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
import hr.foi.nj3m.interfaces.Enumerations.SensorSide;
import hr.foi.nj3m.interfaces.IUltraSonic;

import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.LastCommand;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.Null;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.Rotate;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RunMotors;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.SpeedUpLeft;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.SpeedUpRight;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.StopMotors;
import static hr.foi.nj3m.interfaces.Enumerations.SensorSide.Front;
import static hr.foi.nj3m.interfaces.Enumerations.SensorSide.Left;
import static hr.foi.nj3m.interfaces.Enumerations.SensorSide.Right;

public class MBotPathFinder {

    private List<IUltraSonic> Sensors = null;

    private IUltraSonic LeftSensor = null;
    private IUltraSonic RightSensor = null;
    private IUltraSonic FrontSensor = null;

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

    public List<CommandsToMBot> FindPath()
    {
        List<CommandsToMBot> finalCommandList = null;

        if(FrontSensor != null && RightSensor != null && LeftSensor != null)
        {
            finalCommandList.add(centerMBotThreeSensors());
            finalCommandList.addAll(findPathThreeSensors());
        }

        else if(FrontSensor != null && RightSensor == null && LeftSensor == null)
        {
            finalCommandList = findPathFrontSensor();
        }

        else if(FrontSensor != null && RightSensor != null && LeftSensor == null)
        {
            finalCommandList = findPathFrontSensor();
        }

        else if(FrontSensor != null && RightSensor == null && LeftSensor != null)
        {
            finalCommandList = findPathFrontSensor();
        }

        return finalCommandList;
    }

    private List<CommandsToMBot> findPathThreeSensors()
    {
        List<CommandsToMBot> commandsToMBotList = null;

        commandsToMBotList.add(centerMBotThreeSensors());


        return commandsToMBotList;
    }

    private CommandsToMBot centerMBotThreeSensors()
    {
        CommandsToMBot returnCommand = Null;

        if(LeftSensor.seesObstacle())
            returnCommand = SpeedUpLeft;

        else if(RightSensor.seesObstacle())
            returnCommand = SpeedUpRight;

        return returnCommand;
    }

    private List<CommandsToMBot> findPathFrontSensor()
    {
        List<CommandsToMBot> commandsToMBotList = null;
        if(!FrontSensor.seesObstacle())
            commandsToMBotList.add(RunMotors);
        else
        {
            commandsToMBotList.add(StopMotors);
            commandsToMBotList.add(Rotate);
            commandsToMBotList.add(LastCommand);
        }

        return commandsToMBotList;
    }
}
