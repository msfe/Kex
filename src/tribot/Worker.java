package tribot;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import db.readThread;

public class Worker {

	private readThread read;
	private EditingDistance distance;

	public Worker() {
		read = new readThread();
		distance = new EditingDistance();
	}

	public HashMap<String, Integer> newTweet(String tweet) {
		int startTweetA;
		int startTweetB;
		int lengthTweetA = tweet.length();
		String words;
		String compare ="";
		HashMap<String,Integer> map = new HashMap<String,Integer>();
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
		LinkedHashSet<Triple> result = read.TribotCaller(tweet);
		

		for (Triple triple : result) {
			// Find where the tweets match
			words = triple.getWords();
			startTweetA = tweet.indexOf(words);
			startTweetB = triple.getTweet().indexOf(words);
			
			
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
			
			distance.WFMatrix(tweet, compare);
			map.put(triple.getId(),distance.Distance(tweet, compare));
			
			/* Läsbar Output
			map.put(triple.getTweet(),distance.Distance(tweet, compare));
			*/

		}
		
        sorted_map.putAll(map);
        
		return map;

	}
	
	private class ValueComparator implements Comparator<String> {

	    Map<String, Integer> base;
	    public ValueComparator(HashMap<String, Integer> map) {
	        this.base = map;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(String a, String b) {
	        if (base.get(a) >= base.get(b)) {
	            return 1;
	        } else {
	            return -1;
	        } // returning 0 would merge keys
	    }
	}
}
