package hr.foi.nj3m.interfaces;

public interface ILineFollower extends ISensors {

    boolean isOnRightSide();
    boolean isOnLeftSide();
    boolean isOnCrossroad();
}
