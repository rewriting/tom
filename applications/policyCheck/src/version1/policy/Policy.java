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
	 * @return the decision for the demanded request
	 */
	public Decision transition(Request r);

	/**
	 * The predicate that should be verified by the policy
	 * 
	 * @return true if the current state respects the predicate, false otherwise
	 */
	public boolean valid();
	
}
