package asia_pacific_airport;

public class SuppliesProcess extends Thread {
	private Aircraft aircraft;

	public SuppliesProcess(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	@Override
	public void run() {
		try {
			System.out.println(aircraft.getName() + ": Stocking up supplies.");
			// simulate stocking up supplies
			Thread.sleep(5000);
			aircraft.setHasSupplies(true);
			System.out.println(aircraft.getName() + ": Supplies have been stocked.");
		} catch (InterruptedException e) {

		}
	}
}
