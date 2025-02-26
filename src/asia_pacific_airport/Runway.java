package asia_pacific_airport;

public class Runway extends Thread {
	private Aircraft currentAircraft;

	public Runway() {
		this.currentAircraft = null;
	}

	@Override
	public void run() {

	}

	public synchronized void arrive(Aircraft aircraft, Gate gate, Object callback) {
		if (isOccupied()) {
			System.out.println("Runway: Invalid arrival, " + aircraft.getName()
					+ " cannot enter the runway. Runway is still occupied by " + currentAircraft.getName() + ".");
			return;
		}
		System.out.println("Runway: " + aircraft.getName() + " has entered the runway.");
		this.currentAircraft = aircraft;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		System.out.println("Runway: " + aircraft.getName() + " has exited the runway.");
		gate.occupy(aircraft);
		this.currentAircraft = null;
		synchronized (aircraft) {
			aircraft.notifyRunwayLock();
		}
		synchronized (callback) {
			callback.notify();
		}
	}

	public synchronized void depart(Aircraft aircraft, Gate gate, Object callback) {
		if (isOccupied()) {
			System.out.println("Runway: Invalid departure, " + aircraft.getName()
					+ " cannot enter the runway. Runway is still occupied by " + currentAircraft.getName() + ".");
			return;
		}
		if (gate.getCurrentAircraft() != aircraft) {
			System.out.println("Runway: Invalid departure, " + aircraft.getName() + " is not on " + gate.getName());
			return;
		}
		System.out.println("Runway: " + aircraft.getName() + " has entered the runway.");
		this.currentAircraft = aircraft;
		gate.vacate(aircraft);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		System.out.println("Runway: " + aircraft.getName() + " has exited the runway.");
		this.currentAircraft = null;
		synchronized (aircraft) {
			aircraft.notifyRunwayLock();
		}
		synchronized (callback) {
			callback.notify();
		}
	}

	public synchronized boolean isOccupied() {
		return currentAircraft != null;
	}
}
