  import java.util.ArrayList;
import java.util.List;
import mathsEnumeration.*;

public class GenerateSets {
	
	int numberOfSubjects;
	int numberOfSecurityLevels;
	int numberOfObjects;
	int numberOfAccessModes;
	ArrayList<ArrayList<Integer>> subjectSets;
	ArrayList<ArrayList<Integer>> objectSets;
	ArrayList<ArrayList<Integer>> allRequests;
	
	GenerateSets(int numberOfSubjects,
	int numberOfSecurityLevels,
	int numberOfObjects,
	int numberOfAccessModes){
		this.numberOfSubjects=numberOfSubjects;
		this.numberOfSecurityLevels=numberOfSecurityLevels;
		this.numberOfObjects=numberOfObjects;
		this.numberOfAccessModes=numberOfAccessModes;
		generateSets();	
	}
	
	  public void generateSets(){
		subjectSets=Combination.getCombination(numberOfSubjects,numberOfSecurityLevels-1);
   		objectSets=Combination.getCombination(numberOfObjects,numberOfSecurityLevels-1);
   		List<Integer> sizeOfsets=new ArrayList<Integer>();
	    sizeOfsets.add(numberOfSubjects);
	    sizeOfsets.add(numberOfObjects);
	    sizeOfsets.add(numberOfAccessModes);
	    allRequests=Product.getProduct(sizeOfsets);
	  }
}
