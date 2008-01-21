package policy;
import accesscontrol.*;
import accesscontrol.types.*;
import java.util.ArrayList;
import policy.Policy;

public class BellAndLaPadula extends FlowPolicy{  
	%include { ../accesscontrol/accesscontrol.tom }
	


  public BellAndLaPadula(PartiallyOrderdedSetOfSecurityLevels securityLevelsOrderImproved){
    this.securityLevelsOrderImproved=securityLevelsOrderImproved;
  }


  // Rewrite rules implementing the Bell and LaPadula policy
  public Response transition(RequestUponState req) {
    %match (req) {
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),am(0),_)),
          s0@state(_,accesses(_*,access(subject(i1,l1),securityObject(i3,l3),am(1),_),_*))  ) -> { 
        if (!(compare(`l2,`l3))){
          return `response(false,s0);
        } 
      }
			rus(request(add(),access(subject(_,l1),securityObject(_,l2),am(0),_)),s1) -> { 
         if (!(compare(`l2,`l1))){
          return `response(false,s1);
        } 
      }
			rus(request(add(),a@access(_,_,am(0),_)),state(e,i)) -> { 
        return `response(true,state(accesses(a,e),i)); 
      }
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),am(1),_)),
          s2@state(accesses(_*,access(subject(i1,l1),securityObject(_,l3),am(0),_),_*),_)) -> { 
         if (!(compare(`l3,`l2))){
          return `response(false,s2);
        } 
      }
			rus(request(add(),a@access(_,_,am(1),_)),state(i,e))-> {
        return `response(true,state(i,accesses(a,e))); 
      }
			rus(request(delete(),a),state(accesses(x*,a,y*),i))-> { 
        return `response(true,state(accesses(x*,y*),i)); 
      }
      rus(request(delete(),a),state(i,accesses(x*,a,y*)))-> { 
        return `response(true,state(i,accesses(x*,y*))); 
      }
    }
    throw new RuntimeException("should not be there");
  }


  public boolean compare(SecurityLevel l1,SecurityLevel l2){
    %match (PartiallyOrderdedSetOfSecurityLevels securityLevelsOrderImproved){
		   setsl(_*,cl(_*,a,_*,b,_*),_*)->{if ((l1.equals(`a) && l2.equals(`b)) || l1.equals(l2) ){return true;}}
	   }
	   return false;
  }

public boolean valid(State setOfAccesses){return super.valid(setOfAccesses);}



}
