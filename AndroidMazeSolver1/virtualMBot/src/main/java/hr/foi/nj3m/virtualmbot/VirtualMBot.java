package hr.foi.nj3m.virtualmbot;

import javax.xml.parsers.FactoryConfigurationError;

import hr.foi.nj3m.interfaces.virtualCommunication.IMsgContainer;
import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.virtualMBot.IVirtualMsgSolverBot;


public class VirtualMBot implements IVirtualMsgSolverBot {
    public String recvdMessage = "";
    IMsgContainer vContainer = null;
    int[][] maze = null;

    private VirtualUltrasonicSensor FrontUltrasonic = null;
    private VirtualUltrasonicSensor RightUltrasonic = null;
    private VirtualUltrasonicSensor LeftUltrasonic = null;
    private Sides SmjerZadnjeStrane = null;

    private int trenutnoX = 0;
    private int trenutnoY = 0;


    /**
     * Konstruktor virtualnog mBota
     * @param vc spremnik za citanje i pisanje poruka
     * @param matrix matrica labirinta
     * @param startingXPosition pocetna x pozicija mBota u matrici
     * @param startingYPosition pocetna y pozicija mBota u matrici
     * @param smjerPrednjegSenzora pocetna orijentacija mBota
     */
    public VirtualMBot (IMsgContainer vc, int[][] matrix, int startingXPosition, int startingYPosition, Sides smjerPrednjegSenzora)
    {
        this.maze = matrix;

        FrontUltrasonic = new VirtualUltrasonicSensor(Sides.Null);
        RightUltrasonic = new VirtualUltrasonicSensor(Sides.Null);
        LeftUltrasonic = new VirtualUltrasonicSensor(Sides.Null);

        setSensorSides(smjerPrednjegSenzora);

        trenutnoX = startingXPosition;
        trenutnoY = startingYPosition;
        vContainer = vc;
    }

    /**
     * Izvrsava radnju ovisno o procitanoj poruci
     */
    public void izvrsiRadnju()
    {
        if(this.recvdMessage.equals("RL"))
            rotateLeft();
        else if(this.recvdMessage.equals("RR"))
            rotateRight();
        else if(this.recvdMessage.equals("FR"))
            fullRotate();
        else if(this.recvdMessage.equals("RM"))
            moveForward();
    }

    /**
     * @return true ukoliko je mBot pronasao izlaz
     */
    @Override
    public boolean isExit() {
        return exitFound;
    }

    /**
     * Preuzima poruku iz spremnika za citanje i pisanje
     */
    @Override
    public void receieveMsg() {
        this.recvdMessage = vContainer.getMessage();
    }


    public boolean exitFound = false;

    /**
     * Pise poruku o informacijama sa senzora u spremnik za citanje i pisanje
     */
    @Override
    public void sendSensorInfo() {
            String strToSend = "FS:" + FrontUltrasonic.measureSensor(trenutnoY, trenutnoX, maze);
            strToSend += "LS:" + LeftUltrasonic.measureSensor(trenutnoY, trenutnoX, maze);
            strToSend += "RS:" + RightUltrasonic.measureSensor(trenutnoY, trenutnoX, maze);

            vContainer.setMessage(strToSend);
    }

    /**
     * Mijenja strane, tj. smjerove na kojima se nalaze senzori, ovisno o ulaznom parametru, tj. prednjem senzpru
     * @param smjerPrednjegSenzora zeljeni smjer prednjeg senzora
     */
    private void setSensorSides(Sides smjerPrednjegSenzora)
    {
        FrontUltrasonic.setNewSensorSide(smjerPrednjegSenzora);

        switch (smjerPrednjegSenzora) {
            case Left:
                this.LeftUltrasonic.setNewSensorSide(Sides.Down);
                this.RightUltrasonic.setNewSensorSide(Sides.Up);
                this.SmjerZadnjeStrane = Sides.Right;
                break;
            case Right:
                this.LeftUltrasonic.setNewSensorSide(Sides.Up);
                this.RightUltrasonic.setNewSensorSide(Sides.Down);
                this.SmjerZadnjeStrane = Sides.Left;
                break;
            case Up:
                this.LeftUltrasonic.setNewSensorSide(Sides.Left);
                this.RightUltrasonic.setNewSensorSide(Sides.Right);
                this.SmjerZadnjeStrane = Sides.Down;
                break;
            case Down:
                this.LeftUltrasonic.setNewSensorSide(Sides.Right);
                this.RightUltrasonic.setNewSensorSide(Sides.Left);
                this.SmjerZadnjeStrane = Sides.Up;
                break;
        }
    }

    /**
     * Pokrece mBota - virtualna radnja kretanja po labirintu - matrici
     */
    @Override
    public void moveForward()
    {
        Sides frontSide = FrontUltrasonic.getSensorSide();
        maze[trenutnoY][trenutnoX] = 4;

        switch(frontSide)
        {
            case Up:
                trenutnoY--;
                break;
            case Down:
                trenutnoY++;
                break;
            case Left:
                trenutnoX--;
                break;
            case Right:
                trenutnoX++;
                break;
        }
        if(maze[trenutnoY][trenutnoX]==3)
            exitFound = true;

        maze[trenutnoY][trenutnoX] = 2;
    }

    /**
     * Rotira mBota desno u virtualnom labirintu - matrici
     */
    @Override
    public void rotateRight() {
        setSensorSides(RightUltrasonic.getSensorSide());
    }


    /**
     * Rotira mBota lijevo u virtualnom labirintu - matrici
     */
    @Override
    public void rotateLeft() {
        setSensorSides(LeftUltrasonic.getSensorSide());
    }

    /**
     * Rotira mBota za 180 stupnjeva u virtualnom labirintu - matrici
     */
    @Override
    public void fullRotate() {
        rotateRight();
        rotateRight();
    }
}
