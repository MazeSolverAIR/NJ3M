package hr.foi.nj3m.virtualmbot;

import hr.foi.nj3m.interfaces.Enumerations.Sides;
import hr.foi.nj3m.interfaces.virtualMBot.IVirtualUltrasonic;

public class VirtualUltrasonicSensor implements IVirtualUltrasonic {

    private Sides CurrentSensorSide = null;
    private int CurrentValue = 0;

    /**
     * Konstruktor senzora
     * @param side strana na koju zelimo postaviti senzor
     */
    public VirtualUltrasonicSensor(Sides side)
    {
        CurrentSensorSide = side;
    }


    /**
     * @return vraca trenutnu stranu senzora
     */
    @Override
    public Sides getSensorSide() {
        return CurrentSensorSide;
    }


    /**
     * Mijenja stranu senzora
     * @param side nova strana senzora
     */
    @Override
    public void setNewSensorSide(Sides side) {
        this.CurrentSensorSide = side;
    }


    /**
     * Mjeri udaljenost senzora do zida
     * @param y trenutna y koordinata
     * @param x trenutna x koordinata
     * @param matrix trenutni labirint
     * @return udaljenost ovog senzora do zida
     */
    @Override
    public int measureSensor(int y, int x, int[][] matrix) {
        CurrentValue = 0;
        while(matrix[y][x]!=1)
        {
            if(matrix[y][x]==3)
            {
                CurrentValue+=5;
                break;
            }
            CurrentValue++;
            switch (this.CurrentSensorSide) {
                case Up:
                    y--;
                    break;
                case Down:
                    y++;
                    break;
                case Left:
                    x--;
                    break;
                case Right:
                    x++;
                    break;
            }
        }

        return this.CurrentValue;
    }
}
