package asia_pacific_airport;

public class RefuelingRequestMessage extends Message {

	private Gate gate;
	private Aircraft aircraft;

	public RefuelingRequestMessage(Aircraft aircraft, String message, Gate gate) {
		super(MessageType.REFUELING_REQUEST, message, false);
		this.aircraft = aircraft;
		this.gate = gate;
	}

	public Aircraft getAircraft() {
		return this.aircraft;
	}

	public Gate getGate() {
		return this.gate;
	}

}
