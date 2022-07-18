package subscribers.Zad1;

/**
 * Object contains informations on the current state of comunication with the
 * specific client.
 *
 */
public class Session {
	/**
	 * Data that was fetched from the client.
	 */
	StringBuilder query;
	/**
	 * We start from reading mode.
	 */
	boolean reading = true;

	public Session() {
		query = new StringBuilder();
	}

	public void addQueryData(String str) {
		query.append(str);
		if(str.endsWith(";;"))
			reading=false;
	}

	public String getReceivedMessage() {
		return query.toString();
	}

	public boolean isReading() {
		return reading;
	}
}
