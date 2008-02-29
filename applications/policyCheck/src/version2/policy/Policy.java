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
public interface Policy {

	/**
	 * The implementation of the policy: returns a decision for a given request
	 * 
	 * @param r the access request 
	 * @param s the state
	 * @return the decision for the demanded request
	 */
	public Decision transition(Request r, State s);

	/**
	 * The predicate that should be verified by the policy
	 * 
	 * @param s the state
	 * @return true if the state respects the predicate, false otherwise
	 */
	public boolean valid(State s);
	
}
