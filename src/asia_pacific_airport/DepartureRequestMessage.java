package asia_pacific_airport;

public class DepartureRequestMessage extends Message {
	private Aircraft aircraft;

	public DepartureRequestMessage(Aircraft aircraft, boolean urgent, String message) {
		super(MessageType.DEPARTURE_REQUEST, message, urgent);
		this.aircraft = aircraft;
	}

	public Aircraft getAircraft() {
		return aircraft;
	}
}
