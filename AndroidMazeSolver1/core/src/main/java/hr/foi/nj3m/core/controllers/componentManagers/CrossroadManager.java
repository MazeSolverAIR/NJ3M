package hr.foi.nj3m.core.controllers.componentManagers;

import java.util.ArrayList;

import hr.foi.nj3m.core.R;
import hr.foi.nj3m.core.controllers.algorithms.MBotPathFinder;
import hr.foi.nj3m.core.controllers.components.Crossroad;
import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
import hr.foi.nj3m.interfaces.Enumerations.Sides;

import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.Null;
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

    /**
     * Provjerava nalazi li se mBot na krizanju
     * @param sensorDistanceSum suma vrijednosti s bocnih senzora
     * @return true ukoliko se mBota nalazi na krizanju
     */
    public static boolean checkIfCrossroad(double sensorDistanceSum)
    {
        double distanceToCheck = sensorDistanceSum + MBotPathFinder.mBotWidth;

        return (distanceToCheck > (MBotPathFinder.labyrinthWidth + 2*MBotPathFinder.ultrasonicSensorhWidth));
    }

    /**
     * Provjerava moze li mBot skrenuti na stranu unesene udaljenosti
     * @param sensorDistanceFromWall udaljenost do zida
     * @return true ukoliko moze skrenuti na tu stranu
     */
    public static boolean checkCrossroadSide(double sensorDistanceFromWall)
    {
        return (sensorDistanceFromWall > (MBotPathFinder.labyrinthWidth-MBotPathFinder.mBotWidth));
    }

    /**
     * Testira nalazi li se mBot u slijepoj ulici
     * @param sensorDistanceSum suma vrijednosti bocnih senzora
     * @return true ukoliko se nalazi u slijepoj ulici
     */
    private static boolean CheckIfDeadEnd(double sensorDistanceSum)
    {
        if(!checkIfCrossroad(sensorDistanceSum) && MBotPathFinder.FrontSensor.seesObstacle())
            return true;

        return false;
    }

    static double lastSensorsDistance = 0;

    /**
     * Odlucuje o naredbi koju je potrebno izvrsiti na krizanju
     * @param rightWallDistance udaljenost desnog senzora od zida
     * @param leftWallDistance udaljenost lijevog senzora od zida
     * @return naredba za mBota
     */
    public static CommandsToMBot manageCrossroad(double rightWallDistance, double leftWallDistance)
    {
        CommandsToMBot commandsToMBotList = Null;
        Sides sideToTurn = null;
        double sideSensorsDistanceSum = rightWallDistance + leftWallDistance;

        if(CrossroadManager.checkCrossroadSide(rightWallDistance) && checkIfCanTurn(sideSensorsDistanceSum))
        {
            commandsToMBotList = RotateRight;
            sideToTurn = Right;
        }
        else if(CrossroadManager.checkCrossroadSide(MBotPathFinder.FrontSensor.getNumericValue()) && checkIfCanTurn(sideSensorsDistanceSum))
        {
            commandsToMBotList = RunMotors;
            sideToTurn = Front;
        }
        else if(CrossroadManager.checkCrossroadSide(leftWallDistance) && checkIfCanTurn(sideSensorsDistanceSum))
        {
            commandsToMBotList = RotateLeft;
            sideToTurn = Left;
        }
        else if(CrossroadManager.CheckIfDeadEnd(sideSensorsDistanceSum))
        {
            commandsToMBotList = RotateFull;
            sideToTurn = FullRotate;
        }
        manageCrossroadsList(sideToTurn);

        lastSensorsDistance = sideSensorsDistanceSum;

        return commandsToMBotList;
    }


    /**
     * Upravlja listom krizanja
     * @param sideToTurn strana na koju ce mBot skrenuti
     */
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

    /**
     * Provjerava moze li mBot ponovno skrenuti
     * @param currentSensorDIstance suma bocnih senzora
     * @return true ukoliko je ova suma razliciza od prosle sume
     */
    private static boolean checkIfCanTurn(double currentSensorDIstance)
    {
        return Math.abs(lastSensorsDistance - currentSensorDIstance) > 20;

    }

}
