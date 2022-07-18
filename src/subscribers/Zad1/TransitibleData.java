package subscribers.Zad1;

public interface TransitibleData {
	/**
	 * Text representation of this data to transit. Should have form of simple text
	 * or if it is complex than should be with brackets {some data;next data}
	 * 
	 * @return
	 */
	public String toSentMessage();

	/**
	 * 
	 * @param message from which object will be made
	 */
	public TransitibleData fromReceivedMessage(String message);

	/**
	 * Deletes the {} brackets at the begining and the end of a message.
	 * 
	 * @param message
	 * @return
	 */
	public static String truncBrackets(String message) throws Exception {
		if (message.length() < 2)
			throw new Exception("message to short " + message);
		if (message.charAt(0) == '{' && message.charAt(message.length() - 1) == '}')
			return message.substring(1, message.length() - 1);
		else
			return message;
	}
}
