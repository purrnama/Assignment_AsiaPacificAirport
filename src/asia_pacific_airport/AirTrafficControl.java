package asia_pacific_airport;

public class AirTrafficControl extends Thread {
	private MessageQueue msgQueue;
	private Gate[] gates;
	private Runway runway;
	private ArrivalDepartureProcess arrivalDeparture;
	private RunwayProcess runwayProcess;;

	public AirTrafficControl(Gate[] gates, Runway runway) {
		this.msgQueue = new MessageQueue(10);
		this.gates = gates;
		this.runway = runway;
		this.arrivalDeparture = new ArrivalDepartureProcess(msgQueue);
		this.runwayProcess = new RunwayProcess(arrivalDeparture, this.runway, this.gates);
	}

	public void run() {

		// Receive arrival and departure requests from aircrafts
		this.arrivalDeparture.setName("ATC (Arrival Departure Process)");
		this.arrivalDeparture.start();

		// Coordinate landing and departing aircrafts on the runway
		this.runwayProcess.setName("ATC (Runway Process)");
		this.runwayProcess.start();

		try {
			this.arrivalDeparture.join();
			this.runwayProcess.join();
		} catch (InterruptedException e) {

		}
	}

	public MessageQueue getMessageQueue() {
		return msgQueue;
	}

	public void stopRunning() {
		this.arrivalDeparture.stopRunning();
		this.runwayProcess.stopRunning();
	}

}
