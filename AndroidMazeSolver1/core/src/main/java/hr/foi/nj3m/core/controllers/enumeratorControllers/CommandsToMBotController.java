package hr.foi.nj3m.core.controllers.enumeratorControllers;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;

public class CommandsToMBotController {

    public static String getStringFromComandEnum(CommandsToMBot command) {
        switch (command) {
            case RunMotors:
                return "RRunMotors";
            case StopMotors:
                return "RStopMotors";
            case RotateFull:
                return "RRotateFull";
            case RotateLeft:
                return "RRotateLeft";
            case RotateRight:
                return "RRotateRight";
            case SpeedUpLeft:
                return "RSpeedUpLeft";
            case SpeedUpRight:
                return "RSpeedUpRight";
            case LastCommand:
                return "ROver";
        }

        return "RStopMotors";
    }
}
