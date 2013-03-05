package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.LinkedList;

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

	
	
	public Hashtable<String, String> TribotCaller(String tweet){

		LinkedList<String> hits = new LinkedList<String>();
		Hashtable<String, String> resultTable = new Hashtable<String, String>();
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
				resultTable.put(InternalID,(r.getString("Tweet")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultTable;
	}
}
