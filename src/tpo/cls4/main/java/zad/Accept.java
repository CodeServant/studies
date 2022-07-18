package tpo.cls4.main.java.zad;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet that accepts the user request.<br>
 */
@WebServlet("/accept")
public class Accept extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Accept() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		String charset = "UTF-8";
		response.setContentType("text/html; charset=" + charset);
		
		ServletContext context = request.getServletContext();
		RequestDispatcher disp = context.getRequestDispatcher("/data");
		disp.include(request, response);
		
		disp = context.getRequestDispatcher("/server");
		disp.forward(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
