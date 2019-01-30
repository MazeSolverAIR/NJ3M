package hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm;

import hr.foi.nj3m.core.controllers.componentManagers.VirtualMBotController;
import hr.foi.nj3m.core.controllers.components.VirtualCrossroad;

public class LoopInVirtualMaze {

    private static int twiceInRow = 0;

    /**
     * @param decidedComand naredba o smjeru kretanja
     * @param prednjiSenzor udaljenost prednjeg senzora od sida
     * @param desniSenzor udaljenost desnog senzora od sida
     * @param lijeviSenzor udaljenost lijevog senzora od sida
     * @return naredba koja izvaci mBota iz petlje
     */
    public static String manageLoop(String decidedComand, int prednjiSenzor, int desniSenzor, int lijeviSenzor)
    {
        if(decidedComand.equals("RR"))
        {
            if(VirtualCrossroad.checkCrossroadSide(lijeviSenzor))
                return "RL";
            else if(VirtualCrossroad.checkCrossroadSide(prednjiSenzor))
            {
                twiceInRow++;
                if(twiceInRow==2)
                {
                    twiceInRow = 0;
                    return "RR";
                }
                return "RM";
            }
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
            if(VirtualMBotController.canMoveTo(desniSenzor))
                return "RR";
            else if(VirtualCrossroad.checkCrossroadSide(lijeviSenzor))
                return "RL";
        }

        return "FR";
    }


}
