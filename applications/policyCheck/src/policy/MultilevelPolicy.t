package policy;
import java.util.*;

import accesscontrol.*;
import accesscontrol.types.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Generic  policy defined by the lattice of security levels
 * and by a current state containing the current (read/write) accesses
 *
 * @author 
 * @version
 * 
 */
public abstract class FlowPolicy implements Policy {
  %include { sl.tom }
  %include { ../accesscontrol/Accesscontrol.Tom }

  private SecurityLevelsLattice slL;

  private State currentState;

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
  public boolean valid(){

    // make explicit implicit accesses
    // can we do it less often ??
    State res = (State)`RepeatId(makeExplicit()).visit(this.currentState);

    //add implicit accesses to "implicitRequestsUponOriginalState"
    %match(res){

      state(reads@accesses(_*,access(subject(sid,sl(sl)),resource(rid,sl(rl)),read(),_),_*),_) -> {
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

}
