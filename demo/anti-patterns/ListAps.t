import listaps.term.types.*;

class ListAps {  

	%gom {
		module Term		
		abstract syntax

		Element = a()
				| b()
				| c()

		List = conc( Element* )
	}

	public final static void main(String[] args) {
		List l;

        l = `conc();
		match(l);
	
		l = `conc(a(),b(),c());
		match(l);				

		l = `conc(b(),c(),b());
		match(l);	

        l = `conc(a(),a(),a());
		match(l);				
	}
	
	private static void match(List subject) {
 		%match(subject) {
            p1:conc(_*,a(),_*) ->{
				System.out.println("Contains an element a(): " + `subject);
				break p1;			
			}
			p2:conc(_*,!a(),_*) ->{
				System.out.println("Contains an element different from a(): " + `subject);
				break p2;			
			}
			p3:!conc(_*,a(),_*) ->{
				System.out.println("Does not contain a(): " + `subject);
				break p3;
			}
			p4:!conc(_*,!a(),_*) ->{
				System.out.println("Does not contain an element different from a(): " + `subject);
				break p4;
			}
			p5:!conc(_*,x,_*,x,_*) ->{
				System.out.println("AllDiff: " + `subject);
				break p5;
			}
			p6:!conc(_*,!x,_*,x,_*) ->{
				System.out.println("AllEqual: " + `subject);
				break p6;
			}
		}
		System.out.println();
	}
}
