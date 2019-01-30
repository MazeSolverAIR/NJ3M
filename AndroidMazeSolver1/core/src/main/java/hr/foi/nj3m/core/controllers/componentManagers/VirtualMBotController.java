package hr.foi.nj3m.core.controllers.componentManagers;

import hr.foi.nj3m.core.controllers.algorithms.virtualMBotAlgorithm.AlgorithmVirtualRobot;
import hr.foi.nj3m.interfaces.Enumerations.Sides;

public class VirtualMBotController {


    /**
     * Sluzi za orijentaciju robota, ovisno o smjeru prednjeg senzora, podese se svi ostali senzori
     * @param alg instanca objekta AlgorithmVirtualRobot kako bi mu se izmjenile odgovarajuce vrijednosti
     * @param smjerPrednjegSenzora strana (Sides) na koju se postavlja prednji senzor
     */
    public static void setMBotRotation(AlgorithmVirtualRobot alg, Sides smjerPrednjegSenzora)
    {
        alg.prednjaStranae = smjerPrednjegSenzora;

        switch (smjerPrednjegSenzora) {
            case Left:
                alg.lijevaStrana = Sides.Down;
                alg.desnaStrana = Sides.Up;
                alg.zadnjaStrana = Sides.Right;
                break;
            case Right:
                alg.lijevaStrana = Sides.Up;
                alg.desnaStrana = Sides.Down;
                alg.zadnjaStrana = Sides.Left;
                break;
            case Up:
                alg.lijevaStrana = Sides.Left;
                alg.desnaStrana = Sides.Right;
                alg.zadnjaStrana = Sides.Down;
                break;
            case Down:
                alg.lijevaStrana = Sides.Right;
                alg.desnaStrana = Sides.Left;
                alg.zadnjaStrana = Sides.Up;
                break;
        }
    }


    /**
     * "Pokrece virtualnog mBota"
     * @param alg instanca objekta AlgorithmVirtualRobot kako bi se mogle promijeniti trenutne vrijednosti koordinata robota
     */
    public static void move(AlgorithmVirtualRobot alg)
    {
        switch(alg.prednjaStranae)
        {
            case Up:
                alg.trenutniY--;
                break;
            case Down:
                alg.trenutniY++;
                break;
            case Left:
                alg.trenutniX--;
                break;
            case Right:
                alg.trenutniX++;
                break;
        }
    }

    /**
     * Provjerava moze li se mBot kretati na stranu na kojoj je senzor koji daje ulaznu vrijednost parametra
     * @param distance integer koji je udaljenost senzora koji se provjerava
     * @return true ako robot mjeri udaljenost vecu od 1
     */
    public static boolean canMoveTo(int distance)
    {
        return distance > 1;
    }
}
