package hr.foi.nj3m.core.controllers.algorithms;

import hr.foi.nj3m.core.controllers.componentManagers.CrossroadManager;

public class AlgoritamVirtualRobot {

    public String trenutniSenzorValue = "";
    private int lastSumSenzora = 0;
    private boolean rotatedLeft = false;
    private boolean rotatedRight = false;

    public String FindPath(String message)
    {
        String returnString = "";

        String pomocna = message.substring(message.indexOf(":")+1);
        int prednjiSenzor = Integer.parseInt(message.substring(message.indexOf(":")+1,message.lastIndexOf("L")));
        int lijeviSenzor = Integer.parseInt(pomocna.substring(pomocna.indexOf(":")+1,pomocna.lastIndexOf("R")));
        int desniSenzor = Integer.parseInt(pomocna.substring(pomocna.lastIndexOf(":")+1));

        int sumBocnihSenzora = lijeviSenzor + desniSenzor;

        if(checkIfCrossroad(sumBocnihSenzora) && sumBocnihSenzora!=lastSumSenzora)
        {
            returnString = manageCrossroad(prednjiSenzor, desniSenzor, lijeviSenzor);
        }
        else if(canMoveTo(prednjiSenzor))
        {
            returnString = "RM";
            rotatedLeft = false;
            rotatedRight = false;
        }
        else
        {
            returnString = "FR";
        }


        lastSumSenzora = sumBocnihSenzora;

        return returnString;
    }

    private String manageCrossroad(int prednjiSenzor, int desniSenzor, int lijeviSenzor)
    {
        if(checkCrossroadSide(desniSenzor) && !rotatedRight && desniSenzor > lijeviSenzor)
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

    private boolean canMoveTo(int distance)
    {
       return distance > 1;
    }

    private boolean checkCrossroadSide(int distance)
    {
        return distance > 3;
    }

    private boolean checkIfCrossroad(int sumDist)
    {
        return sumDist > 4;
    }

    private boolean checkIfDeadEnd(int sumDist, int prednjiSensor)
    {
        return !checkIfCrossroad(sumDist) && !canMoveTo(prednjiSensor);
    }


}