package elevator;

/**
 * Created by gulsunde on 25/10/14.
 */
public class Elevator {
    private final ElevatorGlobals globals;
    private ElevatorState state;
    private int lastFloorStopped;
    private int secondsToDestination;
    private int secondsInFullSpeed;
    private int movingTo;

    public Elevator(ElevatorGlobals globals) {
        this.globals = globals;
        this.state = ElevatorState.STOPPED;
        this.lastFloorStopped = 0;
    }

    public void closeDoor() {
        switch (state) {
            case MOVING:
                break;
            case OPEN:
                state = ElevatorState.STOPPED;
        }
    }

    public void openDoor() {
        switch (state) {
            case STOPPED:
                state = ElevatorState.OPEN;
                break;
            case MOVING:
                System.err.println("Cannot open elevator door when moving");
                System.exit(2);
        }
    }

    public void timeElapsed(int seconds) {
        switch (state) {
            case MOVING:
                secondsToDestination -= seconds;
                if (secondsToDestination <= 0) {
                    state = ElevatorState.STOPPED;
                    lastFloorStopped = movingTo;
                }
                break;
        }
    }

    public int getDirection() {
        switch (state) {
            case OPEN:
                return 0;
            case MOVING:
                return lastFloorStopped < movingTo ? 1 : -1;
        }

        return 0;
    }

    public void moveTo(int floor) {
        switch (state) {
            case STOPPED:
                state = ElevatorState.MOVING;
                movingTo = floor;
                secondsInFullSpeed = Math.abs(floor - lastFloorStopped * globals.getSecondsBetweenFloors());
                secondsToDestination = globals.getSecondsToAccelerate() + globals.getSecondsToSlowDown() + secondsInFullSpeed;
                break;
            case MOVING:
                System.err.println("Cannot give new command when moving");
                System.exit(2);
        }
    }

    public int getCurrentFLoor() {
        switch (state) {
            case STOPPED:
                return lastFloorStopped;
            case MOVING:
                if (secondsToDestination < globals.getSecondsToSlowDown()) {
                    return movingTo;
                } else if (secondsToDestination > globals.getSecondsToSlowDown() + secondsInFullSpeed) {
                    return lastFloorStopped;
                } else {
                    int floorsRemaining = (secondsToDestination - globals.getSecondsToSlowDown()) / globals.getSecondsBetweenFloors();
                    return movingTo - getDirection() * floorsRemaining;
                }
        }
        return 0;
    }

    public ElevatorState getState() {
        return state;
    }

    public String getStatusString() {
        switch (state) {
            case STOPPED:
                return String.format("-- %d", lastFloorStopped);
            case OPEN:
                return String.format("|| %d", lastFloorStopped);
            case MOVING:
                return String.format("%c %d->%d", getDirection() > 0 ? '^' : 'v', getCurrentFLoor(), movingTo);
        }
        return "error";
    }
}
