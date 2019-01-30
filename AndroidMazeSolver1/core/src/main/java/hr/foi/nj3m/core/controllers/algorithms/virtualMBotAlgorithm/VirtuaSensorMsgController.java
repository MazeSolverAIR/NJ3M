package hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm;

public class VirtuaSensorMsgController {


    /**
     * Metoda koja postavlja u ulazni objekt, vrijednosti senzora koje mBot salje
     * @param alg objekt tipa AlgorithmVirtualRobot kako bismo u tom objektu mogli promijeniti vrijednosti senzora
     * @param msg trenutno primljena poruka od mBota - iz nje se uzimaju podaci
     */
    public static void setSensorValues(AlgorithmVirtualRobot alg, String msg) {

        String pomocna = msg.substring(msg.indexOf(":")+1);
        alg.prednjiSenzor = Integer.parseInt(msg.substring(msg.indexOf(":")+1,msg.lastIndexOf("L")));
        alg.lijeviSenzor = Integer.parseInt(pomocna.substring(pomocna.indexOf(":")+1,pomocna.lastIndexOf("R")));
        alg.desniSenzor = Integer.parseInt(pomocna.substring(pomocna.lastIndexOf(":")+1));
    }
}
