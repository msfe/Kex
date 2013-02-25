package classifier;
import java.util.Map;

import com.uclassify.api._1.responseschema.Classification;


public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Här anges texten vi ska klassifiera, vilken klassifierare vi ska använda och vad usern till den heter.
		//Caller.categorize("I whistled to across campus. He looked up and knew it was me!", "Topics", "uclassify");
	
		String text = "I whistled to across campus. He looked up and knew it was me!";
		String classifier = "Topics";
		String user = "uclassify";
		Caller caller = new Caller();
		Map<String, Classification> result = caller.categorizeString(text, classifier, user);
		printResult(result);
	        
	}


	/**
	* Prints the result.
	* 
	* @param classifications the classifications
	*/
		private static void printResult(Map<String, Classification> classifications) {
		System.out.println("================ Classifications ==================");
		for(String text : classifications.keySet()) {
		Classification classification = classifications.get(text);
		System.out.println(text);
		System.out.println("====================");
		for (com.uclassify.api._1.responseschema.Class clazz : classification.getClazz()) {
		System.out.println(clazz.getClassName() + ":" + clazz.getP());
				}
			}
		}
}
