package subscribers.Zad1;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * Extract data from received message to the server.
 * 
 */
public class SrvReceiver extends Receiver{
	
	public SrvReceiver(String ms) {
		super(ms);
	}
	
	public Requests request() throws Exception {
		return Message.reqFromMessage(payload);
	}
	public String user() throws Exception {
		return Message.userFromMessage(payload);
	}
	
	public Message messageParse(SortedMap<Integer, Function<String, TransitibleData>> map, int to) {
		Message message = null;
		try {
			message = new Message(payload, map, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}
}
