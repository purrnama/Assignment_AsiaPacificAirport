package asia_pacific_airport;

public class Passenger {
	private String name;
	private Aircraft fromAircraft;
	private Gate fromGate;

	private Aircraft toAircraft;
	private Gate toGate;

	public Passenger(String name, Aircraft initialAircraft, Gate initialGate) {
		this.fromAircraft = initialAircraft;
		this.fromGate = initialGate;
		this.name = name;
		if (initialAircraft != null && initialGate != null) {
			// this wont run if the passenger is spawned correctly
			System.out.println(this.name + ": Where the heck am I!?");
		}
	}

	public String getName() {
		return this.name;
	}

	public void enterAircraft(Aircraft aircraft) {
		this.toAircraft = aircraft;
		System.out.println(
				this.name + ": Exiting " + this.fromGate.getName() + " and entering " + this.toAircraft.getName());
	}

	public void exitAircraft(Gate gate) {
		this.toGate = gate;
		System.out.println(
				this.name + ": Exiting " + this.fromAircraft.getName() + " and entering " + this.toGate.getName());

	}

	public String printJourney() {
		String result = "";
		if (this.fromAircraft != null && this.fromGate == null) {
			result = result + this.fromAircraft.getName();
		} else if (this.fromAircraft == null && this.fromGate != null) {
			result = result + this.fromGate.getName();
		}
		if (this.toAircraft != null && this.toGate == null) {
			result = result + " -> " + this.toAircraft.getName();
		} else if (this.toAircraft == null && this.toGate != null) {
			result = result + " -> " + this.toGate.getName();
		}
		return result;
	}
}
