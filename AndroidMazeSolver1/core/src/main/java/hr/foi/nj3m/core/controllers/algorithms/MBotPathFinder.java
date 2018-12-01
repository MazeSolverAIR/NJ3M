package hr.foi.nj3m.core.controllers.algorithms;


import java.util.List;

import hr.foi.nj3m.core.R;
import hr.foi.nj3m.core.controllers.components.Crossroad;
import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.IUltraSonic;

import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.LastCommand;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.Null;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RotateLeft;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RotateRight;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RunMotors;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.SpeedUpLeft;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.SpeedUpRight;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.StopMotors;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.Front;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.Left;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.Right;

public class MBotPathFinder {

    private List<IUltraSonic> Sensors = null;

    public IUltraSonic LeftSensor = null;
    public IUltraSonic RightSensor = null;
    public IUltraSonic FrontSensor = null;

    public List<Crossroad> CrossroadsList = null;

    private MBotPathFinder Instance = null;

    public MBotPathFinder createInstance(List<IUltraSonic> sensors)
    {
        this.Instance = new MBotPathFinder(sensors);

        return this.Instance;
    }

    private MBotPathFinder(List<IUltraSonic> sensors)
    {
        this.Sensors = sensors;

        setSensorVariables();
    }

    private void setSensorVariables() {
        for (IUltraSonic sensor : Sensors) {
            Sides sensorSide = sensor.getSensorSide();
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
        double sensorDistanceSum = LeftSensor.getNumericValue() + RightSensor.getNumericValue();

        if(!FrontSensor.seesObstacle())
            commandsToMBotList.add(RunMotors);
        else
            commandsToMBotList.add(StopMotors);

        if(Crossroad.checkIfCrossroad(sensorDistanceSum))
            commandsToMBotList.addAll(manageCrossroad());


        return commandsToMBotList;
    }

    private CommandsToMBot centerMBotThreeSensors()
    {
        CommandsToMBot returnCommand = Null;

        double sensorDistanceSum = LeftSensor.getNumericValue() + RightSensor.getNumericValue();

        if(!Crossroad.checkIfCrossroad(sensorDistanceSum))
        {
            if(LeftSensor.getNumericValue() > RightSensor.getNumericValue())
                returnCommand = SpeedUpLeft;


            else if(RightSensor.getNumericValue() > LeftSensor.getNumericValue())
                returnCommand = SpeedUpRight;
        }

        return returnCommand;
    }

    private List<CommandsToMBot> manageCrossroad()
    {
        List<CommandsToMBot> commandsToMBotList = null;
        Crossroad crossroad = null;

        if(this.CrossroadsList == null)
        {
            crossroad = new Crossroad(this);
            this.CrossroadsList.add(crossroad);
        }
        else
        {
            /*trebam napraviti jos da provjerava zadnjeg u listi, ako je kod njega smijer kretanja == rotirajFull
            onda predzadnjeg povecaj za jedan.....
             */
        }


        commandsToMBotList.add(StopMotors);

        if(Crossroad.checkCrossroadSide(this.RightSensor))
        {
            commandsToMBotList.add(RotateRight);
        }

        else if(Crossroad.checkCrossroadSide(this.FrontSensor))
        {
            commandsToMBotList.add(RunMotors);
        }

        else if(Crossroad.checkCrossroadSide(this.LeftSensor))
        {
            commandsToMBotList.add(RotateLeft);
        }

        return commandsToMBotList;
    }


    private List<CommandsToMBot> findPathFrontSensor()
    {
        List<CommandsToMBot> commandsToMBotList = null;
        if(!FrontSensor.seesObstacle())
            commandsToMBotList.add(RunMotors);
        else
        {
            commandsToMBotList.add(StopMotors);
            commandsToMBotList.add(RotateRight);
            commandsToMBotList.add(LastCommand);
        }

        return commandsToMBotList;
    }
}
