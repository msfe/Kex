package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

/**
 * This is the worknig thread for the DB, this will be the only thread with the
 * rights to write into the DB
 * 
 * @author Mattias
 * 
 */
public class workerThread extends Thread {

	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
	JSONObject json;
	Twitter twitter = TwitterFactory.getSingleton();

	public workerThread() {

		try {
			// Init DB
			connect = DriverManager.getConnection("jdbc:mysql://localhost/kex?"
					+ "user=worker&password=workerpw");
			// Init Language detection
			try {
				DetectorFactory.loadProfile(".\\profiles");
			} catch (LangDetectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			runTwitterStream();

		} catch (SQLException e) {

		}

	}

	private void runTwitterStream() {
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				try {
					parseJSON(status);
					if (checkEnglish(json.getString("tweet"))) {
						TweetToDB(json.getString("strid"),
								json.getString("tweet"),
								json.getInt("followers_count"));
					}
				} catch (SQLException | JSONException e) {
				}

			}

			@Override
			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:"
						+ numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId
						+ " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}

			@Override
			public void onStallWarning(StallWarning arg0) {

			}
		};
		twitterStream.addListener(listener);
		twitterStream.sample();
	}

	private void parseJSON(Status status) {

		try {
			json = new JSONObject();
			json.accumulate("strid", String.valueOf(status.getId()));
			String text = cleanTweet(status.getText());
			json.accumulate("tweet", text);
			json.accumulate("followers_count", status.getUser()
					.getFollowersCount());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private String cleanTweet(String text) {
		String[] words = text.split(" ");
		LinkedList<String> ok = new LinkedList<String>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			if (words[i].contains("@")) {
				continue;
			}
			if (words[i].contains("#")) {
				continue;
			}
			if (words[i].toLowerCase().startsWith("http")) {
				continue;
			}

			ok.addLast(words[i]);
		}
		if (ok.size() > 2) {
			for (String word : ok) {
				sb.append(word + " ");
			}
			return sb.toString();
		}
		return "";
	}

	public void TweetToDB(String twitterID, String tweet, int followers)
			throws SQLException {
		int InternalId = createObjectInTweets(twitterID, tweet, followers);
		if (InternalId == 0) {
			return;
		}
		createTRIMap(InternalId, tweet);

	}

	private void createTRIMap(int internalId, String tweet)
			throws SQLException {
		String[] strParts = tweet.split(" ");
		int count = 0;
		while (count < strParts.length - 2) {
			preparedStatement = connect
					.prepareStatement("insert into kex.trimap values (default, ?, ?)");
			preparedStatement.setInt(1, internalId);
			preparedStatement.setString(2, strParts[count] + " "
					+ strParts[count + 1] + " " + strParts[count + 2]);
			preparedStatement.executeUpdate();
			// System.out.println(strParts[count] + " " + strParts[count+1] +
			// " " + strParts[count+2]);
			count++;
		}
	}

	private int createObjectInTweets(String twitterID, String tweet, int followers)
			throws SQLException {
		// Check that this tweet aint in dataBase
		Statement statement = connect.createStatement();
		
		ResultSet r = statement
				.executeQuery("SELECT InternalID FROM kex.tweets where TwitterID = "
						+ twitterID);
		try {
			r.next();
			r.getString("InternalID");
			//System.err.println("Tweet already in db");
			return 0; //Tweet allready exsists in DB
		} catch (Exception e) {
			//Will end up here if it does not exsist in DB
		}

		preparedStatement = connect
				.prepareStatement("insert into kex.tweets values (default, ?, ?, ?)");
		// "twitterID, tweet, followers tweets from kex.tweets");
		// Parameters start with 1
		preparedStatement.setString(1, twitterID);
		preparedStatement.setString(2, tweet);
		preparedStatement.setLong(3, followers);
		preparedStatement.executeUpdate();

		// Get internal ID
		statement = connect.createStatement();
		r = statement
				.executeQuery("SELECT InternalID FROM kex.tweets where TwitterID = "
						+ twitterID);
		r.first();
		int internalID = (r.getInt("InternalID"));
		return (internalID);

	}

	// TODO don't duplicate code
	private boolean checkEnglish(String text) {
		try {
			Detector detector = DetectorFactory.create();
			detector.append(text);
			if (detector.detect().equals("en")) {
				return true;
			}
		} catch (LangDetectException e) {

		}
		// System.out.println("Ej engelska bekräftat");
		return false;
	}

}
