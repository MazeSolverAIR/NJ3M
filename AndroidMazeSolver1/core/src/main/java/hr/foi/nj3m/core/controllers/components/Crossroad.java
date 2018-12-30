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

    public Crossroad(MBotPathFinder finder, Sides sideToTurn)
    {
        this.lastTurnSide = sideToTurn;
        this.Finder = finder;
    }

    public void setCrossroadSize()
    {
        if(!Finder.FrontSensor.seesObstacle())
            this.maxNumberOfVisits++;
        if(!Finder.LeftSensor.seesObstacle())
            this.maxNumberOfVisits++;
        if(!Finder.RightSensor.seesObstacle())
            this.maxNumberOfVisits++;

        //zato jer se zadnji senzor ne moze provjeriti
        maxNumberOfVisits++;
    }

    public void newVisit()
    {
        numberOfVisits++;
    }
}
