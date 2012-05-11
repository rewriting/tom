import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.*;
import org.eclipse.emf.ecore.xmi.impl.*;

import SimplePDLSemantics.DDMMSimplePDL.*;
import petrinetsemantics.DDMMPetriNet.*;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

import tom.library.utils.ReferenceClass;
import tom.library.utils.LinkClass;
import tom.library.sl.*;
import tom.library.emf.*;

public class SimplePDLToPetriNoHash {

  %include{ sl.tom }
  %include{ LinkClass.tom }
  %include{ emf/ecore.tom }

  %include{ DDMMPetriNetPackage.tom }
  %include{ DDMMSimplePDLPackage.tom }

  /*%include{ EDMMPetriNetPackage.tom }
  %include{ EDMMSimplePDLPackage.tom }
  %include{ SDMMPetriNetPackage.tom }
  %include{ SDMMSimplePDLPackage.tom }
  %include{ TM3PetriNetPackage.tom }
  %include{ TM3SimplePDLPackage.tom }*/


  %typeterm SimplePDLToPetriNoHash { implement { SimplePDLToPetriNoHash }}
  
  private PetriNet pn = null;
  private LinkClass link;

  public SimplePDLToPetriNoHash() {
    this.link = new LinkClass();
  }


  %transformation SimplePDLToPetriNet(translator:SimplePDLToPetriNoHash,link:LinkClass,pn:PetriNet) with(Process, WorkDefinition, WorkSequence) to (Place, Transition) (src:PetriNetSemantics_updated.ecorePetriNetSemantics_updated.ecore,dst:SimplePDLSemantics_updated.ecore) {

    definition P2PN traversal `TopDown(P2PN(translator,link,pn)) {
      p@Process[name=name] -> {
        Node p_ready  = `Place(name + "_ready", pn,ArcEList(), ArcEList(), 1);
        Node p_running  = `Place(name + "_running", pn,ArcEList(), ArcEList(), 0);
        Node p_finished  = `Place(name + "_finished", pn,ArcEList(), ArcEList(), 0);
        String n1 = `name+"_start";
        %tracelink(Node, t_start, `Transition(n1, pn,ArcEList(), ArcEList(), 1, 1));
        n1 = `name+"_finish";
        %tracelink(Node, t_finish, `Transition(n1, pn,ArcEList(), ArcEList(), 1, 1));
        
        `Arc(t_start, p_ready, pn,normal(), 1);
        `Arc(p_running, t_start, pn,normal(), 1);
        `Arc(t_finish, p_running, pn,normal(), 1);
        `Arc(p_finished, t_finish, pn,normal(), 1);

        WorkDefinition from = `p.getFrom();
        if (from!=null) {
          Transition source = %resolve(from:WorkDefinition,t_start:Transition);
          source.setNet(pn);
          Arc tmpZoomIn = `Arc(p_ready,source,pn,normal(), 1);

          Transition target = %resolve(from:WorkDefinition,t_finish:Transition);
          target.setNet(pn);
          Arc tmpZoomOut = `Arc(target,p_finished,pn,read_arc(), 1);
        }
      }
    }

    definition WD2PN traversal `TopDown(WD2PN(translator,link,pn)) {
      wd@WorkDefinition[name=name] -> {
        Node p_ready  = `Place(name + "_ready", pn,ArcEList(), ArcEList(), 1);
        String n1 = `name+"_started";
        %tracelink(Node, p_started, `Place(n1, pn,ArcEList(), ArcEList(), 0));
        Node p_running  = `Place(name+"_running", pn,ArcEList(), ArcEList(), 0);
        String n1 = `name+"_finished";
        %tracelink(Node, p_finished, `Place(n1, pn,ArcEList(), ArcEList(), 0));
        String n1 = `name+"_start";
        %tracelink(Node, t_start, `Transition(n1, pn,ArcEList(), ArcEList(), 1, 1));
        String n1 = `name+"_finish";
        %tracelink(Node, t_finish, `Transition(n1, pn,ArcEList(), ArcEList(), 1, 1));

        `Arc(t_start, p_ready, pn,normal(), 1);
        `Arc(p_started, t_start, pn,normal(), 1);
        `Arc(p_running, t_start, pn,normal(), 1);
        `Arc(t_finish, p_running, pn,normal(), 1);
        `Arc(p_finished, t_finish, pn,normal(), 1);

        SimplePDLSemantics.DDMMSimplePDL.Process parent = `wd.getParent();
        Transition source = %resolve(parent:Process,t_start:Transition);
        source.setNet(pn);
        Arc tmpDistribute = `Arc(p_ready,source,pn,normal(), 1);

        Transition target = %resolve(parent:Process,t_finish:Transition);
        target.setNet(pn);
        Arc tmpRejoin = `Arc(target,p_finished,pn,read_arc(), 1);
      }
    }

    definition WS2PN traversal `TopDown(WS2PN(translator,link,pn)) {
      ws@WorkSequence[predecessor=p,successor=s,linkType=linkType] -> {
        Place source= null;
        Transition target= null;
        WorkDefinition pre = `p;
        WorkDefinition suc = `s;
        %match(linkType) { 
          (finishToFinish|finishToStart)[] -> { source = %resolve(pre:WorkDefinition,p_finished:Place); }
          (startToStart|startToFinish)[]   -> { source = %resolve(pre:WorkDefinition,p_started:Place); }

          (finishToStart|startToStart)[]   -> { target = %resolve(suc:WorkDefinition,t_start:Transition); }
          (startToFinish|finishToFinish)[] -> { target = %resolve(suc:WorkDefinition,t_finish:Transition); }
        }
        source.setNet(pn);
        target.setNet(pn);

        Arc tmp = `Arc(target,source, pn,read_arc(), 1);  
      }
    }
  }

  /**
    *
    * @param resolveNode temporary ResolveNode that should be replaced
    * @param newNode node (stored in the HashMap) that will replace the ResolveNode
    * @param translator the TimplePDLToPetri3
    */
/*  public static void resolveInverseLinks(EObject resolveNode, Node newNode, PetriNet pn) {
    ECrossReferenceAdapter adapter = new ECrossReferenceAdapter(); //create an adapter
    translator.pn.eAdapters().add(adapter); //attach it to PetriNet

    Collection<EStructuralFeature.Setting> references = adapter.getInverseReferences(resolveNode);

    boolean toSet = (false
        | resolveNode instanceof ResolveWorkDefinitionPlace 
        | resolveNode instanceof ResolveWorkDefinitionTransition
        | resolveNode instanceof ResolveProcessTransition
        );

    for (EStructuralFeature.Setting setting:references) {
      EObject current = setting.getEObject();
      if (current instanceof Arc) {
        Arc newCurrent = (Arc)current;
        if(newCurrent.getSource().equals(resolveNode) && toSet) {
          newCurrent.setSource(newNode); 
        } else if(newCurrent.getTarget().equals(resolveNode) && toSet) {
          newCurrent.setTarget(newNode); 
        } else {
          throw new RuntimeException("should not be there");
        }
      }
    }

  }*/

  /*
  %strategy Resolve(link:LinkClass,pn:PetriNet) extends Identity() {
    visit Place {
      pr@ResolveWorkDefinitionPlace[o=o,name=name] -> {
        Place res = (Place) link.get(`o).get(`name);
        resolveInverseLinks(`pr, res, pn);
        return res;
      }
    }

    visit Transition {
      tr@ResolveWorkDefinitionTransition[o=o,name=name] -> {
        Transition res = (Transition) link.get(`o).get(`name);
        resolveInverseLinks(`tr, res, pn);
        return res;
      }
      ptr@ResolveProcessTransition[o=o,name=name] -> {
        Transition res = (Transition) link.get(`o).get(`name);
        resolveInverseLinks(`ptr, res, pn);
        return res;
      }
    }
  }
   */

  public static void main(String[] args) {
    System.out.println("\nStarting…\n");

    XMIResourceImpl resource = new XMIResourceImpl();
    SimplePDLSemantics.DDMMSimplePDL.Process p_root;
    Map opts = new HashMap();
    opts.put(XMIResource.OPTION_SCHEMA_LOCATION, java.lang.Boolean.TRUE);

    if (args.length>0) {
      DDMMSimplePDLPackage packageInstance = DDMMSimplePDLPackage.eINSTANCE;
      File input = new File(args[0]);
      try {
        resource.load(new FileInputStream(input),opts);
      } catch (Exception e) {
        e.printStackTrace();
      }
      p_root = (SimplePDLSemantics.DDMMSimplePDL.Process) resource.getContents().get(0);
    } else {
      System.out.println("No model instance given in argument. Using default hardcoded model.");
      WorkDefinition wd1 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),"A",null);
      WorkDefinition wd2 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),"B",null);
      WorkDefinition wd3 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),"C",null);
      WorkDefinition wd4 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),"D",null);
      WorkSequence ws1 = `WorkSequence(null,startToStart(),wd1,wd2);
      WorkSequence ws2 = `WorkSequence(null,startToFinish(),wd3,wd4);

      p_root = `Process("root",ProcessElementEList(wd1,wd2,ws1),null);
      SimplePDLSemantics.DDMMSimplePDL.Process p_child = `Process("child",ProcessElementEList(wd3,wd4,ws2), wd2);

      wd1.setParent(p_root);
      wd2.setParent(p_root);
      wd2.setProcess(p_child);

      wd3.setParent(p_child);
      wd4.setParent(p_child);

      ws1.setParent(p_root);
      ws2.setParent(p_child);
    }
    SimplePDLToPetriNoHash translator = new SimplePDLToPetriNoHash();

    try {
      translator.pn = `PetriNet(NodeEList(),ArcEList(),"main");

      /*
      Strategy transformer = `Sequence(
          TopDown(Process2PetriNet(translator)),
          TopDown(WorkDefinition2PetriNet(translator)),
          TopDown(WorkSequence2PetriNet(translator))
       );
       */
      Strategy transformer = `SimplePDLToPetriNet(translator,translator.link,translator.pn);
      transformer.visit(p_root, new EcoreContainmentIntrospector());

      /*
      System.out.println("\nBefore Resolve");
      `Sequence(TopDown(PrintTransition()),TopDown(PrintPlace())).visit(translator.pn, new EcoreContainmentIntrospector());
       */

      /*
      System.out.println("\nTest strategy resolve");
      `TopDown(Resolve(translator)).visit(translator.pn, new EcoreContainmentIntrospector());
       */

      System.out.println("\nResult");
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
        return `tr;
      }

      ptr@ResolveProcessTransition[o=o,name=name] -> {
        System.out.println("tr process resolve " + `name);
        return `ptr;
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
        return `pl;
      }

      Place[name=name,initialMarking=w] && w!=0 -> {
        System.out.println("pl " + `name + " " + `w);
      }

    }
  }
  
}
