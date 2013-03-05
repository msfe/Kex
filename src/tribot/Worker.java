package tribot;

import java.util.LinkedHashSet;

import org.apache.commons.lang3.StringUtils;

import db.readThread;

public class Worker {

	private readThread read;
	private EditingDistance distance;
	private String bestAnswer;

	public Worker() {
		read = new readThread();
		distance = new EditingDistance();
		bestAnswer = "";
	}

	public String newTweet(String tweet) {
		int startTweetA;
		int startTweetB;
		int postWordsA;
		int lengthTweetA = tweet.length();
		String words;
		String compare ="";
		LinkedHashSet<Triple> result = read.TribotCaller(tweet);

		for (Triple triple : result) {
			// Find where the tweets match
			words = triple.getWords();
			startTweetA = tweet.indexOf(words);
			startTweetB = triple.getTweet().indexOf(words);
			postWordsA = startTweetA + words.length();
			if(startTweetA>startTweetB){
				compare = StringUtils.leftPad(triple.getTweet(), startTweetA+(triple.getTweet().length()-startTweetB), '*');
				compare = compare.substring(0, lengthTweetA);
			}
			if(startTweetA<startTweetB){
				compare = triple.getTweet().substring(startTweetB-startTweetA);
				if (compare.length()>lengthTweetA){
					compare = compare.substring(0,lengthTweetA);
				}
			}

			

		}
		return bestAnswer;

	}
}
