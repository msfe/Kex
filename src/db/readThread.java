package db;

import java.sql.Connection;
import java.sql.DriverManager;
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

	
	
	public LinkedHashSet<Triple> TribotCaller(String tweet){

		Hashtable<String,String> hits = new Hashtable<String,String>();
		LinkedHashSet<Triple> resultSet = new LinkedHashSet<Triple>();
		try {
			Statement statement = connect.createStatement();
			ResultSet r = statement
					.executeQuery("SELECT * FROM kex.trimap");
			while (r.next()) {
				if(tweet.contains(r.getString("WORDS"))){
					hits.put(r.getString("INTERNALID"),r.getString("WORDS"));
				}
			}
			for(String InternalID: hits.keySet()){
				statement = connect.createStatement();
				r = statement
						.executeQuery("SELECT TWEET FROM kex.tweets WHERE INTERNALID=" + InternalID);
				r.next();
				resultSet.add(new Triple(hits.get(InternalID), r.getString("Tweet"), InternalID));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultSet;
	}
}
