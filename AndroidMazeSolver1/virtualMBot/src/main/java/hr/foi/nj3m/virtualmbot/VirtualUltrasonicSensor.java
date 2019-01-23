package hr.foi.nj3m.virtualmbot;

import hr.foi.nj3m.interfaces.Enumerations.Sides;

public class VirtualUltrasonicSensor implements IVirtualUltrasonic {

    private Sides CurrentSensorSide = null;
    int CurrentValue = 0;

    public VirtualUltrasonicSensor(Sides side)
    {
        CurrentSensorSide = side;
    }

    @Override
    public Sides getSensorSide() {
        return CurrentSensorSide;
    }

    @Override
    public void setNewSensorSide(Sides side) {
        this.CurrentSensorSide = side;
    }

    @Override
    public int measureSensor(int y, int x, int[][] matrix) {
        CurrentValue = 0;
        while(matrix[y][x]!=1)
        {
            switch (this.CurrentSensorSide) {
                case Up:
                    y++;
                    break;
                case Down:
                    y--;
                    break;
                case Left:
                    x--;
                    break;
                case Right:
                    x++;
                    break;
            }
            CurrentValue++;
        }

        return this.CurrentValue;
    }
}
