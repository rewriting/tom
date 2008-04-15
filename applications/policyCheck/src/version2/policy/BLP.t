package policy;
import accesscontrol.*;
import accesscontrol.types.*;
import policy.Policy;

public class BLP extends Policy {
	%include { sl.tom }
  %include { ../accesscontrol/accesscontrol.tom }

	/**
	 * @param the security levels lattice 
	 */
 	public BLP(SecurityLevelsLattice slL) {
    super(slL);
	}

  /**
	 * The predicate that should be verified by the policy
	 * 
	 * @param s the state
	 * @return true if the current state respects the predicate, false otherwise
	 */
  public boolean valid(State cs) {
    SecurityLevelsLattice slL = getSecurityLevelsLattice();

    %match(cs) {
      //read only (comparable and) lower level resources 
      state(accesses(_*,read(subject[sl=ssl],resource[sl=rsl]),_*)) -> {
        if(! `slL.leq(`rsl,`ssl)) {
          System.out.println("read: " + `rsl + " by " + `ssl);
          return false;
        }
      } 

      //*-security property
      state(accesses(_*,read(s,resource[sl=rsl1]),_*,
                        write(s,resource[sl=rsl2]),_*)) -> {
        if(! `slL.leq(`rsl1,`rsl2)) {
          System.out.println("write: " + `rsl2 + " by " + `s);
          System.out.println("read:  " + `rsl1 + " by " + `s);
          return false;
        }
      }

    }
    
    // if no leakage then OK
    return true;
  }

 
	/**
   * Rewrite rules implementing the Bell and LaPadula policy
   * done with two level match for add cases
   * ==> should be more clear (but less efficient?) with one level and non-linear matching
	 * 
	 * @param req the request to be performed
	 * @param cs the state
   *
	 * @return the decision for the given request - grant/deny/n(ot)a(pplicable)
	 */
	public Decision transition(Request req, State cs) {
    SecurityLevelsLattice slL = getSecurityLevelsLattice();


		%match(req) {
			add(read(subject[sl=ssl1],resource[sl=rsl1]))  -> { 
        // not enough privileges to read
        if(! `slL.leq(`rsl1,`ssl1)) {
          return `deny(cs);
        }
      }
    }


		%match(req,cs) {
      // READ access  (if a WRITE already exists it should be comparable and bigger)
			add(newAccess@read(s1@subject[sl=ssl1],resource[sl=rsl1])),
        state(accesses(_*,write(s2,resource[sl=rsl2]),_*)) -> {
        // existing write access with lower level
            if(`s1==`s2 && ! `slL.leq(`rsl1,`rsl2)) {
              return `deny(cs);
            }
        }
    
      // WRITE access (if a READ already exists it should be comparable and smaller)
			add(newAccess@write(s1,resource[sl=rsl1])), 
        state(accesses(_*,read(s2,resource[sl=rsl2]),_*)) -> {
        // existing write access with lower level
            if( `s1==`s2 && ! `slL.leq(`rsl2,`rsl1)) {
              return `deny(cs);
            }
        }

			add(newAccess), 
        state(accesses) -> {
        // no existing read that is not bigger
        return `grant(state(accesses(newAccess,accesses*)));
      }

      // remove a READ or WRITE access 
      // ==> granted if the access exists
      // Q: it looks like forgetting the history - is this OK?
      delete(access), state(accesses(la*,access,ra*)) -> {
        // remove the access
        return `grant(state(accesses(la*,ra*)));
      }
      
      delete(access), state(!accesses(la*,access,ra*)) -> {
        // doesn't exist
        return `deny(cs);
      }
    }

    // all the other cases
    return `na(cs);
	}

}
