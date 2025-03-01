package asia_pacific_airport;

public class RefuelingTruck extends Thread {

	private MessageQueue queue;
	private Gate currentGate;
	private int fuelCapacity;
	private final int maxCapacity = 100;
	private boolean running;

	public RefuelingTruck(MessageQueue queue) {
		this.queue = queue;
		this.currentGate = null;
		this.fuelCapacity = maxCapacity;
		this.running = true;
	}

	@Override
	public void run() {
		System.out.println(this.getName() + ": Now listening to refuel requests.");
		try {
			while (running) {
				if (!queue.isEmpty()) {
					Message req = queue.receive();
					if (req instanceof RefuelingRequestMessage refuelReq) {
						System.out.println(this.getName() + ": " + refuelReq.getAircraft().getName()
								+ ", received refueling request.");
						refuelProcess(refuelReq);
					}
					continue;
				} else {
					if (currentGate != null) {
						returnToBase("No more refuel requests in queue.");
					}
				}
				Thread.sleep(1000);
			}
			System.out.println(this.getName() + ": Stopped listening to refuel requests.");
		} catch (InterruptedException e) {

		}
	}

	public void refuelProcess(RefuelingRequestMessage req) {
		synchronized (req) {
			if (req.getAircraft().getRequiredFuel() > this.fuelCapacity) {
				returnToBase("Insufficient fuel. Available fuel: " + this.fuelCapacity + ", Required fuel: "
						+ req.getAircraft().getRequiredFuel());
			}
			goToGate(req.getGate());
			refuelAircraft(req.getAircraft());
			req.notify();
		}
	}

	private void refuelAircraft(Aircraft aircraft) {
		try {
			System.out.println(this.getName() + ": Refueling " + aircraft.getName());
			while (aircraft.getRequiredFuel() > 0) {
				Thread.sleep(50);
				aircraft.refuel(1);
				this.fuelCapacity--;
			}
		} catch (InterruptedException e) {

		}
	}

	private void goToGate(Gate gate) {
		try {
			if (currentGate != gate) {
				System.out.println(this.getName() + ": Travelling to " + gate.getName());
				Thread.sleep(1000);
				currentGate = gate;
			}
		} catch (InterruptedException e) {

		}
	}

	private void returnToBase(String reason) {
		try {
			if (currentGate != null) {
				System.out.println(this.getName() + ": Returning to base. Reason: " + reason);
				Thread.sleep(1000);
				currentGate = null;
			}
			while (this.fuelCapacity < maxCapacity) {
				Thread.sleep(50);
				this.fuelCapacity++;
			}
			System.out.println(this.getName() + ": Fuel replenished");
		} catch (InterruptedException e) {

		}
	}

	public MessageQueue getMessageQueue() {
		return queue;
	}

	public void stopRunning() {
		this.running = false;
	}

}
