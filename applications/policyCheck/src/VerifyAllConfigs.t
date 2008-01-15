import java.util.*;
import mathsEnumeration.PermutationGenerator;
import accesscontrol.*;
import accesscontrol.types.*;

public class VerifyAllConfigs{
	%include { sl.tom }
	%include { accesscontrol/accesscontrol.tom }
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
        boolean o=CAPOR.checkConfiguration();
        System.out.println("For subjects :\n"+Subjects);
        System.out.println("and objects :\n"+Objects);
        if (!o){
          System.out.println("No leakage detected for all permutations");
        }else{
          LeakageDetected=true;
          //System.out.println("Leakage detected for permutations :\n"+Arrays.toString(o));
          System.out.println("Leakage detected");
          result=false;
        }
      }}
    return result;
  }
  
  
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

}
