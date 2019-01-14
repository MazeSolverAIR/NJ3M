package hr.foi.nj3m.core.controllers.algorithms;

import java.util.ArrayList;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;

import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.LastCommand;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.RunMotors;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.SendAgain;
import static hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot.StopMotors;

public class CommandsGenerator {

    public static ArrayList<CommandsToMBot> StartMBot()
    {
        ArrayList<CommandsToMBot> returnList = new ArrayList<>();
        returnList.add(RunMotors);
        returnList.add(LastCommand);

        return returnList;
    }

    public static ArrayList<CommandsToMBot> StopMBot()
    {
        ArrayList<CommandsToMBot> returnList = new ArrayList<>();
        returnList.add(StopMotors);
        returnList.add(LastCommand);

        return returnList;
    }

    public static ArrayList<CommandsToMBot> SendMeAgain()
    {
        ArrayList<CommandsToMBot> returnList = new ArrayList<>();
        returnList.add(SendAgain);
        returnList.add(LastCommand);

        return returnList;
    }
}
