package dicserver.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Message which the managing server is makineg to Language Server.
 * 
 */
public class ServerQuery extends Query {
	protected String word;
	protected InetAddress clientAddress;

	/**
	 * Port on which client wait for answer.
	 */
	protected int receivePort;
	private final static String payloadRegex = "\\{\"(.*)\",([\\d\\:\\.]+),(\\d+)\\}";

	public ServerQuery(String word, InetAddress clientAddress, int receivePort) throws QueryException {
		final int minPort = 1024;
		final int maxPort = 65535;
		if (!(receivePort >= minPort && receivePort <= maxPort))
			throw new QueryException("wrong port number");
		if (!word.matches("[\\w\\sóÓłŁęĘśŚąĄŻżŹźćĆńŃ]+"))
			throw new QueryException("word wrong format");
		this.word = word;
		this.clientAddress = clientAddress;
		this.receivePort = receivePort;
	}

	/**
	 * Created ServerQuery from input stream.
	 * 
	 * @param inStream
	 * @throws QueryException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public ServerQuery(InputStream inStream) throws QueryException, UnknownHostException, IOException {
		this.load(inStream);
	}

	@Override
	public void load(InputStream inStream) throws IOException, QueryException, UnknownHostException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		String line = reader.readLine();
		Pattern pattern = Pattern.compile(this.payloadRegex);
		Matcher matcher = pattern.matcher(line);
		if (!matcher.matches())
			throw new QueryException("incoming data wrongly formatted");
		this.clientAddress = InetAddress.getByName(matcher.group(2));
		this.word = matcher.group(1);
		this.receivePort = Integer.parseInt(matcher.group(3));
		reader.close();
	}

	public String toString() {
		return "{\"" + this.word + "\"," + this.clientAddress.getHostAddress() + "," + this.receivePort + "}";
	}

	public String getWord() {
		return this.word;
	}

	public InetAddress getClientAddress() {
		return this.clientAddress;
	}

	public int getReceivingPort() {
		return this.receivePort;
	}

	public boolean hasWord() {
		return word.length() > 0;
	}

	@Override
	protected String getPayloadRegex() {
		return this.payloadRegex;
	}
}
