package hr.foi.nj3m.core.controllers.enumeratorControllers;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;

public class CommandsToMBotController {

    public String getStringFromComantEnum(CommandsToMBot command) {
        switch (command) {
            case Kreni:
                return "Kreni naprije";
            case ZaustaviSe:
                return "Zaustavi se";
            case Rotiraj:
                return "Skreni";
        }

        return "Zaustavi se";
    }
}
