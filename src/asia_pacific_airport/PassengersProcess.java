package asia_pacific_airport;

public class PassengersProcess extends Thread {

	private Aircraft aircraft;

	public PassengersProcess(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	@Override
	public void run() {
		try {
			System.out.println(aircraft.getName()
					+ " (Passengers Process): Old passengers may exit the cabin, thank you for flying with us!");
			while (!this.aircraft.getPassengers().isEmpty()) {
				Passenger p = this.aircraft.getPassengers().removeFirst();
				Thread.sleep(500);
				p.exitAircraft(this.aircraft.getArrivalGate());
				this.aircraft.getArrivalGate().getArrivingPassengers().add(p);
			}
			System.out.println(this.aircraft.getName()
					+ " (Passengers Process): New passengers may enter the cabin, welcome and hope you enjoy your flight!");
			while (!this.aircraft.getArrivalGate().getBoardingPassengers().isEmpty()) {
				Passenger p = this.aircraft.getArrivalGate().getBoardingPassengers().removeFirst();
				Thread.sleep(500);
				p.enterAircraft(this.aircraft);
				this.aircraft.getPassengers().add(p);
			}
			this.aircraft.getArrivalGate().spawnBoardingPassengers();
		} catch (InterruptedException e) {

		}
	}
}
