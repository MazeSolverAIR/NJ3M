package hr.foi.nj3m.core.controllers.checkACK;

import java.util.List;

import hr.foi.nj3m.core.controllers.interfaceControllers.MSMessageFromACK;

public class ACKChecker {

    public static boolean checkSum(MSMessageFromACK ackObject)
    {
        return ackObject.calculatedAsciiSum == ackObject.expectedAsciiSum;
    }
    public static boolean checkNumerOfRecvMessages(List<MSMessageFromACK> listOfRcvMessages)
    {
        for (MSMessageFromACK msg: listOfRcvMessages)
        {
            if(msg.expectedNumberOfMessages != listOfRcvMessages.size())
                return false;
        }
        return true;
    }
}
