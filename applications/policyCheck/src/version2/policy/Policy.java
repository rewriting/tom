package policy;

import accesscontrol.*;
import accesscontrol.types.*;

/**
 * Generic policy 
 *
 * @author 
 * @version
 * 
 */
public abstract class Policy {

  private SecurityLevelsLattice slL;

	/**
	 * Starts with an empty current state 
	 * 
	 * @param the security levels lattice 
	 */
 	public Policy(SecurityLevelsLattice slL) {
    this.slL = slL;
	}
  
	/**
	 * Get the security lattice
	 * 
	 * @return the security lattice
	 */
  public SecurityLevelsLattice getSecurityLevelsLattice() {
    return slL;
  }
  
	/**
	 * The implementation of the policy: returns a decision for a given request
	 * 
	 * @param r the access request 
	 * @param s the state
	 * @return the decision for the demanded request
	 */
	public abstract  Decision transition(Request r, State s);

	/**
	 * The predicate that should be verified by the policy
	 * 
	 * @param s the state
	 * @return true if the state respects the predicate, false otherwise
	 */
	public abstract  boolean valid(State s);
	
}
