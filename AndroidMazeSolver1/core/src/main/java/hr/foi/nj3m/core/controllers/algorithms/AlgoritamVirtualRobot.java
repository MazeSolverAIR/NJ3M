package hr.foi.nj3m.core.controllers.algorithms;

import hr.foi.nj3m.core.controllers.componentManagers.CrossroadManager;
import hr.foi.nj3m.core.controllers.componentManagers.MazeDrawer;
import hr.foi.nj3m.interfaces.Enumerations.Sides;

public class AlgoritamVirtualRobot {

    public String trenutniSenzorValue = "";
    private int lastSumSenzora = 0;
    private boolean rotatedLeft = false;
    private boolean rotatedRight = false;

    Sides prednjaStranae = null;
    Sides lijevaStrana = null;
    Sides desnaStrana = null;
    Sides zadnjaStrana = null;

    private MazeDrawer mazeDrawer = null;

    private int trenutniX = 0;
    private int trenutniY = 0;

    private Sides lastRotateSide = null;

    boolean inLoop = false;

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
            returnString = "FR";

        if(returnString.equals("RM"))
        {
            kreceSe();
            mazeDrawer.addpathOn(trenutniX, trenutniY);

            inLoop = mazeDrawer.checkIfLoop();
        }

        if(inLoop)
        {
            korakUnatrag();
            mazeDrawer.pathList.remove(mazeDrawer.pathList.size()-1);
            returnString = manageLoop(returnString, prednjiSenzor, desniSenzor, lijeviSenzor);
        }

        if(returnString.equals("RR"))
            postaviSmjerRobota(desnaStrana);
        else if(returnString.equals("RL"))
            postaviSmjerRobota(lijevaStrana);
        else if (returnString.equals("FR"))
        {
            postaviSmjerRobota(desnaStrana);
            postaviSmjerRobota(desnaStrana);
        }
        else if(returnString.equals("RM"))
        {
            kreceSe();
            mazeDrawer.addpathOn(trenutniX, trenutniY);
        }


        lastSumSenzora = sumBocnihSenzora;

        return returnString;
    }

    private String manageCrossroad(int prednjiSenzor, int desniSenzor, int lijeviSenzor)
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

    private void korakUnatrag()
    {
        switch(prednjaStranae)
        {
            case Up:
                this.trenutniY++;
                break;
            case Down:
                this.trenutniY--;
                break;
            case Left:
                this.trenutniX++;
                break;
            case Right:
                this.trenutniX--;
                break;
        }
    }

    private String manageLoop(String decidedComand, int prednjiSenzor, int desniSenzor, int lijeviSenzor)
    {
        if(decidedComand.equals("RR"))
        {
            if(checkCrossroadSide(desniSenzor))
                return "RL";
            else if(checkCrossroadSide(prednjiSenzor))
                return "RM";
        }
        else if(decidedComand.equals("RM"))
        {
            if(checkCrossroadSide(lijeviSenzor))
                return "RL";
            else if(checkCrossroadSide(prednjiSenzor))
                return "RM";
        }

        return "FR";
    }

}