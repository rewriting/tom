// gom accesscontrol.gom; tom *.t; javac *.java; tom policy/*.t; javac policy/*.java
import accesscontrol.*;
import accesscontrol.types.*;
import policy.*;
import java.util.*;

public class Tests {

  %include { accesscontrol/accesscontrol.tom }

	public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    scan.useDelimiter("");

    Subject s0 = `subject(0,sl("very high"));
    Subject s1 = `subject(1,sl("high"));
    Subject s2 = `subject(2,sl("medium"));
    Subject s3 = `subject(3,sl("low"));
    Subject s4 = `subject(4,sl("very low"));

    Resource r0 = `resource(0,sl("very high"));
    Resource r1 = `resource(1,sl("high"));
    Resource r2 = `resource(2,sl("medium"));
    Resource r3 = `resource(3,sl("low"));
    Resource r4 = `resource(4,sl("very low"));
    Resource r5 = `resource(5,sl("5"));
    Resource r6 = `resource(6,sl("6"));
    Resource r7 = `resource(7,sl("7"));
    Resource r8 = `resource(8,sl("8"));
 
    /*
     * check a configuration
     */
    int numberOfAccessMode = 2;
    SecurityLevelsLattice sls = `slLattice();
    //SecurityLevelsLattice sls = `slLattice(slSet(sl("low"),sl("high")));
    //SecurityLevelsLattice sls = `slLattice(slSet(sl("low"),sl("medium"),sl("high")));
    //SecurityLevelsLattice sls = `slLattice(slSet(sl("very low"),sl("low"),sl("medium"),sl("high")));

    //ListOfSubjects slist = `subjects(s3,s1);
    //ListOfResources rlist = `resources(r3,r1);
    //ListOfSubjects slist = `subjects(s3,s2,s1);
    //ListOfResources rlist = `resources(r3,r2,r1);
    ListOfSubjects slist = `subjects(s3,s2,s1);
    ListOfResources rlist = `resources(r3,r2,r1);
    //ListOfSubjects slist = `subjects(s4,s3,s2,s1,s0);
    //ListOfResources rlist = `resources(r4,r3,r2,r1,r0);

    ListOfRequests lor = genListOfRequests(slist,rlist,numberOfAccessMode);
    System.out.println("start with lor = " + lor);

    //lor = `simplifyRequests(sls,lor);
    //System.out.println("start with lor = " + lor);

    Policy mcl = new McLean(sls);

    System.out.println("check McLean");
    runChecker(mcl,lor);
    System.out.println("END McLean"+nbStates);

    System.out.println("check BLP");
    runChecker(new BLP(sls),lor);

    System.out.println("END");
	}
	
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
    otherChecker(`state(accesses()),p,lor,`traces());
    //checker(`state(accesses()),p,`requests(),lor,`traces());
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
  private static LRUCache<State,Boolean> cacheValid = new LRUCache<State,Boolean>(100000);
  private static LRUCache<Pair,Boolean> cachePair = new LRUCache<Pair,Boolean>(100000);
  private static int nbAccesses = 100;
  private static int nbStates = 0;


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
    nbStates++;
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
          //newTraces = `traces(RequestToTrace(r),StateToTrace(newState),traces*);
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
        if(cacheValid.get(s) != null) {
          if(p.valid(s) == false) {
            if(traces.length()<nbAccesses){
              nbAccesses = traces.length();
              System.out.println("\nLEAKAGE DETECTED ("+nbAccesses+")");
              ListOfTraces t = traces.reverse();
              while(!t.isEmptytraces()) {
                System.out.println(traces.getHeadtraces());
                t = t.getTailtraces();
              }
              System.out.println("final state = " + s);
            }
          } else {
            cacheValid.put(s,Boolean.TRUE);
          }
        }
      }
    }
  }

  /**
	 * look for an information leakage
	 * 
	 * @param s the current state (initially empty)
	 * @param p the policy
	 * @param lor the list of requests to check
	 * @param traces the trace that explain how the leakage has been found (initially empty)
	 * print LEAKAGE with a trace in an information leakage is found 
	 */
  private static void otherChecker(State s, Policy p, ListOfRequests lor, ListOfTraces traces) {
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
    }

    if(p.valid(s) == false) {
      if(traces.length()<nbAccesses) {
        nbAccesses = traces.length();
        System.out.println("***LEAKAGE DETECTED ("+nbAccesses+")");
        ListOfTraces t = traces.reverse();
        while(!t.isEmptytraces()) { 
          System.out.println(t.getHeadtraces());
          t = t.getTailtraces();
        }
        System.out.println("final state = " + s);
      }
      return;
    }

    %match(lor) {
      // sol1: be naive and generate all possible permutations
      requests(R1*,r@(add|delete)((read|write)(subject,resource)),R2*) -> {
        Decision decision1 = p.transition(`r,s);
        State newState = decision1.getstate();
        ListOfRequests nextLor;
        ListOfTraces newTraces = traces;
        if(decision1.isgrant()) {
          //System.out.print("+");
          //newTraces = `traces(RequestToTrace(r),StateToTrace(newState),traces*);
          nextLor = `requests(R1*,R2*);
          otherChecker(newState,p,nextLor,newTraces);
        } 
//           If the access is not granted then it will be never granted
//           independently of the accesses that would be added to the access list
//           --> this is not true if we can have not only ADD(access) but also REMOVE(access)
//         else {
//           //System.out.print(".");
//           nextPrevious = `requests(previous*,R1*);
//           nextLor = `requests(R2*);
//         }
//         otherChecker(newState,p,nextPrevious,nextLor,newTraces);
         // note: do not perform a return, otherwise, the enumeration is stopped
      }
    }
  }
 
  private static ListOfRequests clearRequests(SecurityLevelsLattice slL, Request r, ListOfRequests lor) {
    if(lor.isEmptyrequests()){
      return `requests();
    }

    %match(r) {
			add(read(s1@subject[sl=ssl1],resource[sl=rsl1]))  -> {
        %match(lor) {
          requests(R1*,add(read(s2@subject[sl=ssl2],resource[sl=rsl2])),R2*) -> {
            if(`s1==`s2 && `slL.ge(`rsl1,`rsl2)) {
              return `clearRequests(slL,r,requests(R1*,R2*));
            } 
          }
          _ -> {
            return lor;
          }
//               ListOfRequests lr =  `clearRequests(slL,r,requests(R1*,R2*));
//               return `requests(a,lr*);
        }
      }
			add(write(s1@subject[sl=ssl1],resource[sl=rsl1]))  -> {
        %match(lor) {
          requests(R1*,add(write(s2@subject[sl=ssl2],resource[sl=rsl2])),R2*) -> {
            if(`s1==`s2 && `slL.ge(`rsl2,`rsl1)) {
              return `clearRequests(slL,r,requests(R1,R2));
            }
          } 
          _ -> {
            return lor;
          }
        }
      }
    }
    //ERROR
    return `requests();
  }


  private static ListOfRequests simplifyRequests(SecurityLevelsLattice slL, ListOfRequests lor) {
    if(lor.isEmptyrequests()){
      return `requests();
    }

    %match(lor) {
      requests(R1*,a@add(read(s1,resource[sl=rsl1])),R0*,add(read(s1,resource[sl=rsl2])),R2*) -> {
        if(`slL.ge(`rsl1,`rsl2)) {
          return `simplifyRequests(slL,requests(R1*,a,R0*,R2*));
        } 
      }
      requests(R1*,add(read(s1,resource[sl=rsl2])),R0*,a@add(read(s1,resource[sl=rsl1])),R2*) -> {
        if(`slL.ge(`rsl1,`rsl2)) {
          return `simplifyRequests(slL,requests(R1*,R0*,a,R2*));
        } 
      }

      requests(R1*,a@add(write(s1,resource[sl=rsl1])),R0*,add(write(s1,resource[sl=rsl2])),R2*) -> {
            if(`slL.ge(`rsl2,`rsl1)) {
              return `simplifyRequests(slL,requests(R1*,a,R0*,R2*));
            }
      } 
      requests(R1*,add(write(s1,resource[sl=rsl2])),R0*,a@add(write(s1,resource[sl=rsl1])),R2*) -> {
            if(`slL.ge(`rsl2,`rsl1)) {
              return `simplifyRequests(slL,requests(R1*,R0*,a,R2*));
            }
      } 
      _ -> {
        return lor;
      }
    }
    //ERROR
    return `requests();
  }

}
