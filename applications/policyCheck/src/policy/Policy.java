package policy;

import accesscontrol.*;
import accesscontrol.types.*;

public interface Policy {
	public Response transition(RequestUponState rus);

	public boolean valid(State setOfAccesses);
}
