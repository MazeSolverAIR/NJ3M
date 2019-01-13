package hr.foi.nj3m.core.controllers.interfaceControllers;

import hr.foi.nj3m.interfaces.IMessageACK;

import static hr.foi.nj3m.core.controllers.algorithms.AsciiSumCalc.calculateAsciiSum;

public class MSMessageToACK implements IMessageACK {
    private String mMessage;
    private int mNumberOfMessages;

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public void setNumberOfMessages(int mNumberOfMessages)
    {
        this.mNumberOfMessages = mNumberOfMessages;
    }

    @Override
    public String returnFinalMessage() {
        int asciiSum = calculateAsciiSum(this.mMessage);
        return "MS:" + this.mMessage + ";" + asciiSum + "#" + this.mNumberOfMessages;
    }
}
