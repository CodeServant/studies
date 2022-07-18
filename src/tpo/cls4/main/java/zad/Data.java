package tpo.cls4.main.java.zad;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet to get data from the servers drive.
 */
@WebServlet("/data")
public class Data extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Data() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//String charset = "UTF-8";
		//response.setContentType("text/html; charset=" + charset);
		
		HttpSession session = request.getSession();
		try {
			session.setAttribute("cars", dataReach(oracleDBConnection(), request.getParameter("car")));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private java.sql.Connection oracleDBConnection() throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		String pass = "oracle12";
		return DriverManager.getConnection("jdbc:oracle:thin:@db-oracle.pjwstk.edu.pl:1521:baza", "s18543", pass);
	}
	
	private java.sql.Connection mariaDBConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.mariadb.jdbc.Driver");
		String pass = "password";
		return DriverManager.getConnection("jdbc:mariadb://localhost/samochody", "root", pass);
	}

	private StringBuilder dataReach(java.sql.Connection con, String typ) throws SQLException {
		String sqlQ = "SELECT typ, marka, rokProd, spalanie FROM cars WHERE typ = '"+typ+"'";
		Statement stmt = con.createStatement();
		ResultSet res = stmt.executeQuery(sqlQ);
		StringBuilder returned = new StringBuilder();
		returned.append("marka\trokProd\tspalanie\n");
		while (res.next()) {
			returned.append(res.getString("marka") + "\t");
			returned.append(res.getInt("rokProd") + "\t");
			returned.append(res.getBigDecimal("spalanie") + "\n");
		}
		return returned;
	}

	
}
