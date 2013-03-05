package tribot;

import java.util.LinkedHashSet;

import db.readThread;

public class Worker {

	private readThread read;
	String bestAnswer;

	public Worker() {
		read = new readThread();
		bestAnswer = "";
	}

	public String newTweet(String tweet) {
		LinkedHashSet<Triple> result = read.TribotCaller(tweet);

		
		for(Triple triple: result){
			System.out.println(triple.tweet);
			//Find where the tweets match
			
		}
		return bestAnswer;

	}

}
