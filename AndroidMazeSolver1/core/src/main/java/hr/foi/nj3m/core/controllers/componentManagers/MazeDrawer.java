package hr.foi.nj3m.core.controllers.componentManagers;

import java.util.ArrayList;
import java.util.List;

import hr.foi.nj3m.core.controllers.components.Path;

public class MazeDrawer {

    public List<Path> pathList;
    private int lastX = 0;
    private int lastY = 0;

    public MazeDrawer() {
        this.pathList = new ArrayList<>();
    }

    public void addpathOn(int x, int y)
    {
        lastX = x;
        lastY = y;
        pathList.add(new Path(x, y));
    }

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
