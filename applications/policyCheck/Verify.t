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

      //Implementation of the interface Comparable for the sort SecurityLevel
      sort SecurityLevel:interface() { Comparable }
      sort SecurityLevel:block() {
        public int compareTo(SecurityLevel sl){
          return this.getl()-sl.getl();
        }
    }

  }

  
	//Number of subjects within each of the Subject sets generated 
	int numberOfSubjects;
	//Number of objects within each of the Subject sets generated 
	int numberOfObjects;
	//Number of security levels   
	int numberOfSecurityLevels;
	//Number of access modes
	int numberOfAccessModes;
	//Policy to check
	Policy policy;
	
	//State stateToVerify;
	
	// Set of Subject Sets (a subject set is a set of binomials [a,b] where "a" is an integer subject identifier
	// and "b" is an integer representing the security level of the subject    
	ArrayList<ArrayList<Integer>> subjectSets;
	// Set of Object Sets (a object set is a set of binomials [a,b] where "a" is an integer object identifier
	// and "b" is an integer representing the security level of the object    
	ArrayList<ArrayList<Integer>> objectSets;
	// Direct products of subject sets with objects sets
	ArrayList<ArrayList<Integer>> subjecSetsXobjectSetSets;
	// Set of all possible requests, a request is a triplet of 3 integers (the subject identifier, the object identifier and the access mode)
	ArrayList<ArrayList<Integer>> allRequests;
	// Set of computed implicit requests
	ArrayList<RequestUponState> implicitRequestsUponOriginalState;
	// Flag for detecting a leakage
	boolean LeakageDetected=false;
	// Request creating a leakage
	RequestUponState CurrentRequestOfScenario;
  
  // Constructor
  public Verify(int numberOfSubjects, int numberOfObjects, int numberOfSecurityLevels, int numberOfAccessModes, Policy p){
    this.numberOfSubjects=numberOfSubjects;
    this.numberOfObjects=numberOfObjects;
    this.numberOfSecurityLevels=numberOfSecurityLevels;
    this.numberOfAccessModes=numberOfAccessModes;
    this.policy=p;
  }




  // Fixed configuration for Subjects and Objects; PG generates all permutation of requests
  public int[] checkAllPermutationsOfRequests(ArrayList<Subject> Subjects, ArrayList<SecurityObject> Objects){
	  PermutationGenerator PG= new PermutationGenerator (allRequests.size());
      //foreach permutation
	  while (PG.hasMore ()) {
		  //get a permuation
		  int[] currentPermutation=PG.getNext();
		  //check this permutation of requests with the fixed configuration of Subjects and objects
		  if(!check(Subjects,Objects,currentPermutation)){
			  //return the current permutation if it generates a leakage 
			  return currentPermutation;
		  }
      System.out.println("for permutation :"+toStringArray(currentPermutation));
    }
	
    int[] rep={0};
    //return the array rep if no leakage is detected  
    return rep;
  }

 



  // For a fixed configuration of subjects and objects and a given permutation for requests  
  public boolean check(ArrayList<Subject> Subjects, ArrayList<SecurityObject> Objects, int[] permutationOfRequests){
	  //Create an empty state
	  State M=`state(accesses(),accesses());
      //for each request whose index in "allRequests" is determined by "permutationOfRequests" 
	  for (int i = 0; i < permutationOfRequests.length; i++) {
		  //the request represented by 3 indexes , the subject, the object and the access mode
      ArrayList<Integer> requestIndexes=allRequests.get(permutationOfRequests[i]);
      	  //create an access
      Access a=`access(Subjects.get(requestIndexes.get(0)),Objects.get(requestIndexes.get(1)),(aM(requestIndexes.get(2))),explicit());
      	  // create the corresponding request 	
      Request r=`request(add(),a);
      	  // create the binomial (request,state)  
      RequestUponState rus=`rus(r,M);
      	  // try to add the access to the state given the implementation of the policy
      Response response=policy.transition(rus);
      	  // if the request is granted, get the new state
      if (response.getGranted())M=response.getState();
      	  // if the request fails generate an error message an return false
      if (Verification(M)==false){
        System.out.println("Information leakage detected");
        return false;}
	  }
	  	  //else generate a message and return true 
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

  // Make explicit implicit accesses
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

  //Verification of state
  public boolean Verification(State setOfAccesses){
    try {
    	//make explicit implicit accesses
      State res=(State)`RepeatId(makeExplicit()).visit(setOfAccesses);
      implicitRequestsUponOriginalState=new ArrayList<RequestUponState>();
      	//add implicit accesses to "implicitRequestsUponOriginalState"
      %match(res){
        state(e,accesses(_*,a@access(_,_,_,implicit()),_*))->{implicitRequestsUponOriginalState.add(`rus(request(add(),a),setOfAccesses));}
        state(accesses(_*,a@access(_,_,_,implicit()),_*),e)->{implicitRequestsUponOriginalState.add(`rus(request(add(),a),setOfAccesses));}
      }

      //for each implicit access 
      for(Iterator<RequestUponState> iterator=implicitRequestsUponOriginalState.iterator(); iterator.hasNext();){
        RequestUponState iruos=(RequestUponState)iterator.next();
        //test if the implicit access is accepted
        if (!(policy.transition(iruos).getGranted())){
        	//behavior if the access is not granted which means that there is a leakage
          CurrentRequestOfScenario=iruos;
          System.out.println("Scenario detected :"+CurrentRequestOfScenario);
          return false;
        }
      }


    } catch (Exception e) {
      System.out.println("A problem occured while applying strategy");
    }
    // behavior if the access is granted
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
    int[] o=checkAllPermutationsOfRequests(Subjects,Objects);
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

    int[] o=checkAllPermutationsOfRequests(Subjects,Objects);
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





  // check all possibilities
  public boolean checkAllSets(){
    generateSets();
    //for each subject set
    for(int indexSubjectSet=0; indexSubjectSet<subjectSets.size();indexSubjectSet++){
    	//for each object set
    	for(int indexObjectSet=0; indexObjectSet<objectSets.size();indexObjectSet++){
        System.out.println("current Subject Set :"+indexSubjectSet);
        System.out.println("current Object Set :"+indexObjectSet);
        ArrayList<Subject> Subjects=new ArrayList<Subject>();
        ArrayList<SecurityObject> Objects=new ArrayList<SecurityObject>();
        
        //Create the configuration (subjects)
        int i=0;
        for (Iterator<Integer> iterator = (subjectSets.get(indexSubjectSet)).iterator(); iterator.hasNext();) {
          Integer securityLevel = iterator.next();
          Subjects.add(`subject(i,sL(securityLevel)));
          i++;
        }
        //Create the configuration (objects)
        i=0;
        for (Iterator<Integer> iterator = (objectSets.get(indexObjectSet)).iterator(); iterator.hasNext();) {
          Integer securityLevel = iterator.next();
          Objects.add(`securityObject(i,sL(securityLevel)));
           i++;
        }
        // Check all the permutations of requests upon the given configuration
        int[] o=checkAllPermutationsOfRequests(Subjects,Objects);
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
	  Policy p=new McLean();
	  Verify Verification=new Verify(2,3,5,2,p);
    //Verification.checkSpecificSets(0,1);
    //Verification.checkAllSetsMcClean();
    //Verification.checkAllSets();
    //System.out.println(((Verification.LeakageDetected)?"a leakage is detected":"no leakage is detected"));
    Verification.generateSets();
    ArrayList<Subject> Subjects=new ArrayList<Subject>();
    ArrayList<SecurityObject> Objects=new ArrayList<SecurityObject>();
    Subjects.add(`subject(0,sL(3)));
    Subjects.add(`subject(1,sL(2)));
    Objects.add(`securityObject(0,sL(1)));
    Objects.add(`securityObject(1,sL(2)));
    Objects.add(`securityObject(2,sL(3)));
    int[] o=Verification.checkAllPermutationsOfRequests(Subjects,Objects);
    System.out.println("For subjects :\n"+Subjects);
    System.out.println("and objects :\n"+Objects);
    if (o.length==1){
    System.out.println("No leakage detected for all permutations");
    }else{
    System.out.println("Leakage detected for permutations :\n"+toStringArray(o));
    }
    //System.out.println(((Verification.LeakageDetected)?"a leakage is detected":"no leakage is detected"));
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
