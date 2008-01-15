import java.util.*;
import accesscontrol.*;
import accesscontrol.types.*;
import mathsEnumeration.PermutationGenerator;

public class VerifyAState{
	%include { sl.tom }
	%include { accesscontrol/accesscontrol.tom }
	// Flag for detecting a leakage
	boolean LeakageDetected=false;

public static State fromForPrintToMainStateSort (ListOfSubjects LS, ListOfObjects LO, StateForPrint sfr){
State StateToTest=`state(accesses(),accesses());
%match(ListOfSubjects LS,ListOfObjects LO,sfr){
	subjects(_*,s@subject(x,_),_*),objects(_*,o@securityObject(y,_),_*),acs(_*,a(s(x),o(y),m),_*)->{
	Access newAccess;
	if(`m==`read()){newAccess=`access(s,o,aM(0),explicit());%match(StateToTest){state(accesses(R*),W)->{StateToTest=`state(accesses(R*,newAccess),W);}}}
	else {newAccess=`access(s,o,aM(1),explicit());%match(StateToTest){state(R,accesses(W*))->{StateToTest=`state(R,accesses(W*,newAccess));}}}
	}
}
return StateToTest;
}



public static void main(String[] args){
/*
Example of arguments

Sets(CL(sL(1),sL(3)),CL(sL(0),sL(1)),CL(sL(0),sL(2)),CL(sL(0),sL(3)),CL(sL(0),sL(4)),CL(sL(0),sL(4)),CL(sL(1),sL(4)),CL(sL(2),sL(4)),CL(sL(3),sL(4))) subjects(subject(1,sL(3)),subject(2,sL(2))) objects(securityObject(1,sL(1)),securityObject(2,sL(2)),securityObject(3,sL(3))) acs(a(s(1),o(3),read()),a(s(1),o(3),write())) 1

*/



	String order=args[0];
	String subjects=args[1];
	String objects=args[2];
	String state=args[3];


	//fromForPrintToMainSort(ListOfSubjects.fromString(subjects), ListOfObjects.fromString(objects), StateForPrint.fromString(state));
	
	Policy p;
    if (args[4].equals("0")){
		p=new BellAndLaPadula(PartiallyOrderdedSetOfSecurityLevels.fromString(args[0]));
	}else{
		p=new McLean(PartiallyOrderdedSetOfSecurityLevels.fromString(args[0]));
	}

	boolean o=TestAccess.Verification(fromForPrintToMainStateSort(ListOfSubjects.fromString(subjects), ListOfObjects.fromString(objects), StateForPrint.fromString(state)),p);
      
	System.out.println((!o)?"a leakages have been detected":"no leakage");

}



}
