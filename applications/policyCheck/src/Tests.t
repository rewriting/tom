import accesscontrol.*;
import accesscontrol.types.*;
import policy.*;

public class Tests {

  %include { accesscontrol/accesscontrol.tom }

	public static void main(String[] args) {
    //     SecurityLevelsLattice sls = `slLattice(slSet(sl(0),sl(1)),slSet(sl(3),sl(4),sl(5)));
    SecurityLevelsLattice sls = `slLattice(slSet(sl(1),sl(3)));
    //     boolean c = `sls.smaller(`sl(1),`sl(3));
    // 		System.out.println("C ="+c);

		System.out.println("START  BLP---------------------");

    BLP blp = new BLP(sls);
    Resource r1 = `resource(1,sl(1));
    Resource r2 = `resource(2,sl(2));
    Resource r3 = `resource(3,sl(3));
    Subject s1 = `subject(1,sl(3));
    Subject s2 = `subject(2,sl(2));
    Request req = `request(add(),access(s1,r3,read(),explicit()));
    Decision result = blp.transition(req);
		System.out.println("Request: "+`req+"------------------------------------>"+`result);
		System.out.println(""+`blp);

    req = `request(add(),access(s1,r2,write(),explicit()));
    result = blp.transition(req);
		System.out.println("Request: "+`req+"------------------------------------>"+`result);
		System.out.println(""+`blp); 

    req = `request(add(),access(s2,r2,read(),explicit()));
    result = blp.transition(req);
		System.out.println("Request: "+`req+"------------------------------------>"+`result);
		System.out.println(""+`blp);

    req = `request(add(),access(s2,r1,write(),explicit()));
    result = blp.transition(req);
		System.out.println("Request: "+`req+"------------------------------------>"+`result);
		System.out.println(""+`blp);

		System.out.println("\nVALID STATUS: "+ blp.valid()+"\n");


		System.out.println("START  McLean ---------------------");

    McLean mcl = new McLean(sls);

    req = `request(add(),access(s1,r3,read(),explicit()));
    result = mcl.transition(req);
		System.out.println("Request: "+`req+"------------------------------------>"+`result);
		System.out.println(""+`mcl);

    req = `request(add(),access(s1,r2,write(),explicit()));
    result = mcl.transition(req);
		System.out.println("Request: "+`req+"------------------------------------>"+`result);
		System.out.println(""+`mcl); 

    req = `request(add(),access(s2,r2,read(),explicit()));
    result = mcl.transition(req);
		System.out.println("Request: "+`req+"------------------------------------>"+`result);
		System.out.println(""+`mcl);

    req = `request(add(),access(s2,r1,write(),explicit()));
    result = mcl.transition(req);
		System.out.println("Request: "+`req+"------------------------------------>"+`result);
		System.out.println(""+`mcl);

		System.out.println("\nVALID STATUS: "+ blp.valid()+"\n");

	}
	
	
	

}
