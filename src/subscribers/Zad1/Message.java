package subscribers.Zad1;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Package the data to send to the server.
 *
 */

public class Message extends Payload {
	protected Requests requestIssue;
	protected String user;
	public Message(Requests requestIssue, String user){
		if(user==null) throw new NullPointerException();
		this.requestIssue=requestIssue;
		this.user=user;
		
	}
	/**
	 * Creates Message with objects beeing formated according the functions in map.
	 * @param msg
	 * @param maps functions to convert individual data string and integer from which data to start applying this function 
	 * @throws Exception
	 */
	public Message(String msg, SortedMap<Integer, Function<String, TransitibleData>> maps) throws Exception{
		this(msg, maps, -1);
	}
	/**
	 * 
	 * @param msg
	 * @param maps
	 * @param to exclusive
	 * @throws Exception
	 */
	public Message(String msg, SortedMap<Integer, Function<String, TransitibleData>> maps, int to) throws Exception {
		super(msg, maps, to);
		
		Pattern pat = Pattern.compile("\\{(.*);(.*)\\}");
		Matcher mat = pat.matcher(this.extractPreambule(msg));
		mat.find();
		this.requestIssue = Requests.toRequest(mat.group(1));
		this.user=mat.group(2);
	}
	
	public static Requests reqFromMessage(String message) throws Exception {
		final String preammbule = Payload.extractPayload(0, message);
		final String stringReq = Payload.readContent(0, preammbule);
		return Requests.toRequest(stringReq);
	}
	public static String userFromMessage(String message) throws Exception {
		final String preammbule = Payload.extractPayload(0, message);
		final String user = Payload.readContent(1, preammbule);
		return user;
	}
	protected String extractPreambule(String toExtract){
		return Payload.extractPayload(0, toExtract);
	}
	
	protected String extractPayload(String toExtract) {
		return Payload.extractPayload(1, toExtract);
	}
	
	@Override
	protected String preambule() {
		return this.requestIssue.toString() + this.sep + this.user;
	}
}
