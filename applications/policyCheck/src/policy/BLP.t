package policy;
import accesscontrol.*;
import accesscontrol.types.*;
import java.util.ArrayList;
import policy.Policy;

public class BLP extends MultilevelPolicy{
	%include { sl.tom }
  %include { ../accesscontrol/accesscontrol.tom }

	//Constructor
	public BLP(SecurityLevelsLattice slL){
    super(slL);
	}
 
	/**
	 * The predicate that should be verified by  the policy
	 * 
	 * @return true if the current state respects the predicate, false otherwise
	 */
  public boolean valid() {
    SecurityLevelsLattice slL = getSecurityLevelsLattice();
    State cs = getCurrentState();
    State res = cs;

    // make explicit implicit accesses
    // can we do it less often ??
    try{
      res = getExpandedCurrentState();
    } catch(tom.library.sl.VisitFailure vfe){
      System.out.println("VERIFICATION PROBLEM !!!");
    }
    
    //read only (comparable and) lower level resources 
    %match(res){
      state(reads@accesses(_*,access(subject(sid,ssl),resource(rid,rsl),read(),_),_*),_) -> {
        if(! `slL.smaller(`rsl,`ssl)) {
          return false;
        }
      } 
    }
    
    //*-security property
    %match(res){
      state(reads@accesses(_*,access(subject(sid,ssl),resource(rid1,rsl1),read(),_),_*),
            writes@accesses(_*,access(subject(sid,ssl),resource(rid2,rsl2),write(),_),_*)) -> {
        if(! `slL.smaller(`rsl1,`rsl2)) {
          return false;
        }
      }
    }

    // if no leakage than OK
    return true;
  }

 
	/**
   * Rewrite rules implementing the Bell and LaPadula policy
   * done with two level match for add cases
   * ==> should be more clear (but less efficient?) with one level and non-linear matching
	 * 
	 * @pram the request to be performed
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
          state(_,writes@accesses(_*,access(subject(sidS,sslS),resource(ridS,rslS),write(),_),_*)) -> {
            if(`sid==`sidS && 
               // `ssl.equals(`sslS) && 
               ! `slL.smaller(`rsl,`rslS)){
              return `deny();
            }
          }
        }
        // none of the previous ones is satisfied
        // ==> good privileges and no existing write that is not smaller
        %match(cs) {
          state(accesses(la*),writes@accesses(_*)) -> {
            // add the new access
            setCurrentState(`state(accesses(newAccess,la),writes));
          }
        }
        return `grant();
      }

      // WRITE access (if a READ already exists it should be comparable and smaller)
			request(add(),newAccess@access(subject(sid,ssl),resource(rid,rsl),write(),_))  -> { 
        // existing write access with lower level
        %match(cs) {
          state(reads@accesses(_*,access(subject(sidS,sslS),resource(ridS,rslS),read(),_),_*),_) -> {
            if( `sid==`sidS && 
                // `ssl.equals(`sslS) &&
               ! `slL.smaller(`rslS,`rsl)){
              return `deny();
            }
          }
        }
        // no  existing read that is not bigger
        %match(cs) {
          state(reads@accesses(_*),accesses(la*)) -> {
            // add the new access
            setCurrentState(`state(reads,accesses(newAccess,la)));
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
        setCurrentState(`state(accesses(la,ra),writes));
        return `grant();
      }
      request(delete(),access(subject(sid,ssl),resource(rid,rsl),rw,_)),
        state(reads,accesses(la*,access(subject(sid,ssl),resource(rid,rsl),rw,_),ra*)) -> {  
        // remove the access
        setCurrentState(`state(reads,accesses(la,ra)));
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
      state(_,accesses(_*,a@access(_,_,_,_),_*)) -> {
        s += "\n " + `a;
      }
      state(accesses(_*,a@access(_,_,_,_),_*),_) -> {
        s += "\n " + `a;
      }
    }
    return s;
  }

}
