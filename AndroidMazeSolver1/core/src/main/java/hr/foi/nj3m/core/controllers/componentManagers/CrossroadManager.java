package hr.foi.nj3m.core.controllers.componentManagers;

import java.util.ArrayList;

import hr.foi.nj3m.core.R;
import hr.foi.nj3m.core.controllers.algorithms.MBotPathFinder;
import hr.foi.nj3m.core.controllers.components.Crossroad;
import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.IUltraSonic;

import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RotateFull;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RotateLeft;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RotateRight;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RunMotors;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.StopMotors;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.Front;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.FullRotate;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.Left;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.Right;

public class CrossroadManager {

    public static boolean checkIfCrossroad(double sensorDistanceSum)
    {
        double distanceToCheck = sensorDistanceSum + R.integer.mBot_width;

        return (distanceToCheck > (R.integer.labyrinth_width + 2*R.integer.ultrasonic_sensor_width));
    }

    private static boolean checkCrossroadSide(double sensorDistanceFromWall)
    {
        return sensorDistanceFromWall > (R.integer.labyrinth_width-R.integer.mBot_width);
    }

    private static boolean CheckIfDeadEnd(MBotPathFinder finder, double sensorDistanceSum)
    {
        if(!checkIfCrossroad(sensorDistanceSum) && finder.FrontSensor.seesObstacle())
            return true;

        return false;
    }

    public static ArrayList<CommandsToMBot> manageCrossroad(double rightWallDistance, double leftWallDistance, MBotPathFinder pFinder)
    {
        ArrayList<CommandsToMBot> commandsToMBotList = new ArrayList<>();
        Sides sideToTurn = null;
        double sideSensorsDistanceSum = rightWallDistance + leftWallDistance;

        commandsToMBotList.add(StopMotors);

        if(CrossroadManager.checkCrossroadSide(rightWallDistance))
        {
            commandsToMBotList.add(RotateRight);
            sideToTurn = Right;
        }
        else if(CrossroadManager.checkCrossroadSide(pFinder.FrontSensor.getNumericValue()))
        {
            commandsToMBotList.add(RunMotors);
            sideToTurn = Front;
        }
        else if(CrossroadManager.checkCrossroadSide(leftWallDistance))
        {
            commandsToMBotList.add(RotateLeft);
            sideToTurn = Left;
        }
        else if(CrossroadManager.CheckIfDeadEnd(pFinder, sideSensorsDistanceSum))
        {
            commandsToMBotList.add(RotateFull);
            sideToTurn = FullRotate;
        }
        manageCrossroadsList(sideToTurn, pFinder);

        return commandsToMBotList;
    }


    //TODO: Možda ne bi radilo najbolje za 2 senzora, dok za 3 bi. Treba testirati
    private static void manageCrossroadsList(Sides sideToTurn, MBotPathFinder pFinder)
    {
        Crossroad lastCRFromList = null;

        if(!pFinder.CrossroadsList.isEmpty())
            lastCRFromList = pFinder.CrossroadsList.get(pFinder.CrossroadsList.size() - 1);

        if(pFinder.CrossroadsList.isEmpty() || (lastCRFromList.numberOfVisits != lastCRFromList.maxNumberOfVisits))
        {
            Crossroad newCrossroad = new Crossroad(pFinder, sideToTurn);
            newCrossroad.setCrossroadSize();
            newCrossroad.newVisit();
            pFinder.CrossroadsList.add(newCrossroad);
        }
        else
        {
            pFinder.CrossroadsList.remove(lastCRFromList);
            pFinder.CrossroadsList.get(pFinder.CrossroadsList.size() - 1).newVisit();
        }
    }

}
