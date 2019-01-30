package hr.foi.nj3m.core.controllers.algorithms;

public class MBotInfoCutter {


    /**
     * Uzima substring primljene poruke tako da se dobije korisna poruka
     * @param msg primljena poruka od mBota
     * @return poruka s korisnim podacima
     */
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


    /**
     * @param msg cista, korisna poruka dobivena metodom getSubstringedMessage
     * @return vrijednost senzora kao double
     */
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
