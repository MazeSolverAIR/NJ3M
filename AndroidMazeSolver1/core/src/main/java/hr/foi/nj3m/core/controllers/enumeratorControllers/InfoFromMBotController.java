package hr.foi.nj3m.core.controllers.enumeratorControllers;

import hr.foi.nj3m.interfaces.Enumerations.InfoFromMBot;

import static hr.foi.nj3m.interfaces.Enumerations.InfoFromMBot.FrontUltrasonic;
import static hr.foi.nj3m.interfaces.Enumerations.InfoFromMBot.LastMessage;
import static hr.foi.nj3m.interfaces.Enumerations.InfoFromMBot.LeftUltrasonic;
import static hr.foi.nj3m.interfaces.Enumerations.InfoFromMBot.LineReader;
import static hr.foi.nj3m.interfaces.Enumerations.InfoFromMBot.Null;
import static hr.foi.nj3m.interfaces.Enumerations.InfoFromMBot.RightUltrasonic;
import static hr.foi.nj3m.interfaces.Enumerations.InfoFromMBot.SendAgain;


public class InfoFromMBotController {

    public static InfoFromMBot getStringFromComandEnum(String infoFromMBot) {

        String workingString = infoFromMBot.substring(0, infoFromMBot.lastIndexOf(':'));

        if(workingString.equals("PSnz"))
            return FrontUltrasonic;

        else if(workingString.equals("LSnz"))
            return LeftUltrasonic;

        else if(workingString.equals("DSnz"))
            return RightUltrasonic;

        else if(workingString.equals("RdSnz"))
            return LineReader;

        else if(workingString.equals("Over"))
            return LastMessage;

        else if(workingString.equals("SendAgain"))
            return SendAgain;

        return Null;
    }
}
