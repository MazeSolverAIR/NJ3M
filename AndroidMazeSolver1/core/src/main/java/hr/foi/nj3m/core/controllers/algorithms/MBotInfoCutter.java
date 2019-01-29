package hr.foi.nj3m.core.controllers.algorithms;

public class MBotInfoCutter {

    public static String getSubstringedMessage(String msg)
    {
        String returnMsg = "";

        try{
        returnMsg = msg.substring(0, msg.indexOf("b"));
        }
        catch(Exception ex)
        {

        }
        return returnMsg;
    }

    public static Double getSensorValue(String msg)
    {
        Double returnVal = 0.0;

        try{
            returnVal = Double.parseDouble(msg);
        }
        catch(Exception ex)
        {

        }

        return returnVal;
    }
}
