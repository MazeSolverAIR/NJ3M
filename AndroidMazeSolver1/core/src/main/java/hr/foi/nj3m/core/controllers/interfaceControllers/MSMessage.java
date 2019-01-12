package hr.foi.nj3m.core.controllers.interfaceControllers;

import java.util.List;

import hr.foi.nj3m.interfaces.Enumerations.CommandsToMBot;
import hr.foi.nj3m.interfaces.IMessageACK;

public class MSMessage implements IMessageACK {
    String mMessage;
    int mNumberOfMessages;

    @Override
    public void GetMessage(String message, int numberOfMessages) {
        mMessage = message;
        mNumberOfMessages = numberOfMessages;
    }

    @Override
    public String ReturnFinalMessage() {
        int asciiSum = CalculateAsciiSum();
        return "MS:" + mMessage + ";" + asciiSum + "#" + mNumberOfMessages;
    }

    private int CalculateAsciiSum() {
        int output = 0;
        for (int i = 0; i < mMessage.length(); i++) {
            output += (int) mMessage.charAt(i);
        }
        return output;
    }
}
