package subscribers.Zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import static subscribers.Zad1.Requests.*;

public class Server {
	Map<String, Subscriber> subscribers = new TreeMap<String, Subscriber>();

	InetSocketAddress thisAddress;

	public Server(InetSocketAddress address) {
		this.thisAddress = address;
	}

	public static void main(String[] args) {
		Server serv = new Server(new InetSocketAddress("localhost", 50001));
//		TreeSet<Topic> topics = new TreeSet<>();
//		topics.add(new Topic("polityka"));
//		topics.add(new Topic("gospodarka"));
//		
//		Information info = new Information("some info",topics);
//		try {
//			Information.addInformation(info);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		serv.listen();
	}

	public void addSubscribers(Set<Subscriber> subscribers) {
		for (Subscriber sub : subscribers) {
			addSubscriber(sub);
		}
	}

	public void addSubscriber(Subscriber subscriber) {
		this.subscribers.put(subscriber.username, subscriber);
	}

	public void delSubscriber(String user) {
		this.subscribers.remove(user);
	}

	/**
	 * Make the server to listen for incomming conection an serve the clients.
	 */
	public void listen() {
		ServerSocketChannel serverChannel;
		try {
			serverChannel = ServerSocketChannel.open();
			serverChannel.socket().bind(thisAddress);
			serverChannel.configureBlocking(false);
			Selector selector = Selector.open();
			SelectionKey servKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);

			while (true) {
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iter = keys.iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					iter.remove();

					if (key.isAcceptable()) {
						SocketChannel cc = serverChannel.accept();
						cc.configureBlocking(false);
						cc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

						cc.keyFor(selector).attach(new Session());

						continue;
					}

					if (key.isReadable()) {
						this.whileRead(key);
						continue;
					}

					if (key.isWritable()) {
						this.whileWrite(key);
						continue;
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void whileRead(SelectionKey key) throws IOException {
		Session session = (Session) key.attachment();

		SocketChannel cc = (SocketChannel) key.channel();
		ByteBuffer buf = ByteBuffer.allocate(2000);
		final int bytes = cc.read(buf);
		buf.flip();

		if (session.isReading()) {
			session.addQueryData(buf.asCharBuffer().toString());
		}
	}

	protected void whileWrite(SelectionKey key) throws IOException {
		SocketChannel cc = (SocketChannel) key.channel();
		final Session session = (Session) key.attachment();
		if (!session.isReading()) {
			final String message = this.respond(session);

			if (!message.isBlank()) {
				ByteBuffer buf = ByteBuffer.allocate(message.length() * 2);
				CharBuffer chbuf = buf.asCharBuffer();
				chbuf.append(message);
				chbuf.flip();
				buf.limit(chbuf.limit() * 2);
				cc.write(buf);
				cc.close();
			}
		}
	}

	protected String respond(Session session) {
		SrvReceiver receiver = new SrvReceiver(session.getReceivedMessage());
		String user = null;
		try {
			user = receiver.user();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Requests req = null;
		try {
			req = receiver.request();
		} catch (Exception e) {
			e.printStackTrace();
		}
		switch (req) {
		case SUB:
			try {
				return this.subscribeUser(user, receiver.justTopics()).getMessage();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new Response(Status.WRONG_REQUEST).getMessage();
		case FETCHINFO:
			return this.sendInformations(user).getMessage();
		case NEWINFO:
			try {
				return this.joinNewInfo(user, receiver.infoWithTopics()).getMessage();
			} catch (Exception e1) {
				e1.printStackTrace();
				return new Response(Status.WRONG_REQUEST).getMessage();
			}
		case UNSUB:
			return unsubscribe(user).getMessage();
		case DELTOPIC:
			try {
				return delTopics(user, receiver.justTopics()).getMessage();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new Response(Status.WRONG_REQUEST).getMessage();
		case ADDTOPIC:
			try {
				return addTopics(user, receiver.justTopics()).getMessage();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new Response(Status.WRONG_REQUEST).getMessage();
		case FETCHTOPIC:
			try {
				return fetchTopics().getMessage();
			} catch (Exception e) {
				e.printStackTrace();
			}
			new Response(Status.WRONG_REQUEST).getMessage();
		}
		return "unnown server error";

	}

	protected Response subscribeUser(String user, Set<Topic> topics) {
		this.addSubscriber(new Subscriber(user, topics));
		System.out.println(user+" subscribed "+topics.toString());
		return new Response(Status.OK); // test połączenia
	}

	protected Response sendInformations(String user) {
		if (subscribers.containsKey(user)) {
			final Subscriber subscriber = subscribers.get(user);
			Calendar cal = subscriber.lastFetchedDate();
			System.out.println(user +" last fetched date "+cal);
			List<TransitibleData> infoList;

			if (cal != null)
				infoList = (List<TransitibleData>) (List<?>) Information.getInformations(subscriber.getTopics(),
						subscriber.lastFetchedDate());
			else {
				infoList = (List<TransitibleData>) (List<?>) Information.getAll(subscriber.getTopics());
			}
			System.out.println(subscriber.getTopics());
			System.out.println(infoList.toString());
			Response resp;
			resp = infoList.size() > 0 ? new Response(Status.OK) : new Response(Status.NO_NEW_INFO);
			resp.addTransDatas(infoList);
			subscriber.bumpFetchedDate();
			System.out.println(resp.getMessage());
			return resp;
		} else
			return new Response(Status.AUTH_ERROR);
	}

	protected Response joinNewInfo(String user, Information informations) {

		try {
			Information.addInformation(informations);
			return new Response(Status.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(Status.WRONG_REQUEST);
		}
		
	}

	protected Response unsubscribe(String user) {
		if (subscribers.containsKey(user)) {
			this.delSubscriber(user);
			return new Response(Status.OK);
		} else
			return new Response(Status.AUTH_ERROR);
	}

	protected Response delTopics(String user, Set<Topic> topics) {
		topics.forEach(Information::delTopic);
		return new Response(Status.OK);
	}

	protected Response addTopics(String user, Set<Topic> topics) {
		Information.addTopicsToDB(topics);
		return new Response(Status.OK);
	}

	protected Response fetchTopics() {
		Response resp = new Response(Status.OK);
		resp.addTransDatas(new ArrayList<TransitibleData>(Information.getAllTopics()));
		return resp;
	}

}
