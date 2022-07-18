package dicserver.protocol;

import java.io.*;
import java.util.regex.*;

/**
 * Message which the client is making for managing server.
 * 
 */
public class ClientQuery extends Query {
	protected String word;
	protected String langCode;
	protected int receivePort;
	protected static final String payloadRegex = "\\{\"(.*)\",\"(\\w{2})\",(\\d+)\\}";

	public ClientQuery(String word, String langCode, int receivePort) throws QueryException {

		final int minPort = 1024;
		final int maxPort = 65535;
		if (!(minPort <= receivePort && maxPort >= receivePort))
			throw new QueryException("wrong port number");
		if (!word.matches("[\\w\\sóÓłŁęĘśŚąĄŻżŹźćĆńŃ]+"))
			throw new QueryException("word wrong format");
		if (!langCode.matches("\\w{2}"))
			throw new QueryException("language code don't match 2 letters");

		this.word = word;
		this.langCode = langCode;
		this.receivePort = receivePort;
	}

	/**
	 * Attempts to make ClientQuery prom InputStream.
	 * 
	 * @param inStream
	 * @throws QueryException
	 * @throws IOException
	 */
	public ClientQuery(InputStream inStream) throws QueryException, IOException {
		load(inStream);
	}

	public String toString() {
		return "{\"" + this.word + "\",\"" + this.langCode + "\"," + this.receivePort + "}";
	}

	@Override
	/**
	 * Not tested.
	 */
	public void load(InputStream inStream) throws QueryException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		String line = reader.readLine();
		Pattern pattern = Pattern.compile(this.payloadRegex);
		Matcher matcher = pattern.matcher(line);

		if (!isQuery(line.trim()))
			throw new QueryException("incoming data wrongly formatted");
		matcher.find();
		this.word = matcher.group(1);
		this.langCode = matcher.group(2);
		this.receivePort = Integer.parseInt(matcher.group(3));
		reader.close();
	}

	public String getLangCode() {
		return this.langCode;
	}

	public String getWord() {
		return this.word;
	}

	public int getReceivePort() {
		return this.receivePort;
	}

	@Override
	protected String getPayloadRegex() {
		return this.payloadRegex;
	}
}
