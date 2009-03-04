package lemu2.kernel.converters;

import lemu2.kernel.proofterms.types.*;
import lemu2.kernel.*;
import lemu2.util.*;
import fj.data.List;
import fj.data.Option;
import fj.pre.*;
import fj.*;


public class LKFtoLK {

  private static F2<List<P2<String,Name>>,String,Option<Name>> 
    strlookup = List.<String,Name>lookup(Equal.<String>anyEqual());

  %include { kernel/proofterms/proofterms.tom } 

  private static ProofTerm convertFoldL(
      ProofTerm pt,
      List<P2<String,Name>> map,
      Prop subject) {
    %match(pt) {
      foldL(id,FoldLPrem1(x,px,M),n) -> {
        Option<Name> ax = strlookup.`f(map,id);
      }
    }
    return null;
  }

}
