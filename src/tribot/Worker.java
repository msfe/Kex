package tribot;

import java.util.Hashtable;

import db.readThread;

public class Worker {

	private readThread read;
	String bestAnswer;

	public Worker() {
		read = new readThread();
		bestAnswer = "";
	}

	public String newTweet(String tweet) {
		Hashtable<String, String> result = read.TribotCaller(tweet);
		for (String key : result.keySet()) {
			System.out.println(result.get(key));
		}
		return bestAnswer;

	}

}
