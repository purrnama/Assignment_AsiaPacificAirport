package asia_pacific_airport;

public class ArrivalDepartureProcess extends Thread {

	private MessageQueue requestQueue, arrivalQueue, departureQueue;
	private boolean running;

	public ArrivalDepartureProcess(MessageQueue queue) {
		this.requestQueue = queue;
		this.arrivalQueue = new MessageQueue(10);
		this.departureQueue = new MessageQueue(10);
		running = true;
	}

	@Override
	public void run() {
		System.out.println(this.getName() + ": Now listening for arrival and departure requests.");
		try {
			while (running) {
				if (!requestQueue.isEmpty()) {
					Message req = requestQueue.receive();
					if (req instanceof ArrivalRequestMessage arrivalReq) {
						System.out.println(this.getName() + ": " + arrivalReq.getAircraft().getName() + ", received"
								+ (arrivalReq.isUrgent() ? " emergency" : "") + " landing request.");
						this.arrivalQueue.send(arrivalReq);
						continue;
					}
					if (req instanceof DepartureRequestMessage departureReq) {
						System.out.println(this.getName() + ": " + departureReq.getAircraft().getName()
								+ ", received departure request.");
						this.departureQueue.send(departureReq);
						continue;
					}
				}
				Thread.sleep(1000);
			}

			System.out.println(this.getName() + ": Stopped listening for arrival and departure requests.");
		} catch (InterruptedException e) {

		}
	}

	public MessageQueue getArrivalQueue() {
		return arrivalQueue;
	}

	public MessageQueue getDepartureQueue() {
		return departureQueue;
	}

	public void stopRunning() {
		this.running = false;
	}
}
