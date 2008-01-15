import java.util.*;

import verify.example.*;
import verify.example.types.*;
import java.util.ArrayList;
import policy.BellAndLaPadula;
import policy.McLean;
import policy.Policy;
import mathsEnumeration.PermutationGenerator;

public class VerifyAConfig{
	%include { sl.tom }
	%include { verify/example/Example.tom }
	//Object checking all permutation of requests for the given subjects and objects and policy
	CheckAllPermutationsOfRequests CAPOR;
	// Flag for detecting a leakage
	boolean LeakageDetected=false;
	// Request creating a leakage
	RequestUponState CurrentRequestOfScenario;
  
  // Constructor
  public VerifyAConfig(ListOfSubjects ListOfSubjects, ListOfObjects ListOfObjects,Policy policy){
    this.CAPOR=new CheckAllPermutationsOfRequests(ListOfSubjects, ListOfObjects, policy);
  }



public static void main(String[] args){
/*
Example of arguments

Sets(CL(sL(1),sL(3)),CL(sL(0),sL(1)),CL(sL(0),sL(2)),CL(sL(0),sL(3)),CL(sL(0),sL(4)),CL(sL(0),sL(4)),CL(sL(1),sL(4)),CL(sL(2),sL(4)),CL(sL(3),sL(4))) subjects(subject(1,sL(3)),subject(2,sL(2))) objects(securityObject(1,sL(1)),securityObject(2,sL(2)),securityObject(3,sL(3))) 1

*/
	String order=args[0];
	String subjects=args[1];
	String objects=args[2];
	Policy p;
    if (args[3].equals("0")){
		p=new BellAndLaPadula(PartiallyOrderdedSetOfSecurityLevels.fromString(args[0]));
	}else{
		p=new McLean(PartiallyOrderdedSetOfSecurityLevels.fromString(args[0]));
	}
	VerifyAConfig Verification=new VerifyAConfig(ListOfSubjects.fromString(subjects),ListOfObjects.fromString(objects),p);
	boolean o=Verification.CAPOR.checkConfiguration();
        if (!o){
          System.out.println("No leakage detected for all permutations");
        }else{
          Verification.LeakageDetected=true;
          //System.out.println("Leakage detected for permutations :\n"+Arrays.toString(o));
        }
	System.out.println(((Verification.LeakageDetected)?"leakages are detected":"no leakage is detected"));
}



}
