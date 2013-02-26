package classifier;
import java.util.Map;

import com.uclassify.api._1.responseschema.Classification;


public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//H�r anges texten vi ska klassifiera, vilken klassifierare vi ska anv�nda och vad usern till den heter.
		//Caller.categorize("I whistled to across campus. He looked up and knew it was me!", "Topics", "uclassify");
	
		String text = "I whistled to across campus. He looked up and knew it was me!";
		String classifier = "Topics";
		String user = "uclassify";
		//Caller caller = new Caller();
		Map<String, Classification> result = Caller.categorizeString(text, classifier, user);
		
		String cat =bestMatch(result);
		//printResult(result);
		classifier = cat+" Topics";
		result = Caller.categorizeString(text, classifier, user);
		printResult(result);
	}


	/**
	* Prints the result.
	* 
	* @param classifications the classifications
	*/
		private static void printResult(Map<String, Classification> classifications) {
		

		double h�gstProcent = 0;
		
		String kategorin = "";
		
		System.out.println("================ Classifications ==================");
		for(String text : classifications.keySet()) {
		Classification classification = classifications.get(text);
		System.out.println(text);
		System.out.println("====================");
		
		for (com.uclassify.api._1.responseschema.Class clazz : classification.getClazz()) {
		System.out.println(clazz.getClassName() + ":" + clazz.getP());
		
		
		}
		//loop som tar ut den kategori som f�tt b�st tr�ff. G�r att ta bort sen.
		for (com.uclassify.api._1.responseschema.Class clazzy : classification.getClazz()) {
			String namn = clazzy.getClassName();
			double procent = clazzy.getP();
			if (procent > h�gstProcent){
				h�gstProcent = procent;
				kategorin = namn;
					}
				}
			System.out.println("");
			System.out.println("h�gst v�rde:");
			System.out.println(kategorin);
			}
		}
		
		//Tar ut den kategori som f�tt b�st tr�ff. Leverar n�sta klassifierares namn
		private static String bestMatch(Map<String, Classification> classifications) {
			
			double h�gstProcent = 0;
			String kategorin = "";
			
			for(String text : classifications.keySet()) {
			Classification classification = classifications.get(text);
			
			for (com.uclassify.api._1.responseschema.Class clazz : classification.getClazz()) {
			System.out.println(clazz.getClassName() + ":" + clazz.getP());
			
			
			}
			//loop som tar ut den kategori som f�tt b�st tr�ff.
			for (com.uclassify.api._1.responseschema.Class clazzy : classification.getClazz()) {
				String namn = clazzy.getClassName();
				double procent = clazzy.getP();
				if (procent > h�gstProcent){
					h�gstProcent = procent;
					kategorin = namn;
					}
				}
			}
			
			//f�r att f�r r�tt klassifierare:
			if(kategorin.equals("Arts"))return("Art"); 
			if(kategorin.equals("Computers"))return("Computer");
			if(kategorin.equals("Games"))return("Game");
			if(kategorin.equals("Sports"))return("Sport");
			
			
			return(kategorin);
		}
}