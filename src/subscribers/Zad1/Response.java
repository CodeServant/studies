package subscribers.Zad1;

import java.util.SortedMap;
import java.util.function.Function;

public class Response extends Payload {
	
	protected Status status;
	
	public Response(Status st){
		this.status = st;
	}
	
	public Response(String payload, SortedMap<Integer, Function<String, TransitibleData>> maps, int to) throws Exception{
		super(payload, maps, to);
		this.status = extractStatus(payload);
	}
	
	@Override
	protected String preambule() {
		return this.status.code().toString();
	}
	
	public static Status extractStatus(String toExtract){
		Integer code = Integer.parseInt(Payload.extractPayload(0, toExtract).replaceAll("[\\{\\}]", ""));
		return Status.toStatus(code);
	}
	public Status getStatus() {
		return status;
	}
	@Override
	protected String extractPayload(String toExtract) {
		return Payload.extractPayload(1, toExtract);
	}
}
