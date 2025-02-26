package asia_pacific_airport;

import static asia_pacific_airport.AsiaPacificAirport.allPassengers;

import java.time.Instant;
import java.util.Random;
import java.util.Vector;

public class Aircraft extends Thread {

	private MessageQueue atcQueue;
	private MessageQueue refuelQueue;

	private Gate arrivalGate;
	private boolean hasSupplies = false;
	private int fuel, initialFuel;
	private final int maxFuel = 100;
	private int numInitialPassengers;
	Object runwayLock;

	Vector<Passenger> passengers = new Vector<Passenger>();

	private Instant initialTime, arrivalWaitTime, gateWaitTime, departureWaitTime, finalTime;

	Random random = new Random();

	public Aircraft(MessageQueue atcQueue, MessageQueue refuelQueue, int numPassengers) {
		this.atcQueue = atcQueue;
		this.refuelQueue = refuelQueue;
		this.fuel = random.nextInt(51) + 5;
		this.initialFuel = fuel;
		this.runwayLock = new Object();
		this.numInitialPassengers = numPassengers;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < numInitialPassengers; i++) {
				Passenger p = new Passenger("Passenger " + (allPassengers.size() + 1), this, null);
				allPassengers.add(p);
				passengers.add(p);
			}
			System.out.println(this.getName() + ": Arriving at airport. Passengers on board: " + passengers.size());
			this.initialTime = Instant.now();
			Thread.sleep(1000);
			// send arrival request message to ATC, send emergency if fuel is low
			String arrivalMsg = this.getName() + ": Asia Pacific Tower,"
					+ (this.fuel <= 30 ? " Emergency, low fuel." : "") + " Requesting to land.";
			ArrivalRequestMessage arrivalRequest = new ArrivalRequestMessage(this, (this.fuel <= 30 ? true : false),
					arrivalMsg);
			atcQueue.send(arrivalRequest);
			System.out.println(arrivalMsg);
			// wait to receive arrival permission from request
			synchronized (arrivalRequest) {
				try {
					arrivalRequest.wait();
				} catch (InterruptedException e) {

				}
			}
			this.arrivalWaitTime = Instant.now();
			System.out.println(this.getName() + ": Received permission to land at " + this.arrivalGate.getName()
					+ ". Entering runway now.");
			// traveling through runway to gate
			synchronized (runwayLock) {
				try {
					runwayLock.wait();
				} catch (InterruptedException e) {

				}
			}
			System.out.println(this.getName() + ": Arrived at " + this.arrivalGate.getName() + ".");
			Thread.sleep(1000);
			// run three processes, clear old passengers and welcome new passengers, stock
			// up supplies and refuel
			PassengersProcess passengersProcess = new PassengersProcess(this);
			SuppliesProcess supplies = new SuppliesProcess(this);
			RefuelingProcess refuel = new RefuelingProcess(this);
			passengersProcess.start();
			supplies.start();
			refuel.start();
			// wait for all processes to complete
			try {
				passengersProcess.join();
				supplies.join();
				refuel.join();
			} catch (InterruptedException e) {

			}
			this.gateWaitTime = Instant.now();
			// send departure request message to ATC
			String departureMsg = this.getName() + ": Asia Pacific Tower, requesting to depart.";
			DepartureRequestMessage departureRequest = new DepartureRequestMessage(this, false, departureMsg);
			atcQueue.send(departureRequest);
			System.out.println(arrivalMsg);
			// wait to receive departure permission from request
			synchronized (departureRequest) {
				try {
					departureRequest.wait();
				} catch (InterruptedException e) {

				}
			}
			this.departureWaitTime = Instant.now();
			System.out.println(this.getName() + ": Received departure permission. Entering runway now.");
			// traveling on runway to depart
			synchronized (runwayLock) {
				try {
					runwayLock.wait();
				} catch (InterruptedException e) {

				}
			}
			this.finalTime = Instant.now();
			System.out.println(this.getName() + ": Departed from airport. Passengers on board: " + passengers.size());
		} catch (InterruptedException e) {

		}
	}

	public String printPassengers() {
		String result = "";
		int index = 0;
		for (Passenger p : passengers) {
			result = result + (index > 0 ? ", " : "") + p.getName();
			index++;
		}
		return result;
	}

	public Gate getArrivalGate() {
		return this.arrivalGate;
	}

	public void setArrivalGate(Gate arrivalGate) {
		this.arrivalGate = arrivalGate;
	}

	public void setHasSupplies(boolean hasSupplies) {
		this.hasSupplies = hasSupplies;
	}

	public boolean getHasSupplies(boolean hasPassengers) {
		return this.hasSupplies;
	}

	public MessageQueue getRefuelQueue() {
		return this.refuelQueue;
	}

	public int getRequiredFuel() {
		return maxFuel - fuel;
	}

	public void refuel(int amount) {
		this.fuel = fuel + amount;
	}

	public void notifyRunwayLock() {
		synchronized (runwayLock) {
			this.runwayLock.notify();
		}
	}

	public Instant getInitialTime() {
		return this.initialTime;
	}

	public Instant getArrivalWaitTime() {
		return this.arrivalWaitTime;
	}

	public Instant getGateWaitTime() {
		return this.gateWaitTime;
	}

	public Instant getDepartureWaitTime() {
		return this.departureWaitTime;
	}

	public Instant getFinalTime() {
		return this.finalTime;
	}

	public Vector<Passenger> getPassengers() {
		return passengers;
	}

	public int getInitialFuel() {
		return this.initialFuel;
	}
}
