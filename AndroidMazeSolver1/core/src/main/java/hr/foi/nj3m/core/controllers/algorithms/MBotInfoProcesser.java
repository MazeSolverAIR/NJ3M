package hr.foi.nj3m.core.controllers.algorithms;

import java.util.List;

import hr.foi.nj3m.core.controllers.checkACK.ACKChecker;
import hr.foi.nj3m.core.controllers.enumeratorControllers.InfoFromMBotController;
import hr.foi.nj3m.core.controllers.interfaceControllers.MSMessageFromACK;
import hr.foi.nj3m.interfaces.Enumerations.InfoFromMBot;

public class MBotInfoProcesser
{
    public static int ProcessInfo(List<MSMessageFromACK> listOfMsg)
    {

        if(!ACKChecker.checkNumerOfRecvMessages(listOfMsg)) {
            //Posalji zahtjev za ponovnim slajnjem informacija

            return -2;
        }

        return CheckRecvListInfo(listOfMsg); //ponovno saljem info ili zapisujem podatke u senzore ako su dobri
    }

    private static int CheckRecvListInfo(List<MSMessageFromACK> listOfMsg)
    {
        for(MSMessageFromACK msg: listOfMsg)
        {
            InfoFromMBot infoFromMBot = InfoFromMBotController.getStringFromComandEnum(msg.returnFinalMessage());

            switch (infoFromMBot)
            {
                case SendAgain:
                    return -1;
                case FrontUltrasonic:
                    //MBotPathFinder.FrontSensor.setCurrentValue(msg.returnFinalMessage());
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
        return 0;
    }
}
