package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnection {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	
	public DatabaseConnection(){
		try {
		Class.forName("com.mysql.jdbc.Driver");
		//readDataBase();
		//new workerThread();
		new readThread();
	
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/kex?"
							+ "user=worker&password=workerpw");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery("select * from kex.tweets");
			writeResultSet(resultSet);

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("insert into kex.tweets values (default, ?, ?, ?)");
			// "twitterID, text, followers tweets from kex.tweets");
			// Parameters start with 1
			preparedStatement.setString(1, "000");
			preparedStatement.setString(2, "TestEmail");
			preparedStatement.setString(3, "3");
			preparedStatement.executeUpdate();

			preparedStatement = connect
					.prepareStatement("SELECT TWITTERID, TWEET, FOLLOWERS from kex.tweets");
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);

			// Remove again the insert comment
			preparedStatement = connect
					.prepareStatement("delete from kex.tweets where twitterid= ? ; ");
			preparedStatement.setString(1, "000");
			preparedStatement.executeUpdate();

			resultSet = statement
					.executeQuery("select * from kex.tweets");
			writeMetaData(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " "
					+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String twitterID = resultSet.getString("twitterID");
			String tweet = resultSet.getString("tweet");
			String followers = resultSet.getString("followers");
			System.out.print("TwitterID: " + twitterID);
			System.out.print("\tTweet: " + tweet);
			System.out.println("\tFollowers: " + followers);
		}
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

	public static void main(String[] args) throws Exception {
		new DatabaseConnection();
	}

}
