package hr.foi.nj3m.core.controllers.components;

import static hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm.AlgoritamVirtualRobot.canMoveTo;

public class VirtualCrossroad {


    public static boolean rotatedLeft = false;
    public static boolean rotatedRight = false;

    public static String manageCrossroad(int prednjiSenzor, int desniSenzor, int lijeviSenzor)
    {
        if(checkCrossroadSide(desniSenzor) && !rotatedRight && desniSenzor >= lijeviSenzor)
        {
            rotatedRight = true;
            return "RR";
        }
        else if(checkCrossroadSide(prednjiSenzor))
        {
            return "RM";
        }
        else if(checkCrossroadSide(lijeviSenzor) && !rotatedLeft)
        {
            rotatedLeft = true;
            return "RL";
        }
        else if(checkIfDeadEnd(desniSenzor + lijeviSenzor, prednjiSenzor))
        {
            return "FR";
        }

        return "";
    }

    public static boolean checkCrossroadSide(int distance)
    {
        return distance > 3;
    }

    private static boolean checkIfDeadEnd(int sumDist, int prednjiSensor)
    {
        return !checkIfCrossroad(sumDist) && !canMoveTo(prednjiSensor); //TODO: to mozda nije dobro
    }

    public static boolean checkIfCrossroad(int sumDist)
    {
        return sumDist > 4;
    }
}
