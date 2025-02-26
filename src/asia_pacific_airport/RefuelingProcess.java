package asia_pacific_airport;

public class RefuelingProcess extends Thread {

	private Aircraft aircraft;
	private Gate gate;
	private MessageQueue refuelQueue;

	public RefuelingProcess(Aircraft aircraft) {
		this.aircraft = aircraft;
		this.gate = aircraft.getArrivalGate();
		this.refuelQueue = aircraft.getRefuelQueue();
	}

	@Override
	public void run() {
		// send message to refueling truck
		String refuelMsg = this.aircraft.getName() + ": Requesting for refuel.";
		System.out.println(refuelMsg);
		RefuelingRequestMessage refuelReq = new RefuelingRequestMessage(this.aircraft, refuelMsg, this.gate);
		this.refuelQueue.send(refuelReq);
		// wait for refuel to complete
		synchronized (refuelReq) {
			try {
				refuelReq.wait();
			} catch (InterruptedException e) {

			}
		}
		System.out.println(this.aircraft.getName() + ": Refuel complete.");

	}
}
