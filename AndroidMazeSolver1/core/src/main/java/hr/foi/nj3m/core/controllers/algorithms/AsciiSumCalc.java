package hr.foi.nj3m.core.controllers.algorithms;

public class AsciiSumCalc {

    public static int calculateAsciiSum(String message) {
        int output = 0;
        for (int i = 0; i < message.length(); i++) {
            output += (int) message.charAt(i);
        }
        return output;
    }

}
