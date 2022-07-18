package dicserver.protocol;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class for queries, that indicates special message by specific party
 * in comunication.
 *
 */
public abstract class Query {
	protected abstract String getPayloadRegex();

	/**
	 * Loads Query from InputStream.
	 * 
	 * @param inStream
	 * @throws QueryException
	 * @throws IOException
	 */
	public abstract void load(InputStream inStream) throws QueryException, IOException;

	public abstract String toString();

	public Query() {
		super();
	}

	/**
	 * Creates Query from InputStream
	 * 
	 * @param inStream
	 * @throws QueryException
	 * @throws IOException
	 */
	public Query(InputStream inStream) throws QueryException, IOException {
		this.load(inStream);
	}

	/**
	 * Sends this query to the given output stream.
	 * 
	 * @param oStream
	 */
	public void send(OutputStream oStream) {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(oStream));
		String message = this.toString();
		try {
			writer.write(message, 0, message.length());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tests if tested text matches payloadRegex
	 * 
	 * @param tested
	 * @return
	 */
	public boolean isQuery(String tested) {
		String payloadRegex = this.getPayloadRegex();
		Pattern pattern = Pattern.compile(payloadRegex);
		Matcher matcher = pattern.matcher(tested);
		if (!matcher.matches())
			return false;
		return true;
	}

	public static boolean isLangCode(String code) {
		return code.matches("\\w{2}");
	}

}
