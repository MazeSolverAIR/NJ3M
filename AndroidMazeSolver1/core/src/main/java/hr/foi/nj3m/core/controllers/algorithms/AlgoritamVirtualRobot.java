package hr.foi.nj3m.core.controllers.algorithms;

import hr.foi.nj3m.core.controllers.componentManagers.MazeDrawer;
import hr.foi.nj3m.core.controllers.components.VirtualCrossroad;
import hr.foi.nj3m.interfaces.Enumerations.Sides;

import static hr.foi.nj3m.core.controllers.algorithms.LoopInVirtualMaze.canMoveTo;
import static hr.foi.nj3m.core.controllers.algorithms.LoopInVirtualMaze.manageLoop;
import static hr.foi.nj3m.core.controllers.components.VirtualCrossroad.manageCrossroad;

public class AlgoritamVirtualRobot {

    private int lastSumSenzora = 0;

    private Sides prednjaStranae = null;
    private Sides lijevaStrana = null;
    private Sides desnaStrana = null;
    private Sides zadnjaStrana = null;

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

        String pomocna = message.substring(message.indexOf(":")+1);
        int prednjiSenzor = Integer.parseInt(message.substring(message.indexOf(":")+1,message.lastIndexOf("L")));
        int lijeviSenzor = Integer.parseInt(pomocna.substring(pomocna.indexOf(":")+1,pomocna.lastIndexOf("R")));
        int desniSenzor = Integer.parseInt(pomocna.substring(pomocna.lastIndexOf(":")+1));

        int sumBocnihSenzora = lijeviSenzor + desniSenzor;

        if(VirtualCrossroad.checkIfCrossroad(sumBocnihSenzora) && sumBocnihSenzora!=lastSumSenzora)
        {
            returnString = manageCrossroad(prednjiSenzor, desniSenzor, lijeviSenzor);
        }
        else if(canMoveTo(prednjiSenzor))
        {
            returnString = "RM";
            VirtualCrossroad.rotatedLeft = false;
            VirtualCrossroad.rotatedRight = false;
        }
        else
            returnString = "FR";


        if(inLoop)
        {
            returnString = manageLoop(returnString, prednjiSenzor, desniSenzor, lijeviSenzor);
        }

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

        lastSumSenzora = sumBocnihSenzora;

        return returnString;
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
}