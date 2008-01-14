import java.util.*;

import accesscontrol.*;
import accesscontrol.types.*;
import java.util.ArrayList;
import mathsEnumeration.PermutationGenerator;

public class VerifyAllConfigs{
	%include { sl.tom }
	%include { accesscontrol/Accesscontrol.Tom }
	//Policy to check
	Policy policy;
	//Generated sets
	GenerateSets generatedSets;
	// Flag for detecting a leakage
	boolean LeakageDetected=false;
	// Request creating a leakage
	RequestUponState CurrentRequestOfScenario;
  
  // Constructor
  public VerifyAllConfigs(int numberOfSubjects, int numberOfObjects, int numberOfSecurityLevels, int numberOfAccessModes, Policy p){
    this.generatedSets=new GenerateSets(numberOfSubjects,
	numberOfSecurityLevels,
	numberOfObjects,
	numberOfAccessModes);
    this.policy=p;
  }


/*

  // Fixed configuration for Subjects and Objects; PG generates all permutation of requests
  public int[] checkAllPermutationsOfRequests(ArrayList<Subject> Subjects, ArrayList<SecurityObject> Objects){
	  PermutationGenerator PG= new PermutationGenerator (generatedSets.allRequests.size());
      //foreach permutation
	  while (PG.hasMore ()) {
		  //get a permuation
		  int[] currentPermutation=PG.getNext();
		  //check this permutation of requests with the fixed configuration of Subjects and objects
		  if(!check(Subjects,Objects,currentPermutation)){
			  //return the current permutation if it generates a leakage 
			  return currentPermutation;
		  }
      System.out.println("for permutation :"+Arrays.toString(currentPermutation));
    }
	
    int[] rep={0};
    //return the array rep if no leakage is detected  
    return rep;
  }

 
public Request createRequest(ArrayList<Subject> Subjects,ArrayList<SecurityObject> Objects,ArrayList<Integer> requestIndexes){	  //create an access
      Access a=`access(Subjects.get(requestIndexes.get(0)),Objects.get(requestIndexes.get(1)),(aM(requestIndexes.get(2))),explicit());
      	  // create the corresponding request 	
      return `request(add(),a);  
}




  // For a fixed configuration of subjects and objects and a given permutation for requests  
  public boolean check(ArrayList<Subject> Subjects, ArrayList<SecurityObject> Objects, int[] permutationOfRequests){
	  //boolean to store the check result  
	  boolean result=true;
	  //Create an empty state
	  State M=`state(accesses(),accesses());
      //for each request whose index in "allRequests" is determined by "permutationOfRequests" 
	  for (int i = 0; i < permutationOfRequests.length; i++) {
		  //Get the element i from the list "permutationOfRequests" where it is represented by 3 indexes: the subject, the object and the access mode
		  //and generate the couple (request,state)  
		  RequestUponState rus=`rus(createRequest(Subjects,Objects,generatedSets.allRequests.get(permutationOfRequests[i])),M);
      	  // try to add the access to the state given the implementation of the policy
		  Object[] checkResult=TestAccess.check(rus,policy); 
		  //update the current state
		  M=(State)checkResult[0];
		  //set the result boolean verifying leakage detection
		  result=(Boolean)checkResult[1];
      }
	  //else generate a message and return true
	if (result) System.out.println("No information leakage detected");
    
    return result;
  }

*/
 /* //public void checkAllSets(int numberOfSubjects, int numberOfObjects, int numberOfSecurityLevels, int numberOfAccessModes){
  public boolean checkSpecificSets(int iss,int ios){
	boolean  result=true;
    //generateSets(numberOfSubjects,numberOfObjects,numberOfSecurityLevels,numberOfAccessModes);
//    generateSets();
    int indexSubjectSet=iss;
    int indexObjectSet=ios;
    System.out.println("current Subject Set :"+indexSubjectSet);
    System.out.println("current Object Set :"+indexObjectSet);
    ArrayList<Subject> Subjects=new ArrayList<Subject>();
    ArrayList<SecurityObject> Objects=new ArrayList<SecurityObject>();
    int i=0;
//     for (Iterator<Integer> iterator = (subjectSets.get(indexSubjectSet)).iterator(); iterator.hasNext();) {
//       Integer securityLevel = iterator.next();
    for (Integer securityLevel: generatedSets.subjectSets.get(indexSubjectSet)) {
      Subjects.add(`subject(i,sL(securityLevel)));
      i++;
    }
    i=0;
    for (Iterator<Integer> iterator = (generatedSets.objectSets.get(indexObjectSet)).iterator(); iterator.hasNext();) {
      Integer securityLevel =  iterator.next();
      Objects.add(`securityObject(i,sL(securityLevel)));
      i++;
    }

    int[] o=checkAllPermutationsOfRequests(Subjects,Objects);
    System.out.println("For subjects :\n"+Subjects);
    System.out.println("and objects :\n"+Objects);
    if (o.length==1){
      System.out.println("No leakage detected for all permutations");
    }else{
      LeakageDetected=true;
      System.out.println("Leakage detected for permutations :\n"+Arrays.toString(o));
      result=false;
    }

 

    return result;
  }

*/



  // check all possibilities
  public boolean checkAllSets(){
	 boolean result=true;
//    generateSets();
    //for each subject set
    for(int indexSubjectSet=0; indexSubjectSet<generatedSets.subjectSets.size();indexSubjectSet++){
    	//for each object set
    	for(int indexObjectSet=0; indexObjectSet<generatedSets.objectSets.size();indexObjectSet++){
        System.out.println("current Subject Set :"+indexSubjectSet);
        System.out.println("current Object Set :"+indexObjectSet);
        ArrayList<Subject> Subjects=new ArrayList<Subject>();
        ArrayList<SecurityObject> Objects=new ArrayList<SecurityObject>();
        
        //Create the configuration (subjects)
        int i=0;
        for (Iterator<Integer> iterator = (generatedSets.subjectSets.get(indexSubjectSet)).iterator(); iterator.hasNext();) {
          Integer securityLevel = iterator.next();
          Subjects.add(`subject(i,sL(securityLevel)));
          i++;
        }
        //Create the configuration (objects)
        i=0;
        for (Iterator<Integer> iterator = (generatedSets.objectSets.get(indexObjectSet)).iterator(); iterator.hasNext();) {
          Integer securityLevel = iterator.next();
          Objects.add(`securityObject(i,sL(securityLevel)));
           i++;
        }
        // Check all the permutations of requests upon the given configuration
        CheckAllPermutationsOfRequests CAPOR=new CheckAllPermutationsOfRequests(Subjects,Objects,policy,generatedSets.allRequests);
        int[] o=CAPOR.checkConfiguration();
        System.out.println("For subjects :\n"+Subjects);
        System.out.println("and objects :\n"+Objects);
        if (o.length==1){
          System.out.println("No leakage detected for all permutations");
        }else{
          LeakageDetected=true;
          System.out.println("Leakage detected for permutations :\n"+Arrays.toString(o));
          result=false;
        }


      }}
    return result;
  }
  
/*
public static ArrayList<ArrayList<Integer>> processString(String order){
    ArrayList<ArrayList<Integer>> securityLevelsOrder=new ArrayList<ArrayList<Integer>>();
    Scanner scanner = new Scanner(order);
    scanner.useDelimiter(";");
    while ( scanner.hasNext() ){
      ArrayList<Integer> subSetWithTotalOrder=new ArrayList<Integer>();
      String subSetWithTotalOrderString = scanner.next();
      Scanner scanner2 = new Scanner(subSetWithTotalOrderString);
      scanner2.useDelimiter(",");
      while (scanner2.hasNext()){
      String value = scanner2.next();
      subSetWithTotalOrder.add(Integer.parseInt(value));
      }
      scanner2.close();
      securityLevelsOrder.add(subSetWithTotalOrder);
    }
    scanner.close();
    return securityLevelsOrder;
  }
 */

/*
public static Object[] processString(String order){

	LevelsOfSecurity l=new LevelsOfSecurity();
	Object[] result=new Object[2];
    ArrayList<ArrayList<Integer>> securityLevelsOrder=new ArrayList<ArrayList<Integer>>();
    TreeSet<Integer> allValues=new TreeSet<Integer>();
    Scanner scanner = new Scanner(order);
    scanner.useDelimiter(";");
    while ( scanner.hasNext() ){
      ArrayList<Integer> subSetWithTotalOrder=new ArrayList<Integer>();
      String subSetWithTotalOrderString = scanner.next();
      Scanner scanner2 = new Scanner(subSetWithTotalOrderString);
      scanner2.useDelimiter(",");
      while (scanner2.hasNext()){
      String value = scanner2.next();
      int valueInteger = Integer.parseInt(value);
      allValues.add(valueInteger);
      subSetWithTotalOrder.add(valueInteger);
      }
      scanner2.close();
      securityLevelsOrder.add(subSetWithTotalOrder);
    }
    scanner.close();
    result[0]=securityLevelsOrder;
    result[1]=allValues.size();
    return result;
}
*/
  
public static int numberOfLevels(String order){

	TreeSet<Integer> allValues=new TreeSet<Integer>();
    Scanner scanner = new Scanner(order);
    scanner.useDelimiter(";");
    while ( scanner.hasNext() ){
      String subSetWithTotalOrderString = scanner.next();
      Scanner scanner2 = new Scanner(subSetWithTotalOrderString);
      scanner2.useDelimiter(",");
      while (scanner2.hasNext()){
      String value = scanner2.next();
      int valueInteger = Integer.parseInt(value);
      allValues.add(valueInteger);
      }
      scanner2.close();
    }
    scanner.close();
    return allValues.size();
}


public static void main(String[] args){
	String order=args[0];
	int numberOfSecurityLevels =Integer.parseInt(args[1]);
	int numberOfSubjects=Integer.parseInt(args[2]);
	int numberOfObjects=Integer.parseInt(args[3]);
	int numberOfAccessMode=Integer.parseInt(args[4]);
	//Object[] processedString=processString(order);
	//int numberOfSecurityLevels =numberOfLevels(order);
	Policy p;
    if (args[5].equals("0")){
		p=new BellAndLaPadula(PartiallyOrderdedSetOfSecurityLevels.fromString(args[0]));
	}else{
		p=new McLean(PartiallyOrderdedSetOfSecurityLevels.fromString(args[0]));
	}
	VerifyAllConfigs Verification=new VerifyAllConfigs(numberOfSubjects,numberOfObjects,numberOfSecurityLevels,numberOfAccessMode,p);
	Verification.checkAllSets();
	System.out.println(((Verification.LeakageDetected)?"leakages are detected":"no leakage is detected"));
}




/*
  public static void main(String[] args) {
    //Verify(int numberOfSubjects, int numberOfObjects, int numberOfSecurityLevels, int numberOfAccessModes);
	String order="1,3;0,1,4;0,2,4;0,3,4";
	Policy p=new McLean(processString(order));
	Verify Verification=new Verify(2,3,5,2,p);
    //Verification.checkSpecificSets(0,1);
    //Verification.checkAllSetsMcClean();
    //Verification.checkAllSets();
    //System.out.println(((Verification.LeakageDetected)?"a leakage is detected":"no leakage is detected"));
    Verification.generateSets();
    ArrayList<Subject> Subjects=new ArrayList<Subject>();
    ArrayList<SecurityObject> Objects=new ArrayList<SecurityObject>();
    Subjects.add(`subject(0,sL(3)));
    Subjects.add(`subject(1,sL(2)));
    Objects.add(`securityObject(0,sL(1)));
    Objects.add(`securityObject(1,sL(2)));
    Objects.add(`securityObject(2,sL(3)));
    int[] o=Verification.checkAllPermutationsOfRequests(Subjects,Objects);
    System.out.println("For subjects :\n"+Subjects);
    System.out.println("and objects :\n"+Objects);
    if (o.length==1){
    System.out.println("No leakage detected for all permutations");
    }else{
    System.out.println("Leakage detected for permutations :\n"+Arrays.toString(o));
    }
    //System.out.println(((Verification.LeakageDetected)?"a leakage is detected":"no leakage is detected"));
}
*/
}
