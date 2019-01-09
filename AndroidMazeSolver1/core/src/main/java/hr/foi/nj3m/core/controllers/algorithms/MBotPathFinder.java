package hr.foi.nj3m.core.controllers.algorithms;


import java.util.ArrayList;
import java.util.List;

import hr.foi.nj3m.core.R;
import hr.foi.nj3m.core.controllers.componentManagers.CrossroadManager;
import hr.foi.nj3m.core.controllers.components.Crossroad;
import hr.foi.nj3m.core.controllers.components.LineFollower;
import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.IUltraSonic;

import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.LastCommand;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.Null;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RotateFull;
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

    private static MBotPathFinder Instance = null;

    private LineFollower lineFollower = null;

    public static MBotPathFinder createInstance(List<IUltraSonic> sensors)
    {
        Instance = new MBotPathFinder(sensors);

        return Instance;
    }

    public static MBotPathFinder createInstance()
    {
        Instance = new MBotPathFinder();
        return Instance;
    }
    private MBotPathFinder()
    {

    }

    public ArrayList<CommandsToMBot> TestMethod()
    {
        ArrayList<CommandsToMBot> commandsList = new ArrayList<>();

        commandsList.add(RotateLeft);
        commandsList.add(RotateRight);
        commandsList.add(RunMotors);
        commandsList.add(LastCommand);

        return commandsList;
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

        lineFollower = new LineFollower();
    }

    public ArrayList<CommandsToMBot> FindPath()
    {
        ArrayList<CommandsToMBot> finalCommandList = new ArrayList<>();

        if(FrontSensor != null && RightSensor != null && LeftSensor != null) //sva tri senzora su odabrana
        {
            double rightWallDistance = this.RightSensor.getNumericValue();
            double leftWallDistance = this.LeftSensor.getNumericValue();

            finalCommandList.add(centerMBotTwoOrMoreSensors(this.RightSensor.getNumericValue(), this.LeftSensor.getNumericValue()));
            finalCommandList.addAll(findPathTwoOrMoreSensors(rightWallDistance, leftWallDistance));
        }

        else if(FrontSensor != null && RightSensor != null && LeftSensor == null) //prednji i desni seenzori su odabrani
        {
            double rightWallDistance = this.RightSensor.getNumericValue();
            double leftWallDistance = R.integer.labyrinth_width - R.integer.mBot_width - R.integer.ultrasonic_sensor_width - rightWallDistance;

            finalCommandList.add(centerMBotTwoOrMoreSensors(rightWallDistance, leftWallDistance));
            finalCommandList.addAll(findPathTwoOrMoreSensors(rightWallDistance, leftWallDistance));
        }

        else if(FrontSensor != null && RightSensor == null && LeftSensor != null) //prednji i lijevi senzori su odabrani
        {
            double leftWallDistance = this.LeftSensor.getNumericValue();
            double rightWallDistance = R.integer.labyrinth_width - R.integer.mBot_width - R.integer.ultrasonic_sensor_width - leftWallDistance;

            finalCommandList.add(centerMBotTwoOrMoreSensors(rightWallDistance, leftWallDistance));
            finalCommandList.addAll(findPathTwoOrMoreSensors(rightWallDistance, leftWallDistance));
        }

        else if(FrontSensor != null && RightSensor == null && LeftSensor == null) //samo prednji senzor je odabran
        {
            finalCommandList.addAll(centerMBotFrontSensor());
            finalCommandList.addAll(findPathFrontSensor());
        }

        finalCommandList.add(LastCommand);
        return finalCommandList;
    }

    private ArrayList<CommandsToMBot> findPathTwoOrMoreSensors(double rightWallDistance, double leftWallDistance)
    {
        ArrayList<CommandsToMBot> commandsToMBotList = new ArrayList<>();

        double sensorDistanceSum = rightWallDistance + leftWallDistance;

        if(CrossroadManager.checkIfCrossroad(sensorDistanceSum))
            commandsToMBotList.addAll(CrossroadManager.manageCrossroad(rightWallDistance, leftWallDistance, this));

        else if(!FrontSensor.seesObstacle())
            commandsToMBotList.add(RunMotors);
        else
            commandsToMBotList.add(StopMotors);

        return commandsToMBotList;
    }

    private CommandsToMBot centerMBotTwoOrMoreSensors(double rightWallDistance, double leftWallDistance)
    {
        CommandsToMBot returnCommand = Null;

        double sensorDistanceSum = rightWallDistance + leftWallDistance;

        if(!CrossroadManager.checkIfCrossroad(sensorDistanceSum))
        {
            if(leftWallDistance > rightWallDistance)
                returnCommand = SpeedUpRight;


            else if(rightWallDistance > leftWallDistance)
                returnCommand = SpeedUpLeft;
        }

        return returnCommand;
    }


    private boolean rotated = false;

    private ArrayList<CommandsToMBot> findPathFrontSensor()
    {
        ArrayList<CommandsToMBot> commandsToMBotList = new ArrayList<>();

        if(lineFollower.isOnCrossroad() && !rotated)
        {
            commandsToMBotList.add(StopMotors);
            commandsToMBotList.add(RotateRight);
            rotated = true;
        }
        else if(!FrontSensor.seesObstacle())
        {
            commandsToMBotList.add(RunMotors);
            rotated = false;
        }
        else
        {
            commandsToMBotList.add(RotateFull);
        }

        return commandsToMBotList;
    }

    private ArrayList<CommandsToMBot> centerMBotFrontSensor()
    {
        ArrayList<CommandsToMBot> commandsToMBotList = new ArrayList<>();

        if(lineFollower.isOnLeftSide())
            commandsToMBotList.add(SpeedUpLeft);
        else if(lineFollower.isOnRightSide())
            commandsToMBotList.add(SpeedUpRight);

        return commandsToMBotList;
    }
}
