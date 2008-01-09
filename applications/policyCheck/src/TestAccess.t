import java.util.*;

import verify.example.*;
import verify.example.types.*;
import java.util.ArrayList;
import policy.*;

public class TestAccess{
	%include { sl.tom }
	%include { verify/example/Example.tom }




%strategy makeExplicit() extends `Identity() {
    visit State {
    	state(reads@accesses(X1*,access(s1,o1,aM(0),_),X2*,access(s2,o2,aM(0),_),X3*),
            writes@accesses(X4*,access(s,o,aM(1),_),X5*))->{
        if (`((s==s1 && o==o2))){
          ListOfAccesses l=`accesses(X1*,X2*,X3*);
          boolean contains=false;
          %match(l){
            accesses(X*,access(s3,o3,aM(0),_),Y*) ->{
              if (`s2==`s3 && `o1==`o3){
                contains=true;
              }
            }
          } 
          if (contains) return `state(reads,writes);
          else return `state(accesses(access(s2,o1,aM(0),implicit()),reads),writes);
        }else  if (`((s==s2 && o==o1))){
          ListOfAccesses l=`accesses(X1*,X2*,X3*);
          boolean contains=false;
          %match(l){
            accesses(X*,access(s3,o3,aM(0),_),Y*) ->{
              if (`s1==`s3 && `o2==`o3){
                contains=true;
              }
            }
          } 
          if (contains) return `state(reads,writes);
          else return `state(accesses(access(s1,o2,aM(0),implicit()),reads),writes);
        }
      }  
    }
  }

  //Verification of state
  public static boolean Verification(State setOfAccesses,Policy policy){
	 RequestUponState CurrentRequestOfScenario;
	 ArrayList<RequestUponState> implicitRequestsUponOriginalState=new ArrayList<RequestUponState>();
    try {
    	//make explicit implicit accesses
      State res=(State)`RepeatId(makeExplicit()).visit(setOfAccesses);
      implicitRequestsUponOriginalState=new ArrayList<RequestUponState>();
      	//add implicit accesses to "implicitRequestsUponOriginalState"
      %match(res){
        state[Writes=accesses(_*,a@access[E=implicit()],_*)]->{implicitRequestsUponOriginalState.add(`rus(request(add(),a),setOfAccesses));}
        state[Reads=accesses(_*,a@access[E=implicit()],_*)]->{implicitRequestsUponOriginalState.add(`rus(request(add(),a),setOfAccesses));}
      }

      //for each implicit access 
      for(Iterator<RequestUponState> iterator=implicitRequestsUponOriginalState.iterator(); iterator.hasNext();){
        RequestUponState iruos=(RequestUponState)iterator.next();
        //test if the implicit access is accepted
        if (!(policy.transition(iruos).getGranted())){
        	//behavior if the access is not granted which means that there is a leakage
          CurrentRequestOfScenario=iruos;
          System.out.println("Scenario detected :"+CurrentRequestOfScenario);
          return false;
        }
      }


    } catch (Exception e) {
      System.out.println("A problem occured while applying strategy");
    }
    // behavior if the access is granted
    return true;

  }

  // For a fixed configuration of subjects and objects and a given permutation for requests  
  public static Object[] check(RequestUponState rus, Policy policy){
	  Object[] result=new Object[2];
	  result[1]=true;
      	  // try to add the access to the state given the implementation of the policy
      Response response=policy.transition(rus);
      	  // if the request is granted, get the new state
      if(response.getGranted()) {
        result[0]=response.getState();
      }
      	  // if the request fails generate an error message an return false
      if (Verification((State)result[0],policy)==false){
        System.out.println("Information leakage detected");
        result[1]=false;
      }
	  	  //else generate a message and return the result 
    //System.out.println("No information leakage detected");
    return result;
  }




}