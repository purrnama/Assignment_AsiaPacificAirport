package asia_pacific_airport;

public class RunwayProcess extends Thread {

	private Runway runway;
	private Gate[] gates;
	private boolean running;

	ArrivalDepartureProcess arrivalDeparture;

	public RunwayProcess(ArrivalDepartureProcess arrivalDeparture, Runway runway, Gate[] gates) {
		this.runway = runway;
		this.gates = gates;
		this.arrivalDeparture = arrivalDeparture;
		this.running = true;
	}

	@Override
	public void run() {
		System.out.println(this.getName() + ": Now coordinating aircrafts on runway.");
		try {
			while (running) {
				Thread.sleep(1000);
				// wait for runway to be free before
				while (runway.isOccupied()) {
					try {
						System.out.println(this.getName() + ": Waiting for runway to clear.");
						wait();
					} catch (InterruptedException e) {

					}
				}
				// check if a gate is vacant
				Gate vacantGate = null;
				for (Gate gate : gates) {
					if (!gate.isOccupied()) {
						vacantGate = gate;
						break;
					}
				}
				// if there is a vacant gate, cater an arrival request if there is one
				if (vacantGate != null && !arrivalDeparture.getArrivalQueue().isEmpty()) {
					ArrivalRequestMessage arrivalReq = (ArrivalRequestMessage) arrivalDeparture.getArrivalQueue()
							.receive();
					synchronized (arrivalReq) {
						System.out.println(this.getName() + ": " + arrivalReq.getAircraft().getName()
								+ ", enter runway, report to " + vacantGate.getName());
						arrivalReq.getAircraft().setArrivalGate(vacantGate);
						arrivalReq.notify();
					}
					runway.arrive(arrivalReq.getAircraft(), vacantGate, this);
					continue;
				}
				// else, cater a departure request to free a gate, if there is one
				else if (!arrivalDeparture.getDepartureQueue().isEmpty()) {
					DepartureRequestMessage departureReq = (DepartureRequestMessage) arrivalDeparture
							.getDepartureQueue().receive();
					synchronized (departureReq) {
						System.out.println(this.getName() + ": " + departureReq.getAircraft().getName()
								+ ", enter runway, cleared for take off.");
						departureReq.notify();
					}
					runway.depart(departureReq.getAircraft(), departureReq.getAircraft().getArrivalGate(), this);
					continue;
				}
			}
			System.out.println(this.getName() + ": Stopped coordinating aircrafts on runway.");
		} catch (InterruptedException e) {

		}
	}

	public void stopRunning() {
		this.running = false;
	}
}
