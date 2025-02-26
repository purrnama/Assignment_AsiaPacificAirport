package asia_pacific_airport;

import java.util.Vector;

public class MessageQueue {
	private int capacity;
	private Vector<Message> queue = new Vector<Message>(capacity);

	public MessageQueue(int capacity) {
		this.capacity = capacity;
	}

	public synchronized void send(Message message) {
		if (message.isUrgent()) {
			this.queue.add(0, message);
		} else {
			this.queue.add(message);
		}
	}

	public synchronized Message receive() {
		Message msg = queue.firstElement();
		queue.removeFirst();
		return msg;
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

}
