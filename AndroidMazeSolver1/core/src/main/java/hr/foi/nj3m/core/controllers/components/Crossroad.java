package hr.foi.nj3m.core.controllers.components;

import java.util.List;

import hr.foi.nj3m.core.R;
import hr.foi.nj3m.core.controllers.algorithms.MBotPathFinder;
import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.IUltraSonic;

public class Crossroad {

    public int numberOfVisits = 0;
    public int maxNumberOfVisits = 0;
    public Sides lastTurnSide = null;

    private MBotPathFinder Finder = null;

    public Crossroad(MBotPathFinder finder)
    {
        this.Finder = finder;
    }

    public static boolean checkIfCrossroad(double sensorDistanceSum)
    {
        double distanceToCheck = sensorDistanceSum + R.integer.mBot_width;

        return (distanceToCheck <= (R.integer.labyrinth_width + 2*R.integer.ultrasonic_sensor_width)) &&
                (distanceToCheck >= (R.integer.labyrinth_width - 2*R.integer.ultrasonic_sensor_width));
    }

    public static boolean checkCrossroadSide(IUltraSonic sensor)
    {
        return sensor.getNumericValue() > (R.integer.labyrinth_width-R.integer.mBot_width);
    }

    public void setCrossroadSize()
    {
        if(!Finder.FrontSensor.seesObstacle())
            this.maxNumberOfVisits++;
        if(!Finder.LeftSensor.seesObstacle())
            this.maxNumberOfVisits++;
        if(!Finder.RightSensor.seesObstacle())
            this.maxNumberOfVisits++;

        this.numberOfVisits++;
    }

    public void newVisit()
    {
        numberOfVisits++;
    }
}
