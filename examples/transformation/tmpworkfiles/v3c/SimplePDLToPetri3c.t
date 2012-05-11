import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

import simplepdl.*;
import petrinetsemantics.DDMMPetriNet.*;

import java.util.*;

import tom.library.sl.*;
import tom.library.emf.*;

import tom.library.utils.LinkClass;

public class SimplePDLToPetri3c {

  %include{ sl.tom }
  %include{ DDMMPetriNetPackage.tom }
  //%include{ SimplePDL3c.tom }
  %include{ SimplepdlPackage.tom }

  %include{ LinkClass.tom }

  %typeterm SimplePDLToPetri3c { implement { SimplePDLToPetri3c }}
  

  private Hashtable<Object,HashMap<String,Object>> table;
  private PetriNet pn = null;

  public SimplePDLToPetri3c() {
    this.table = new Hashtable<Object,HashMap<String,Object>>();
  }

  %transformation SimplePDLToPetriNet(translator:SimplePDLToPetri3c) with (Process, WorkDefinition, WorkSequence) to (Place, Transition) {
    (Process2PetriNet##TopDown) reference P2PN {
      p@Process[name=name] -> {

        Node p_ready  = `Place(name + "_ready", translator.pn,ArcEList(), ArcEList(), 1);
        Node p_running  = `Place(name + "_running", translator.pn,ArcEList(), ArcEList(), 0);
        Node p_finished  = `Place(name + "_finished", translator.pn,ArcEList(), ArcEList(), 0);
        //Node t_start  = `Transition(name + "_start", translator.pn,ArcEList(), ArcEList(), 1, 1);
        %tracelink(Node, t_start, `Transition(name + "_start", translator.pn,ArcEList(), ArcEList(), 1, 1));
        %tracelink(Node, t_finish, `Transition(name + "_finish", translator.pn,ArcEList(), ArcEList(), 1, 1));

        `Arc(t_start, p_ready, translator.pn,normal(), 1);
        `Arc(p_running, t_start, translator.pn,normal(), 1);
        `Arc(t_finish, p_running, translator.pn,normal(), 1);
        `Arc(p_finished, t_finish, translator.pn,normal(), 1);

        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("p_ready",p_ready);
        map.put("p_running",p_running);
        map.put("p_finished",p_finished);
        map.put("t_start",t_start);
        map.put("t_finish",t_finish);

        translator.table.put(`p,map);

        WorkDefinition from = `p.getFrom();
        if (from!=null) {
          Transition source = new ResolveWorkDefinitionTransition(from, "t_start");
          source.setNet(translator.pn);
          Arc tmpZoomIn = `Arc(p_ready,source,translator.pn,normal(), 1);

          Transition target = new ResolveWorkDefinitionTransition(from, "t_finish");
          target.setNet(translator.pn);
          Arc tmpZoomOut = `Arc(target,p_finished,translator.pn,read_arc(), 1);
        }
      }
    }

    (WorkDefinition2PetriNet##TopDown) reference WD2PN {
      wd@WorkDefinition[name=name] -> {

        Node p_ready  = `Place(name + "_ready", translator.pn,ArcEList(), ArcEList(), 1);
        Node p_started  = `Place(name + "_started", translator.pn,ArcEList(), ArcEList(), 0);
        Node p_running  = `Place(name + "_running", translator.pn,ArcEList(), ArcEList(), 0);
        Node p_finished  = `Place(name + "_finished", translator.pn,ArcEList(), ArcEList(), 0);
        Node t_start  = `Transition(name + "_start", translator.pn,ArcEList(), ArcEList(), 1, 1);
        Node t_finish  = `Transition(name + "_finish", translator.pn,ArcEList(), ArcEList(), 1, 1);


        `Arc(t_start, p_ready, translator.pn,normal(), 1);
        `Arc(p_started, t_start, translator.pn,normal(), 1);
        `Arc(p_running, t_start, translator.pn,normal(), 1);
        `Arc(t_finish, p_running, translator.pn,normal(), 1);
        `Arc(p_finished, t_finish, translator.pn,normal(), 1);

        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("p_ready",p_ready);
        map.put("p_started",p_started);
        map.put("p_running",p_running);
        map.put("p_finished",p_finished);
        map.put("t_start",t_start);
        map.put("t_finish",t_finish);

        translator.table.put(`wd,map);

        simplepdl.Process parent = `wd.getParent();
        Transition source = new ResolveProcessTransition(parent , "t_start");
        source.setNet(translator.pn);
        Arc tmpDistribute = `Arc(p_ready,source,translator.pn,normal(), 1);

        Transition target = new ResolveProcessTransition(parent, "t_finish");
        target.setNet(translator.pn);
        Arc tmpRejoin = `Arc(target,p_finished,translator.pn,read_arc(), 1);
      }
    }

    (WorkSequence2PetriNet##) reference WS2PN {
      WorkSequence[predecessor=p,successor=s,linkType=linkType] -> { 

        Place source= null;
        Transition target= null;
        %match(linkType) { 
          (finishToFinish|finishToStart)[] -> { source = new ResolveWorkDefinitionPlace(`p,"p_finished"); }
          (startToStart|startToFinish)[]   -> { source = new ResolveWorkDefinitionPlace(`p,"p_started"); }

          (finishToStart|startToStart)[]   -> { target = new ResolveWorkDefinitionTransition(`s,"t_start"); }
          (startToFinish|finishToFinish)[] -> { target = new ResolveWorkDefinitionTransition(`s,"t_finish"); }
        }
        source.setNet(translator.pn);
        target.setNet(translator.pn);
        Arc tmp = `Arc(target,source, translator.pn,read_arc(), 1);  

      }
    }
  }

  /**
    *
    * @param resolveNode temporary ResolveNode that should be replaced
    * @param newNode node (stored in the HashMap) that will replace the ResolveNode
    * @param translator the TimplePDLToPetri3
    */
  public static void resolveInverseLinks(EObject resolveNode, Node newNode, SimplePDLToPetri3c translator) {
    /* collect arcs having ResolveWorkDefinitionPlace and ResolveWorkDefinitionTransition */

    ECrossReferenceAdapter adapter = new ECrossReferenceAdapter(); //create an adapter
    translator.pn.eAdapters().add(adapter); //attach it to PetriNet

    /*
     * 'references' will contains a set of objects (i.e.
     * EStructuralFeature.Setting) from which we can retrieve 
     * (thanks to getEObject()) objects that
     * contains references (i.e. pointers) to resolveNode
     */
    Collection<EStructuralFeature.Setting> references = adapter.getInverseReferences(resolveNode);

    // for each type of Resolve
    boolean toSet = (false
        | resolveNode instanceof ResolveWorkDefinitionPlace 
        | resolveNode instanceof ResolveWorkDefinitionTransition
        | resolveNode instanceof ResolveProcessTransition
        );

    for (EStructuralFeature.Setting setting:references) {
      // current is an object that references resolveNode
      EObject current = setting.getEObject();
      if (current instanceof Arc) {
        Arc newCurrent = (Arc)current;
          // for each field of Arc
        if(newCurrent.getSource().equals(resolveNode) && toSet) {
          newCurrent.setSource(newNode); 
        } else if(newCurrent.getTarget().equals(resolveNode) && toSet) {
          newCurrent.setTarget(newNode); 
        } else {
          throw new RuntimeException("should not be there");
        }
      }
    }

  }


  /*
   * Strategy that replaces all Resolve nodes by a normal node
   */
  /*%strategy Resolve(translator:SimplePDLToPetri3c) extends Identity() {
    visit Place {
      pr@ResolveWorkDefinitionPlace[o=o,name=name] -> {
        Place res = (Place) translator.table.get(`o).get(`name);
        resolveInverseLinks(`pr, res, translator);
        return res;
      }
    }

    visit Transition {
      tr@ResolveWorkDefinitionTransition[o=o,name=name] -> {
        Transition res = (Transition) translator.table.get(`o).get(`name);
        resolveInverseLinks(`tr, res, translator);
        return res;
      }
      ptr@ResolveProcessTransition[o=o,name=name] -> {
        Transition res = (Transition) translator.table.get(`o).get(`name);
        resolveInverseLinks(`ptr, res, translator);
        return res;
      }

    }

  }*/

  public static void main(String[] args) {
    System.out.println("bonne compilation (v3c)\n .......");

    /* parent is null since processes have not been yet created */
    WorkDefinition wd1 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),null,"A");
    WorkDefinition wd2 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),null,"B");
    WorkDefinition wd3 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),null,"C");
    WorkDefinition wd4 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),null,"D");
    WorkSequence ws1 = `WorkSequence(null,startToStart(),wd1,wd2);
    WorkSequence ws2 = `WorkSequence(null,startToFinish(),wd3,wd4);

    simplepdl.Process p_root = `Process("root",ProcessElementEList(wd1,wd2,ws1),null);
    simplepdl.Process p_child = `Process("child",ProcessElementEList(wd3,wd4,ws2),wd2);

    wd1.setParent(p_root);
    wd2.setParent(p_root);
    wd2.setProcess(p_child);

    wd3.setParent(p_child);
    wd4.setParent(p_child);

    ws1.setParent(p_root);
    ws2.setParent(p_child);

    SimplePDLToPetri3c translator = new SimplePDLToPetri3c();

    try {
      translator.pn = `PetriNet(NodeEList(),ArcEList(),"main");
      /*Strategy transformer = `Sequence(
          TopDown(Process2PetriNet(translator)),
          TopDown(WorkDefinition2PetriNet(translator)),
          TopDown(WorkSequence2PetriNet(translator))
       );*/
      Strategy transformer = `SimplePDLToPetriNet(translator);
      transformer.visit(p_root, new EcoreContainmentIntrospector());

      /*System.out.println("\nBefore Resolve");
      `Sequence(TopDown(PrintTransition()),TopDown(PrintPlace())).visit(translator.pn, new EcoreContainmentIntrospector());*/

      /*System.out.println("\nTest strategy resolve");
      `TopDown(Resolve(translator)).visit(translator.pn, new EcoreContainmentIntrospector());*/

      /*`TopDown(PrintArc()).visit(pn, new EcoreContainmentIntrospector());*/
      /*`TopDown(PrintTransition()).visit(pn, new EcoreContainmentIntrospector());*/

      System.out.println("\nAfter Resolve");
      `Sequence(TopDown(PrintTransition()),TopDown(PrintPlace())).visit(translator.pn, new EcoreContainmentIntrospector());

    } catch(VisitFailure e) {
        System.out.println("strategy fail");
    }
  }


  %strategy PrintArc() extends Identity() {
    visit Arc {
      Arc[source=node1, target=node2] -> {
        System.out.println(`node1.getName() + " -> " + `node2.getName());
      }
    }
  }

  %strategy PrintTransition() extends Identity() {
    visit Transition {
      tr@ResolveWorkDefinitionTransition[o=o,name=name] -> {
        System.out.println("tr resolve " + `name);
        return `tr; /* to avoid printing a Transition twice*/
      }

      ptr@ResolveProcessTransition[o=o,name=name] -> {
        System.out.println("tr process resolve " + `name);
        return `ptr; /* to avoid printing a Transition twice*/
      }

      Transition[name=name,incomings=sources,outgoings=targets] -> {
        String s = "";
        String t = "";
        %match {
         ArcEList(_*,Arc[kind=k,weight=w,source=node],_*) << sources && Place[name=placename]<< node -> {
           s += `placename + ((`k==`read_arc())?"?":"*") + `w + " "; 
         }
         ArcEList(_*,Arc[kind=k,weight=w,target=node],_*) << targets && Place[name=placename]<< node -> { 
           t += `placename + ((`k==`read_arc())?"?":"*") + `w + " "; 
         }
        }
        System.out.println("tr " + `name + " " + s + " --> " + t);
      }

      
    }
  }
  
  %strategy PrintPlace() extends Identity() {
    visit Place {
      pl@ResolveWorkDefinitionPlace[o=o,name=name] -> {
        System.out.println("pl resolve " + `name);
        return `pl; /* to avoid printing a Place twice */
      }

      Place[name=name,initialMarking=w] && w!=0 -> {
        System.out.println("pl " + `name + " " + `w);
      }

    }
  }
  
}
