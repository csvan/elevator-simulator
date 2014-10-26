package simulator;

import elevator.Elevator;
import elevator.ElevatorGlobals;
import person.Person;
import person.PersonGlobals;
import person.PersonState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by gulsunde on 25/10/14.
 */
public class Simulator {

    private int numberOfElevators;
    private int numberOfPersons;
    private int lowestFloor;
    private int highestFloor;
    private int secondsToAccelerate;
    private int secondsToSlowDown;
    private int secondsBetweenFloors;

    // Globals for the simulator components
    private ElevatorGlobals elevatorGlobals;
    private PersonGlobals personGlobals;

    // Elevators and persons to run the simulation on
    private List<Elevator> elevators = new ArrayList<Elevator>();
    private List<Person> persons = new ArrayList<Person>();

    public Simulator(int numberOfElevators, int numberOfPersons, int lowestFloor, int highestFloor, int secondsToAccelerate, int secondsToSlowDown, int secondsBetweenFloors) {
        this.numberOfElevators = numberOfElevators;
        this.numberOfPersons = numberOfPersons;
        this.lowestFloor = lowestFloor;
        this.highestFloor = highestFloor;
        this.secondsToAccelerate = secondsToAccelerate;
        this.secondsToSlowDown = secondsToSlowDown;
        this.secondsBetweenFloors = secondsBetweenFloors;

        elevatorGlobals = new ElevatorGlobals(lowestFloor, highestFloor, secondsToAccelerate, secondsToSlowDown, secondsBetweenFloors);
        for (int i = 0; i < numberOfElevators; i++) {
            elevators.add(new Elevator(elevatorGlobals));
        }

        personGlobals = new PersonGlobals(elevators);
        Random randomGenerator = new Random();
        for (int i = 0; i < numberOfPersons; i++) {
            persons.add(new Person(personGlobals, "PERSON " + i,
                    randomGenerator.nextInt((highestFloor - lowestFloor) + 1) + lowestFloor,
                    randomGenerator.nextInt((highestFloor - lowestFloor) + 1) + lowestFloor));
        }
    }

    private void showStatus() {

        System.out.println("ELEVATORS:");
        for (Elevator elevator : elevators) {
            System.out.println(elevator.getStatusString());
        }
        System.out.println("\n");
        for (Person person : persons) {
            System.out.println(person.getStatusString());
        }
    }

    private boolean everybodyReachedDestination() {
        for (Person person : persons) {
            if (person.getState() != PersonState.ARRIVED) {
                return false;
            }
        }
        return true;
    }

    private void simulateSecondsElapsed(int secs) {

        for (Elevator elevator : elevators) {
            elevator.timeElapsed(secs);
        }
        for (Person person : persons) {
            person.personDoAction();
        }
    }

    public void run() {
        showStatus();
        while (!everybodyReachedDestination()) {
            try {
                Thread.sleep(1);
                simulateSecondsElapsed(1);
                showStatus();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        int numberOfElevators = -1;
        int numberOfPersons = -1;
        int lowestFloor = -1;
        int highestFloor = -1;
        int secondsToAccelerate = -1;
        int secondsToSlowDown = -1;
        int secondsBetweenFloors = -1;

        for (int i = 1; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("--elevators")) {
                numberOfElevators = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("--persons")) {
                numberOfPersons = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("--lowestFloor")) {
                lowestFloor = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("--highestFloor")) {
                highestFloor = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("--accelerationTime")) {
                secondsToAccelerate = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("--slowDownTime")) {
                secondsToSlowDown = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("--betweenFloorsTime")) {
                secondsBetweenFloors = Integer.parseInt(args[i + 1]);
            }
        }
        if (numberOfElevators == -1) {
            throw new IllegalArgumentException("Number of Elevators is not given");
        }
        if (numberOfPersons == -1) {
            throw new IllegalArgumentException("Number of Persons is not given");
        }
        if (lowestFloor == -1) {
            throw new IllegalArgumentException("Lowest Floor is not given");
        }
        if (highestFloor == -1) {
            throw new IllegalArgumentException("Highest Floor is not given");
        }
        if (secondsToAccelerate == -1) {
            throw new IllegalArgumentException("Acceleration Time is not given");
        }
        if (secondsToSlowDown == -1) {
            throw new IllegalArgumentException("Slow Down Time is not given");
        }
        if (secondsBetweenFloors == -1) {
            throw new IllegalArgumentException("Between Floors Time is not given");
        }

        Simulator simulator = new Simulator(numberOfElevators, numberOfPersons, lowestFloor, highestFloor, secondsToAccelerate, secondsToSlowDown, secondsBetweenFloors);
        simulator.run();
    }
}
