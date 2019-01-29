package hr.foi.nj3m.core.controllers.componentManagers;

import hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm.AlgorithmVirtualRobot;
import hr.foi.nj3m.interfaces.Enumerations.Sides;

public class VirtualMBotController {

    public static void setMBotRotation(AlgorithmVirtualRobot alg, Sides smjerPrednjegSenzora)
    {
        alg.prednjaStranae = smjerPrednjegSenzora;

        switch (smjerPrednjegSenzora) {
            case Left:
                alg.lijevaStrana = Sides.Down;
                alg.desnaStrana = Sides.Up;
                alg.zadnjaStrana = Sides.Right;
                break;
            case Right:
                alg.lijevaStrana = Sides.Up;
                alg.desnaStrana = Sides.Down;
                alg.zadnjaStrana = Sides.Left;
                break;
            case Up:
                alg.lijevaStrana = Sides.Left;
                alg.desnaStrana = Sides.Right;
                alg.zadnjaStrana = Sides.Down;
                break;
            case Down:
                alg.lijevaStrana = Sides.Right;
                alg.desnaStrana = Sides.Left;
                alg.zadnjaStrana = Sides.Up;
                break;
        }
    }

    public static void move(AlgorithmVirtualRobot alg)
    {
        switch(alg.prednjaStranae)
        {
            case Up:
                alg.trenutniY--;
                break;
            case Down:
                alg.trenutniY++;
                break;
            case Left:
                alg.trenutniX--;
                break;
            case Right:
                alg.trenutniX++;
                break;
        }
    }

    public static boolean canMoveTo(int distance)
    {
        return distance > 1;
    }
}
