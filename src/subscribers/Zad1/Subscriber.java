package subscribers.Zad1;

import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

public class Subscriber extends User {
	Calendar lastFetchedDate;
	Set<Topic> topics;

	public Subscriber(String name) {
		super(name);
		this.topics = new TreeSet<Topic>();
	}

	public Subscriber(String name, Set<Topic> topics) {
		super(name);
		this.topics = topics;
	}
	public Calendar lastFetchedDate() {
		return lastFetchedDate;
	}
	
	public void setFetchedDate(Calendar c) {
		this.lastFetchedDate=c;
	}
	public void bumpFetchedDate() {
		this.setFetchedDate(Calendar.getInstance());
	}
	public Set<Topic> getTopics() {
		return topics;
	}
}
