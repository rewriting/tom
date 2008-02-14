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


	/**
	 * Stra
	 * 
	 * @param r the access request 
	 * @return the decision for the demanded request
	 */
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


	/**
	 * The predicate that should be verified by  the policy
	 * 
	 * @return true if the current state respects the predicate, false otherwise
	 */
  public boolean valid(){

    // make explicit implicit accesses
    // can we do it less often ??
    State res = (State)`RepeatId(makeExplicit()).visit(this.currentState);

    //add implicit accesses to "implicitRequestsUponOriginalState"
    %match(res){

      state(reads@accesses(_*,access(subject(sid,sl(sl)),resource(rid,sl(rl)),read(),_),_*),_) -> {
        if() {
          return false;
        }
      }
      
    }

    // if no leakage than OK
    return true;
  }

}
