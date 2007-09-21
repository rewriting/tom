package compilation.moleculargraphs;

import compilation.moleculargraphs.term.*;
import compilation.moleculargraphs.term.types.*;	

public class PrettyPrintingTools {

%include{ term/term.tom } 
 
	public String prettyPrint(Structure str) {
		return prettyPrint(str, 0);
	}
	 
  public String prettyPrint(Structure structure, int count) {
  	%match(structure) {
  		struct(m1, m2, x*) -> {
  			return ++count + ") " + prettyPrint(`m1) + " &&\n " + prettyPrint(`struct(m2, x*), count);
  		}
  		struct(m1) -> {
  			return ++count + ") " + prettyPrint(`m1, count);
  		}
   		Nodes(m1) -> {
  			return prettyPrint(`m1);
  		} 		
  		struct() -> {
  			return "";
  		}
  	}
  	throw new RuntimeException("strange structure: " + structure);
  }	
  
  public String prettyPrint(NodeList nodes) {
  	%match(nodes) {
  		concN(m1, m2, x*) -> {
  			return prettyPrint(`m1) + ", " + prettyPrint(`concN(m2, x*));
  		}
  		concN(m1) -> {
  			return prettyPrint(`m1);
  		}
  		concN() -> {
  			return "";
  		}
  	}
  	throw new RuntimeException("strange nList: " + nodes);
  }	

  public String prettyPrint(Node node) {
	%match(node) {
		labNode(i, node(uid(i, name), pl)) -> {
			return "<" + `i + ":" + `name + "||" + prettyPrint(`pl) + ">"; 
		}
		node(uid(i, name), pl) -> {
			return "<" + `i + ":" + `name + "||" + prettyPrint(`pl) + ">"; 
		}
		refNode(name) -> {
			return `name + "";
		}
		/*x@pathNode(_*) -> {
			return  `x + "";
		}*/
	}
	throw new RuntimeException("strange node: " + node);
  }	

	public String prettyPrint(PortList plist) {
 		%match(plist) {
	  		concP(m1, m2, x*) -> {
  				return prettyPrint(`m1) + "," + prettyPrint(`concP(m2, x*));
	  		}
  			concP(m1) -> {
  				return prettyPrint(`m1);
	  		} 
  			concP() -> {
  				return "";
	  		}
  		}
		throw new RuntimeException("strange pList: " + plist);
	}
	
	public String prettyPrint(Port p) {
		%match(p) {
			labPort(name, port(name ,nl, state)) -> {
				return "(" + `name + "->" + prettyPrint(`nl) + prettyPrint(`state) + ")";
			}
			port(name, nl, state) -> {
				return "(" + `name + "->" + prettyPrint(`nl) + prettyPrint(`state) + ")";
			}
			refPort(name) -> {
				return "" + `name;
			}
			/*x@pathPort(_*) -> {
				return "" + `x;
			}*/
		}
		throw new RuntimeException("strange port: " + p);
	}
	
	public String prettyPrint(State state) {
		%match(state) {
			b() -> { return ""; }
			v() -> { return "v"; }
			h() -> { return "h"; }
		}
		return "" + state;
	}	

	public String prettyPrint(NeighbourList nlist) {
		 %match(nlist) {
		 	concNG(m1, m2, x*) -> {
  				return prettyPrint(`m1) + "," + prettyPrint(`concNG(m2, x*));
  			}
	  		concNG(m1) -> {
  				return prettyPrint(`m1);
  			}
	  		concNG() -> {
  				return "";
  			}
  		}
		throw new RuntimeException("strange neighlist: " + nlist);
	}
	 
	public String prettyPrint(Neighbour neigh) {
		%match(neigh) {
			neighbour(node, plist) -> {
				return prettyPrint(`node) + "^" + prettyPrint(`plist);
			}
		}
		throw new RuntimeException("strange neigh: " + neigh);
	}	   
}
