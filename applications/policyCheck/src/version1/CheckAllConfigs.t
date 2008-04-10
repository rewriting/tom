import java.util.*;
import accesscontrol.*;
import accesscontrol.types.*;
import java.util.ArrayList;
import policy.BellAndLaPadula;
import policy.McLean;
import policy.Policy;
import policy.FlowPolicy;
import mathsEnumeration.PermutationGenerator;

public class CheckAllConfigs{
	%include { sl.tom }
	%include { accesscontrol/accesscontrol.tom }
	
	//Policy to check
	Policy policy;
	//Generated sets
	GenerateSets generatedSets;
	// Flag for detecting a leakage
	boolean leakageDetected=false;
	
  
	// Constructor
	public CheckAllConfigs(int numberOfSubjects, int numberOfObjects, int numberOfSecurityLevels, int numberOfAccessModes, Policy p) {
		this.generatedSets=new GenerateSets(numberOfSubjects,
				numberOfSecurityLevels,
				numberOfObjects,
				numberOfAccessModes);
		this.policy=p;
	}

	//Constructor
	public CheckAllConfigs(int numberOfSubjects, int numberOfObjects, Policy p) {
		this.generatedSets=new GenerateSets(numberOfSubjects,numberOfSecurityLevels(((FlowPolicy)p).securityLevelsOrderImproved),numberOfObjects,2);
		this.policy=p;
	}

	// check all possibilities
	public boolean checkAllSets() {
		boolean result=true;
		//    generateSets();
		//for each subject set
		for(int indexSubjectSet=0; indexSubjectSet<generatedSets.subjectSets.size();indexSubjectSet++) {
			//for each object set
			for(int indexObjectSet=0; indexObjectSet<generatedSets.objectSets.size();indexObjectSet++) {
				System.out.println("current Subject Set :"+indexSubjectSet);
				System.out.println("current Object Set :"+indexObjectSet);
				ArrayList<Subject> subjects=new ArrayList<Subject>();
				ArrayList<SecurityObject> objects=new ArrayList<SecurityObject>();
				//	Create the configuration (subjects)
				int i=0;
				for (Iterator<Integer> iterator = (generatedSets.subjectSets.get(indexSubjectSet)).iterator(); iterator.hasNext();) {
					Integer securityLevel = iterator.next();
					subjects.add(`subject(i,sl(securityLevel)));
					i++;
				}
				//	Create the configuration (objects)
				i=0;
				for (Iterator<Integer> iterator = (generatedSets.objectSets.get(indexObjectSet)).iterator(); iterator.hasNext();) {
					Integer securityLevel = iterator.next();
					objects.add(`securityObject(i,sl(securityLevel)));
					i++;
				}
				// 	Check all the permutations of requests upon the given configuration
				CheckAllPermutationsOfRequests capor=new CheckAllPermutationsOfRequests(subjects,objects,policy,generatedSets.allRequests);
				boolean o=capor.checkConfiguration();
				System.out.println("For subjects :\n"+subjects);
				System.out.println("and objects :\n"+objects);
				if (!o) {
					System.out.println("No leakage detected for all permutations");
				} else {
					leakageDetected=true;
					System.out.println("Leakage detected");
					result=false;
				}
			}
		}
		return result;
	}
  

  
	//Calculate the number of security levels
	public static int numberOfSecurityLevels(PartiallyOrderdedSetOfSecurityLevels sol) {
		HashSet<SecurityLevel> securityLevels=new HashSet<SecurityLevel>();
		%match(sol) {
			setsl(_*,cl(_*,l,_*),_*)->{securityLevels.add(`l);}
		}
		return securityLevels.size();
	} 

	public static void main(String[] args) {
		/*
		Example of arguments
	
		Sets(CL(sL(0),sL(1),sL(2)),CL(sL(1),sL(3)),CL(sL(2),sL(4))) 5 2 2 2 0
	
		 */
		String order=args[0];
		int numberOfSecurityLevels =Integer.parseInt(args[1]);
		int numberOfSubjects=Integer.parseInt(args[2]);
		int numberOfObjects=Integer.parseInt(args[3]);
		int numberOfAccessMode=Integer.parseInt(args[4]);
		Policy p;
		if (args[5].equals("0")){
			p=new BellAndLaPadula(PartiallyOrderdedSetOfSecurityLevels.fromString(args[0]));
		} else {
			p=new McLean(PartiallyOrderdedSetOfSecurityLevels.fromString(args[0]));
		}
		CheckAllConfigs verification=new CheckAllConfigs(numberOfSubjects,numberOfObjects,numberOfSecurityLevels,numberOfAccessMode,p);
		verification.checkAllSets();
		System.out.println(((verification.leakageDetected)?"leakages are detected":"no leakage is detected"));
	}


}
