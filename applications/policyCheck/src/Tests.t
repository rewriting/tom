import accesscontrol.*;
import accesscontrol.types.*;
import policy.*;

public class Tests {

  %include { accesscontrol/accesscontrol.tom }

	public static void main(String[] args) {

		System.out.println("START ---------------------");

    AccessMode am = `am(0);
		System.out.println("AM: "+am);

    SecurityLevelsLattice sls = `slLattice(slSet(sl(0),sl(1)),slSet(sl(0),sl(1)));
    boolean c = `sls.smaller(`sl(0),`sl(1));
		System.out.println("C ="+c);
    c = `sls.smaller(`sl(1),`sl(0));
		System.out.println("C ="+c);
    c = `sls.smaller(`sl(1),`sl(1));
		System.out.println("C ="+c);
    c = `sls.smaller(`sl(10),`sl(0));
		System.out.println("C ="+c);

    State s = `state(accesses(),accesses());
		System.out.println("State: "+`s);

    BLP blp = new BLP(sls);
		System.out.println("BLP: "+`blp);
    Decision result = blp.transition(`request(add(),access(subject(5,sl(0)),resource(1,sl(0)),write(),explicit())));
		System.out.println("Access granted: "+`result);
		System.out.println("BLP: "+`blp);
	}
	
	
	

}
