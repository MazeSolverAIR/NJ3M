package hr.foi.nj3m.core.controllers.enumeratorControllers;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;

public class CommandsToMBotController {

    public static String getStringFromComandEnum(CommandsToMBot command) {
        switch (command) {
            case RunMotors:
                return "RunMotors";
            case StopMotors:
                return "StopMotors";
            case RotateFull:
                return "RotateFull";
            case RotateLeft:
                return "RotateLeft";
            case RotateRight:
                return "RotateRight";
            case SpeedUpLeft:
                return "SpeedUpLeft";
            case SpeedUpRight:
                return "SpeedUpRight";
            case LastCommand:
                return "Over";
            case SendAgain:
                return "SendAgain";
        }

        return "StopMotors";
    }
}
