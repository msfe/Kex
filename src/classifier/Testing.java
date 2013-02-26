package classifier;


public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String text = "I whistled to across campus. He looked up and knew it was me!";

		Caller caller = new Caller();
		String classifyed1 = caller.categorizeFirst(text);
		
		String classifyed2 = caller.categorizeSecound(text, classifyed1);
		System.out.println(classifyed1);
		System.out.println(classifyed2);
	}
}
