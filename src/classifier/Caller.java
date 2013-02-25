package classifier;
import java.util.Arrays;
import java.util.Map;

import com.google.code.uclassify.client.UClassifyClient;
import com.google.code.uclassify.client.UClassifyClientFactory;
import com.uclassify.api._1.responseschema.Classification;


public class Caller {
    
    //ReadKEY
    String readKeyNumber;
    
    public Caller(){
    	readKeyNumber = "U2L5HeL3uGgFd38lAhk1mHkh8xA"; //readKey hårdkodat in
    }
    
    public Map<String, Classification> categorizeString(String text,String classifier,String user){
    	
    	 final UClassifyClientFactory factory = UClassifyClientFactory.newInstance(readKeyNumber, null);
    	    final UClassifyClient client = factory.createUClassifyClient();
    	 
    	    return (client.classify(user, classifier, Arrays.asList(text)));
	          
    }
}