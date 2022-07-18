package subscribers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import subscribers.Zad1.*;

class Tests {
	private Topic a = new Topic("polityka"), b = new Topic("edukacja");
	
	@Test
	void testMessage() {
		Set topics = new TreeSet<Topic>();
		topics.add(a);
		topics.add(b);
		Information info = null;
		try {
			info = new InformationMock("informacja informacja", topics);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals("2022-02-01 00:00:00 informacja informacja", info.toString(), "toString metode failure");
		Message message = new MessageMock(Requests.NEWINFO,"admin");
		message.addTransData(info);
		
		assertEquals("{newinfo;admin}{{2022-02-01 00:00:00;informacja informacja}}", message.getMessage());
		message.addTransData(a);
		assertEquals("{newinfo;admin}{{2022-02-01 00:00:00;informacja informacja};polityka}", message.getMessage());
		assertEquals("{{2022-02-01 00:00:00;informacja informacja};polityka}", message.getPayload());
		SortedMap<Integer, Function<String, TransitibleData>> maps = new TreeMap<>();
		maps.put(0, (str) -> new Information("").fromReceivedMessage(str));
		maps.put(1, (str) -> new Topic("").fromReceivedMessage(str));
		try {
			Message message2 = new Message(message.getMessage(), maps);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Information retrievedInfo = (Information) message.getTransData(0);
		
		Topic retrievedTopic = (Topic) message.getTransData(1);
		assertEquals("2022-02-01 00:00:00 informacja informacja", retrievedInfo.toString());
		assertEquals("polityka", retrievedTopic.toString());
		
	}
	
	@Test
	void testSrvReceiver() {
		Set<Topic> topics= new TreeSet<>();
		topics.add(a);
		topics.add(b);
		Information info=new Information("ważniejsza informacja");
		
		SrvReceiverMock receiver = new SrvReceiverMock("{sub;user}{edukacja;polityka}");
		try {
			assertTrue(receiver.justTopics().size()==2);
			assertTrue(receiver.justTopics().containsAll(topics));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		receiver = new SrvReceiverMock("{sub;user}{{2022-02-01 00:00:00;informacja informacja};{2022-02-02 00:00:00;ważniejsza informacja}}");
		info.fromReceivedMessage("{2022-02-02 00:00:00;ważniejsza informacja}");
		assertTrue(receiver.justInformations().contains(info));
		assertEquals("ważniejsza informacja", info.getText());
		receiver = new SrvReceiverMock("{sub;user}{{2022-02-01 00:00:00;informacja informacja};edukacja;polityka}");
		try {
			info = receiver.infoWithTopics();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		assertEquals("informacja informacja", info.getText());
		assertEquals("2022-02-01 00:00:00 informacja informacja", info.toString());
		assertEquals(2, info.getTopics().size());
		assertTrue(info.getTopics().contains(a));
		assertTrue(info.getTopics().contains(b));
		receiver = new SrvReceiverMock("{sub;user}{{2022-02-01 00:00:00;informacja informacja};{2022-02-02 00:00:00;ważniejsza informacja}}");
		try {
			assertEquals(Requests.SUB, receiver.request());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	void testCliReceiver() {
		Set<Topic> topics= new TreeSet<>();
		topics.add(a);
		topics.add(b);
		Information info=new Information("ważniejsza informacja");
		CliReceiver receiver = new CliReceiver("{1}{edukacja;polityka}");
		assertEquals(Status.OK, receiver.getStatus());
		Set<Topic> topics2;
		try {
			topics2 = receiver.justTopics();
			assertEquals(topics, topics2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	void testSegment() {
		try {
			assertEquals("{edukacja}", MessageMock.extractPayload(0, "{edukacja};{polityka}"));
			assertEquals(2, MessageMock.contentCount("{edukacja;polityka}"));
			assertEquals(3, MessageMock.contentCount("{edukacja;polityka;redukcja}"));
			assertEquals("edukacja", MessageMock.readContent(0, "{edukacja;polityka}"));
			String cont = MessageMock.readContent(1, "{sub;user}{{2022-02-01 00:00:00;informacja informacja};edukacja;polityka}");
			assertNotEquals("{2022-02-01 00:00:00;informacja informacja}", cont, "function build to read payload content");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	void testTrunc() {
		try {
			assertEquals("polityka", TransitibleData.truncBrackets("{polityka}"));
			assertEquals("{polityka}", TransitibleData.truncBrackets("{{polityka}}"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	void testRequests() {
		assertEquals(Requests.SUB, Requests.toRequest("sub"));
		assertThrows(NoSuchElementException.class, ()->Requests.toRequest("a7bsfdfdererb"));
		assertEquals(Requests.NEWINFO, Requests.toRequest("newinfo"));
	}
}
