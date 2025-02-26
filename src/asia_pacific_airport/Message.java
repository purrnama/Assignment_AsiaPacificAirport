package asia_pacific_airport;

public class Message {
	private MessageType type;
	private String message;
	private boolean urgent;

	public Message(MessageType type, String message, boolean urgent) {
		this.type = type;
		this.message = message;
		this.urgent = urgent;
	}

	public boolean isUrgent() {
		return urgent;
	}

	public MessageType getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}
}
