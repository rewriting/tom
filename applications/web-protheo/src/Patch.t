import tom.library.xml.*;

import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import aterm.*;
import java.util.*;
import java.io.*;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import java.lang.reflect.Array;

public class Patch {

  %include{mutraveler.tom}
  %include{adt/tnode/TNode.tom}

  private XmlTools xtools;
  private static List members;

  public Patch(TNode mbrs,TNode bib){
    xtools = new XmlTools();
    members= new ArrayList();
    VisitableVisitor ruleId1 = `GetMembers();
    VisitableVisitor ruleId2 = `ParseBib();
    try{
      MuTraveler.init(`BottomUp(ruleId1)).visit(mbrs);
      MuTraveler.init(`BottomUp(ruleId2)).visit(bib);
    } catch (VisitFailure e) {
      System.err.println("Visit failed");
    }
    Collections.sort(members);
    Iterator i = members.listIterator();
    while(i.hasNext()) {
      System.out.println(i.next());
    }
  }
  
  %strategy ParseBib() extends `Identity() {

    visit TNode{
      <author>#TEXT(a)</author> -> {
        String first="", last = "";
        String authArray[] = `a.split(" and ");//must be surrouded by blanks (don't split name 'Brand' for instance)
        for (int i=0;i<(Array.getLength(authArray));i++) {//for each author
          String current_a[] = authArray[i].split(",");
          last = current_a[0].trim();
          if (Array.getLength(current_a) > 1){//case field firstname empty
            first = current_a[1].trim();
          }
          if(!members.contains(last + " " + first)) {
            members.add(last + " " + first);
          }
        }
      }
    }
  }

  %strategy GetMembers() extends `Identity(){

    visit TNode {
      <person><firstname>#TEXT(first)</firstname><lastname>#TEXT(last)</lastname><status>#TEXT(s)</status></person> -> {
        members.add(`last + " " + `first + " <<<<<<<<");
      }
    }
  }
}
// sed s/moi/toi/g fich.moi > fich.toi
