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
        String msgToSave = message.substring(message.lastIndexOf(':')+1);
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
        String numberOfMsgFromString = msg.substring(msg.lastIndexOf('#')+1);

        try{
            returnNumberOfMsg = Integer.parseInt(numberOfMsgFromString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.expectedNumberOfMessages = returnNumberOfMsg;
    }

    @Override
    public String returnFinalMessage() {
        return this.mMessage.substring(0, this.mMessage.lastIndexOf(';'));
    }

    private void setExpectedAsciiSum(String msgToSave)
    {
        String expectedAscii = "";
        expectedAscii = msgToSave.substring(msgToSave.lastIndexOf(';')+1, msgToSave.lastIndexOf('#'));

        try{
            this.expectedAsciiSum = Integer.parseInt(expectedAscii);
        }
        catch(Exception e)
        {
            this.expectedAsciiSum = 0;
        }
    }
}
