package hr.foi.nj3m.core.controllers.algorithms;

public class AlgoritamVirtualRobot {

    public String trenutniSenzorValue = "";

    public String FindPath()
    {
        return "BOK";
    }

    public void getInfoFromVirtualMBot(String str)
    {
        trenutniSenzorValue = str;
    }

}