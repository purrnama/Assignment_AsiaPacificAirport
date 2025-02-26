package asia_pacific_airport;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class AsiaPacificAirport {

	// store all passengers spawned from gates and aircrafts
	public static Vector<Passenger> allPassengers = new Vector<Passenger>();

	public static void main(String[] args) {
		System.out.println("========================");
		System.out.println("  ASIA PACIFIC AIRPORT  ");
		System.out.println("========================");
		System.out.println("Muhammad Qayyum Bin Mahamad Yazid (TP075129)");
		System.out.println("Concurrent Programming Assignment");

		MessageQueue refuelQueue = new MessageQueue(10);

		Random random = new Random();
		int minPassengers = 0;
		int maxPassengers = 50;

		// Initialize Airport Objects
		System.out.println("\n====== Simulation ======");
		Gate[] gates = new Gate[3];
		gates[0] = new Gate("Gate A");
		gates[1] = new Gate("Gate B");
		gates[2] = new Gate("Gate C");
		RefuelingTruck refuelTruck = new RefuelingTruck(refuelQueue);
		refuelTruck.setName("Refueling Truck");
		Runway runway = new Runway();
		AirTrafficControl atc = new AirTrafficControl(gates, runway);
		atc.setName("ATC");
		atc.start();
		refuelTruck.start();

		int aircraftQuantity = 6;
		Aircraft[] aircrafts = new Aircraft[aircraftQuantity];

		int minTime = 0;
		int maxTime = 2000;

		System.out.println(
				aircraftQuantity + " aircraft" + (aircraftQuantity > 1 ? "s" : "") + " will start arriving soon.");

		for (int i = 0; i < aircraftQuantity; i++) {
			try {
				Thread.sleep((long) (random.nextInt(maxTime + 1 - minTime) + minTime));
				aircrafts[i] = new Aircraft(atc.getMessageQueue(), refuelTruck.getMessageQueue(),
						(random.nextInt(maxPassengers + 1 - minPassengers) + minPassengers));
				aircrafts[i].setName("Aircraft " + (i + 1));
				aircrafts[i].start();
			} catch (InterruptedException e) {
			}
		}
		try {
			for (Aircraft a : aircrafts) {
				a.join();
			}
		} catch (InterruptedException e) {
		}
		System.out.println("Simulation complete! Stopping all threads...");
		atc.stopRunning();
		refuelTruck.stopRunning();
		try {
			atc.join();
			refuelTruck.join();
		} catch (InterruptedException e) {
		}

		System.out.println("\n===== Statistics =====");
		System.out.println("Total passengers served: " + allPassengers.size());
		for (Passenger p : allPassengers) {
			System.out.println(p.getName() + ": " + p.printJourney());
		}
		long avgTime = 0;
		long longestTime = 0;
		Aircraft longestTimeAircraft = null;
		long shortestTime = 999;
		Aircraft shortestTimeAircraft = null;
		ArrayList<Aircraft> priorityAircrafts = new ArrayList<Aircraft>();

		for (Aircraft a : aircrafts) {
			// calculate average
			avgTime += Duration.between(a.getInitialTime(), a.getFinalTime()).getSeconds();

			// determine longest time
			if (Duration.between(a.getInitialTime(), a.getFinalTime()).getSeconds() > longestTime) {
				longestTime = Duration.between(a.getInitialTime(), a.getFinalTime()).getSeconds();
				longestTimeAircraft = a;
			}

			// determine shortest time
			if (Duration.between(a.getInitialTime(), a.getFinalTime()).getSeconds() < shortestTime) {
				shortestTime = Duration.between(a.getInitialTime(), a.getFinalTime()).getSeconds();
				shortestTimeAircraft = a;
			}

			// gather aircrafts receiving priority queue
			if (a.getInitialFuel() <= 30) {
				priorityAircrafts.add(a);
			}

		}
		avgTime /= aircraftQuantity;
		System.out.println("\nTotal aircrafts arrived: " + aircrafts.length);
		System.out.println(
				"Longest time in airport: " + longestTimeAircraft.getName() + " (" + longestTime + " seconds)");
		System.out.println(
				"Shortest time in airport: " + shortestTimeAircraft.getName() + " (" + shortestTime + " seconds)");
		System.out.println("Average total time in airport: " + avgTime + " seconds");
		String priority = "";
		int priorityIndex = 0;
		for (Aircraft a : priorityAircrafts) {
			priority = priority + (priorityIndex > 0 ? ", " : "") + a.getName();
			priorityIndex++;
		}
		System.out.println("\nAircrafts that received priority landing: { " + priority + " }");
	}

}
