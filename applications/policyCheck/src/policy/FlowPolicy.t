package policy;
import java.util.*;

import accesscontrol.*;
import accesscontrol.types.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class FlowPolicy implements Policy {
  %include { sl.tom }
  %include { ../accesscontrol/Accesscontrol.Tom }


  public PartiallyOrderdedSetOfSecurityLevels securityLevelsOrderImproved;

  %strategy makeExplicit() extends `Identity() {
    visit State {
      state(reads@accesses(x1*,access(s1,o1,am(0),_),x2*,access(s2,o2,am(0),_),x3*),
          writes@accesses(x4*,access(s,o,am(1),_),x5*))->{
        %match(accesses(x1*,x2*,x3*)) {
          accesses(x*,access(s3,o3,am(0),_),y*) -> {
            if( (`s2==`s3 && `o1==`o3) || (`s1==`s3 && `o2==`o3)) {
              return `state(reads,writes);
            }
          }
        }
        return `state(accesses(access(s1,o2,am(0),implicit()),reads),writes);
      }
    }
  }

  /*
     if (`((s==s1 && o==o2))){
     ListOfAccesses l=`accesses(x1*,x2*,x3*);
     boolean contains=false;
     %match(l){
     accesses(x*,access(s3,o3,am(0),_),y*) ->{
     if (`s2==`s3 && `o1==`o3){
     contains=true;
     }
     }
     } 
     if (contains) return `state(reads,writes);
     else return `state(accesses(access(s2,o1,am(0),implicit()),reads),writes);
     }else  if (`((s==s2 && o==o1))){
     ListOfAccesses l=`accesses(x1*,x2*,x3*);
     boolean contains=false;
     %match(l){
     accesses(x*,access(s3,o3,am(0),_),y*) ->{
     if (`s1==`s3 && `o2==`o3){
     contains=true;
     }
     }
     } 
     if (contains) return `state(reads,writes);
     else return `state(accesses(access(s1,o2,am(0),implicit()),reads),writes);
     }
     }  
    }
  }
*/
  //Verification of state
  public boolean valid(State setOfAccesses){
	 RequestUponState currentRequestOfScenario;
	 ArrayList<RequestUponState> implicitRequestsUponOriginalState=new ArrayList<RequestUponState>();
    try {
    	//make explicit implicit accesses
      State res=(State)`RepeatId(makeExplicit()).visit(setOfAccesses);
      implicitRequestsUponOriginalState=new ArrayList<RequestUponState>();
      	//add implicit accesses to "implicitRequestsUponOriginalState"
      %match(res){
        state[writes=accesses(_*,a@access[e=implicit()],_*)]->{implicitRequestsUponOriginalState.add(`rus(request(add(),a),setOfAccesses));}
        state[reads=accesses(_*,a@access[e=implicit()],_*)]->{implicitRequestsUponOriginalState.add(`rus(request(add(),a),setOfAccesses));}
      }

      //for each implicit access 
      for(Iterator<RequestUponState> iterator=implicitRequestsUponOriginalState.iterator(); iterator.hasNext();){
        RequestUponState iruos=(RequestUponState)iterator.next();
        //test if the implicit access is accepted
        if (!(transition(iruos).getgranted())){
        	//behavior if the access is not granted which means that there is a leakage
          currentRequestOfScenario=iruos;
          System.out.print("Scenario detected :"+currentRequestOfScenario);
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
  public Object[] check(RequestUponState rus){
	  Object[] result=new Object[2];
	  result[1]=true;
      	  // try to add the access to the state given the implementation of the policy
      Response response=transition(rus);
      	  // get the new state whether it has changed or not
      result[0]=response.getstate();
      	  // if the request is granted
      if(response.getgranted()) {
    	  	// Verify the new state, if the verification fails generate an error message and return false
        if (valid((State)result[0])==false){
        	//System.out.println("Information leakage detected");
        	result[1]=false;
        }
      }else{
    	  result[1]=true;
      }
      
	  	  //else generate a message and return the result 
    //System.out.println("No information leakage detected");
    return result;
  }

}
