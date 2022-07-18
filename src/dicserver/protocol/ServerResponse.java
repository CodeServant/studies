package dicserver.protocol;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The response Language Server is making to the client.
 * 
 */
public class ServerResponse extends Query {
	protected String translated;
	protected static final String payloadRegex = "\\{\"([\\w\\s]+)\"\\}";

	public ServerResponse() throws QueryException {

	}

	public ServerResponse(String translated) throws QueryException {

		if (translated != null) {
			if (!translated.matches("[\\w\\s]+"))
				throw new QueryException("not valid format");
			this.translated = translated;
		} else
			this.translated = "translation failed";
	}

	public ServerResponse(InputStream inStream) throws IOException, QueryException {
		load(inStream);
	}

	public String getTranslated() {
		return this.translated;
	}

	@Override
	public void load(InputStream inStream) throws IOException, QueryException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
		String line = reader.readLine();
		Pattern pattern = Pattern.compile(this.payloadRegex);
		Matcher matcher = pattern.matcher(line);
		if (!isQuery(line))
			throw new QueryException("incoming data wrongly formatted");
		matcher.find();
		this.translated = matcher.group(1);
		reader.close();
	}

	@Override
	public String toString() {
		return "{\"" + this.translated + "\"}";
	}

	@Override
	protected String getPayloadRegex() {
		return this.payloadRegex;
	}
}
