import accesscontrol.*;
import accesscontrol.types.*;

public class Tests {

  %include { accesscontrol/accesscontrol.tom }
	
	public static void main(String[] args) {

		System.out.println("START ---------------------");

    AccessMode am = `am(0);
		System.out.println("AM: "+am);

    SecurityLevel l1 = `sl(11);
    SecurityLevel l2 = `sl(2);
// 		System.out.println("l1 < l2: "+`l1.compareTo(l2));
		System.out.println("l1 < l2: "+`l1.compare2(l2));
	}
	
	
	

}
