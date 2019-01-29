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
                return "FR";
            case RotateLeft:
                return "SL";
            case RotateRight:
                return "SD";
            case SpeedUpLeft:
                return "SUL";
            case SpeedUpRight:
                return "SUR";
        }

        return "";
    }
}
