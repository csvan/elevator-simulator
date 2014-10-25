package person;

import elevator.Elevator;
import elevator.ElevatorState;

/**
 * Created by gulsunde on 25/10/14.
 */
public class Person {

    private final PersonGlobals globals;

    private PersonState state;

    private Elevator elevator;

    private int fromFloor;

    private int toFloor;

    private int secsToBeNice;

    String name;

    public Person(PersonGlobals globals, String name, int fromFloor, int toFloor) {
        this.globals = globals;
        this.name = name;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;

        this.state = PersonState.INITIAL;
        this.elevator = null;
    }

    public PersonState getState() {
        return state;
    }

    public void personDoAction() {
        switch (state) {
            case INITIAL:
                doInitialAction();
            case WAITING:
                doWaitingAction();
            case IN_ELEVATOR:
                doInElevatorAction();
            case ARRIVED:
                break;
        }
    }

    private void doInElevatorAction() {

        switch (elevator.getState()) {
            case OPEN:
                if (elevator.getCurrentFLoor() == toFloor) {
                    state = PersonState.ARRIVED;
                }
                elevator.closeDoor();
                break;
            case STOPPED:
                if (elevator.getCurrentFLoor() == toFloor) {
                    elevator.openDoor();
                    state = PersonState.ARRIVED;
                    elevator.closeDoor();
                } else if (elevator.getCurrentFLoor() == fromFloor) {
                    elevator.moveTo(toFloor);
                } else {
                    secsToBeNice--;
                    if (secsToBeNice <= 0) elevator.moveTo(toFloor);
                }
                break;
            case MOVING:
                secsToBeNice = 3;
                break;
        }
    }

    private void enterElevator(Elevator elevator) {
        state = PersonState.IN_ELEVATOR;
        this.elevator = elevator;
    }

    private void doWaitingAction() {

        switch (elevator.getState()) {
            case OPEN:
                if (elevator.getCurrentFLoor() == fromFloor) {
                    enterElevator(elevator);
                } else {
                    state = PersonState.INITIAL;
                    doInitialAction();
                }
                break;
            case STOPPED:
                if (elevator.getCurrentFLoor() == fromFloor) {
                    elevator.openDoor();
                    enterElevator(elevator);
                    state = PersonState.IN_ELEVATOR;
                } else {
                    state = PersonState.INITIAL;
                    elevator = null;
                    doInitialAction();
                }
                break;
            case MOVING:
                break;
        }
    }

    private void doInitialAction() {
        for (Elevator currentElevator : globals.getElevators()) {
            switch (currentElevator.getState()) {
                case OPEN:
                    if (currentElevator.getCurrentFLoor() == fromFloor) {
                        enterElevator(currentElevator);
                        return;
                    }
                    break;
                case STOPPED:
                    if (currentElevator.getCurrentFLoor() == fromFloor) {
                        currentElevator.openDoor();
                        enterElevator(currentElevator);
                        return;
                    }
                    break;
                case MOVING:
                    break;
            }
        }

        // Look for the closest non-busy elevator available
        Elevator closestElevator = null;
        int closestElevatorsDistance = 1000;
        for (Elevator currentElevator : globals.getElevators()) {
            if (currentElevator.getState() == ElevatorState.STOPPED) {
                int distance = Math.abs(currentElevator.getCurrentFLoor() - fromFloor);
                if (distance < closestElevatorsDistance) {
                    closestElevator = currentElevator;
                    closestElevatorsDistance = distance;
                }
            }
        }

        if (closestElevator != null) {
            elevator = closestElevator;
            state = PersonState.WAITING;
            closestElevator.moveTo(fromFloor);
        }
    }

    public String getStatusString() {
        String status = String.format("‰s, %d ---> %d", name, fromFloor, toFloor);
        switch (state) {
            case INITIAL:
                return status + " Just entered into the system";
            case WAITING:
                return status + " Waiting for elevator to arrive ";
            case IN_ELEVATOR:
                return status + " In elevator ";
            case ARRIVED:
                break;
            default:
                return status + " error";
        }
        return status + " error";
    }
}
