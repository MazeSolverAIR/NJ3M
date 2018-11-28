package hr.foi.nj3m.core.controllers.enumeratorControllers;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;

public class CommandsToMBotController {

    public String getStringFromComandEnum(CommandsToMBot command) {
        switch (command) {
            case RunMotors:
                return "RunMotors:";
            case StopMotors:
                return "StopMotors:";
            case Rotate:
                return "Rotate:";
            case SpeedUpLeft:
                return "SpeedUpLeft:";
            case SpeedUpRight:
                return "SpeedUpRight:";
            case LastCommand:
                return "Over:";
        }

        return "StopMotors:";
    }
}
