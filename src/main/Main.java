package main;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import tribot.Worker;
import twitter4j.PagableResponseList;
import twitter4j.RelatedResults;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserMentionEntity;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import db.workerThread;

public class Main {
	
	private Worker tribot;
	//Lägg till clazzbot;
	Twitter twitter;
	Hashtable<String, Long> lastTweetID;
	
	public Main(boolean fillDB){
		try {
		Class.forName("com.mysql.jdbc.Driver");
		if(fillDB){
			new workerThread();	
		}
		tribot = new Worker();

		run();
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void run() {
		// Initialize the bot. This takes a few secounds
				try {
					initiate();
				} catch (LangDetectException | TwitterException e) {
					// This is supposed to be the only place where the bot can crash
					System.err.println("Failed to initiate");
					e.printStackTrace();
				}
				System.out.println("start");
				findResponse(tribot.newTweet("I want to have some nice things"));
				/*
				Set<String> friends = lastTweetID.keySet();
				int numberOfFriends = friends.size();
				
				while (true) {
					try {
						for (String userName : friends) {
							ResponseList<Status> timeline;
							timeline = twitter.getUserTimeline(userName);
							if (timeline.get(0).getId() <= lastTweetID.get(userName)) {
								continue;
							}
							lastTweetID.put(userName, timeline.get(0).getId());
							handleNewTweet(userName);
						}

						if (twitter.getFriendsList("read521", -1).size() > numberOfFriends) {
							updateLastTweetID();
						}
					} catch (TwitterException e) {
						e.printStackTrace();
						continue;
					}
					
					//Only check every 15 minutes
					long timer = System.currentTimeMillis();
					while((timer+900000)>System.currentTimeMillis()){
						//Do nothing
					}
					
				}*/
			}

	/**
	 * This is the the master method for handeling new tweets from the bots
	 * friends.
	 * 
	 * @param userName
	 *            The username of the twitteraccount
	 */
	public void handleNewTweet(String userName) {

		String tweet;
		try {
			ResponseList<Status> timeline = twitter.getUserTimeline(userName);
			tweet = timeline.get(0).getText();
		} catch (TwitterException e) {
			e.printStackTrace();
			tweet = "NaN";
		}
		if (checkEnglish(tweet)) {
			 TreeMap<String, Integer> bestAnswers = tribot.newTweet(tweet);
			String response = findResponse(bestAnswers);
			//postResponse(response, userName);
			System.out.println(response); //Use this to read here instead of posting
		}
	}
	
	
			private String findResponse(TreeMap<String, Integer> bestAnswers) {
				Map.Entry<String, Integer> sugestion;
				String twitterId;
				boolean run = true;
		while(!bestAnswers.isEmpty() && run){
			sugestion = bestAnswers.pollFirstEntry();
			twitterId = sugestion.getKey();
			try {
				RelatedResults relatedResults = twitter.getRelatedResults(new Long(twitterId));
				ResponseList<Status> replys = relatedResults.getTweetsWithReply();
				for(Status s : replys){
					System.out.println(s.getText());
					run = false;
				}
				/*List<Status> mentionStatuts = null;
			      mentionStatuts = twitter.getMentionsTimeline();
			      for( Status status1 : mentionStatuts){
			      if( status.getId() == status1.getInReplyToStatusId()){*/
			} catch (NumberFormatException | TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "Awesome!";
	}

			/**
			 * Initializes the language detection library and twitter.
			 * 
			 * @throws TwitterException
			 */
			private void initiate() throws LangDetectException, TwitterException {
				// Language Detection
				DetectorFactory.loadProfile("\\profiles");

				// Twitter
				twitter = TwitterFactory.getSingleton();
				lastTweetID = new Hashtable<String, Long>();
				updateLastTweetID();
			}

		
	

private void updateLastTweetID() throws TwitterException {
	PagableResponseList<User> friends = twitter.getFriendsList("read521",
			-1);
	for (int i = 0; i < friends.size(); i++) {
		String userName = friends.get(i).getScreenName();
		ResponseList<Status> timeline = twitter.getUserTimeline(userName);
		lastTweetID.put(userName, timeline.get(0).getId());
	}

}

private boolean checkEnglish(String text) {
	try {
		Detector detector = DetectorFactory.create();
		detector.append(text);
		if (detector.detect().equals("en")) {
			return true;
		}
	} catch (LangDetectException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("Ej engelska bekräftat");
	return false;
}

private void postResponse(String response, String userName) {
	try {
		ResponseList	<Status> timeline = twitter.getUserTimeline(userName);
		twitter.updateStatus(new StatusUpdate("@"+userName + " " + response).inReplyToStatusId(timeline.get(0).getId()));
	} catch (TwitterException e) {
		e.printStackTrace();
	}
}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main(false);
		//new workerThread();
	}

}
