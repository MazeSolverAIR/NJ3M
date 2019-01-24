package hr.foi.nj3m.virtualmbot;

import hr.foi.nj3m.communications.VirtualMsgContainer;
import hr.foi.nj3m.interfaces.Enumerations.Sides;


public class VirtualMBot implements IVirtualMBot, IVirtualMessenger
{
    public String recvdMessage = "";
    VirtualMsgContainer vContainer = null;
    int[][] maze = null;

    private VirtualUltrasonicSensor FrontUltrasonic = null;
    private VirtualUltrasonicSensor RightUltrasonic = null;
    private VirtualUltrasonicSensor LeftUltrasonic = null;
    private Sides SmjerZadnjeStrane = null;

    private int trenutnoX = 0;
    private int trenutnoY = 0;


    public VirtualMBot (VirtualMsgContainer vc, int startingXPosition, int startingYPosition, Sides smjerPrednjegSenzora)
    {
        this.maze = VirtualMaze.getMatrix();

        FrontUltrasonic = new VirtualUltrasonicSensor(Sides.Null);
        RightUltrasonic = new VirtualUltrasonicSensor(Sides.Null);
        LeftUltrasonic = new VirtualUltrasonicSensor(Sides.Null);

        setSensorSides(smjerPrednjegSenzora);

        trenutnoX = startingXPosition;
        trenutnoY = startingYPosition;
        vContainer = vc;
    }

    public void IzvrsiRadnju()
    {
        if(this.recvdMessage.equals("RL"))
            rotateLeft();
        else if(this.recvdMessage.equals("RR"))
            rotateRight();
        else if(this.recvdMessage.equals("FR"))
            fullRotate();
        else if(this.recvdMessage.equals("RM"))
            moveForward();
        else if(this.recvdMessage.equals("SM"))
            moveForward();
    }

    @Override
    public void receieveMsg() {
        this.recvdMessage = vContainer.getMessage();
    }

    @Override
    public void sendSensorInfo() {
        String strToSend = "FS:"+FrontUltrasonic.measureSensor(trenutnoY, trenutnoX, maze);
        strToSend+="LS:"+LeftUltrasonic.measureSensor(trenutnoY, trenutnoX, maze);
        strToSend+="RS:"+RightUltrasonic.measureSensor(trenutnoY, trenutnoX, maze);

        vContainer.setMessage(strToSend);
    }

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
                this.LeftUltrasonic.setNewSensorSide(Sides.Left);
                this.RightUltrasonic.setNewSensorSide(Sides.Right);
                this.SmjerZadnjeStrane = Sides.Up;
                break;
        }
    }


    @Override
    public void moveForward() {

        Sides frontSide = FrontUltrasonic.getSensorSide();
        maze[trenutnoY][trenutnoX] = 2;

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
    }

    @Override
    public void stopMotors(){
        Sides frontSide = FrontUltrasonic.getSensorSide();
        maze[trenutnoY][trenutnoX] = 2;

    }

    @Override
    public void rotateRight() {
        setSensorSides(RightUltrasonic.getSensorSide());
    }

    @Override
    public void rotateLeft() {
        setSensorSides(LeftUltrasonic.getSensorSide());
    }

    @Override
    public void fullRotate() {
        rotateRight();
        rotateRight();
    }
}
