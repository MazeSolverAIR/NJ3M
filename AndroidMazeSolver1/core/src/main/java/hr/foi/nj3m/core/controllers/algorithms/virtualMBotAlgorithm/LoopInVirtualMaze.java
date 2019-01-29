package hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm;

import hr.foi.nj3m.core.controllers.components.VirtualCrossroad;

public class LoopInVirtualMaze {

    public static String manageLoop(String decidedComand, int prednjiSenzor, int desniSenzor, int lijeviSenzor)
    {
        if(decidedComand.equals("RR"))
        {
            if(VirtualCrossroad.checkCrossroadSide(lijeviSenzor))
                return "RL";
            else if(VirtualCrossroad.checkCrossroadSide(prednjiSenzor))
                return "RM";
        }
        else if(decidedComand.equals("RM"))
        {
            if(VirtualCrossroad.checkCrossroadSide(prednjiSenzor))
                return "RM";
            else if(VirtualCrossroad.checkCrossroadSide(lijeviSenzor))
                return "RL";
        }
        else if(decidedComand.equals("RL"))
        {
            if(AlgorithmVirtualRobot.canMoveTo(desniSenzor))
                return "RR";
            else if(VirtualCrossroad.checkCrossroadSide(lijeviSenzor))
                return "RL";
        }

        return "FR";
    }


}
