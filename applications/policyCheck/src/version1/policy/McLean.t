package policy;
import accesscontrol.*;
import accesscontrol.types.*;
import policy.Policy;

public class McLean extends MultilevelPolicy implements Cloneable {
	%include { sl.tom }
  %include { ../accesscontrol/accesscontrol.tom }

	/**
	 * Starts with an empty current state 
	 * 
	 * @param the security levels lattice 
	 */
 	public McLean(SecurityLevelsLattice slL) {
    super(slL);
	}

	/**
	 * Start with a given current state 
   * ==> used to test the valid predicate
	 * 
	 * @param the security levels lattice 
	 * @param the current state
	 */
 	public McLean(SecurityLevelsLattice slL, State state) {
    this(slL);
    setCurrentState(state);
	}
 
  public Object clone() {
    McLean theClone = new McLean(getSecurityLevelsLattice(),getCurrentState());
    return theClone;
  }
	/**
	 * The predicate that should be verified by  the policy
	 * 
	 * @return true if the current state respects the predicate, false otherwise
	 */
  public boolean valid() {
    SecurityLevelsLattice slL = getSecurityLevelsLattice();
    
    // make explicit implicit accesses
    // can we do it less often ??
    State ecs = getExpandedCurrentState();

    %match(ecs) {
      //read only (comparable and) lower level resources 
      state[reads=accesses(_*,access(subject(sid,ssl),resource(rid,rsl),read(),_),_*)] -> {
        if(! `slL.smaller(`rsl,`ssl)) {
          return false;
        }
      } 

    //*-security property
      state[reads=accesses(_*,access(subject(sid,ssl),resource(rid1,rsl1),read(),_),_*),
            writes=accesses(_*,access(subject(sid,ssl),resource(rid2,rsl2),write(),_),_*)] -> {
        if(`slL.smaller(`rsl2,`rsl1)) {
          return false;
        }
      }
    }
    
    // if no leakage then OK
    return true;
  }

 
	/**
   * Rewrite rules implementing the McLean policy
   * done with two level match for add cases
   * ==> should be more clear (but less efficient?) with one level and non-linear matching
	 * 
	 * @param the request to be performed
   *
	 * @return the decision for the given request - accept/deny/n(ot)a(pplicable)
	 */
	public Decision transition(Request req) {
    SecurityLevelsLattice slL = getSecurityLevelsLattice();
    State cs = getCurrentState();

    // TODO: access that already exists

		%match (req) {
      // READ access  (if a WRITE already exists it should be comparable and bigger)
			request(add(),newAccess@access(subject(sid,ssl),resource(rid,rsl),read(),_))  -> { 
        // not enough privileges to read
        if(! `slL.smaller(`rsl,`ssl)) {
          return `deny();
        }
        // existing write access with lower level
        %match(cs) {
          state[writes=accesses(_*,access(subject(sidS,sslS),resource(ridS,rslS),write(),_),_*)] -> {
            if(`sid==`sidS && 
               // `ssl.equals(`sslS) && 
               `slL.smaller(`rslS,`rsl)) {
              return `deny();
            }
          }
        }
        // none of the previous ones is satisfied
        // ==> good privileges and no existing write that is not smaller
        %match(cs) {
          state(reads,writes) -> {
            // add the new access
            setCurrentState(`state(accesses(newAccess,reads),writes));
          }
        }
        return `grant();
      }

      // WRITE access (if a READ already exists it should be comparable and smaller)
			request(add(),newAccess@access(subject(sid,ssl),resource(rid,rsl),write(),_))  -> { 
        // existing write access with lower level
        %match(cs) {
          state[reads=accesses(_*,access(subject(sidS,sslS),resource(ridS,rslS),read(),_),_*)] -> {
            if( `sid==`sidS && 
                // `ssl.equals(`sslS) &&
               `slL.smaller(`rsl,`rslS)){
              return `deny();
            }
          }
        }
        // no  existing read that is not bigger
        %match(cs) {
          state(reads,writes) -> {
            // add the new access
            setCurrentState(`state(reads,accesses(newAccess,writes)));
          }
        } 
        return `grant();
      }
    }

    // remove a READ or WRITE access 
    // ==> granted if the access exists
    // Q: it looks like forgetting the history - is this OK?
    %match(req,cs) {
      request(delete(),access(subject(sid,ssl),resource(rid,rsl),rw,_)),
        state(reads@accesses(la*,access(subject(sid,ssl),resource(rid,rsl),rw,_),ra*),writes) -> {  
        // remove the access
        setCurrentState(`state(accesses(la*,ra*),writes));
        return `grant();
      }
      request(delete(),access(subject(sid,ssl),resource(rid,rsl),rw,_)),
        state(reads,accesses(la*,access(subject(sid,ssl),resource(rid,rsl),rw,_),ra*)) -> {  
        // remove the access
        setCurrentState(`state(reads,accesses(la*,ra*)));
        return `grant();
      }
      request(delete(),access(subject(sid,ssl),resource(rid,rsl),rw,_)),
        // state(reads,writes) -> {  //the previous two don't match
        state(reads,! accesses(la*,access(subject(sid,ssl),resource(rid,rsl),rw,_),ra*)) -> {  
        // doens't exist
        return `deny();
      }
    }

    // all the other cases
    return `na();
	}

  public String toString(){
    String s = "ACCESSES : ";
    State cs = getCurrentState();

    %match(cs) {
      state[writes=accesses(_*,a@access[],_*)] -> {
        s += "\n " + `a;
      }
      state[reads=accesses(_*,a@access[],_*)] -> {
        s += "\n " + `a;
      }
    }
    return s;
  }

}
