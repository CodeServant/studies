package dicserver.langServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import dicserver.protocol.*;

public class LangServer {
	protected int listeningPort;
	protected Translator translator;

	/**
	 * Object representation od language server.
	 * 
	 * @param language
	 * @param listeningPort
	 * @throws Exception
	 */
	public LangServer(String language, int listeningPort) throws Exception {
		translator = new Translator(language);
		if (listeningPort < 1024)
			throw new Exception("reserved port number");
		this.listeningPort = listeningPort;
		translator = new Translator(language);
	}

	/**
	 * Serves the given query to a proper client.
	 * 
	 * @param query
	 * @throws QueryException
	 * @throws IOException
	 */
	public void serve(ServerQuery query) throws QueryException, IOException {
		String translation = translator.translate(query.getWord());
		ServerResponse response;
		response = new ServerResponse(translation);

		Socket socket = new Socket(query.getClientAddress(), query.getReceivingPort());
		OutputStream socOut = socket.getOutputStream();
		response.send(socOut);
		socOut.close();
	}

	/**
	 * Listent to and serve the incoming connection.
	 * 
	 * @throws IOException
	 * @throws QueryException
	 */
	public void listen() throws IOException, QueryException {
		while (true) {
			ServerSocket servSocket = new ServerSocket(this.listeningPort);
			Socket smSocket = servSocket.accept();
			ServerQuery query = new ServerQuery(smSocket.getInputStream());
			this.serve(query);
			servSocket.close();
		}
	}

	public Translator getTranslator() {
		return this.translator;
	}
}
