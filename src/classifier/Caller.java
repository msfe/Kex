package classifier;
import java.util.Arrays;
import java.util.Map;

import com.google.code.uclassify.client.UClassifyClient;
import com.google.code.uclassify.client.UClassifyClientFactory;
import com.uclassify.api._1.responseschema.Classification;


public class Caller {
    
    //ReadKEY
   String readKeyNumber;
   String user; 
    public Caller(){
    	readKeyNumber = "CKIVxyPCaz7VUZCiMnJbEIpSOnE";//"U2L5HeL3uGgFd38lAhk1mHkh8xA"; //////readKey h�rdkodat in
    	user = "uclassify";
    }
    
    
    public String categorizeFirst(String text){
    	 final UClassifyClientFactory factory = UClassifyClientFactory.newInstance(readKeyNumber, null);
    	    final UClassifyClient client = factory.createUClassifyClient();
    	    String classifier = "Topics";
    	    return bestMatch((client.classify(user, classifier, Arrays.asList(text))));         
    }
    
    public String categorizeSecound(String text, String classifier){
   	 final UClassifyClientFactory factory = UClassifyClientFactory.newInstance(readKeyNumber, null);
   	    final UClassifyClient client = factory.createUClassifyClient();
   	    return bestMatch((client.classify(user, classifier +" Topics", Arrays.asList(text))));         
   }
    
  //Tar ut den kategori som f�tt b�st tr�ff. Leverar n�sta klassifierares namn
  		private String bestMatch(Map<String, Classification> classifications) {
  			
  			double h�gstProcent = 0;
  			String kategorin = "";
  			
  			for(String text : classifications.keySet()) {
  			Classification classification = classifications.get(text);
  			
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