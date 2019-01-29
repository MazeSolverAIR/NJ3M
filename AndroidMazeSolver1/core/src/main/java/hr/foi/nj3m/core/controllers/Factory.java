package hr.foi.nj3m.core.controllers;

import java.io.File;

import hr.foi.nj3m.core.controllers.components.UltrasonicSensor;
import hr.foi.nj3m.interfaces.sensors.IUltraSonic;
import hr.foi.nj3m.interfaces.virtualCommunication.IMsgContainer;
import hr.foi.nj3m.communications.VirtualMsgContainer;
import hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm.AlgorithmVirtualRobot;
import hr.foi.nj3m.core.controllers.threads.SendReceive;
import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.virtualMBot.IVirtualMaze;
import hr.foi.nj3m.interfaces.virtualMBot.IVirtualMsgSolverBot;
import hr.foi.nj3m.virtualmbot.VirtualMBot;
import hr.foi.nj3m.virtualmbot.VirtualMaze;

public class Factory {

    public static SendReceive createSendRecieve(android.os.Handler handler)
    {
        return new SendReceive(handler);
    }

    public static File CreateFile(String myfolder)
    {
        return new File(myfolder);
    }

    public static AlgorithmVirtualRobot CreateAlgForVRobot()
    {
        return new AlgorithmVirtualRobot();
    }

    public static IVirtualMsgSolverBot CreateVirtualMbot(IMsgContainer container, int[][] matrix, int x, int y, Sides side)
    {
        return new VirtualMBot(container, matrix, x, y, side);
    }

    public static IMsgContainer CreateVirtualContainer ()
    {
        return new VirtualMsgContainer();
    }

    public static IVirtualMaze CreateMaze()
    {
        return new VirtualMaze();
    }

    public static IUltraSonic CreateUltrasonicSensor(Sides side)
    {
        return new UltrasonicSensor(side);
    }

}
