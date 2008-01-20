import java.util.*;
import mathsEnumeration.PermutationGenerator;
import accesscontrol.*;
import accesscontrol.types.*;
import input.*;
import input.types.*;
import java.io.*;
import policy.*;


public class Verify{
	%include { sl.tom }
	%include { input/input.tom }
	%include { accesscontrol/accesscontrol.tom }

  public static State fromForPrintToMain_State (ListOfSubjectsForPrint lsfp, ListOfObjectsForPrint lofp, StateForPrint sfp){
    State stateToTest=`state(accesses(),accesses());
    ListOfSubjects ls = fromForPrintToMain_ListOfSubjects(lsfp);
    ListOfObjects lo = fromForPrintToMain_ListOfObjects(lofp);
    %match(ListOfSubjects ls,ListOfObjects lo,StateForPrint sfp){
      subjects(_*,s@subject(x,_),_*),objects(_*,o@securityObject(y,_),_*),la(_*,a(s(x),o(y),m),_*)->{
        Access newAccess;
        if(`m==`r()){
          newAccess=`access(s,o,am(0),explicit());
          %match(stateToTest){
            state(accesses(r*),w)->{
              stateToTest=`state(accesses(r*,newAccess),w);
            }
          }
        }
        else {
          newAccess=`access(s,o,am(1),explicit());
          %match(stateToTest){
            state(r,accesses(w*))->{
              stateToTest=`state(r,accesses(w*,newAccess));
            }
          }
        }
      }
    }
    try {
	System.out.print("State:"+stateToTest);
	BufferedReader waiter = new BufferedReader(new InputStreamReader(System.in));
    waiter.readLine();
    } catch (Exception e) {}
    return stateToTest;
  }

public static ListOfSubjects fromForPrintToMain_ListOfSubjects (ListOfSubjectsForPrint lsfp){
	ListOfSubjects los=`subjects();  
	%match(ListOfSubjectsForPrint lsfp){
		ls(_*,sd(id,sl),_*)->{
			Subject newSubject=`subject(id,sl(sl));
			%match(los){subjects(X*)->{los=`subjects(X*,newSubject);}}
        }
    }
  try {
	System.out.print("Subjects:"+los);
	BufferedReader waiter = new BufferedReader(new InputStreamReader(System.in));
    waiter.readLine();
} catch (Exception e) {
	
}
    return los;
}

public static ListOfObjects fromForPrintToMain_ListOfObjects (ListOfObjectsForPrint lofp){
	ListOfObjects loo=`objects();  
	%match(ListOfObjectsForPrint lofp){
		lo(_*,od(id,sl),_*)->{
			SecurityObject newObject=`securityObject(id,sl(sl));
			%match(loo){objects(X*)->{loo=`objects(X*,newObject);}}
        }
    }
try {
	System.out.print("Objects:"+loo);
	BufferedReader waiter = new BufferedReader(new InputStreamReader(System.in));
    waiter.readLine();
} catch (Exception e) {	}
    return loo;
}

public static PartiallyOrderdedSetOfSecurityLevels fromForPrintToMain_PartiallyOrderdedSetOfSecurityLevels(PartiallyOrderdedSetOfSecurityLevelsForPrint possfp){
PartiallyOrderdedSetOfSecurityLevels poss=`setsl();
%match(possfp){
	lll(_*,subSet,_*)->{
							OrderedSubSetOfSecurityLevels ossos=`cl();
							
							%match(subSet){
								ll(_*,l(i),_*)->{
											SecurityLevel newSecurityLevel=`sl(i);
											%match(ossos){cl(e*)->{ossos=`cl(e*,newSecurityLevel);}}
											}
							}
							%match(poss){setsl(e*)->{poss=`setsl(e*,ossos);}}

						}
}
try {
	System.out.print("Order:"+poss);
	BufferedReader waiter = new BufferedReader(new InputStreamReader(System.in));
    waiter.readLine();
} catch (Exception e) {	}
return poss;
}

public static void fromForPrintToMain_Verify(InputVerify iv){
boolean b=true;
%match(iv){
	verifyState(order,subjects,objects,state,b())->{Policy policy=new BellAndLaPadula(fromForPrintToMain_PartiallyOrderdedSetOfSecurityLevels(`order));
													b=policy.valid(fromForPrintToMain_State(`subjects,`objects,`state));
													}
	verifyState(order,subjects,objects,state,m())->{Policy policy=new McLean(fromForPrintToMain_PartiallyOrderdedSetOfSecurityLevels(`order));
													b=policy.valid(fromForPrintToMain_State(`subjects,`objects,`state));
													}
	verifyConfig(order,subjects,objects,b())	 ->{Policy policy=new BellAndLaPadula(fromForPrintToMain_PartiallyOrderdedSetOfSecurityLevels(`order));
													CheckAllPermutationsOfRequests capr=new CheckAllPermutationsOfRequests(fromForPrintToMain_ListOfSubjects(`subjects), fromForPrintToMain_ListOfObjects(`objects), policy);
													b=capr.checkConfiguration();
													}
	verifyConfig(order,subjects,objects,m())     ->{Policy policy=new McLean(fromForPrintToMain_PartiallyOrderdedSetOfSecurityLevels(`order));
													CheckAllPermutationsOfRequests capr=new CheckAllPermutationsOfRequests(fromForPrintToMain_ListOfSubjects(`subjects), fromForPrintToMain_ListOfObjects(`objects), policy);
													b=capr.checkConfiguration();
													}
	verifyAll(order,x,y,b())              ->{Policy policy=new BellAndLaPadula(fromForPrintToMain_PartiallyOrderdedSetOfSecurityLevels(`order));
													CheckAllConfigs caf=new CheckAllConfigs(`x,`y, policy);
													b=caf.checkAllSets();
													}
	verifyAll(order,x,y,m())              ->{Policy policy=new McLean(fromForPrintToMain_PartiallyOrderdedSetOfSecurityLevels(`order));
													CheckAllConfigs caf=new CheckAllConfigs(`x,`y, policy);
													b=caf.checkAllSets();
													}
}
System.out.println((b)?"\nNo leakage detected":"\nLeakage detected");
}



	public static void main(String[] args){
		String query;
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/queryAll.input")); 
			fromForPrintToMain_Verify(InputVerify.fromString(reader.readLine()));
		}catch(IOException e){
			e.printStackTrace();

			}
		
	}
}