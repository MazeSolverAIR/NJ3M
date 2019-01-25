package hr.foi.nj3m.core.controllers.components;

import hr.foi.nj3m.core.controllers.algorithms.MBotPathFinder;
import hr.foi.nj3m.core.controllers.componentManagers.CrossroadManager;
import hr.foi.nj3m.interfaces.Enumerations.Sides;

public class Crossroad {

    public int numberOfVisits = 0;
    public int maxNumberOfVisits = 0;
    public Sides lastTurnSide = null;

    public Crossroad(Sides sideToTurn)
    {
        this.lastTurnSide = sideToTurn;
    }

    public void setCrossroadSize()
    {

        if(CrossroadManager.checkCrossroadSide(MBotPathFinder.FrontSensor.getNumericValue()))
            this.maxNumberOfVisits++;
        if(CrossroadManager.checkCrossroadSide(MBotPathFinder.LeftSensor.getNumericValue()))
            this.maxNumberOfVisits++;
        if(CrossroadManager.checkCrossroadSide(MBotPathFinder.RightSensor.getNumericValue()))
            this.maxNumberOfVisits++;

        //zato jer se zadnji senzor ne moze provjeriti
        maxNumberOfVisits++;
    }

    public void newVisit()
    {
        numberOfVisits++;
    }
}
