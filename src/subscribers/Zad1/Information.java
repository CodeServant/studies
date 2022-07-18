package subscribers.Zad1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents object of information (news).
 * 
 */

public class Information implements TransitibleData, Comparable<Information> {

	protected String text;
	protected Calendar date;
	protected final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	protected Set<Topic> topics = new TreeSet<>();

	/**
	 * Set of informaitons.
	 */
	static Set<Information> informations = new TreeSet<>();
	static Set<Topic> allTopics = new TreeSet<>();

	public Information(String text, Set<Topic> topics) {
		if (text == null || topics == null)
			throw new NullPointerException();
		this.topics = topics;
		this.text = text;
		this.date = Calendar.getInstance();
	}

	public Information(String text) {
		if (text == null)
			throw new NullPointerException();
		this.text = text;
		this.date = Calendar.getInstance();
	}

	public Information(String text, Calendar cal) {
		if (text == null || cal == null)
			throw new NullPointerException();
		this.text = text;
		this.date = cal;
	}

	/**
	 * 
	 * Gets the list of information from database.
	 * 
	 * @param topics topics for info to find
	 * @param from   newer then this date
	 * @return
	 */
	public static List<Information> getInformations(Set<Topic> topics, Calendar from) {
		final Set<Information> set = informations.stream()
				.filter((info) -> info.hasOneOf(topics) && info.getDate().compareTo(from) >= 0)
				.collect(Collectors.toSet());
		return new LinkedList<Information>(set);
	}

	public static void addInformations(Set<Information> infos) throws Exception {
		for (Information in : infos) {
			Information.addInformation(in);
		}

	}

	public static void addInformation(Information info) throws Exception {
		Set<Topic> topics = info.getTopics();
		if(topics.size()==0) throw new Exception("no topics added to information");
		informations.add(info);
		allTopics.addAll(topics);
	}

	public static void delTopic(Topic topic) {
		informations.forEach((info) -> info.getTopics().remove(topic));
		allTopics.remove(topic);
	}

	public static List<Information> getAll(Set<Topic> topics) {
		final Set<Information> set = informations.stream().filter((info) -> info.hasOneOf(topics))
				.collect(Collectors.toSet());
		return new LinkedList<Information>(set);
	}

	public static void addTopicsToDB(Set<Topic> topic) {
		allTopics.addAll(topic);
	}

	public static Set<Topic> getAllTopics() {
		return allTopics;
	}

	public boolean hasOneOf(Set<Topic> topics) {
		for (Topic t : topics) {
			if (this.topics.contains(t))
				return true;
		}
		return false;

	}

	@Override
	public String toSentMessage() {
		final String op = "{", cl = "}", sep = ";";
		StringBuilder bdr = new StringBuilder();
		bdr.append(op);
		bdr.append(this.dateFormat.format(this.date.getTime()) + sep);
		bdr.append(this.text + cl);
		return bdr.toString();
	}

	@Override
	public Information fromReceivedMessage(String message) {
		try {
			message = TransitibleData.truncBrackets(message);
		} catch (Exception e1) {
			return new Information("");
		}
		String[] splited = message.split(";");
		Calendar c = Calendar.getInstance();

		try {
			c.setTime(dateFormat.parse(splited[0]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Information(splited[1], c);
	}

	public Calendar getDate() {
		return this.date;
	}

	public String getText() {
		return this.text;
	}

	public String toString() {
		return formatedDate() + " " + this.text;
	}

	public Set<Topic> getTopics() {
		return topics;
	}

	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}

	public String formatedDate() {
		return this.dateFormat.format(this.date.getTime());
	}

	@Override
	public int compareTo(Information o) {
		return this.text.compareTo(o.getText());
	}
}
