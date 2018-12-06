package hr.foi.nj3m.core.controllers.algorithms;


import java.util.ArrayList;
import java.util.List;

import hr.foi.nj3m.core.controllers.components.Crossroad;
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
import static hr.foi.nj3m.interfaces.Enumerations.Sides.FullRotate;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.Left;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.Right;

public class MBotPathFinder {

    private List<IUltraSonic> Sensors = null;

    public IUltraSonic LeftSensor = null;
    public IUltraSonic RightSensor = null;
    public IUltraSonic FrontSensor = null;

    public List<Crossroad> CrossroadsList = null;

    private static MBotPathFinder Instance = null;

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

    public ArrayList<String> TestMethod()
    {
        ArrayList<String> commandsList = new ArrayList<>();

        commandsList.add("RotateLeft");
        commandsList.add("StopMotors");
        commandsList.add("RunMotors");
        commandsList.add("LastCommand");

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
    }

    public ArrayList<CommandsToMBot> FindPath()
    {
        ArrayList<CommandsToMBot> finalCommandList = new ArrayList<>();

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

    private ArrayList<CommandsToMBot> findPathThreeSensors()
    {
        ArrayList<CommandsToMBot> commandsToMBotList = new ArrayList<>();

        //zbroj ovih dužina sa širinom mBota bi trebala biti širina staze labirinta
        double sensorDistanceSum = LeftSensor.getNumericValue() + RightSensor.getNumericValue();

        if(!FrontSensor.seesObstacle())
            commandsToMBotList.add(RunMotors);
        else
            commandsToMBotList.add(StopMotors);

        if(Crossroad.checkIfCrossroad(sensorDistanceSum))
            commandsToMBotList.addAll(manageCrossroad(sensorDistanceSum));


        commandsToMBotList.add(LastCommand);
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

    private ArrayList<CommandsToMBot> manageCrossroad(double sensorDistanceSum)
    {
        ArrayList<CommandsToMBot> commandsToMBotList = new ArrayList<>();
        Crossroad crossroad = null;
        Sides stranaZaSkretanje = null;

        commandsToMBotList.add(StopMotors);

        //Provjeri stranu raskrizja
        if(Crossroad.checkCrossroadSide(this.RightSensor))
        {
            commandsToMBotList.add(RotateRight);
            stranaZaSkretanje = Right;
        }

        else if(Crossroad.checkCrossroadSide(this.FrontSensor))
        {
            commandsToMBotList.add(RunMotors);
            stranaZaSkretanje = Front;
        }

        else if(Crossroad.checkCrossroadSide(this.LeftSensor))
        {
            commandsToMBotList.add(RotateLeft);
            stranaZaSkretanje = Left;
        }

        else if(Crossroad.CheckIfDeadEnd(this, sensorDistanceSum))
        {
            commandsToMBotList.add(RotateFull);
            stranaZaSkretanje = FullRotate;
        }

        //ako je lista prazna ili ako zadnji element liste nije slijepa ulica
        crossroad = new Crossroad(this, stranaZaSkretanje);
        crossroad.setCrossroadSize();
        crossroad.newVisit();
        this.CrossroadsList.add(crossroad);

        return commandsToMBotList;
    }


    private ArrayList<CommandsToMBot> findPathFrontSensor()
    {
        ArrayList<CommandsToMBot> commandsToMBotList = new ArrayList<>();
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
