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
public abstract class MultilevelPolicy implements Policy {
  %include { sl.tom }
  %include { ../accesscontrol/Accesscontrol.Tom }

  private SecurityLevelsLattice slL;

  private State currentState;

	/**
	 * Checks if an implicit state can be deduced from the set of explcit states 
   * -->  can be used with a repeat to build the closure for the set of accesses
	 * 
	 * @return the new state containing the implcit access
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

    //read only lower level resources

    %match(res){
      state(reads@accesses(_*,access(subject(sid,ssl),resource(rid,rsl),read(),_),_*),_) -> {
        if(`slL.compare(ssl,rsl)<0) {
          return false;
        }
      }
      
    }

    //*-security property

    // if no leakage than OK
    return true;
  }

}
