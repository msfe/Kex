package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.LinkedHashSet;

import tribot.Triple;

public class readThread extends Thread {

	private Connection connect = null;

	public readThread() {
		try {
			// Init DB
			connect = DriverManager.getConnection("jdbc:mysql://localhost/kex?"
					+ "user=worker&password=workerpw");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public LinkedHashSet<Triple> TribotCaller(String tweet) {

		Hashtable<String, String> hits = new Hashtable<String, String>();
		LinkedHashSet<Triple> resultSet = new LinkedHashSet<Triple>();
		try {
			Statement statement = connect.createStatement();
			ResultSet r = statement.executeQuery("SELECT * FROM kex.trimap");
			while (r.next()) {
				if (tweet.contains(r.getString("WORDS"))) {
					hits.put(r.getString("INTERNALID"), r.getString("WORDS"));
				}
			}
			for (String InternalID : hits.keySet()) {
				statement = connect.createStatement();
				r = statement
						.executeQuery("SELECT TWEET,TWITTERID FROM kex.tweets WHERE INTERNALID="
								+ InternalID);
				r.next();
				resultSet.add(new Triple(hits.get(InternalID), r
						.getString("Tweet"), r.getString("TWITTERID")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public Hashtable<String, String> ClassbotCaller(String category) {

		Hashtable<String, String> resultSet = new Hashtable<String, String>();
		try {
			PreparedStatement statement = connect
					.prepareStatement("SELECT TWEET,TWITTERID FROM kex.tweets WHERE CATEGORYTWO = ? OR CATEGORY1 = ?");
			statement.setString(1, category);
			statement.setString(2, category);
			ResultSet r = statement.executeQuery();
			while (r.next()) {
				resultSet.put(r.getString("TWITTERID"), r.getString("Tweet"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultSet;
	}

}
