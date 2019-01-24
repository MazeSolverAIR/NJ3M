package hr.foi.nj3m.core.controllers.algorithms;

public class AlgoritamVirtualRobot {

    public String trenutniSenzorValue = "";

    public String FindPath(String message){

        String pomocna = message.substring(message.indexOf(":")+1);
        int prednjiSenzor = Integer.parseInt(message.substring(message.indexOf(":")+1,message.lastIndexOf("L")));
        int lijeviSenzor = Integer.parseInt(pomocna.substring(pomocna.indexOf(":")+1,pomocna.lastIndexOf("R")));
        int desniSenzor = Integer.parseInt(pomocna.substring(pomocna.lastIndexOf(":")+1));

        if((prednjiSenzor>=lijeviSenzor)&&(prednjiSenzor>=desniSenzor)){
            return "RM";
        }
        else if((lijeviSenzor>=prednjiSenzor)&&(lijeviSenzor>=desniSenzor)){
            return "RL";
        }
        else
            return "RR";

    }

    public void getInfoFromVirtualMBot(String str)
    {
        trenutniSenzorValue = str;
    }

}