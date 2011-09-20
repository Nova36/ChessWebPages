package Servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.data.xy.XYSeries;

import Graphs.LineGraph;

/**
 * Servlet implementation class Statistics
 */
public class Statistics extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String dbUrl = "jdbc:mysql://chess.neumont.edu:3306/learningchess";
	public static final String dbClass = "com.mysql.jdbc.Driver";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Statistics() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		final XYSeries WinRatioOverTime = new XYSeries("WinRatioOverTime");
		XYSeries AverageMovesPerGame = new XYSeries("Average Moves Per Game");

		// WinRatioOverTime.add(1.0, Math.random() * 15);
		// WinRatioOverTime.add(2.0, Math.random() * 45);
		// WinRatioOverTime.add(3.0, Math.random() * 100);
		// WinRatioOverTime.add(4.0, Math.random() * 210);
		// WinRatioOverTime.add(5.0, Math.random() * 300);
		// WinRatioOverTime.add(6.0, Math.random() * 500);
		Calendar today = Calendar.getInstance();

		Calendar startingDate = Calendar.getInstance();
		startingDate.set(Calendar.DAY_OF_MONTH, 1);
		startingDate.set(Calendar.MONTH, Calendar.SEPTEMBER);
		startingDate.set(Calendar.YEAR, 2011);

		long startingMillisecond = startingDate.getTimeInMillis();
		long todayMillisecond = today.getTimeInMillis();

		long difference = todayMillisecond - startingMillisecond;
		long step = difference / 20;
		int c = 1;
		for (long i = startingMillisecond; i < todayMillisecond; i += step) {
			Date first = new Date(i);
			Date second = new Date(i + step);
			int gamesWon = getGamesWon(first, second);
			int totalGames = getTotalGamesPlayed(first, second);
			double winRatio = (double) totalGames == 0 ? 0 : (double) gamesWon / (double) totalGames;
			WinRatioOverTime.add(c, winRatio);
			AverageMovesPerGame.add(c, getAverageMovesPerGame(first,second));
			c++;
			
		}

		new LineGraph(WinRatioOverTime, getServletContext().getRealPath("."), "WinRatioOverTime");
		 new LineGraph(AverageMovesPerGame, getServletContext().getRealPath("."), "AverageMovesPerGame");
		response.sendRedirect("Statistics.jsp");
	}

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private int getGamesWon(Date begin, Date end) {
		int count = 0;
		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "Ch3ssCh3ss");

			PreparedStatement stmt = con.prepareStatement("select count(*) from history where datePlayed between ? and ? and winnerType = '3'");
			stmt.setDate(1, begin);
			stmt.setDate(2, end);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	private int getTotalGamesPlayed(Date begin, Date end) {
		int count = 0;
		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "Ch3ssCh3ss");

			PreparedStatement stmt = con.prepareStatement("select count(*) from history where datePlayed between ? and ?");
			stmt.setDate(1, begin);
			stmt.setDate(2, end);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	private int getAverageMovesPerGame(Date begin, Date end) {
		int count = 0;
		try {
			Class.forName(dbClass);
			Connection con = DriverManager.getConnection(dbUrl, "root", "Ch3ssCh3ss");

			PreparedStatement stmt = con.prepareStatement("select avg(moveCount) from history where datePlayed between ? and ?");
			stmt.setDate(1, begin);
			stmt.setDate(2, end);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

}
