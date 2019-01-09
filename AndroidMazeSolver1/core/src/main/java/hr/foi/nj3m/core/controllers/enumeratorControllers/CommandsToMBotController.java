package hr.foi.nj3m.core.controllers.enumeratorControllers;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;

public class CommandsToMBotController {

    public static String getStringFromComandEnum(CommandsToMBot command) {
        switch (command) {
            case RunMotors:
                return "MS:RunMotors";
            case StopMotors:
                return "MS:StopMotors";
            case RotateFull:
                return "MS:RotateFull";
            case RotateLeft:
                return "MS:RotateLeft";
            case RotateRight:
                return "MS:RotateRight";
            case SpeedUpLeft:
                return "MS:SpeedUpLeft";
            case SpeedUpRight:
                return "MS:SpeedUpRight";
            case LastCommand:
                return "MS:Over";
        }

        return "MS:StopMotors";
    }
}
