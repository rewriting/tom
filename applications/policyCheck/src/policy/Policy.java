package policy;

import accesscontrol.*;
import accesscontrol.types.*;

/**
 * Generic policy defined by the lattice of security levels
 * and by a current state containing the current (read/write) accesses
 *
 * @author 
 * @version
 * 
 */
public abstract class Policy {

  private SecurityLevelsLattice slL;

  private State currentState;

	/**
	 * The implementation of the policy: returns a decision for a given request
	 * 
	 * @param r the access request 
	 * @return the decision for the demanded request
	 */
	public abstract Decision transition(Request r);

	/**
	 * The predicate that should be verified by  the policy
	 * 
	 * @return true if the current state respects the predicate, false otherwise
	 */
	public abstract boolean valid();
	
}
