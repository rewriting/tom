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

    Resource r1 = `resource(1,sl("low"));
    Resource r2 = `resource(2,sl("medium"));
    Resource r3 = `resource(3,sl("high"));
    Subject s1 = `subject(1,sl("high"));
    Subject s2 = `subject(2,sl("medium"));
    Subject s3 = `subject(3,sl("low"));
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
    ListOfResources rlist = `resources(r3,r2,r1);
    int numberOfAccessMode = 2;
    cs = `state(accesses());
   
    ListOfRequests lor = genListOfRequests(slist,rlist,numberOfAccessMode);
    System.out.println("lor = " + lor);

    //System.out.println("check BLP");
    //checker(cs,new BLP(sls),lor, new HashSet<State>(), `traces());

    System.out.println("check McLean");
    checker(cs,new McLean(sls),lor, new HashSet<State>(), `traces());

	}
	
  /*
   * a naive checker
   */
  private static ListOfRequests genListOfRequests(ListOfSubjects subjects, ListOfResources resources, int numberOfAccessMode) {
    ListOfRequests res = `requests();
    %match(subjects, resources) {
      subjects(S1*,s,S2*), resources(R1*,r,R2*) -> {
        // we create a list with all possible access modes
        for(int level = 0 ; level<numberOfAccessMode ; level++) {
          res = `requests( add(access(level,s,r)), res*);
        }
      }
    }
    return res;
  }




  private static void checker(State s, Policy p, ListOfRequests lor, HashSet<State> space, ListOfTrace traces) {
    System.out.print(lor.length());
    System.out.print(" ");
    %match(lor) {
      // sol1: be naive and generate all possible permutations
      requests(R1*,r@(add|delete)((read|write)(subject,resource)),R2*) -> {
      // sol2: look for read() and add the write() by hand
      //requests(R1*,r@(add|delete)(read(subject,resource)),R2*) -> {
        Decision decision1 = p.transition(`r,s);
        if(decision1.isgrant()) {
          State oldState = s;
          s = decision1.getstate();
          //traces = `traces(traces*,StateToTrace(oldState),RequestToTrace(r),StateToTrace(s));
        }

        // needed with sol2:
        //Decision decision2 = p.transition(`r.setaccess(`write(subject,resource)), cs);
        //cs = decision2.getstate();

        // Shouldn't we remove the write(subject,resource) from
        // requests(R1*,r@(add|delete)(read(subject,resource)),R2*) ??

        // We can also match to find the corresponding write ?
       
        // Maybe for the general case we should just take the accesses
        // one by one - not read,write,read,write,...

        if(space.contains(s)) {
          // CUT the search space
          // the current state has already be visited
          // note: do not perform a return, otherwise, the enumeration is stopped

          // En fait c'est surement faux : a-t-on le droit de couper l'espace de recherche
          // independamment des request R1*,R2* ?
          // l'ensemble space est commun a toutes les branches de l'exploration

          //           Il faut peut-etre iterer sur un autre type de STATE - searchSTATE(state,accesses)

        } else {
          //space.add(cs);// the optimisation can be removed to be complete
          checker(s,p,`requests(R1*,R2*),space,traces);
          // note: do not perform a return, otherwise, the enumeration is stopped
        }
      
        //System.out.println("request  = " + `r);
        //System.out.println("decision = " + decision);
      }

      _ -> {
        // this part is executed after the previous match
        // do the validation when no more request matches
        if(p.valid(s) == false) {
          System.out.println("LEAKAGE DETECTED");
          while(!traces.isEmptytraces()) {
            System.out.println(traces.getHeadtraces());
            traces = traces.getTailtraces();
          }
          System.out.println("final state = " + s);
          System.exit(0);
        }

      }
    }

  }
}
