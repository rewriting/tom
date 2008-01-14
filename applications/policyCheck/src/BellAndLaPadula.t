import verify.example.*;
import verify.example.types.*;
import java.util.ArrayList;

public class BellAndLaPadula implements Policy{  
	%include {verify/example/Example.tom }
	

 ArrayList<ArrayList<Integer>> securityLevelsorder;
  PartiallyOrderdedSetOfSecurityLevels securityLevelsOrderImproved;

  public BellAndLaPadula(ArrayList<ArrayList<Integer>> s){
    this.securityLevelsorder=s;
  }

  public BellAndLaPadula(PartiallyOrderdedSetOfSecurityLevels securityLevelsOrderImproved){
    this.securityLevelsOrderImproved=securityLevelsOrderImproved;
  }


  // Rewrite rules implementing the Bell and LaPadula policy
  public Response transition(RequestUponState req) {
    %match (req) {
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),aM(0),_)),
          s0@state(_,accesses(_*,access(subject(i1,l1),securityObject(i3,l3),aM(1),_),_*))  ) -> { 
        if (!(compare(`l2,`l3))){
//        if (`l2.compareTo(`l3)>0){
          return `Response(false,s0);
        } 
      }
			rus(request(add(),access(subject(_,l1),securityObject(_,l2),aM(0),_)),s1) -> { 
         if (!(compare(`l2,`l1))){
//        if (`l2.compareTo(`l1)>0){
          return `Response(false,s1);
        } 
      }
			rus(request(add(),a@access(_,_,aM(0),_)),state(e,i)) -> { 
        return `Response(true,state(accesses(a,e),i)); 
      }
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),aM(1),_)),
          s2@state(accesses(_*,access(subject(i1,l1),securityObject(_,l3),aM(0),_),_*),_)) -> { 
         if (!(compare(`l3,`l2))){
//        if (`l3.compareTo(`l2)>0){
          return `Response(false,s2);
        } 
      }
			rus(request(add(),a@access(_,_,aM(1),_)),state(i,e))-> {
        return `Response(true,state(i,accesses(a,e))); 
      }
			rus(request(delete(),a),state(accesses(X*,a,Y*),i))-> { 
        return `Response(true,state(accesses(X*,Y*),i)); 
      }
      rus(request(delete(),a),state(i,accesses(X*,a,Y*)))-> { 
        return `Response(true,state(i,accesses(X*,Y*))); 
      }
    }
    throw new RuntimeException("should not be there");
  }


  public boolean compare(SecurityLevel l1,SecurityLevel l2){
    %match (securityLevelsOrderImproved){
		   Sets(_*,CL(_*,a,_*,b,_*),_*)->{if ((l1.equals(`a) && l2.equals(`b)) || l1.equals(l2) ){return true;}}
	   }
	   return false;
  }





/*
public static int compare(SecurityLevel sl,SecurityLevel sl2){
          return (sl.getl()-sl2.getl()<=0);
}
*/

}
