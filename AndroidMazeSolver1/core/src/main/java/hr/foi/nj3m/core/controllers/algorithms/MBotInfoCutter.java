package hr.foi.nj3m.core.controllers.algorithms;

public class MBotInfoCutter {

    public static String FrontUltrasonic = "";
    public static String RightUltrasonic = "";
    public static String LeftUltrasonic = "";
    public static boolean LineFollowerLeft = false;
    public static boolean LineFollowerRight = false;

    public static void GetInfoForSensors(String[] infosFromMBot)
    {
        FrontUltrasonic = "";
        RightUltrasonic = "";
        LeftUltrasonic = "";
        LineFollowerRight = false;
        LineFollowerLeft = false;

        for (String info: infosFromMBot)
        {
            switch (getNameOfInfo(info))
            {
                case "FUS":
                    FrontUltrasonic = getValueOfInfo(info);
                    break;
                case "RUS":
                    RightUltrasonic = getValueOfInfo(info);
                    break;
                case "LUS":
                    LeftUltrasonic = getValueOfInfo(info);
                    break;
                case "LFL":
                    if(getValueOfInfo(info).equals("1"))
                        LineFollowerLeft = true;
                    break;
                case "LFR":
                    if(getValueOfInfo(info).equals("1"))
                        LineFollowerRight = true;
                    break;
            }
        }
    }


    private static String getNameOfInfo(String info)
    {
        return info.substring(0, info.lastIndexOf(':'));
    }

    private static String getValueOfInfo(String info)
    {
        return info.substring(info.lastIndexOf(':'), info.lastIndexOf(';'));
    }
}
