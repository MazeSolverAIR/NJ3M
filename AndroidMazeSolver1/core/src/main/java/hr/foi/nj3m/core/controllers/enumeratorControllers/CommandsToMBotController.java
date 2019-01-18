package hr.foi.nj3m.core.controllers.enumeratorControllers;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;

public class CommandsToMBotController {

    public static String getStringFromComandEnum(CommandsToMBot command) {
        switch (command) {
            case RunMotors:
                return "RM";
            case StopMotors:
                return "SM";
            case RotateFull:
                return "RF";
            case RotateLeft:
                return "RL";
            case RotateRight:
                return "RR";
            case SpeedUpLeft:
                return "SUL";
            case SpeedUpRight:
                return "SUR";
        }

        return "SM";
    }
}
