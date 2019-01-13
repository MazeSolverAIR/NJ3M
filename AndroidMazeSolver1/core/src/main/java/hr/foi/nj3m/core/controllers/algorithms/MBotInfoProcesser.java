package hr.foi.nj3m.core.controllers.algorithms;

import java.util.List;

import hr.foi.nj3m.core.controllers.checkACK.ACKChecker;
import hr.foi.nj3m.core.controllers.enumeratorControllers.InfoFromMBotController;
import hr.foi.nj3m.core.controllers.interfaceControllers.MSMessageFromACK;
import hr.foi.nj3m.interfaces.Enumerations.InfoFromMBot;

public class MBotInfoProcesser
{
    private static List<MSMessageFromACK> listOfMessages;

    public static boolean ProcessInfo(List<MSMessageFromACK> listOfMsg)
    {
        listOfMessages = listOfMsg;
        ACKChecker checker = new ACKChecker(listOfMessages);

        if(!checker.CkeckAckAtOnce()) {
            //Posalji zahtjev za ponovnim slajnjem informacija

            return false;
        }

        CheckListInfo();

        return true;
    }

    private static void CheckListInfo()
    {
        for(MSMessageFromACK msg: listOfMessages)
        {
            InfoFromMBot infoFromMBot = InfoFromMBotController.getStringFromComandEnum(msg.returnFinalMessage());

            switch (infoFromMBot)
            {
                case SendAgain:
                    //TODO: Posalji opet sve poruke iz polje kao i prošli put kad si slao
                    break;
                case FrontUltrasonic:
                    //TODO: Spremiti prednji senzor nekamo, Osmilsiti još to
                    break;
                case LeftUltrasonic:
                    //TODO: Spremiti prednji senzor nekamo, Osmilsiti još to
                    break;
                case RightUltrasonic:
                    //TODO: Spremiti prednji senzor nekamo, Osmilsiti još to
                    break;
                case LineReader:
                    //TODO: Spremiti prednji senzor nekamo, Osmilsiti još to
                    break;

                default:
                    break;
            }
        }
    }
}
