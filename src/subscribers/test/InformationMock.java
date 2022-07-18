package subscribers.test;

import java.util.Set;

import subscribers.Zad1.*;

/**
 * Information which date default is 2022-01-01 00:00:00.
 * 
 */
public class InformationMock extends Information {

	public InformationMock(String text) {
		super(text);
		defCal();
	}

	public InformationMock(String text, Set<Topic> topics) {
		super(text);
		this.topics = topics;
		defCal();
	}

	private void defCal() {
		this.date.set(2022, 1, 1, 0, 0, 0);
	}
	public String dateFormated() {
		return this.dateFormat.format(this.date.getTime());
	}
	public void setDate(long move) {
		this.date.setTimeInMillis(this.date.getTimeInMillis() + move);
	}
}
