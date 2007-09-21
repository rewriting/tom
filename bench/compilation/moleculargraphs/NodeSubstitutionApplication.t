package compilation.moleculargraphs;

import compilation.moleculargraphs.term.*;
import compilation.moleculargraphs.term.types.*;	

import tom.library.sl.VisitFailure;

import java.util.*;


public class NodeSubstitutionApplication {

	%include{ term/term.tom } 
 	%include {sl.tom }
	%include {java/util/types/Collection.tom}	

  public static NodeList applyAll(NodeList rhs, NodeSubstitutionList nodeSubsts) {
  	
  	NodeList interm = rhs;	
	
	%match(NodeSubstitutionList nodeSubsts) {
		concNS(_*, x, _*) -> {
			try {
				interm = (NodeList)`InnermostId(ApplyNodeSubst1(x)).visit(interm);
				//System.out.println("interm: " + interm);
				interm = (NodeList)`InnermostId(ApplyNodeSubst2(x)).visit(interm);
			} catch (VisitFailure e) { 
				System.out.println("Error at ApplyNodeSubst inside ApplyAllNodeSubstitution: " + e); 
			}
		}
	}			
	return interm;
  } 


  // elementary node-substitution application on src and tar respectively
  %strategy ApplyNodeSubst1(e:NodeSubstitution) extends `Identity() {
	// ApplySrc
  	visit NodeList {
  		concN(a*, labNode(ID, node(uid(ID, NAME), concP(pl1*))), b*) -> {
  			%match(NodeSubstitution e, String ID, String NAME) {
  				nodeSubstitution(labNode(id, node(uid(id, name), concP(pl2*))), concN(nl*)), id, name -> {
					// pl2* should match a sublist of pl1*, hence the difference of ports should be distributed among all ports in the rhs
					
  					// applying the node-subst v-> v1,...,vk; a subset of the ports of vi is included in sl1*; 
  					// but the ports in vi have an empty set of neighbours; 
  					// we rebuild a new node vi with the neighbours of the ports taken from sl1*
  					
  					NodeList rhs = `concN(nl*);
  					NodeList nodes = `concN();
  					
  					// for each node in the rhs of the node subst
  					%match(NodeList rhs) {
  						concN(_*, labNode(id_i, node(uid(id_i, name_i), concP(pl_i*))), _*) -> {
  						
  							// ?some ports of x occur in the node with id above, 
  							// ?but we need to recover the associated pointers for the ports of x from the port list of id
  							// ?and put in the list the new x with pointers for ports taken from id
  							// ?We do this by merging the two lists of ports and then idenfying the ports followed by merging their neighbour list
  						
  							PortList tempPorts = recoverNeighbours1from2(`concP(pl_i*), `concP(pl1*));
  							nodes = tom_append_list_concN(`concN(labNode(id_i, node(uid(id_i, name_i), tempPorts))), nodes);
  						} 
  					}
					nodes = tom_append_list_concN(`concN(a*), tom_append_list_concN(nodes, `concN(b*))); 
					// Here the difference pl1* - pl2* must be distributed among the nodes
					return nodes;
  				}
  			}
  		}
  	}
  }

 	%strategy ApplyNodeSubst2(e:NodeSubstitution) extends `Identity() {
	// ApplyTar
  	visit NeighbourList {
  		concNG(a*,neighbour(refNode(ID), concP(sl*)), b*) -> {
  			%match(NodeSubstitution e, String ID) {
  				nodeSubstitution(labNode(id, node(uid(id, name), concP(sl2*))), concN(nl*)), id -> {

  					NeighbourList neighbours = `concNG();
  					NodeList rhs = `concN(nl*);  					
   					
   					// for every node id in the rhs of the embedding create a new neighbour with the same port reference list as the initial
  					// hence it requires TO HANDLE THE EXTRA-REFERENCES=EXTRA-EDGES 
  					
					// search in nList(nl*) the nodes that contain the ports referrenced in pList(sl*)
					PortList temp = `concP(sl*);
  					%match(temp, rhs)  {
						concP(_*, refPort(p), _*), concN(_*, labNode(idx, node(_, concP(_*, labPort(p, _), _*))), _*) -> {
							neighbours = tom_append_list_concNG(`concNG(neighbour(refNode(idx), concP(refPort(p)))), neighbours);
						} 
					}
					neighbours = tom_append_list_concNG(`concNG(a*, b*), neighbours);
					
					return neighbours;
  				}
  			}
  		}
  	}
  }
  
  public static PortList recoverNeighbours1from2(PortList pl1, PortList pl2) {
  	%match(PortList pl1, PortList pl2) {
  		concP(a*, port(p, concNG(), s), b*),
  		concP(c*, port(p, concNG(l*), s), d*) -> {
			//System.out.println("Bug? " + `concNG(l*));
  			return tom_append_list_concP(`concP(port(p, concNG(l*), s)), recoverNeighbours1from2(`concP(a*, b*), `concP(c*, d*)));
  		}
  	}
  	return pl1;
  }
    

}

/*
public class NodeSubstitutionApplication {

	%include{ term/term.tom }
 	%include {sl.tom }
	%include {java/util/types/Collection.tom}	

  public static Nodes applyAll(Nodes rhs, NodeSubstitutions nodeSubsts) {
  	
  	Nodes interm = rhs;	
	
	%match(NodeSubstitutions nodeSubsts) {
		nodeSubstitutionList(_*, x, _*) -> {
			try {
				interm = (Nodes)`InnermostId(ApplyNodeSubst1(x)).visit(interm);
				//System.out.println("interm: " + interm);
				interm = (Nodes)`InnermostId(ApplyNodeSubst2(x)).visit(interm);
			} catch (VisitFailure e) { 
				System.out.println("Error at ApplyNodeSubst inside ApplyAllNodeSubstitution: " + e); 
			}
		}
	}			
	return interm;
  } 


  // elementary node-substitution application on src and tar respectively
  %strategy ApplyNodeSubst1(e:NodeSubstitution) extends `Identity() {
	// ApplySrc
  	visit Nodes {
  		nList(a*, labNode(ID, node(uid(ID, NAME), pList(pl1*))), b*) -> {
  			%match(NodeSubstitution e, String ID, String NAME) {
  				nodeSubstitution(labNode(id, node(uid(id, name), pList(pl2*))), nList(nl*)), id, name -> {
					// pl2* should match a sublist of pl1*, hence the difference of ports should be distributed among all ports in the rhs
					
  					// applying the node-subst v-> v1,...,vk; a subset of the ports of vi is included in sl1*; 
  					// but the ports in vi have an empty set of neighbours; 
  					// we rebuild a new node vi with the neighbours of the ports taken from sl1*
  					
  					Nodes rhs = `nList(nl*);
  					Nodes nodes = `nList();
  					
  					// for each node in the rhs of the node subst
  					%match(Nodes rhs) {
  						nList(_*, labNode(id_i, node(uid(id_i, name_i), pList(pl_i*))), _*) -> {
  						
  							// ?some ports of x occur in the node with id above, 
  							// ?but we need to recover the associated pointers for the ports of x from the port list of id
  							// ?and put in the list the new x with pointers for ports taken from id
  							// ?We do this by merging the two lists of ports and then idenfying the ports followed by merging their neighbour list
  						
  							Ports tempPorts = recoverNeighbours1from2(`pList(pl_i*), `pList(pl1*));
  							nodes = tom_append_list_nList(`nList(labNode(id_i, node(uid(id_i, name_i), tempPorts))), nodes);
  						} 
  					}
					nodes = tom_append_list_nList(`nList(a*), tom_append_list_nList(nodes, `nList(b*))); 
					// Here the difference pl1* - pl2* must be distributed among the nodes
					return nodes;
  				}
  			}
  		}
  	}
  }

 	%strategy ApplyNodeSubst2(e:NodeSubstitution) extends `Identity() {
	// ApplyTar
  	visit Neighbours {
  		neighList(a*,neighbour(refNode(ID), pList(sl*)), b*) -> {
  			%match(NodeSubstitution e, String ID) {
  				nodeSubstitution(labNode(id, node(uid(id, name), pList(sl2*))), nList(nl*)), id -> {

  					Neighbours neighbours = `neighList();
  					Nodes rhs = `nList(nl*);  					
   					
   					// for every node id in the rhs of the embedding create a new neighbour with the same port reference list as the initial
  					// hence it requires TO HANDLE THE EXTRA-REFERENCES=EXTRA-EDGES 
  					
					// search in nList(nl*) the nodes that contain the ports referrenced in pList(sl*)
					Ports temp = `pList(sl*);
  					%match(temp, rhs)  {
						pList(_*, refPort(p), _*), nList(_*, labNode(idx, node(_, pList(_*, labPort(p, _), _*))), _*) -> {
							neighbours = tom_append_list_neighList(`neighList(neighbour(refNode(idx), pList(refPort(p)))), neighbours);
						} 
					}
					neighbours = tom_append_list_neighList(`neighList(a*, b*), neighbours);
					
					return neighbours;
  				}
  			}
  		}
  	}
  }
  
  public static Ports recoverNeighbours1from2(Ports pl1, Ports pl2) {
  	%match(Ports pl1, Ports pl2) {
  		pList(a*, port(p, neighList(), state), b*),
  		pList(c*, port(p, neighList(l*), state), d*) -> {
			//System.out.println("Bug? " + `neighList(l*));
  			return tom_append_list_pList(`pList(port(p, neighList(l*), state)), recoverNeighbours1from2(`pList(a*, b*), `pList(c*, d*)));
  		}
  	}
  	return pl1;
  }
    

}
*/