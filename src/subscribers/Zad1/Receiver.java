package subscribers.Zad1;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * Helps to retreive information from the form of {@link Payload#getMessage()}.
 *
 */

public abstract class Receiver {
	/**
	 * Just the payload of a message.
	 */
	protected String payload;
	
	public Receiver(String ms){
		payload = ms;
	}
	public Set<Topic> justTopics() throws Exception{
		return oneDataType(topicFunction());
	}
	
	public Set<Information> justInformations(){
		return oneDataType(infoFunction());
	}
	
	public Information infoWithTopics() throws Exception {
		
		Set<Information> infoSet = oneDataType(0, 1, infoFunction());
		Set<Topic> topicSet = oneDataType(1, -1, topicFunction());
		Information[] infos= new Information[1];
		infoSet.toArray(infos);
		infos[0].setTopics(topicSet);
		return infos[0];
	}
	
	private <T extends TransitibleData> Set<T> oneDataType(Function<String, TransitibleData> fun) {
		return oneDataType(0,-1, fun);
	}
	
	private <T extends TransitibleData> Set<T> oneDataType(int from, int to, Function<String, TransitibleData> fun) {
		SortedMap<Integer, Function<String, TransitibleData>> map = new TreeMap();
		map.put(from, fun);
		Payload message = messageParse(map, to);
		Set<T> tData = new TreeSet<>();
		final int limit = to<0 ? message.transDataCount() : to;
		for(int i=0; i<limit; i++) {
			tData.add((T)message.getTransData(i));
		}
		return tData;
	}
	
	private Function<String, TransitibleData> topicFunction(){
		final TransitibleData t = new Topic("");
		final Function<String, TransitibleData> fun = (s) -> t.fromReceivedMessage(s);
		return fun;
	}
	
	private Function<String, TransitibleData> infoFunction(){
		final TransitibleData i = new Information("");
		final Function<String, TransitibleData> fun = (s) -> i.fromReceivedMessage(s);
		return fun;
	}
	
	public abstract Payload messageParse(SortedMap<Integer, Function<String, TransitibleData>> map, int to);
}
