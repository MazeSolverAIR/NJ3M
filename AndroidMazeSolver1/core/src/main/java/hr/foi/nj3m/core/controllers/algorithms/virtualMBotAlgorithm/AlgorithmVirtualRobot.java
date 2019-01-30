package hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm;

import hr.foi.nj3m.core.controllers.componentManagers.MazeDrawer;
import hr.foi.nj3m.core.controllers.componentManagers.VirtualMBotController;
import hr.foi.nj3m.core.controllers.components.VirtualCrossroad;
import hr.foi.nj3m.interfaces.Enumerations.Sides;

import static hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm.LoopInVirtualMaze.manageLoop;
import static hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm.VirtuaSensorMsgController.setSensorValues;
import static hr.foi.nj3m.core.controllers.componentManagers.VirtualMBotController.setMBotRotation;
import static hr.foi.nj3m.core.controllers.components.VirtualCrossroad.manageCrossroad;

public class AlgorithmVirtualRobot {

    private int lastSensorSum = 0;

    public Sides prednjaStranae = null;
    public Sides lijevaStrana = null;
    public Sides desnaStrana = null;
    public Sides zadnjaStrana = null;
    int prednjiSenzor = 0;
    int lijeviSenzor = 0;
    int desniSenzor = 0;

    private MazeDrawer mazeDrawer = null;

    public int trenutniX = 0;
    public int trenutniY = 0;

    private boolean inLoop = false;

    /**
     * Kreira objekt tipa AlgorithmVirtualRobot
     */
    public AlgorithmVirtualRobot()
    {
        setMBotRotation(this, Sides.Left);
        this.mazeDrawer = new MazeDrawer();
        mazeDrawer.addPathOn(trenutniX,trenutniY);
    }

    /**
     * Metoda sluzi za pronalaza izlaza iz labirinta
     * @param message primljena poruka od mBota
     * @return naredba koja se salje mBotu
     */
    public String FindPath(String message)
    {
        String returnString = "";

        setSensorValues(this, message);
        int sumBocnihSenzora = lijeviSenzor + desniSenzor;

        if(VirtualCrossroad.checkIfCrossroad(sumBocnihSenzora) && sumBocnihSenzora!= lastSensorSum)
            returnString = manageCrossroad(prednjiSenzor, desniSenzor, lijeviSenzor);

        else if(VirtualMBotController.canMoveTo(prednjiSenzor))
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


    /**
     * Metoda koja, ovisno o tome što je algoritam FindPath odlucio poslati kao naredbu, izvršava radnje koje su potrebne za normalan rad algoritma
     * @param returnString naredba koju metoda FindPath vraca do trenutka nastupa ove metode
     */
    private void manageInternalAlgorithmWork(String returnString) {
        switch (returnString) {
            case "RR":
                setMBotRotation(this, desnaStrana);
                break;
            case "RL":
                setMBotRotation(this, lijevaStrana);
                break;
            case "FR":
                setMBotRotation(this, desnaStrana);
                setMBotRotation(this, desnaStrana);
                break;
            case "RM":
                VirtualMBotController.move(this);
                mazeDrawer.addPathOn(trenutniX, trenutniY);
                inLoop = mazeDrawer.checkIfLoop();
                break;
        }
    }

}