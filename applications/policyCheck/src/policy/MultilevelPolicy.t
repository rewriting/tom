package policy;
import java.util.*;

import accesscontrol.*;
import accesscontrol.types.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Generic policy defined by the lattice of security levels
 * and by a current state containing the current (read/write) accesses
 *
 * @author 
 * @version
 * 
 */
public abstract class MultilevelPolicy implements Policy, Cloneable {
  %include { sl.tom }
  %include { ../accesscontrol/accesscontrol.tom }

  private SecurityLevelsLattice slL;

  private State currentState;

  public MultilevelPolicy(SecurityLevelsLattice slL) {
    this.slL = slL;
    this.currentState = `state(accesses(),accesses());
  }

  public abstract Object clone();

	/**
	 * Get the security lattice
	 * 
	 * @return the security lattice
	 */
  public SecurityLevelsLattice getSecurityLevelsLattice() {
    return slL;
  }

	/**
	 * Get the current state
	 * 
	 * @return the current state
	 */
  public State getCurrentState() {
    return currentState;
  }

	/**
	 * Set the current state
	 * 
	 */
  public void setCurrentState(State newState) {
    this.currentState = newState;
  }

	/**
	 * Get the current state expanded with all implicit accesses
   * -- implicit accesses are explicitly put in the state but they are marked with the "implicit" flag
	 * 
	 * @return the expanded current state
	 */
  public State getExpandedCurrentState() {
    try {
      return (State)`RepeatId(makeExplicit()).visitLight(currentState);
    } catch(tom.library.sl.VisitFailure vfe) {
      throw new RuntimeException("VERIFICATION PROBLEM !!!");
    }
  }

	/**
	 * Checks if an implicit state can be deduced from the set of explcit states 
   * -->  can be used with a repeat to build the closure for the set of accesses
	 * 
	 * @return the new state containing the implcit access
	 */
  %strategy makeExplicit() extends Identity() {
    visit State {
      state(reads@accesses(_*,access(s1,o1,read(),_),_*,access(s2,o2,read(),_),_*),
            writes@accesses(_*,access(s1,o2,write(),_),_*)) &&
          !accesses(_*,access(s2,o1,read(),_),_*) << reads -> {
        return `state(accesses(access(s2,o1,read(),implicit()),reads),writes);
      }
      state(reads@accesses(_*,access(s2,o2,read(),_),_*,access(s1,o1,read(),_),_*),
            writes@accesses(_*,access(s1,o2,write(),_),_*)) &&
          !accesses(_*,access(s2,o1,read(),_),_*) << reads -> {
        return `state(accesses(access(s2,o1,read(),implicit()),reads),writes);
      }
    }
  }

}
