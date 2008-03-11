// gom accesscontrol.gom; tom *.t; javac *.java; tom policy/*.t; javac policy/*.java
import accesscontrol.*;
import accesscontrol.types.*;
import policy.*;
import java.util.*;

public class Tests {

  %include { accesscontrol/accesscontrol.tom }

	public static void main(String[] args) {
    SecurityLevelsLattice sls = `slLattice(slSet(sl("very low"),sl("low"),sl("medium"),sl("high"),sl("very high")));
    //     SecurityLevelsLattice sls = `slLattice(slSet(sl("medium"),sl("high")));
    //     SecurityLevelsLattice sls = `slLattice(slSet(sl("very low"),sl("low"),sl("high")));
    //SecurityLevelsLattice sls = `slLattice(slSet(sl("low"),sl("high")));

    Subject s0 = `subject(0,sl("very high"));
    Subject s1 = `subject(1,sl("high"));
    Subject s2 = `subject(2,sl("medium"));
    Subject s3 = `subject(3,sl("low"));
    Subject s4 = `subject(4,sl("very low"));

    Resource r4 = `resource(4,sl("very low"));
    Resource r3 = `resource(3,sl("low"));
    Resource r2 = `resource(2,sl("medium"));
    Resource r1 = `resource(1,sl("high"));
    Resource r0= `resource(0,sl("very high"));

    ListOfSubjects slist = `subjects(s1,s2,s3);
    ListOfResources rlist = `resources(r1,r2,r3);


    /*

    McLean mcl = new McLean(sls);
    State cs = `state(accesses());
    Decision result = null;
    Request req = null;

		System.out.println("START  BLP---------------------");
    Request req = `add(read(s1,r3));
    Decision result = blp.transition(req,cs);
		System.out.println();
		System.out.println("Request: "+req);
		System.out.println("Result:  "+result);
    cs = result.getstate();

    req = `add(write(s1,r2));
    result = blp.transition(req,cs);
		System.out.println();
		System.out.println("Request: "+req);
		System.out.println("Result:  "+result);
    cs = result.getstate();

    req = `add(read(s2,r2));
    result = blp.transition(req,cs);
		System.out.println();
		System.out.println("Request: "+req);
		System.out.println("Result:  "+result);
    cs = result.getstate();

    req = `add(write(s2,r1));
    result = blp.transition(req,cs);
		System.out.println();
		System.out.println("Request: "+req);
		System.out.println("Result:  "+result);
    cs = result.getstate();

		System.out.println("\nVALID STATUS: "+ blp.valid(cs)+"\n");
   
		System.out.println("START  McLean ---------------------");

    cs = `state(accesses());

    req = `add(write(s2,r2));
    result = mcl.transition(req,cs);
		System.out.println();
		System.out.println("Request: "+req);
		System.out.println("Result:  "+result);
    cs = result.getstate();

    req = `add(read(s2,r2));
    result = mcl.transition(req,cs);
		System.out.println();
		System.out.println("Request: "+req);
		System.out.println("Result:  "+result);
    cs = result.getstate();

		System.out.println("\nVALID STATUS: "+ mcl.valid(cs)+"\n");

		System.out.println("START  McLean ---------------------");

    cs = `state(accesses());

    req = `add(read(s1,r3));
    result = mcl.transition(req,cs);
		System.out.println();
		System.out.println("Request: "+req);
		System.out.println("Result:  "+result);
    cs = result.getstate();

    req = `add(write(s1,r2));
    result = mcl.transition(req,cs);
		System.out.println();
		System.out.println("Request: "+req);
		System.out.println("Result:  "+result);
    cs = result.getstate();

    req = `add(read(s2,r2));
    result = mcl.transition(req,cs);
		System.out.println();
		System.out.println("Request: "+req);
		System.out.println("Result:  "+result);
    cs = result.getstate();

    req = `add(write(s2,r1));
    result = mcl.transition(req,cs);
		System.out.println();
		System.out.println("Request: "+req);
		System.out.println("Result:  "+result);
    cs = result.getstate();

		System.out.println("\nVALID STATUS: "+ mcl.valid(cs)+"\n");
*/
 
    /*
     * check a configuration
     */
//     ListOfSubjects slist = `subjects(s1,s2,s3,s4);
//     ListOfResources rlist = `resources(r1,r2,r3,r4);
    int numberOfAccessMode = 2;
    
    sls = `slLattice(slSet(sl("low"),sl("high")));
    slist = `subjects(s1,s2);
    rlist = `resources(r1,r2,r3);

    ListOfRequests lor = genListOfRequests(slist,rlist,numberOfAccessMode);
    System.out.println("start with lor = " + lor);

    //System.out.println("check enum");
    //simplechecker(lor,lor,`requests());

    System.out.println("check McLean");
    runChecker(new McLean(sls),lor);

    System.out.println("check BLP");
    runChecker(new BLP(sls),lor);

    System.out.println("END");
	}
	
  /*
   * a naive checker
   */

  /**
	 * look for an information leakage
	 * 
	 * @param p the policy
	 * @param lor the list of requests to check
	 * print LEAKAGE with a trace in an information leakage is found 
	 */
  private static void runChecker(Policy p, ListOfRequests lor) {
    cacheValid.clear();
    cachePair.clear();
    checker(`state(accesses()),p,`requests(),lor,`traces());
  }


  /**
	 * generate a list of requests
	 * 
	 * @param subjects the list of subjects
	 * @param resources the list of resources
	 * @param numberOfAccessMode the number of different access mode
	 * @return a list of all requests that combines all possible accesses
	 */
  private static ListOfRequests genListOfRequests(ListOfSubjects subjects, ListOfResources resources, int numberOfAccessMode) {
    ListOfRequests res = `requests();
    %match(subjects, resources) {
      subjects(S1*,s,S2*), resources(R1*,r,R2*) -> {
        // we create a list with all possible access modes
        //for(int level = numberOfAccessMode-1 ; level>=0 ;  level--) {
        for(int level = 0 ; level<numberOfAccessMode ;  level++) {
          //res = `requests( add(access(level,s,r)), res*);// does not find a leakage
          res = `requests(res*, add(access(level,s,r)));// find a leakage easilly
        }
      }
    }
    return res;
  }

    /**
     * cacheValid: a global cache that stores valid states
     *            used to avoid call to the valid() predicate
     * cachePair: a global cache that stores pairs <state,lor>
     *            used to cut the search space and avoid doing a same work twice
     */
  private static Collection<State> cacheValid = new HashSet<State>();
  private static LRUCache<Pair,Boolean> cachePair = new LRUCache<Pair,Boolean>(300000);
  //private static HashMap<Pair,Boolean> cachePair = new HashMap<Pair,Boolean>();
  private static int nbAccesses = 100;
  /**
	 * look for an information leakage
	 * 
	 * @param s the current state (initially empty)
	 * @param p the policy
	 * @param previous the list of requests tthat have to be checked after the next grant (initially empty)
	 * @param lor the list of requests to check
	 * @param traces the trace that explain how the leakage has been found (initially empty)
	 * print LEAKAGE with a trace in an information leakage is found 
	 */
  private static int nbCut = 0;
  private static int nbAdd = 0;
  private static void checker(State s, Policy p, ListOfRequests previous, ListOfRequests lor, ListOfTraces traces) {
    Pair key = `pair(s,lor);
    if(cachePair.get(key) != null) {
      nbCut = (nbCut+1) % 10000;
      if(nbCut==0) {
        System.out.print(".");
      }
      return;
    } else {
      cachePair.put(key,Boolean.TRUE);
      nbAdd = (nbAdd+1) % 10000;
      if(nbAdd==0) {
        System.out.print("+");
      }
      //if(cachePair.size() % 1000 == 0) {
      //  System.out.println("cachePair size = " + cachePair.size());
      //}
    }

    %match(lor) {
      // sol1: be naive and generate all possible permutations
      requests(R1*,r@(add|delete)((read|write)(subject,resource)),R2*) -> {
        Decision decision1 = p.transition(`r,s);
        State newState = decision1.getstate();
        ListOfRequests nextLor;
        ListOfRequests nextPrevious;
        ListOfTraces newTraces = traces;
        if(decision1.isgrant()) {
          //System.out.print("+");
          newTraces = `traces(traces*,RequestToTrace(r),StateToTrace(newState));
          nextPrevious = `requests();
          nextLor = `requests(previous*,R1*,R2*);
        } else {
          //System.out.print(".");
          nextPrevious = `requests(previous*,R1*);
          nextLor = `requests(R2*);
        }

        checker(newState,p,nextPrevious,nextLor,newTraces);
        // note: do not perform a return, otherwise, the enumeration is stopped
      }

      //_ -> { // _ instead of requests() to allow sol2
      requests() -> { // with sol1 only
        // this part is executed after the previous match
        // do the validation when no more request matches
        if(!cacheValid.contains(s)) {
          if(p.valid(s) == false) {
            if(traces.length()<nbAccesses){
              nbAccesses = traces.length();
              System.out.println("LEAKAGE DETECTED ("+nbAccesses+")");
              while(!traces.isEmptytraces()) {
                System.out.println(traces.getHeadtraces());
                traces = traces.getTailtraces();
              }
              System.out.println("final state = " + s);
              //System.exit(0);
            }
          } else {
            cacheValid.add(s);
            if(cacheValid.size() % 1000 == 0) {
              System.out.println("#cacheValid = " + cacheValid.size());
            }
          }
        }
      }
    }
  }
 
    // just to check that the enumeration is correct
  private static void simplechecker(ListOfRequests goal, ListOfRequests lor, ListOfRequests trace) {
    //System.out.println("enter trace = " + trace);
    %match(lor) {
      requests(R1*,r,R2*) -> {
        ListOfRequests nextLor = `requests(R1*,R2*);
        // should be ntrace, and trace = ... should not be used
        ListOfRequests ntrace = `requests(trace*,r);
        simplechecker(goal, nextLor, ntrace);
      }

      _ -> { 
        //System.out.println("trace = " + trace);
        if(trace==goal) {
          System.out.println("GOAL");
        }
      }
    }
  }
}
