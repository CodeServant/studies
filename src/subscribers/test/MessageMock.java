package subscribers.test;

import java.util.SortedMap;
import java.util.function.Function;

import javax.management.remote.SubjectDelegationPermission;

import subscribers.Zad1.*;

public class MessageMock extends Message {
	public String payload = super.payload;
	public MessageMock(String msg, SortedMap<Integer, Function<String, TransitibleData>> maps) throws Exception {
		super(msg, maps);
	}
	public MessageMock(Requests a,String b) {
		super(a,b);
	}
	public static String extractPayload(Integer which, String payload) {
		return Message.extractPayload(which, payload);
	}
	public static Integer contentCount(String payload) throws Exception {
		return Message.contentCount(payload);
	}
	public static String readContent(Integer which, String payload) throws Exception {
		return Message.readContent(which, payload);
	}
}
