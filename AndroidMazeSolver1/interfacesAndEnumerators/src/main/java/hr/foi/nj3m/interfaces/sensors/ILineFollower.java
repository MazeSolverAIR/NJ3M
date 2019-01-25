package hr.foi.nj3m.interfaces.sensors;

public interface ILineFollower extends ISensors {

    boolean rightSideOut();
    boolean leftSideOut();
    boolean isOnCrossroad();
}
