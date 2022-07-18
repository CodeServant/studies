package subscribers.Zad1;

import java.util.SortedMap;
import java.util.function.Function;

public class CliReceiver extends Receiver {

	public CliReceiver(String ms) {
		super(ms);
	}
	public Status getStatus(){
		return Response.extractStatus(payload);
	}
	@Override
	public Payload messageParse(SortedMap<Integer, Function<String, TransitibleData>> map, int to) {
		Response message = null;
		try {
			message = new Response(payload, map, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

}
