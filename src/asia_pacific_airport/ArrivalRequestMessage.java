package asia_pacific_airport;

public class ArrivalRequestMessage extends Message {
	private Aircraft aircraft;

	public ArrivalRequestMessage(Aircraft aircraft, boolean urgent, String message) {
		super(MessageType.ARRIVAL_REQUEST, message, urgent);
		this.aircraft = aircraft;
	}

	public Aircraft getAircraft() {
		return aircraft;
	}
}
