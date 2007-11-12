import verify.example.types.*; 
import verify.example.*; 
import java.util.ArrayList;

public class McLean implements Policy{  
	%include { verify/example/Example.tom }

	ArrayList<ArrayList<Integer>> securityLevelsorder;

McLean(ArrayList<ArrayList<Integer>> s){
	this.securityLevelsorder=s;
}
  
	
 public  Response transition(RequestUponState req){
    %match ( req){
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),aM(0),_)),
          s0@state(_,accesses(_*,access(subject(i1,l1),securityObject(i3,l3),aM(1),_),_*))  ) -> { 
         if ((compareStrictMcLean(`l3,`l2))){
          return `Response(false,s0);
        } 
      }
			rus(request(add(),access(subject(_,l1),securityObject(_,l2),aM(0),_)),s1) -> { 
         if (!(compareMcLean(`l2,`l1))){
          return `Response(false,s1);
        } 
      }
			rus(request(add(),a@access(_,_,aM(0),_)),state(e,i)) -> { 
        return `Response(true,state(accesses(a,e),i)); 
      }
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),aM(1),_)),
          s2@state(accesses(_*,access(subject(i1,l1),securityObject(_,l3),aM(0),_),_*),_)) -> { 
         if ((compareStrictMcLean(`l2,`l3))){
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



public boolean compareStrictMcLean(SecurityLevel l1,SecurityLevel l2){
	for(java.util.ArrayList<Integer> orderedSubSet: this.securityLevelsorder ){
		if (orderedSubSet.contains(l1.getl()) && orderedSubSet.contains(l2.getl()))
			return orderedSubSet.indexOf(l1.getl())-orderedSubSet.indexOf(l2.getl())<0;
	}
	return false;
}


public boolean compareMcLean(SecurityLevel l1,SecurityLevel l2){
	for(java.util.ArrayList<Integer> orderedSubSet: this.securityLevelsorder ){
		if (orderedSubSet.contains(l1.getl()) && orderedSubSet.contains(l2.getl()))
			return orderedSubSet.indexOf(l1.getl())-orderedSubSet.indexOf(l2.getl())<=0;
	}
	return false;
}

   
}
/*
  public static boolean compareStrictMcLean(SecurityLevel l1,SecurityLevel l2){
     %match (SecurityLevel l1,SecurityLevel l2){
 			sL(1),sL(3) -> { return true; }
 			sL(3),sL(1) -> { return false; }
 			sL(2),sL(1) -> { return false; }
 			sL(1),sL(2) -> { return false; }
 			sL(2),sL(3) -> { return false; }
 			sL(3),sL(2) -> { return false; }
 			sL(0),sL(0) -> { return false; }
 			sL(4),sL(4) -> { return false; }
 			sL(0),_     -> { return true; }
 			_    ,sL(4) -> { return true; }
 			sL(4),_     -> { return false; }
 			_    ,sL(0) -> { return false; }
 			_    ,_     -> { return false; }
     }
     throw new RuntimeException("should not be there");
   }

   public static boolean compareMcLean(SecurityLevel l1,SecurityLevel l2){
     %match (SecurityLevel l1,SecurityLevel l2){
 			sL(1),sL(3) -> { return true; }
 			sL(3),sL(1) -> { return false; }
 			sL(2),sL(1) -> { return false; }
 			sL(1),sL(2) -> { return false; }
 			sL(2),sL(3) -> { return false; }
 			sL(3),sL(2) -> { return false; }
 			sL(0),sL(0) -> { return true; }
 			sL(4),sL(4) -> { return true; }
 			sL(0),_     -> { return true; }
 			_    ,sL(4) -> { return true; }
 			sL(4),_     -> { return false; }
 			_    ,sL(0) -> { return false; }
 			_    ,_     -> { return true; }
     }
     throw new RuntimeException("should not be there");
   }
   
*/
