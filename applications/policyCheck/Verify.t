import java.util.*;

import verify.example.*;
import verify.example.types.*;

public class Verify{
	%include { sl.tom }
	%gom{ 
    module Example 
      imports int
      abstract syntax
      
      //Security levels : TS Top Secret, S Secret, C Confidential.
      SecurityLevel = sL(l:int)
      
      //Subject who has an ID for identification and a security level SL.
      Subject = subject(ID:int,SL:SecurityLevel)
      
      //Object who has an ID for identification and a security level SL.
      SecurityObject = securityObject(ID:int,SL:SecurityLevel)
      
      //Access mode, in this case could be read or write; others can be added
      AccessMode = aM(m:int)//read()|write()
      
      //Request type: 
      // add - to request the addition of an access,  
      // delete - to request the deletion of an access
      RequestType = add()|delete()
      
      // Sort of expression, explicit to specify that the access is explicit or conversely implicit
      // to specify that the access is implicit
      AccessType =explicit()|implicit()
      
      //An access is defined by the "subject" that accesses  the  "object" in "access mode" 
      //It can be explicitly requested by the subjects or an implicit consequence of other accesses
      Access = access(subject:Subject,securityObject:SecurityObject,A:AccessMode,E:AccessType)
      
      //Request, composed of a request type and an access
      Request = request(RT:RequestType, Ac:Access)
      
      //List of accesses
      ListOfAccesses =accesses(Access *)
      
      //State compose of a list of read accesses (Reads) and a list of write accesses(Write)
      // !!!!!!!!!!!!!! The AccessMode flag is not really necessary then !!!!!!!!!!!!!!
      State = state(Reads: ListOfAccesses,Writes: ListOfAccesses)
      
      //Sort representing a request upon a state
      RequestUponState = rus(R:Request,S:State)


      sort SecurityLevel:interface() { Comparable }
      sort SecurityLevel:block() {
        public int compareTo(SecurityLevel sl){
          return this.getl()-sl.getl();
        }
    }

  }

  

  int numberOfSubjects;
  int numberOfObjects;
  int numberOfSecurityLevels;
  int numberOfAccessModes;
 
  //   verify.example.types.state.state stateToVerify;
  State stateToVerify;
  ArrayList<ArrayList<Integer>> subjectSets;
  ArrayList<ArrayList<Integer>> objectSets;
  ArrayList<ArrayList<Integer>> subjecSetsXobjectSetSets;
  ArrayList<ArrayList<Integer>> allRequests;
  ArrayList<RequestUponState> implicitRequestsUponOriginalState;
  boolean LeakageDetected=false;
  RequestUponState CurrentRequestOfScenario;
  
  
  public Verify(int numberOfSubjects, int numberOfObjects, int numberOfSecurityLevels, int numberOfAccessModes){
    this.numberOfSubjects=numberOfSubjects;
    this.numberOfObjects=numberOfObjects;
    this.numberOfSecurityLevels=numberOfSecurityLevels;
    this.numberOfAccessModes=numberOfAccessModes;
  }


  public Response transition(RequestUponState req){
    %match (RequestUponState req){
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),aM(0),_)),
          s0@state(_,accesses(_*,access(subject(i1,l1),securityObject(i3,l3),aM(1),_),_*))  ) -> { 
//         if (!(compare(`l2,`l3))){
        if (`l2.compareTo(`l3)>0){
          return new Response(false,`s0);
        } 
      }
			rus(request(add(),access(subject(_,l1),securityObject(_,l2),aM(0),_)),s1) -> { 
//         if (!(compare(`l2,`l1))){
        if (`l2.compareTo(`l1)>0){
          return new Response(false,`s1);
        } 
      }
			rus(request(add(),a@access(_,_,aM(0),_)),state(e,i)) -> { 
        return new Response(true,`state(accesses(a,e),i)); 
      }
			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),aM(1),_)),
          s2@state(accesses(_*,access(subject(i1,l1),securityObject(_,l3),aM(0),_),_*),_)) -> { 
//         if (!(compare(`l3,`l2))){
        if (`l3.compareTo(`l2)>0){
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

  // Fixed configuration for Subjects and Objects; PG generates all possible requests
  public int[] checkAllPermutationsOfRequests(ArrayList<Subject> Subjects, ArrayList<SecurityObject> Objects, PermutationGenerator PG){
    while (PG.hasMore ()) {
      int[] currentPermutation=PG.getNext();
      if(!check(Subjects,Objects,currentPermutation)){
        return currentPermutation;
      }
      System.out.println("for permutation :"+toStringArray(currentPermutation));
    }
    int[] rep={0};
    return rep;
  }

  public boolean check(ArrayList<Subject> Subjects, ArrayList<SecurityObject> Objects, int[] permutationOfRequests){
    State M=`state(accesses(),accesses());
    for (int i = 0; i < permutationOfRequests.length; i++) {
      //System.out.println("permutationOfRequests :"+(i+1)+"/"+permutationOfRequests.length);
      ArrayList<Integer> requestIndexes=allRequests.get(permutationOfRequests[i]);
      Access a=`access(Subjects.get(requestIndexes.get(0)),Objects.get(requestIndexes.get(1)),(aM(requestIndexes.get(2))),explicit());
      Request r=`request(add(),a);
      RequestUponState rus=`rus(r,M);
      Response response=transition(rus);
      if (response.getGranted())M=response.getState();
      if (Verification(M)==false){
        System.out.println("Information leakage detected");
        return false;}
    }
    System.out.println("No information leakage detected");
    return true;
  }
  
  
  //public void generateSets(int numberOfSubjects, int numberOfObjects, int numberOfSecurityLevels, int numberOfAccessModes){
  public void generateSets(){
    Combinatory CSubjectSets=new Combinatory(numberOfSubjects,numberOfSecurityLevels-1);
    CSubjectSets.start();
    try {
      CSubjectSets.join();
    } catch (Exception e) {
      System.out.println("join on combination");
    }
    subjectSets=CSubjectSets.combination;

    Combinatory CObjectSets=new Combinatory(numberOfObjects,numberOfSecurityLevels-1);
    CObjectSets.start();
    try {
      CObjectSets.join();
    } catch (Exception e) {
      System.out.println("join on combination");
    }
    objectSets=CObjectSets.combination;

    LinkedList<Integer> sizeOfsets=new LinkedList<Integer>(); 
    sizeOfsets.add(subjectSets.size());
    sizeOfsets.add(objectSets.size());
    DirectProduct DPsubjecSetsXobjectSetSets=new DirectProduct(sizeOfsets);
    DPsubjecSetsXobjectSetSets.compute();
    subjecSetsXobjectSetSets=DPsubjecSetsXobjectSetSets.ProductsList;

    LinkedList<Integer> sizeOfsets2=new LinkedList<Integer>();
    sizeOfsets2.add(numberOfSubjects);
    sizeOfsets2.add(numberOfObjects);
    sizeOfsets2.add(numberOfAccessModes);
    DirectProduct DPsubjecXobjectXaccessMode=new DirectProduct(sizeOfsets2);
    DPsubjecXobjectXaccessMode.compute();
    allRequests=DPsubjecXobjectXaccessMode.ProductsList;
  }


  %strategy makeExplicit() extends `Identity() {
    visit State {
    	state(reads@accesses(X1*,access(s1,o1,aM(0),_),X2*,access(s2,o2,aM(0),_),X3*),
            writes@accesses(X4*,access(s,o,aM(1),_),X5*))->{
        if (`((s==s1 && o==o2))){
          ListOfAccesses l=`accesses(X1*,X2*,X3*);
          boolean contains=false;
          %match(l){
            accesses(X*,access(s3,o3,aM(0),_),Y*) ->{
              if (`s2==`s3 && `o1==`o3){
                contains=true;
              }
            }
          } 
          if (contains) return `state(reads,writes);
          else return `state(accesses(access(s2,o1,aM(0),implicit()),reads),writes);
        }else  if (`((s==s2 && o==o1))){
          ListOfAccesses l=`accesses(X1*,X2*,X3*);
          boolean contains=false;
          %match(l){
            accesses(X*,access(s3,o3,aM(0),_),Y*) ->{
              if (`s1==`s3 && `o2==`o3){
                contains=true;
              }
            }
          } 
          if (contains) return `state(reads,writes);
          else return `state(accesses(access(s1,o2,aM(0),implicit()),reads),writes);
        }
      }  
    }
  }

  public boolean Verification(State setOfAccesses){
    try {
      State res=(State)`RepeatId(makeExplicit()).visit(setOfAccesses);
      implicitRequestsUponOriginalState=new ArrayList<RequestUponState>();
      %match(res){
        state(e,accesses(_*,a@access(_,_,_,implicit()),_*))->{implicitRequestsUponOriginalState.add(`rus(request(add(),a),setOfAccesses));}
        state(accesses(_*,a@access(_,_,_,implicit()),_*),e)->{implicitRequestsUponOriginalState.add(`rus(request(add(),a),setOfAccesses));}
      }
      for(Iterator<RequestUponState> iterator=implicitRequestsUponOriginalState.iterator(); iterator.hasNext();){
        RequestUponState iruos=(RequestUponState)iterator.next();
        if (!(transition(iruos).getGranted())){
          CurrentRequestOfScenario=iruos;
          System.out.println("Scenario detected :"+CurrentRequestOfScenario);
          return false;
        }
      }


    } catch (Exception e) {
      System.out.println("A problem occured while applying strategy");
    }

    return true;

  }

  public void checkRandomSets(){
    //generateSets(numberOfSubjects,numberOfObjects,numberOfSecurityLevels,numberOfAccessModes);
    generateSets();
    ArrayList<Subject> Subjects=new ArrayList<Subject>();
    ArrayList<SecurityObject> Objects=new ArrayList<SecurityObject>();
    int indexSubjectSet=(int)(Math.random()*subjectSets.size());
    int indexObjectSet=(int)(Math.random()*objectSets.size());
    int i=0;
    for (Iterator<Integer> iterator = (subjectSets.get(indexSubjectSet)).iterator(); iterator.hasNext();) {
      Integer securityLevel = iterator.next();
      Subjects.add(`subject(i,sL(securityLevel)));

      i++; 
    }
    System.out.println("Generated Subjects :"+Subjects);
    i=0;
    for (Iterator<Integer> iterator = (objectSets.get(indexObjectSet)).iterator(); iterator.hasNext();) {
      Integer securityLevel = iterator.next();
       Objects.add(`securityObject(i,sL(securityLevel)));
      i++;
    }
    System.out.println("Generated Objects :"+Objects);
    PermutationGenerator PG= new PermutationGenerator (allRequests.size());
    int[] o=checkAllPermutationsOfRequests(Subjects,Objects,PG);
    if (o.length==1){
      System.out.println("No leakage detected for all permutations");
    }else{
      //System.out.println("subjectSet: ="+subjectSets.get(indexSubjectSet));
      //System.out.println("objectSet: "+objectSets.get(indexObjectSet));
    }

  }


  //public void checkAllSets(int numberOfSubjects, int numberOfObjects, int numberOfSecurityLevels, int numberOfAccessModes){
  public boolean checkSpecificSets(int iss,int ios){
    //generateSets(numberOfSubjects,numberOfObjects,numberOfSecurityLevels,numberOfAccessModes);
    generateSets();
    int indexSubjectSet=iss;
    int indexObjectSet=ios;
    System.out.println("current Subject Set :"+indexSubjectSet);
    System.out.println("current Object Set :"+indexObjectSet);
    ArrayList<Subject> Subjects=new ArrayList<Subject>();
    ArrayList<SecurityObject> Objects=new ArrayList<SecurityObject>();
    int i=0;
//     for (Iterator<Integer> iterator = (subjectSets.get(indexSubjectSet)).iterator(); iterator.hasNext();) {
//       Integer securityLevel = iterator.next();
    for (Integer securityLevel: subjectSets.get(indexSubjectSet)) {
      Subjects.add(`subject(i,sL(securityLevel)));
      i++;
    }
    i=0;
    for (Iterator<Integer> iterator = (objectSets.get(indexObjectSet)).iterator(); iterator.hasNext();) {
      Integer securityLevel =  iterator.next();
      Objects.add(`securityObject(i,sL(securityLevel)));
      i++;
    }

    PermutationGenerator PG= new PermutationGenerator (allRequests.size());
    int[] o=checkAllPermutationsOfRequests(Subjects,Objects,PG);
    System.out.println("For subjects :\n"+Subjects);
    System.out.println("and objects :\n"+Objects);
    if (o.length==1){
      System.out.println("No leakage detected for all permutations");
    }else{
      LeakageDetected=true;
      System.out.println("Leakage detected for permutations :\n"+toStringArray(o));
      return false;
    }



    return true;
  }






  public boolean checkAllSets(){
    //generateSets(numberOfSubjects,numberOfObjects,numberOfSecurityLevels,numberOfAccessModes);
    generateSets();
    for(int indexSubjectSet=0; indexSubjectSet<subjectSets.size();indexSubjectSet++){
      for(int indexObjectSet=0; indexObjectSet<objectSets.size();indexObjectSet++){
        System.out.println("current Subject Set :"+indexSubjectSet);
        System.out.println("current Object Set :"+indexObjectSet);
        ArrayList<Subject> Subjects=new ArrayList<Subject>();
        ArrayList<SecurityObject> Objects=new ArrayList<SecurityObject>();
        int i=0;
        for (Iterator<Integer> iterator = (subjectSets.get(indexSubjectSet)).iterator(); iterator.hasNext();) {
          Integer securityLevel = iterator.next();
          Subjects.add(`subject(i,sL(securityLevel)));
          i++;
        }
        i=0;
        for (Iterator<Integer> iterator = (objectSets.get(indexObjectSet)).iterator(); iterator.hasNext();) {
          Integer securityLevel = iterator.next();
          Objects.add(`securityObject(i,sL(securityLevel)));
          i++;
        }

        PermutationGenerator PG= new PermutationGenerator (allRequests.size());
        int[] o=checkAllPermutationsOfRequests(Subjects,Objects,PG);
        System.out.println("For subjects :\n"+Subjects);
        System.out.println("and objects :\n"+Objects);
        if (o.length==1){
          System.out.println("No leakage detected for all permutations");
        }else{
          LeakageDetected=true;
          System.out.println("Leakage detected for permutations :\n"+toStringArray(o));
          return false;
        }


      }}
    return true;
  }

  public static void main(String[] args) {
    //Verify(int numberOfSubjects, int numberOfObjects, int numberOfSecurityLevels, int numberOfAccessModes);
    Verify Verification=new Verify(2,2,2,2);
    //Verification.checkSpecificSets(0,1);
    Verification.checkAllSets();
    System.out.println(((Verification.LeakageDetected)?"a leakage is detected":"no leakage is detected"));
  }

  public static String toStringArray(int[] t){
    String result="[";
    if (t.length>0){
      result+=t[0];
      for (int i = 1; i < t.length; i++) {
        result+=", "+t[i];
      }
    }
    result+="]";
    return result;
  }

}




//   public Response transitionold(RequestUponState req){
//     %match (req){
// 			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),read(),_)),s0@state(_,accesses(_*,access(subject(i1,l1),securityObject(i3,l3),write(),_),_*)))-> { if (!(compare(`l2,`l3))){return new Response(false,`s0);} }
// 			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),read(),_)),s1)-> { if (!(compare(`l2,`l1))){return new Response(false,`s1);} }
// 			rus(request(add(),a@access(subject(i1,l1),securityObject(_,l2),read(),_)),state(e,i))-> { return new Response(true,`state(accesses(a,e),i)); }
// 			rus(request(add(),access(subject(i1,l1),securityObject(_,l2),write(),_)),
//           s2@state(accesses(_*,access(subject(i1,l1),securityObject(i3,l3),read(),_),_*),_))-> { if (!(compare(`l3,`l2))){return new Response(false,`s2);} }
// 			rus(request(add(),a@access(subject(i1,l1),securityObject(_,l2),write(),_)),state(i,e))-> {return new Response(true,`state(i,accesses(a,e))); }
// 			rus(request(delete(),access(subject(i1,l1),securityObject(_,l2),read(),_)),
//           state(accesses(X*,access(subject(i1,l1),securityObject(i2,l2),read(),_),Y*),i))-> { return new Response(true,`state(accesses(X*,Y*),i)); }
//       rus(request(delete(),access(subject(i1,l1),securityObject(_,l2),write(),_)),
//           state(i,accesses(X*,access(subject(i1,l1),securityObject(i2,l2),write(),_),Y*)))-> { return new Response(true,`state(i,accesses(X*,Y*))); }
//     }
//     throw new RuntimeException("should not be there");
//   }


//   // why not compareTo
//   public static boolean compare(SecurityLevel l1,SecurityLevel l2){
//     %match (l1,l2){
// // 			S(),TS() -> { return ????; }


// 			TS(),S() -> { return false; }
// 			TS(),C() -> { return false; }
// 			S(),C() -> { return false; }
// 			_,_ -> { return true; }
			
//     }
//     throw new RuntimeException("should not be there");
//   }
