package hr.foi.nj3m.core.controllers.interfaceControllers;

import hr.foi.nj3m.interfaces.IMessageACK;

import static hr.foi.nj3m.core.controllers.algorithms.AsciiSumCalc.calculateAsciiSum;

public class MSMessageFromACK implements IMessageACK {

    private String mMessage;
    public int calculatedAsciiSum = 0;
    public int expectedAsciiSum = 0;
    public int expectedNumberOfMessages = 0;

    @Override
    public void setMessage(String message) {
        String msgToSave = "";
        try {
            msgToSave = message.substring(message.lastIndexOf(':') + 1, message.lastIndexOf('&'));
        }
        catch(Exception ex)
        {
            msgToSave = "";
        }
        setExpectedAsciiSum(msgToSave);
        setNumberOfMessages(msgToSave);
        this.mMessage = msgToSave;

        this.calculatedAsciiSum = calculateAsciiSum(returnFinalMessage());
    }

    @Override
    public void setNumberOfMessages(int numberOfMsg) {

    }

    private void setNumberOfMessages(String msg)
    {
        int returnNumberOfMsg = 0;

        try{
            String numberOfMsgFromString = msg.substring(msg.lastIndexOf('#')+1);
            returnNumberOfMsg = Integer.parseInt(numberOfMsgFromString);
        }
        catch (Exception e)
        {
             e.printStackTrace();
             returnNumberOfMsg = -1;

        }

        this.expectedNumberOfMessages = returnNumberOfMsg;
    }

    @Override
    public String returnFinalMessage() {
        try
        {
            return this.mMessage.substring(0, this.mMessage.lastIndexOf(';'));
        }
        catch(Exception ex)
        {
            return "";
        }
    }

    private void setExpectedAsciiSum(String msgToSave)
    {
        try{
            String expectedAscii = msgToSave.substring(msgToSave.lastIndexOf(';')+1, msgToSave.lastIndexOf('#'));
            this.expectedAsciiSum = Integer.parseInt(expectedAscii);
        }
        catch(Exception ex)
        {
            this.expectedAsciiSum = -1;
        }
    }
}
