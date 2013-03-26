package tribot;

public class Triple {

	private String words;
	private String tweet;
	private String id;

	public Triple(String words, String tweet, String id) {
		this.words = words;
		this.tweet = tweet;
		this.id = id;
	}

	public String getWords() {
		return words;
	}

	public String getTweet() {
		return tweet;
	}

	public String getId() {
		return id;
	}
}
