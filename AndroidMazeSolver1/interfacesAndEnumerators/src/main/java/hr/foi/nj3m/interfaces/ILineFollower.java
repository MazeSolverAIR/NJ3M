package hr.foi.nj3m.interfaces;

public interface ILineFollower extends ISensors {

    boolean rightSideOut();
    boolean leftSideOut();
    boolean isOnCrossroad();
}
