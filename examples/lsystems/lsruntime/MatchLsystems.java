package lsruntime;

import lsruntime.adt.lsystems.types.Node;
import lsruntime.adt.lsystems.types.NodeList;

public interface MatchLsystems {
  
  /**
   * For the initialization of the algorithm
   *
   * @return a NodeList which contains the axiom
   */
  NodeList init();
  
  /**
   * Try to match the Node token between prev_rev and next
   * 
   * /!\ the NodeList prev_rev is the reversed list of which is before token
   * 
   * the %match should apear like:
   *   
   **   %match(NodeList prev_rev,Node token, NodeList next) {
   **     // F < A -> F
   **     concNode(I*,F,head*),A,_ -> {
   **       if (runtime.UCD(I,extign)) {
   **         return `concNode(F);
   **       }
   **     }
   **     // F > B -> B F
   **     _,F,concNode(I*,B,tail*) -> {
   **       if (runtime.UCD(I,extign)) {
   **         return `concNode(B,F);
   **       }
   **     }
   **     // F > [ B ] B -> F F
   **     _,F,concNode(I1*,SubList(concNode(I2*,B,tail1*)),I3*,B,tail2*) -> {
   **       if (runtime.UCD(I1,ign) && runtime.UCD(I2,ign) && runtime.UCD(I3,ign)) {
   **         return `concNode(F,F);
   **       }
   **     }
   **     // A < B > A -> A
   **     concNode(I1*,A,head*),B,concNode(I2*,A,tail*) -> {
   **       if (runtime.UCD(I1,ign) && runtime.UCD(I2,ign)) {
   **         return `concNode(A);
   **       }
   **     }
   **     // F -> A    Attention : à mettre à la fin
   **     _,F,_ -> {
   **       return `concNode(A);
   **     }
   **   }
   *
   * @return a NodeList which contains the axiom
   */
  NodeList apply(NodeList prev_rev, Node token, NodeList next);
  
}
