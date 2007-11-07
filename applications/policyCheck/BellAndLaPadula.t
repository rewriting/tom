import bellandlapadula.example.*;
import verify.example.*;
import verify.example.types.*;

public class BellAndLaPadula implements Policy{  
	%include { verify/example/Example.tom }
	
  // Rewrite rules implementing the Bell and LaPadula policy
  public static Response transition(RequestUponState req){
    %match (RequestUponState req){
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),aM(0),_)),
          s0@state(_,accesses(_*,access(subject(i1,l1),securityObject(i3,l3),aM(1),_),_*))  ) -> { 
        if (!(compare(`l2,`l3))){
//        if (`l2.compareTo(`l3)>0){
          return new Response(false,`s0);
        } 
      }
			rus(request(add(),access(subject(_,l1),securityObject(_,l2),aM(0),_)),s1) -> { 
         if (!(compare(`l2,`l1))){
//        if (`l2.compareTo(`l1)>0){
          return new Response(false,`s1);
        } 
      }
			rus(request(add(),a@access(_,_,aM(0),_)),state(e,i)) -> { 
        return new Response(true,`state(accesses(a,e),i)); 
      }
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),aM(1),_)),
          s2@state(accesses(_*,access(subject(i1,l1),securityObject(_,l3),aM(0),_),_*),_)) -> { 
         if (!(compare(`l3,`l2))){
//        if (`l3.compareTo(`l2)>0){
          return new Response(false,`s2);
        } 
      }
			rus(request(add(),a@access(_,_,aM(1),_)),state(i,e))-> {
        return new Response(true,`state(i,accesses(a,e))); 
      }
			rus(request(delete(),a),state(accesses(X*,a,Y*),i))-> { 
        return new Response(true,`state(accesses(X*,Y*),i)); 
      }
      rus(request(delete(),a),state(i,accesses(X*,a,Y*)))-> { 
        return new Response(true,`state(i,accesses(X*,Y*))); 
      }
    }
    throw new RuntimeException("should not be there");
  }

public static int compare(SecurityLevel sl,SecurityLevel sl2){
          return (sl.getl()-sl2.getl()<=0);
}


}