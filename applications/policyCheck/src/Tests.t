import accesscontrol.*;
import accesscontrol.types.*;

public class Tests {

  %include { accesscontrol/accesscontrol.tom }

	public static void main(String[] args) {

		System.out.println("START ---------------------");

    AccessMode am = `am(0);
		System.out.println("AM: "+am);

    SecurityLevelsLattice sls = `slLattice(slSet(sl(0),sl(1)),slSet(sl(0),sl(1)));
    int c = `sls.compare(`sl(0),`sl(1));
		System.out.println("C ="+c);
    c = `sls.compare(`sl(1),`sl(0));
		System.out.println("C ="+c);
    c = `sls.compare(`sl(1),`sl(1));
		System.out.println("C ="+c);
    c = `sls.compare(`sl(10),`sl(0));
		System.out.println("C ="+c);

    State s = `state(accesses(),accesses());
		System.out.println("State: "+`s);
	}
	
	
	

}
