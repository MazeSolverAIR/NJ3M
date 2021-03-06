package hr.foi.nj3m.core.controllers.algorithms;


import java.util.ArrayList;
import java.util.List;

import hr.foi.nj3m.core.controllers.componentManagers.CrossroadManager;
import hr.foi.nj3m.core.controllers.components.Crossroad;
import hr.foi.nj3m.core.controllers.components.LineFollower;
import hr.foi.nj3m.core.controllers.components.UltrasonicSensor;
import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.sensors.IUltraSonic;

import static hr.foi.nj3m.core.controllers.algorithms.MBotInfoCutter.getSensorValue;
import static hr.foi.nj3m.core.controllers.algorithms.MBotInfoCutter.getSubstringedMessage;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.LastCommand;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.Null;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RotateFull;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RotateLeft;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RotateRight;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RunMotors;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.SpeedUpLeft;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.SpeedUpRight;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.StopMotors;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.Front;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.Left;
import static hr.foi.nj3m.interfaces.Enumerations.Sides.Right;

public class MBotPathFinder {

    public static final double labyrinthWidth = 55.0;
    public static final double mBotWidth = 13.0;
    public static final double ultrasonicSensorhWidth = 1.8;

    private List<IUltraSonic> Sensors = null;

    public static IUltraSonic LeftSensor = null;
    public static IUltraSonic RightSensor = null;
    public static IUltraSonic FrontSensor = null;

    public static List<Crossroad> CrossroadsList = null;

    private static MBotPathFinder Instance = null;

    private static LineFollower lineFollower = null;

    /**
     * Kreira instancu klase
     * @param sensors lista senzora dobivena iz fragmenta koji sluzi za odabir koristenih senzora
     */
    public static MBotPathFinder createInstance(List<IUltraSonic> sensors)
    {
        Instance = new MBotPathFinder(sensors);

        return Instance;
    }

    /**
     * Kreira instancu MBotPathFinder klase
     * @param i ako 1, kreiraju se tri UZ senzora. Ako 2, kreira se prenji UZ senzor i citac crte
     * @return instancirani objekt
     */
    public static MBotPathFinder createInstance(int i)
    {
        switch(i)
        {
            case 1:
                LeftSensor = new UltrasonicSensor(Left);
                FrontSensor = new UltrasonicSensor(Front);
                RightSensor = new UltrasonicSensor(Right);
                break;
            case 2:
                FrontSensor = new UltrasonicSensor(Front);
                lineFollower = new LineFollower();
                break;
        }
        Instance = new MBotPathFinder();


        CrossroadsList = new ArrayList<>();

        return Instance;
    }
    private MBotPathFinder()
    {
    }



    private MBotPathFinder(List<IUltraSonic> sensors)
    {
        this.Sensors = sensors;

        setSensorVariables();
    }


    /**
     * Postavlja strane za senzore dobivene iz liste
     */
    private void setSensorVariables() {
        for (IUltraSonic sensor : Sensors) {
            Sides sensorSide = sensor.getSensorSide();
            if (sensorSide == Left)
                LeftSensor = sensor;
            else if (sensorSide == Right)
                RightSensor = sensor;
            else if (sensorSide == Front)
                FrontSensor = sensor;
        }
        lineFollower = new LineFollower();
    }

    private static String currentRcvMessage = "";


    /**
     * Odlucuje koju naredbu sljedece poslati mBotu
     * @param currentMsg dobivena poruka od mBota
     * @return naredba koja se salje mBotu
     */
    public CommandsToMBot FindPath(String currentMsg)
    {
        currentRcvMessage = currentMsg;
        CommandsToMBot finalCmd = Null;

        if(FrontSensor != null && RightSensor != null && LeftSensor != null) //sva tri senzora su odabrana
        {
            double rightWallDistance = RightSensor.getNumericValue();
            double leftWallDistance = LeftSensor.getNumericValue();

            //finalCommandList.add(centerMBotTwoOrMoreSensors(rightWallDistance, leftWallDistance));
            finalCmd = findPathTwoOrMoreSensors(rightWallDistance, leftWallDistance);
        }

        else if(FrontSensor != null && RightSensor != null && LeftSensor == null) //prednji i desni seenzori su odabrani
        {
            double rightWallDistance = this.RightSensor.getNumericValue();
            double leftWallDistance = setOtherSideWallDistance(rightWallDistance);

            //finalCommandList.add(centerMBotTwoOrMoreSensors(rightWallDistance, leftWallDistance));
            finalCmd = findPathTwoOrMoreSensors(rightWallDistance, leftWallDistance);
        }

        else if(FrontSensor != null && RightSensor == null && LeftSensor != null) //prednji i lijevi senzori su odabrani
        {
            double leftWallDistance = this.LeftSensor.getNumericValue();
            double rightWallDistance = setOtherSideWallDistance(leftWallDistance);

            //finalCommandList.add(centerMBotTwoOrMoreSensors(rightWallDistance, leftWallDistance));
            finalCmd = findPathTwoOrMoreSensors(rightWallDistance, leftWallDistance);
        }

        else if(FrontSensor != null && RightSensor == null && LeftSensor == null) //samo prednji senzor je odabran
        {
            finalCmd = findPathFrontSensor();
            /*if(!firstCommandDone)
                finalCmd = centerMBotFrontSensor();
            else
            {*/
                //firstCommandDone = false;
            //}
        }

        return finalCmd;
    }


    /**
     * Izracunava udaljenost do drugog zida, ako je poznata udaljenost do jednog
     * @param realDistance udaljenost do jednog zida
     * @return udaljenost do drugog zida
     */
    private double setOtherSideWallDistance(double realDistance)
    {
        double returnVal = 0;
        returnVal = labyrinthWidth - mBotWidth - ultrasonicSensorhWidth;

        if(realDistance <= 38)
            return (returnVal - realDistance);

        else
            return returnVal;
    }


    /**
     * Pronalazi izlaz iz labirinta s 2 ili 3 UZ senzora
     * @param rightWallDistance udaljenost do desnog zida
     * @param leftWallDistance udaljenost do lijevog zida
     * @return naredba koja se mora poslati mBotu
     */
    private CommandsToMBot findPathTwoOrMoreSensors(double rightWallDistance, double leftWallDistance)
    {
        CommandsToMBot commandsToMBotList = Null;

        double sensorDistanceSum = rightWallDistance + leftWallDistance;

        if(CrossroadManager.checkIfCrossroad(sensorDistanceSum))
            commandsToMBotList = CrossroadManager.manageCrossroad(rightWallDistance, leftWallDistance);

        else if(!FrontSensor.seesObstacle())
            commandsToMBotList = RunMotors;
        else
            commandsToMBotList = StopMotors;

        return commandsToMBotList;
    }

    /**
     * Crntrira mBota na sredinu staze labirinta
     * @param rightWallDistance udaljenost do desnog zida
     * @param leftWallDistance udaljenost do lijevog zida
     * @return naredba koja se salje mBotu
     */
    private CommandsToMBot centerMBotTwoOrMoreSensors(double rightWallDistance, double leftWallDistance)
    {
        CommandsToMBot returnCommand = Null;

        double sensorDistanceSum = rightWallDistance + leftWallDistance;

        if(!CrossroadManager.checkIfCrossroad(sensorDistanceSum))
        {
            if(leftWallDistance > rightWallDistance)
                returnCommand = SpeedUpRight;


            else if(rightWallDistance > leftWallDistance)
                returnCommand = SpeedUpLeft;
        }

        return returnCommand;
    }


    private static int numberOfRotatesInARow = 1;
    private static boolean mBotRotated = false;

    /**
     * Pronalazi izlaz iz labirinta uz pomoc prednjeg senzora
     * @return naredba koja se salje mBotu
     */
    private CommandsToMBot findPathFrontSensor()
    {
        CommandsToMBot returnCmd = Null;
        String message = getSubstringedMessage(currentRcvMessage);
        Double frontSensorDist = getSensorValue(message);

        if (message.contains("OK"))
            mBotRotated = true;   //sluzi da znamo kada se je mBot okrenuo

        if (frontSensorDist <= 22)
        {
            returnCmd = decideAboutWall();
            mBotRotated = false;
        }
        else if (frontSensorDist > 22)
        {
            numberOfRotatesInARow = 1;
            returnCmd = RunMotors;
            mBotRotated = false;
        }

        return returnCmd;
    }

    /**
     * Centrira mBota u stazu labirinta na temelju podataka od citaca crte
     * @return naredba koja se salje mBotu
     */
    private CommandsToMBot centerMBotFrontSensor()
    {
        CommandsToMBot returnCmd = StopMotors;

        if(lineFollower.leftSideOut())
            returnCmd = SpeedUpLeft;
        else if(lineFollower.rightSideOut())
            returnCmd = SpeedUpRight;
        else
            returnCmd = RunMotors;

        return returnCmd;
    }

    /**
     * Odlucuje kako se mBot treba rotirati - jedan senzor
     * @return naredba koja se salje mBotu
     */
    private CommandsToMBot decideAboutWall()
    {
        CommandsToMBot returnCmd = Null;
        if (mBotRotated)
            numberOfRotatesInARow++;

        if (numberOfRotatesInARow == 2)
            returnCmd = RotateFull;

        else
            returnCmd = RotateLeft;

        return returnCmd;
    }
}
