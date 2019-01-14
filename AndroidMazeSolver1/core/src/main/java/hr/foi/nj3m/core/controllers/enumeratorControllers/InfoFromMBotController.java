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

        if(infoFromMBot.equals("PSnz"))
            return FrontUltrasonic;

        else if(infoFromMBot.equals("LSnz"))
            return LeftUltrasonic;

        else if(infoFromMBot.equals("DSnz"))
            return RightUltrasonic;

        else if(infoFromMBot.equals("RdSnz"))
            return LineReader;

        else if(infoFromMBot.equals("Over"))
            return LastMessage;

        else if(infoFromMBot.equals("SendAgain"))
            return SendAgain;

        return Null;
    }
}
