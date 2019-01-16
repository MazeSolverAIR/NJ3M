package hr.foi.nj3m.core.controllers.componentManagers;

import java.util.ArrayList;

import hr.foi.nj3m.core.R;
import hr.foi.nj3m.core.controllers.algorithms.MBotPathFinder;
import hr.foi.nj3m.core.controllers.components.Crossroad;
import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
import hr.foi.nj3m.interfaces.Enumerations.Sides;

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
        double distanceToCheck = sensorDistanceSum + MBotPathFinder.mBotWidth;

        return (distanceToCheck > (MBotPathFinder.labyrinthWidth + 2*MBotPathFinder.ultrasonicSensorhWidth));
    }

    private static boolean checkCrossroadSide(double sensorDistanceFromWall)
    {
        return (sensorDistanceFromWall > (MBotPathFinder.labyrinthWidth-MBotPathFinder.mBotWidth));
    }

    private static boolean CheckIfDeadEnd(double sensorDistanceSum)
    {
        if(!checkIfCrossroad(sensorDistanceSum) && MBotPathFinder.FrontSensor.seesObstacle())
            return true;

        return false;
    }

    public static ArrayList<CommandsToMBot> manageCrossroad(double rightWallDistance, double leftWallDistance)
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
        else if(CrossroadManager.checkCrossroadSide(MBotPathFinder.FrontSensor.getNumericValue()))
        {
            commandsToMBotList.add(RunMotors);
            sideToTurn = Front;
        }
        else if(CrossroadManager.checkCrossroadSide(leftWallDistance))
        {
            commandsToMBotList.add(RotateLeft);
            sideToTurn = Left;
        }
        else if(CrossroadManager.CheckIfDeadEnd(sideSensorsDistanceSum))
        {
            commandsToMBotList.add(RotateFull);
            sideToTurn = FullRotate;
        }
        manageCrossroadsList(sideToTurn);

        return commandsToMBotList;
    }


    //TODO: Možda ne bi radilo najbolje za 2 senzora, dok za 3 bi. Treba testirati
    private static void manageCrossroadsList(Sides sideToTurn)
    {
        Crossroad lastCRFromList = null;

        if(!MBotPathFinder.CrossroadsList.isEmpty())
            lastCRFromList = MBotPathFinder.CrossroadsList.get(MBotPathFinder.CrossroadsList.size() - 1);

        if(MBotPathFinder.CrossroadsList.isEmpty() || (lastCRFromList.numberOfVisits != lastCRFromList.maxNumberOfVisits))
        {
            Crossroad newCrossroad = new Crossroad(sideToTurn);
            newCrossroad.setCrossroadSize();
            newCrossroad.newVisit();
            MBotPathFinder.CrossroadsList.add(newCrossroad);
        }
        else
        {
            MBotPathFinder.CrossroadsList.remove(lastCRFromList);
            MBotPathFinder.CrossroadsList.get(MBotPathFinder.CrossroadsList.size() - 1).newVisit();
        }
    }

}
