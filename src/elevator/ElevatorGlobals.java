package elevator;

/**
 * Created by csvanefalk on 25/10/14.
 */
public class ElevatorGlobals {
    private final int lowestFloor;
    private final int highestFloor;
    private final int secondsToAccelerate;
    private final int secondsToSlowDown;
    private final int secondsBetweenFloors;

    public ElevatorGlobals(int lowestFloor, int highestFloor, int secondsToAccelerate, int secondsToSlowDown, int secondsBetweenFloors) {
        this.lowestFloor = lowestFloor;
        this.highestFloor = highestFloor;
        this.secondsToAccelerate = secondsToAccelerate;
        this.secondsToSlowDown = secondsToSlowDown;
        this.secondsBetweenFloors = secondsBetweenFloors;
    }

    public int getLowestFloor() {
        return lowestFloor;
    }

    public int getHighestFloor() {
        return highestFloor;
    }

    public int getSecondsToAccelerate() {
        return secondsToAccelerate;
    }

    public int getSecondsToSlowDown() {
        return secondsToSlowDown;
    }

    public int getSecondsBetweenFloors() {
        return secondsBetweenFloors;
    }
}
