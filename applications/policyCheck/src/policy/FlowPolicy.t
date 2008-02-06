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

  public PartiallyOrderdedSetOfSecurityLevels  securityLevelsOrderImproved;

  %strategy makeExplicit() extends `Identity() {
    visit State {
      state(reads@accesses(_*,access(s1,o1,am(0),_),_*,access(s2,o2,am(0),_),_*),
            writes@accesses(_*,access(s1,o2,am(1),_),_*)) &&
          !accesses(_*,access(s2,o1,am(0),_),_*) << reads -> {
        return `state(accesses(access(s2,o1,am(0),implicit()),reads),writes);
      }
      state(reads@accesses(_*,access(s2,o2,am(0),_),_*,access(s1,o1,am(0),_),_*),
            writes@accesses(_*,access(s1,o2,am(1),_),_*)) &&
          !accesses(_*,access(s2,o1,am(0),_),_*) << reads -> {
        return `state(accesses(access(s2,o1,am(0),implicit()),reads),writes);
      }
    }
  }

  //Verification of state
  public boolean valid(State setOfAccesses){
	  ArrayList<RequestUponState> implicitRequestsUponOriginalState = new ArrayList<RequestUponState>();
	  try {
		  //	make explicit implicit accesses
		  State res = (State)`RepeatId(makeExplicit()).visit(setOfAccesses);
		  //add implicit accesses to "implicitRequestsUponOriginalState"
		  %match(res){
			  state[writes=accesses(_*,a@access[e=implicit()],_*)]->{
				  implicitRequestsUponOriginalState.add(`rus(request(add(),a),setOfAccesses));
			  }
			  state[reads=accesses(_*,a@access[e=implicit()],_*)]->{
				  implicitRequestsUponOriginalState.add(`rus(request(add(),a),setOfAccesses));
			  }
		  }
		  //for each implicit access 
		  for(RequestUponState iruos:implicitRequestsUponOriginalState){
			  //test if the implicit access is accepted
			  if (!(transition(iruos).getgranted())){
				  //behavior if the access is not granted which means that there is a leakage
				  System.out.print("Scenario detected :"+iruos);
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
    	  if (valid((State)result[0])==false) {
    		  //System.out.println("Information leakage detected");
    		  result[1]=false;
    	  }
      } else {
    	  result[1]=true;
      }
 	  //else generate a message and return the result 
      //System.out.println("No information leakage detected");
      return result;
  }

}
