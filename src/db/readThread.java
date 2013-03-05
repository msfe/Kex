package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class readThread extends Thread {

	private Connection connect = null;

	public readThread() {
		try {
			// Init DB
			connect = DriverManager.getConnection("jdbc:mysql://localhost/kex?"
					+ "user=worker&password=workerpw");
			test();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static final String tweet = "I want to be a soccer player";
	
	public void test() throws SQLException {

		LinkedList<String> hits = new LinkedList<String>();
		try {
			Statement statement = connect.createStatement();
			ResultSet r = statement
					.executeQuery("SELECT * FROM kex.trimap");
			while (r.next()) {
				if(tweet.contains(r.getString("WORDS"))){
					hits.add(r.getString("INTERNALID"));
				}
			}
			for(String InternalID: hits){
				statement = connect.createStatement();
				r = statement
						.executeQuery("SELECT TWEET FROM kex.tweets WHERE INTERNALID=" + InternalID);
				r.next();
				System.out.println(r.getString("Tweet"));
			}
			
			System.out.println("done!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
