package hr.foi.nj3m.core.controllers.componentManagers;

import java.util.ArrayList;
import java.util.List;

import hr.foi.nj3m.core.controllers.components.Path;

public class MazeDrawer {

    public List<Path> pathList;
    private int lastX = 0;
    private int lastY = 0;


    /**
     * Konstruktor koji kreira objekt ove klase
     */
    public MazeDrawer() {
        this.pathList = new ArrayList<>();
    }


    /**
     * Dodaje x i y koordinate u listu i pamti zadnje vrijednosti
     * @param x trenutna x koordinata mBota
     * @param y trenutna y koordinata mBota
     */
    public void addPathOn(int x, int y)
    {
        lastX = x;
        lastY = y;
        pathList.add(new Path(x, y));
    }


    /**
     * Metoda koja provjerava nalazi li se mBot u petlji ili ne
     * @return vraca true ukoliko se mBot nalazi u petlji, inace false
     */
    public boolean checkIfLoop()
    {
        if(this.pathList.size()>1)
        {
            for(int i=0;i<pathList.size()-1;i++)
            {
                if(pathList.get(i).X==lastX && pathList.get(i).Y==lastY)
                    return true;
            }
        }

        return false;
    }
}
