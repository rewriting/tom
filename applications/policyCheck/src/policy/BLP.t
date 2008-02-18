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
    }

    //read only lower level resources
 
    %match(res){
      state(reads@accesses(_*,access(subject(sid,ssl),resource(rid,rsl),read(),_),_*),_) -> {
        if(`slL.compare(`ssl,`rsl)<0) {
          return false;
        }
      }
      
    }

    //*-security property

    // if no leakage than OK
    return true;
  }

	// Rewrite rules implementing the Bell and LaPadula policy
	public Decision transition(Request req) {
    SecurityLevelsLattice slL = getSecurityLevelsLattice();
    State cs = getCurrentState();
    State ns = cs;

		%match (req) {
			request(add(),access(subject(sid,ssl),resource(rid,rsl),read(),_))  -> { 
        if(`slL.compare(`ssl,`rsl)<0) {
          return `deny();
        }
      }
			request(add(),access(subject(sid,ssl),resource(rid,rsl),read(),_))  -> { 
        match(cs) {
          state(reads@accesses(_*,access(subject(sidS,sslS),resource(ridS,rslS),write(),_),_*),_) -> {
            if(`ssl.equals(`sslS) && )
          }
        }
        if(`slL.compare(`ssl,`rsl)<0) {
          return `deny();
        }
      }
    }

    return `na();
// 		%match (req, cs) {
// 			request(add(),access(subject(i1,l1),ressource(_,l2),am(0),_)),
//         s0@state(_,accesses(_*,access(subject(i1,l1),securityObject(i3,l3),am(1),_),_*))  )  -> { 
//     }
//   }
// 		throw new RuntimeException("should not be there");
	}




}
