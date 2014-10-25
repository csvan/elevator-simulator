package person;

import elevator.Elevator;

import java.util.List;

/**
 * Created by gulsunde on 25/10/14.
 */
public class PersonGlobals {

    private final List<Elevator> elevators;

    public PersonGlobals(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    public List<Elevator> getElevators() {
        return elevators;
    }
}
