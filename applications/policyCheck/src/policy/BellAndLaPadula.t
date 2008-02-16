package policy;
import accesscontrol.*;
import accesscontrol.types.*;
import java.util.ArrayList;
import policy.Policy;

public class BellAndLaPadula extends FlowPolicy{
	%include { sl.tom }
  	%include { ../accesscontrol/accesscontrol.tom }

	//Constructor
	public BellAndLaPadula(PartiallyOrderdedSetOfSecurityLevels securityLevelsOrderImproved){
		this.securityLevelsOrderImproved=securityLevelsOrderImproved;
	}

	// Rewrite rules implementing the Bell and LaPadula policy
	public Response transition(RequestUponState req) {
		%match (req) {
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),am(0),_)),s0@state(_,accesses(_*,access(subject(i1,l1),securityObject(i3,l3),am(1),_),_*))  ) -> { 
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
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),am(1),_)),s2@state(accesses(_*,access(subject(i1,l1),securityObject(_,l3),am(0),_),_*),_)) -> { 
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


	 %strategy makeExplicit() extends `Identity() {
    visit State {
      state(reads@accesses(_*,access(s1,o1,am(0),_),_*,access(s2,o2,am(0),_),_*),
            writes@accesses(_*,access(s1,o2,am(1),_),_*)) &&
          !accesses(_*,access(s2,o1,am(0),_),_*) << reads -> {
        return `state(accesses(access(s2,o1,am(0),implicit()),reads),writes);
      }
      state(reads@accesses(_*,access(s2,o2,am(0),_),_*,access(s1,o1,am(0),_),_*),
            writes@accesses(_*,access(s1,o2,am(1),_),_*)) &&
          !accesses(_*,access(s2,o1,am(0),_),_*) << reads -> {
        return `state(accesses(access(s2,o1,am(0),implicit()),reads),writes);
      }
    }
  }


   //Verification of state by the predicate without turning implicit accesses explicit 
  public boolean verifyPredicateW(State setOfAccesses){
	  boolean result=true;
	  try {
		  //	make explicit implicit accesses
		  //State res = (State)`RepeatId(makeExplicit()).visit(setOfAccesses);
		  //test property 1 of the predicate
		  result=result && property1(setOfAccesses);
		  //test property 2 of the predicate
		  result=result && property2(setOfAccesses);
    } catch (Exception e) {
    	System.out.println("A problem occured while applying strategy");
    }
    // behavior if the access is granted
    return result;
  }


	   //Verification of state by the predicate
  public boolean verifyPredicate(State setOfAccesses){
	  boolean result=true;
	  try {
		  //	make explicit implicit accesses
		  State res = (State)`RepeatId(makeExplicit()).visit(setOfAccesses);
		  //test property 1 of the predicate
		  result=result && property1(res);
		  //test property 2 of the predicate
		  result=result && property2(res);
    } catch (Exception e) {
    	System.out.println("A problem occured while applying strategy");
    }
    // behavior if the access is granted
    return result;
  }

  //Verify property 1 with implicit accesses included
  public boolean property1(State res){
	  ListOfAccesses explicitAndImplicitReads=res.getreads();
	  %match(explicitAndImplicitReads){
		  accesses(_*,read@access(s,o,_,_),_*)->{
			  if (!compare(`s.getsl(),`o.getsl())){
				  System.out.print("Scenario detected :"+`read);
				  return false;
			  }
		  }
	  }
	  return true;
  }

  //Verify property 2
  public boolean property2(State res){
	  %match(res){
		state(accesses(_*,a@access(s,o1,_,_),_*),accesses(_*,b@access(s,o2,_,_),_*))-> { 
			if (!compare(`o1.getsl(),`o2.getsl())){
				  System.out.print("Scenario detected :"+`a+","+`b);
				  return false;
			  }
		}
	  }
	  return true;
  }



	public boolean valid(State setOfAccesses){
		return super.valid(setOfAccesses);
	}



}
