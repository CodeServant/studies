package subscribers.Zad1;

import java.util.Optional;
import java.util.Set;

/**
 * Possible requests to ask from the client to the server.
 * 
 * @author macie
 *
 */

public enum Requests {
	/**
	 * Indicates that client want to subscribe informations.
	 */
	SUB("sub"),
	/**
	 * Indicates that client want to unsubscribe informations.
	 */
	UNSUB("unsub"),
	/**
	 * Indicates that client want to download new informations.
	 */
	FETCHINFO("fetchinfo"),
	/**
	 * Indicates that admin want to add topic to database.
	 */
	ADDTOPIC("addtopic"),
	/**
	 * Indicates that admin want to add topic to database.
	 */
	DELTOPIC("deltopic"),
	/**
	 * Indicates that admin want to add new information.
	 */
	NEWINFO("newinfo"),
	/**
	 * Client want to knew if there are news on the server.
	 */
	HASNEWS("hasnews"),
	/**
	 * Client want to get list of topics.
	 */
	FETCHTOPIC("fetchtopic");

	protected String reqIssue;

	Requests(String s) {
		this.reqIssue = s;
	}

	public String toString() {
		return reqIssue;
	}

	public static Requests toRequest(String rq) {
		Optional<Requests> val = Set.of(Requests.values()).stream().filter((r) -> rq.equals(r.toString())).findFirst();
		return val.get();
	}
}
