package tpo.cls4.main.java.zad;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet to prepare html table with data.
 */
@WebServlet("/server")
public class Server extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Server() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String meta = "<html><head><meta charset=\"UTF-8\"></head><body>";

		HttpSession session = request.getSession();
		StringBuilder bdr = (StringBuilder) session.getAttribute("cars");

		BufferedReader rdr = new BufferedReader(new StringReader(bdr.toString()));
		StringBuilder bdr2 = new StringBuilder();
		bdr2.append(meta);
		bdr2.append(tablePrepare(rdr, true));
		bdr2.append("</body></html>");
		PrintWriter out = getOutWriter(response.getOutputStream());
		out.println(bdr2);
		out.close();
	}
	
	/**
	 * Gets out Writer to the given stream and with default charset encoding.
	 * 
	 * @param oStream
	 * @return
	 */
	private PrintWriter getOutWriter(OutputStream oStream) {
		OutputStreamWriter osWr = new OutputStreamWriter(oStream, Charset.forName("UTF-8"));
		PrintWriter out = new PrintWriter(osWr, true);
		return out;
	}
	
	/**
	 * Changing data in format data1\tdata2\tdata3 to the html table.
	 * 
	 * @param inBuilder  data formated with tabs CSV style like
	 * @param firstnames the first row will be name of the item in column
	 * @return
	 */
	private StringBuilder tablePrepare(BufferedReader reader, boolean firstnames) {
		StringBuilder outBuilder = new StringBuilder();
		String tb = "table";
		String tr = "tr", th = "th", td = "td";
		outBuilder.append("<" + tb + " border=2px>");
		String read;
		try {
			int i = 0;
			while ((read = reader.readLine()) != null) {
				if (i == 0 && firstnames)
					outBuilder.append(tableRow(read, th));
				else
					outBuilder.append(tableRow(read, td));
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outBuilder;
	}

	private String oTag(String name) {
		return "<" + name + ">";
	}

	private String clTag(String name) {
		return "</" + name + ">";
	}

	private String tableRow(String line, String dataType) {
		String[] lines = line.split("\t");
		String output = "<tr>";
		for (String l : lines) {
			output += oTag(dataType) + l + clTag(dataType);
		}
		output += "</tr>";
		return output;
	}
}
