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
    Decision result = blp.transition(`request(add(),access(subject(5,sl(3)),resource(1,sl(3)),write(),explicit())));
		System.out.println("Access granted: "+`result+"------------------------------------");
		System.out.println(""+`blp);
    result = blp.transition(`request(add(),access(subject(5,sl(3)),resource(1,sl(3)),write(),explicit())));
		System.out.println("Access granted: "+`result+"------------------------------------");
		System.out.println(""+`blp); 
    result = blp.transition(`request(delete(),access(subject(5,sl(3)),resource(1,sl(3)),write(),explicit())));
		System.out.println("Access granted: "+`result+"------------------------------------");
		System.out.println(""+`blp);
    result = blp.transition(`request(delete(),access(subject(3,sl(3)),resource(1,sl(3)),write(),explicit())));
		System.out.println("Access granted: "+`result+"------------------------------------");
		System.out.println(""+`blp);
    result = blp.transition(`request(add(),access(subject(5,sl(3)),resource(1,sl(3)),read(),explicit())));
		System.out.println("Access granted: "+`result+"------------------------------------");
		System.out.println(""+`blp);
    result = blp.transition(`request(add(),access(subject(5,sl(3)),resource(2,sl(4)),read(),explicit())));
		System.out.println("Access granted: "+`result+"------------------------------------");
		System.out.println(""+`blp);


		System.out.println("VALID STATUS: "+ blp.valid());
	}
	
	
	

}
