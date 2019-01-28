package hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm;

import hr.foi.nj3m.core.controllers.componentManagers.MazeDrawer;
import hr.foi.nj3m.core.controllers.components.VirtualCrossroad;
import hr.foi.nj3m.interfaces.Enumerations.Sides;

import static hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm.LoopInVirtualMaze.manageLoop;
import static hr.foi.nj3m.core.controllers.components.VirtualCrossroad.manageCrossroad;

public class AlgoritamVirtualRobot {

    private int lastSensorSum = 0;

    private Sides prednjaStranae = null;
    private Sides lijevaStrana = null;
    private Sides desnaStrana = null;
    private Sides zadnjaStrana = null;
    private int prednjiSenzor = 0;
    private int lijeviSenzor = 0;
    private int desniSenzor = 0;

    private MazeDrawer mazeDrawer = null;

    private int trenutniX = 0;
    private int trenutniY = 0;

    private boolean inLoop = false;

    public AlgoritamVirtualRobot()
    {
        postaviSmjerRobota(Sides.Left);
        this.mazeDrawer = new MazeDrawer();
        mazeDrawer.addpathOn(trenutniX,trenutniY);
    }

    public String FindPath(String message)
    {
        String returnString = "";

        setSensorValues(message);

        int sumBocnihSenzora = lijeviSenzor + desniSenzor;

        if(VirtualCrossroad.checkIfCrossroad(sumBocnihSenzora) && sumBocnihSenzora!= lastSensorSum)
            returnString = manageCrossroad(prednjiSenzor, desniSenzor, lijeviSenzor);
        else if(canMoveTo(prednjiSenzor))
        {
            returnString = "RM";
            VirtualCrossroad.rotatedLeft = false;
            VirtualCrossroad.rotatedRight = false;
        }
        else
            returnString = "FR";

        if(inLoop)
            returnString = manageLoop(returnString, prednjiSenzor, desniSenzor, lijeviSenzor);

        manageInternalAlgorithmWork(returnString);

        lastSensorSum = sumBocnihSenzora;

        return returnString;
    }


    private void manageInternalAlgorithmWork(String returnString) {
        switch (returnString) {
            case "RR":
                postaviSmjerRobota(desnaStrana);
                break;
            case "RL":
                postaviSmjerRobota(lijevaStrana);
                break;
            case "FR":
                postaviSmjerRobota(desnaStrana);
                postaviSmjerRobota(desnaStrana);
                break;
            case "RM":
                kreceSe();
                mazeDrawer.addpathOn(trenutniX, trenutniY);
                inLoop = mazeDrawer.checkIfLoop();
                break;
        }
    }

    private void setSensorValues(String message) {

        String pomocna = message.substring(message.indexOf(":")+1);
        this.prednjiSenzor = Integer.parseInt(message.substring(message.indexOf(":")+1,message.lastIndexOf("L")));
        this.lijeviSenzor = Integer.parseInt(pomocna.substring(pomocna.indexOf(":")+1,pomocna.lastIndexOf("R")));
        this.desniSenzor = Integer.parseInt(pomocna.substring(pomocna.lastIndexOf(":")+1));

    }

    private void postaviSmjerRobota(Sides smjerPrednjegSenzora)
    {
        prednjaStranae = smjerPrednjegSenzora;

        switch (smjerPrednjegSenzora) {
            case Left:
                this.lijevaStrana = Sides.Down;
                this.desnaStrana = Sides.Up;
                this.zadnjaStrana = Sides.Right;
                break;
            case Right:
                this.lijevaStrana = Sides.Up;
                this.desnaStrana = Sides.Down;
                this.zadnjaStrana = Sides.Left;
                break;
            case Up:
                this.lijevaStrana = Sides.Left;
                this.desnaStrana = Sides.Right;
                this.zadnjaStrana = Sides.Down;
                break;
            case Down:
                this.lijevaStrana = Sides.Right;
                this.desnaStrana = Sides.Left;
                this.zadnjaStrana = Sides.Up;
                break;
        }
    }

    private void kreceSe()
    {
        switch(prednjaStranae)
        {
            case Up:
                this.trenutniY--;
                break;
            case Down:
                this.trenutniY++;
                break;
            case Left:
                this.trenutniX--;
                break;
            case Right:
                this.trenutniX++;
                break;
        }

    }

    public static boolean canMoveTo(int distance)
    {
        return distance > 1;
    }
}