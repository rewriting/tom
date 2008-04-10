import java.util.*;
import accesscontrol.*;
import accesscontrol.types.*;
import java.util.ArrayList;
import policy.Policy;
import policy.FlowPolicy;
import mathsEnumeration.Product;
import mathsEnumeration.PermutationGenerator;
import java.io.*;
public class CheckAllPermutationsOfRequests{
%include { sl.tom }
	%include { accesscontrol/accesscontrol.tom }


	//	The list of subjects of the configuration
	ArrayList<Subject> subjects;
	//The list of objects of the configuration
	ArrayList<SecurityObject> objects;
	//The type of policy to verify with
	Policy policy;
	//All permutations of requests
	ArrayList<ArrayList<Integer>> allRequests;

	// Constructor of the Object used for computing
	CheckAllPermutationsOfRequests(ListOfSubjects ListOfSubjects, ListOfObjects ListOfObjects,Policy policy) {
		int numberOfAccessModes=2;
		this.policy=policy;
		this.subjects=ListToArrayS(ListOfSubjects);
		this.objects=ListToArrayO(ListOfObjects);
		List<Integer> sizeOfsets=new ArrayList<Integer>();
		sizeOfsets.add(subjects.size());
		sizeOfsets.add(objects.size());
		sizeOfsets.add(numberOfAccessModes);
		this.allRequests=Product.getProduct(sizeOfsets);
	}

	// Converts a list of subjects (ListOfSubjects) to an ArrayList of subjects 
	public static ArrayList<Subject> ListToArrayS(ListOfSubjects L) {
		ArrayList<Subject> S=new ArrayList<Subject>();
		%match(L){
			subjects(_*,x,_*)->{S.add(`x);}
		}
		return S;
	}

	// Converts a list of objects (ListOfObjects) to an ArrayList of objects 
	public static ArrayList<SecurityObject> ListToArrayO(ListOfObjects L) {
		ArrayList<SecurityObject> O=new ArrayList<SecurityObject>();
		%match(L){
			objects(_*,x,_*)->{O.add(`x);}
		}
		return O;
	}


	//Constructor
	CheckAllPermutationsOfRequests(ArrayList<Subject> subjects, ArrayList<SecurityObject> objects,Policy policy) {
		int numberOfAccessModes=2;
		this.policy=policy;
		this.subjects=subjects;
		this.objects=objects;
		List<Integer> sizeOfsets=new ArrayList<Integer>();
		sizeOfsets.add(subjects.size());
		sizeOfsets.add(objects.size());
		sizeOfsets.add(numberOfAccessModes);
		this.allRequests=Product.getProduct(sizeOfsets);
	}

	//Constructor
	CheckAllPermutationsOfRequests(ArrayList<Subject> subjects, ArrayList<SecurityObject> objects,Policy policy,ArrayList<ArrayList<Integer>> allRequests) {
		this.policy=policy;
		this.subjects=subjects;
		this.objects=objects;
		this.allRequests=allRequests;
	}



	// Fixed configuration for subjects and objects; pg generates all permutation of requests
	public boolean checkConfiguration() {
		boolean b=false;
		PermutationGenerator pg= new PermutationGenerator (allRequests.size());
		//foreach permutation
		while (pg.hasMore ()) {
			//	get a permuation
			int[] currentPermutation=pg.getNext();
			//check this permutation of requests with the fixed configuration of subjects and objects
			System.out.println("for permutation :"+Arrays.toString(currentPermutation));
			if(!check(currentPermutation)){
				b=true;
				try {
					BufferedReader waiter = new BufferedReader(new InputStreamReader(System.in));
					waiter.readLine();
				} catch (Exception e) {}
			}   
		}
		
		//	return the array rep if no leakage is detected  
		return b;
	}	

	// Creates a request
	public Request createRequest(ArrayList<Subject> subjects,ArrayList<SecurityObject> objects,ArrayList<Integer> requestIndexes) {	  
		//create an access
		Access a=`access(subjects.get(requestIndexes.get(0)),objects.get(requestIndexes.get(1)),(am(requestIndexes.get(2))),explicit());
		// 	create the corresponding request 	
		return `request(add(),a);  
	}




	// For a fixed configuration of subjects and objects and a given permutation for requests  
	public boolean check( int[] permutationOfRequests) {
		//boolean to store the check result  
		boolean result=true;
		//Create an empty state
		State m=`state(accesses(),accesses());
		//for each request whose index in "allRequests" is determined by "permutationOfRequests" 
		for (int i = 0; i < permutationOfRequests.length; i++) {
			//Get the element i from the list "permutationOfRequests" where it is represented by 3 indexes: the subject, the object and the access mode
			//and generate the couple (request,state)  
			RequestUponState rus=`rus(createRequest(subjects,objects,allRequests.get(permutationOfRequests[i])),m);
			// try to add the access to the state given the implementation of the policy
			Object[] checkResult=((FlowPolicy)policy).check(rus); 
			//set the result boolean verifying leakage detection
			result=(Boolean)checkResult[1];
			if (!result)break;
			//update the current state
			m=(State)checkResult[0];		  
		}
	  //else generate a message and return true
	if (result) System.out.println("No information leakage detected");
    
    return result;
  }

}