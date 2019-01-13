package hr.foi.nj3m.core.controllers.checkACK;

import java.util.List;

import hr.foi.nj3m.core.controllers.interfaceControllers.MSMessageFromACK;

public class ACKChecker {

    private List<MSMessageFromACK> listOfMessages;

    public ACKChecker(List<MSMessageFromACK> listOfMsg)
    {
        this.listOfMessages = listOfMsg;
    }

    public boolean CkeckAckAtOnce()
    {
        for(MSMessageFromACK msgObjFromList:listOfMessages)
        {
            if(!checkNumerOfRecvMessages(msgObjFromList))
                return false;

            if(!checkSum(msgObjFromList))
                return false;
        }

        return true;
    }

    public boolean checkSum(MSMessageFromACK ackObject)
    {
        return ackObject.calculatedAsciiSum == ackObject.expectedAsciiSum;
    }
    public boolean checkNumerOfRecvMessages(MSMessageFromACK ackObject)
    {
        return ackObject.expectedNumberOfMessages == this.listOfMessages.size();
    }
}
