package hr.foi.nj3m.core.controllers.algorithms;

import java.util.List;

import hr.foi.nj3m.core.controllers.checkACK.ACKChecker;
import hr.foi.nj3m.core.controllers.enumeratorControllers.InfoFromMBotController;
import hr.foi.nj3m.core.controllers.interfaceControllers.MSMessageFromACK;
import hr.foi.nj3m.interfaces.Enumerations.ACKs;
import hr.foi.nj3m.interfaces.Enumerations.InfoFromMBot;

import static hr.foi.nj3m.interfaces.Enumerations.ACKs.DemandMessagesAgain;
import static hr.foi.nj3m.interfaces.Enumerations.ACKs.OK;
import static hr.foi.nj3m.interfaces.Enumerations.ACKs.SendMessagesAgain;

public class MBotInfoProcesser
{
    public static ACKs ProcessInfo(List<MSMessageFromACK> listOfMsg)
    {
        if(!ACKChecker.checkNumerOfRecvMessages(listOfMsg))
            return DemandMessagesAgain;

        return CheckRecvListInfo(listOfMsg); //ponovno saljem info ili zapisujem podatke u senzore ako su dobri
    }

    private static ACKs CheckRecvListInfo(List<MSMessageFromACK> listOfMsg)
    {
        for(MSMessageFromACK msg: listOfMsg)
        {
            InfoFromMBot infoFromMBot = InfoFromMBotController.getStringFromComandEnum(msg.returnFinalMessage());

            switch (infoFromMBot)
            {
                case SendAgain:
                    return SendMessagesAgain;
                case FrontUltrasonic:
                    if(MBotPathFinder.FrontSensor != null)
                        MBotPathFinder.FrontSensor.setCurrentValue(msg.returnFinalMessage());
                    break;
                case LeftUltrasonic:
                    if(MBotPathFinder.LeftSensor != null)
                        MBotPathFinder.LeftSensor.setCurrentValue(msg.returnFinalMessage());
                    break;
                case RightUltrasonic:
                    if(MBotPathFinder.RightSensor != null)
                        MBotPathFinder.RightSensor.setCurrentValue(msg.returnFinalMessage());
                    break;
                case LineReader:
                    //TODO: Spremiti prednji senzor nekamo, Osmilsiti još to
                    break;

                default:
                    break;
            }
        }
        return OK;
    }
}
