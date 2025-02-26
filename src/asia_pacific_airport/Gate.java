package asia_pacific_airport;

import static asia_pacific_airport.AsiaPacificAirport.allPassengers;

import java.util.Random;
import java.util.Vector;

public class Gate {
	private String name;
	private Aircraft currentAircraft;

	private Vector<Passenger> boardingPassengers = new Vector<Passenger>();
	private Vector<Passenger> arrivingPassengers = new Vector<Passenger>();

	Random random = new Random();
	int minPassengers = 0;
	int maxPassengers = 50;

	public Gate(String name) {
		this.name = name;
		this.currentAircraft = null;
		spawnBoardingPassengers();
	}

	public String printBoardingPassengers() {
		String result = "";
		int index = 0;
		for (Passenger p : boardingPassengers) {
			result = result + (index > 0 ? ", " : "") + p.getName();
			index++;
		}
		return result;
	}

	public String printArrivingPassengers() {
		String result = "";
		int index = 0;
		for (Passenger p : arrivingPassengers) {
			result = result + (index > 0 ? ", " : "") + p.getName();
			index++;
		}
		return result;
	}

	public Vector<Passenger> getBoardingPassengers() {
		return boardingPassengers;
	}

	public Vector<Passenger> getArrivingPassengers() {
		return arrivingPassengers;
	}

	public String getName() {
		return this.name;
	}

	public Aircraft getCurrentAircraft() {
		return currentAircraft;
	}

	public boolean isOccupied() {
		return currentAircraft != null;
	}

	public synchronized void occupy(Aircraft aircraft) {
		System.out.println(this.getName() + ": " + aircraft.getName() + " has entered the gate");
		this.currentAircraft = aircraft;
	}

	public synchronized void vacate(Aircraft aircraft) {
		System.out.println(this.getName() + ": " + aircraft.getName() + " has exited the gate");
		this.currentAircraft = null;
	}

	public void spawnBoardingPassengers() {
		int amount = random.nextInt(maxPassengers + 1 - minPassengers) + minPassengers;
		for (int i = 0; i < amount; i++) {
			Passenger p = new Passenger("Passenger " + (allPassengers.size() + 1), null, this);
			allPassengers.add(p);
			boardingPassengers.add(p);
		}
		System.out.println(
				this.getName() + ": Welcome " + amount + " new passengers. Please wait for the next plane to arrive.");
	}
}
