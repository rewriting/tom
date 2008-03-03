// gom accesscontrol.gom; tom *.t; javac *.java; tom policy/*.t; javac policy/*.java
import accesscontrol.*;
import accesscontrol.types.*;
import policy.*;
import java.util.*;

public class Tests {

  %include { accesscontrol/accesscontrol.tom }

	public static void main(String[] args) {
    //     SecurityLevelsLattice sls = `slLattice(slSet(sl("very low"),sl("low")),slSet(sl("high"),sl(4),sl(5)));
    SecurityLevelsLattice sls = `slLattice(slSet(sl("low"),sl("high")));


    BLP blp = new BLP(sls);
    State cs = `state(accesses());

    Subject s1 = `subject(1,sl("high"));
    Subject s2 = `subject(2,sl("medium"));
    Subject s3 = `subject(3,sl("low"));
    Resource r1 = `resource(1,sl("low"));
    Resource r2 = `resource(2,sl("medium"));
    Resource r3 = `resource(3,sl("high"));
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

    McLean mcl = new McLean(sls);
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

    mcl = new McLean(sls);
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

 
    /*
     * check a configuration
     */
    ListOfSubjects slist = `subjects(s1,s2);
    ListOfResources rlist = `resources(r3,r2);
    //ListOfSubjects slist = `subjects(s1,s2);
    //ListOfResources rlist = `resources(r3,r2,r1);
    int numberOfAccessMode = 2;
    cs = `state(accesses());
   
    ListOfRequests lor = genListOfRequests(slist,rlist,numberOfAccessMode);
    System.out.println("start with lor = " + lor);

    //System.out.println("check BLP");
    //checker(cs,new BLP(sls),lor, `traces());

    //System.out.println("check enum");
    //simplechecker(lor,lor,`requests());

    System.out.println("check McLean");
    checker(cs,new McLean(sls),`requests(),lor, `traces());

    System.out.println("END");
	}
	
  /*
   * a naive checker
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


  private static HashSet<State> cache = new HashSet<State>();

  private static void checker(State s, Policy p, ListOfRequests previous, ListOfRequests lor, ListOfTraces traces) {
    //System.out.print(lor.length());
    //System.out.print(" ");
    %match(lor) {
      // sol1: be naive and generate all possible permutations
      requests(R1*,r@(add|delete)((read|write)(subject,resource)),R2*) -> {
        // sol2: look for read() and add the write() by hand
        //requests(R1*,r@(add|delete)(read(subject,resource)),R2*)
        Decision decision1 = p.transition(`r,s);
        State newState = decision1.getstate();
        ListOfRequests nextLor;
        ListOfRequests nextPrevious;
        ListOfTraces newTraces = traces;
        if(decision1.isgrant()) {
          //System.out.print("+");
          newTraces = `traces(traces*,StateToTrace(s),RequestToTrace(r),StateToTrace(newState));
          nextPrevious = `requests();
          nextLor = `requests(previous*,R1*,R2*);
        } else {
          //System.out.print(".");
          nextPrevious = `requests(previous*,R1*);
          nextLor = `requests(R2*);
        }

        // needed with sol2:
        /*
           Decision decision2 = p.transition(`r.setaccess(`write(subject,resource)), s);
           if(decision2.isgrant()) {
           State oldState = s;
           s = decision2.getstate();
           System.out.print("+");
           traces = `traces(traces*,StateToTrace(oldState),RequestToTrace(r),StateToTrace(s));
           } else {
           System.out.print(".");
           }
         */

        checker(newState,p,nextPrevious,nextLor,newTraces);

        // Shouldn't we remove the write(subject,resource) from
        // requests(R1*,r@(add|delete)(read(subject,resource)),R2*) ??

        // We can also match to find the corresponding write ?

        // Maybe for the general case we should just take the accesses
        // one by one - not read,write,read,write,...

        //if(space.contains(s)) {
        // CUT the search space
        // the current state has already be visited
        // note: do not perform a return, otherwise, the enumeration is stopped

        // En fait c'est surement faux : a-t-on le droit de couper l'espace de recherche
        // independamment des request R1*,R2* ?
        // l'ensemble space est commun a toutes les branches de l'exploration

        //           Il faut peut-etre iterer sur un autre type de STATE - searchSTATE(state,accesses)

        //} else {
        //space.add(cs);// the optimisation can be removed to be complete
        //checker(s,p,nextPrevious, nextLor,space,traces);
        // note: do not perform a return, otherwise, the enumeration is stopped
        //}

        //System.out.println("request  = " + `r);
        //System.out.println("decision = " + decision);
      }

      //_ -> { // _ instead of requests() to allow sol2
      requests() -> { // with sol1 only
        // this part is executed after the previous match
        // do the validation when no more request matches
        if(!cache.contains(s)) {
          if(p.valid(s) == false) {
            System.out.println("LEAKAGE DETECTED");
            while(!traces.isEmptytraces()) {
              System.out.println(traces.getHeadtraces());
              traces = traces.getTailtraces();
            }
            System.out.println("final state = " + s);
            System.exit(0);
          } else {
            cache.add(s);
            System.out.println("valid state cache size = " + cache.size());
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
