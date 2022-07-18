package subscribers.Zad1;

/**
 * Represents topic of the information.
 *
 */

public class Topic implements TransitibleData, Comparable<Topic> {

	protected String name;
	
	public Topic(String name){
		if(name==null) throw new NullPointerException();
		this.name = name;
	}
	
	@Override
	public String toSentMessage() {
		return name;
	}

	@Override
	public TransitibleData fromReceivedMessage(String message) {
		return new Topic(message);
	}
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(Topic o) {
		return this.name.compareTo(o.name);
	}
	@Override
	public String toString() {
		return this.name.toString();
	}
	
}
