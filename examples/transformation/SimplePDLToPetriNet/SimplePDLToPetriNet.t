import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.*;
import org.eclipse.emf.ecore.xmi.impl.*;

import SimplePDLSemantics.DDMMSimplePDL.*;
import petrinetsemantics.DDMMPetriNet.*;

import SimplePDLSemantics.EDMMSimplePDL.*;
import petrinetsemantics.EDMMPetriNet.*;
import SimplePDLSemantics.SDMMSimplePDL.*;
import petrinetsemantics.SDMMPetriNet.*;
import SimplePDLSemantics.TM3SimplePDL.*;
import petrinetsemantics.TM3PetriNet.*;

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

public class SimplePDLToPetriNet {

  %include{ sl.tom }
  %include{ LinkClass.tom }
  %include{ emf/ecore.tom }

  %include{ mappings/DDMMPetriNetPackage.tom }
  %include{ mappings/DDMMSimplePDLPackage.tom }

  %include{ mappings/EDMMPetriNetPackage.tom }
  %include{ mappings/EDMMSimplePDLPackage.tom }
  %include{ mappings/SDMMPetriNetPackage.tom }
  %include{ mappings/SDMMSimplePDLPackage.tom }
  %include{ mappings/TM3PetriNetPackage.tom }
  %include{ mappings/TM3SimplePDLPackage.tom }

  %typeterm SimplePDLToPetriNet { implement { SimplePDLToPetriNet }}
  
  private static PetriNet pn = null;
  private static LinkClass tom__linkClass;

  public SimplePDLToPetriNet() {
    this.tom__linkClass = new LinkClass();
  }


  %transformation SimplePDLToPetriNet(tom__linkClass:LinkClass,pn:PetriNet) : "metamodels/SimplePDLSemantics.ecore" -> "metamodels/PetriNetSemantics.ecore" {

    definition P2PN traversal `TopDown(P2PN(tom__linkClass,pn)) {
      p@Process[name=name] -> {
        Place p_ready  = `Place(name + "_ready", pn,ArcEList(), ArcEList(), 1);
        //Place p_running  = `Place(name + "_running", pn, ArcEList(), ArcEList(), 0);
        Place p_running  = `Place[name=(name+"_running"),net=pn,outgoings=ArcEList(),incomings=ArcEList()];
        //Place p_finished  = `Place(name + "_finished", pn,ArcEList(), ArcEList(), 0);
        Place p_finished = `Place[name=(name+"_finished"),net=pn,outgoings=ArcEList(),incomings=ArcEList()];
        String n1 = `name+"_start";
        %tracelink(t_start:Transition, `Transition(n1, pn,ArcEList(), ArcEList(), 1, 1));
        n1 = `name+"_finish";
        %tracelink(t_finish:Transition, `Transition(n1, pn,ArcEList(), ArcEList(), 1, 1));
        
        `Arc(t_start, p_ready, pn,ArcKindnormal(), 1);
        `Arc(p_running, t_start, pn,ArcKindnormal(), 1);
        `Arc(t_finish, p_running, pn,ArcKindnormal(), 1);
        `Arc(p_finished, t_finish, pn,ArcKindnormal(), 1);

        WorkDefinition from = `p.getFrom();
        if (from!=null) {
          Transition source = %resolve(from:WorkDefinition,t_start:Transition);
          source.setNet(pn);
          Arc tmpZoomIn = `Arc(p_ready,source,pn,ArcKindnormal(), 1);

          Transition target = %resolve(from:WorkDefinition,t_finish:Transition);
          target.setNet(pn);
          Arc tmpZoomOut = `Arc(target,p_finished,pn,ArcKindread_arc(), 1);
        }
      }
    }

    definition WD2PN traversal `TopDown(WD2PN(tom__linkClass,pn)) {
      wd@WorkDefinition[name=name] -> {
        //System.out.println("Je suis un A");
        Place p_ready  = `Place(name + "_ready", pn,ArcEList(), ArcEList(), 1);
        String n1 = `name+"_started";
        %tracelink(p_started:Place, `Place(n1, pn,ArcEList(), ArcEList(), 0));
        Place p_running  = `Place(name+"_running", pn,ArcEList(), ArcEList(), 0);
        n1 = `name+"_finished";
        %tracelink(p_finished:Place, `Place(n1, pn,ArcEList(), ArcEList(), 0));
        n1 = `name+"_start";
        %tracelink(t_start:Transition, `Transition(n1, pn,ArcEList(), ArcEList(), 1, 1));
        n1 = `name+"_finish";
        %tracelink(t_finish:Transition, `Transition(n1, pn,ArcEList(), ArcEList(), 1, 1));

        `Arc(t_start, p_ready, pn,ArcKindnormal(), 1);
        `Arc(p_started, t_start, pn,ArcKindnormal(), 1);
        `Arc(p_running, t_start, pn,ArcKindnormal(), 1);
        `Arc(t_finish, p_running, pn,ArcKindnormal(), 1);
        `Arc(p_finished, t_finish, pn,ArcKindnormal(), 1);

        SimplePDLSemantics.DDMMSimplePDL.Process parent = `wd.getParent();
        Transition source = %resolve(parent:Process,t_start:Transition);
        source.setNet(pn);
        Arc tmpDistribute = `Arc(p_ready,source,pn,ArcKindnormal(), 1);

        Transition target = %resolve(parent:Process,t_finish:Transition);
        target.setNet(pn);
        Arc tmpRejoin = `Arc(target,p_finished,pn,ArcKindread_arc(), 1);
      }
    }

    definition WS2PN traversal `TopDown(WS2PN(tom__linkClass,pn)) {
      ws@WorkSequence[predecessor=p,successor=s,linkType=linkType] -> {
        Place source= null;
        Transition target= null;
        WorkDefinition pre = `p;
        WorkDefinition suc = `s;
        %match(linkType) { 
          (WorkSequenceTypefinishToFinish|WorkSequenceTypefinishToStart)[] -> { source = %resolve(pre:WorkDefinition,p_finished:Place); }
          (WorkSequenceTypestartToStart|WorkSequenceTypestartToFinish)[]   -> { source = %resolve(pre:WorkDefinition,p_started:Place); }

          (WorkSequenceTypefinishToStart|WorkSequenceTypestartToStart)[]   -> { target = %resolve(suc:WorkDefinition,t_start:Transition); }
          (WorkSequenceTypestartToFinish|WorkSequenceTypefinishToFinish)[] -> { target = %resolve(suc:WorkDefinition,t_finish:Transition); }
        }
        source.setNet(pn);
        target.setNet(pn);

        Arc tmp = `Arc(target,source, pn,ArcKindread_arc(), 1);  
      }
    }
  }

  public static void main(String[] args) {
    System.out.println("\nStarting…\n");

    XMIResourceImpl resource = new XMIResourceImpl();
    SimplePDLSemantics.DDMMSimplePDL.Process p_root;
    Map opts = new HashMap();
    opts.put(XMIResource.OPTION_SCHEMA_LOCATION, java.lang.Boolean.TRUE);

    //Loading an instance of SimplePDL MM (.xmi)
    if (args.length>0) {
      System.out.println("Using given model instance: "+args[0]);
      DDMMSimplePDLPackage packageInstance = DDMMSimplePDLPackage.eINSTANCE;
      File input = new File(args[0]);
      try {
        resource.load(new FileInputStream(input),opts);
      } catch (Exception e) {
        System.err.println("Exception occured while loading the resource file: " + e.getMessage()); 
        //e.printStackTrace();
      }
      p_root = (SimplePDLSemantics.DDMMSimplePDL.Process) resource.getContents().get(0);
    } else {
      //writing an instance of SimplePDL from scratch, by using Tom
      System.out.println("No model instance given in argument. Using default hardcoded model.");
      WorkDefinition wd1 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),"A",null);
      WorkDefinition wd2 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),"B",null);
      WorkDefinition wd3 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),"C",null);
      WorkDefinition wd4 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),"D",null);
      WorkSequence ws1 = `WorkSequence(null,WorkSequenceTypestartToStart(),wd1,wd2);
      WorkSequence ws2 = `WorkSequence(null,WorkSequenceTypestartToFinish(),wd3,wd4);

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
    SimplePDLToPetriNet translator = new SimplePDLToPetriNet();

    try {
      translator.pn = `PetriNet(NodeEList(),ArcEList(),"main");

      //System.out.println("Initial Petri net");
      //`Sequence(TopDown(PrintTransition()),TopDown(PrintPlace())).visit(translator.pn, new EcoreContainmentIntrospector());

      /*//transformer is equivalent to:
      Strategy transformer = `Sequence(
          TopDown(Process2PetriNet(translator)),
          TopDown(WorkDefinition2PetriNet(translator)),
          TopDown(WorkSequence2PetriNet(translator))
       );
       */

      //NOTE: force the user to give the link as first parameter, and target
      //model as second one
      Strategy transformer = `SimplePDLToPetriNet(translator.tom__linkClass,translator.pn);
      transformer.visit(p_root, new EcoreContainmentIntrospector());
      `TopDown(tom__StratResolve_SimplePDLToPetriNet(translator.tom__linkClass,translator.pn)).visit(translator.pn, new EcoreContainmentIntrospector());

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
      tr@ResolveWorkDefinitionTransition[tom_resolve_element_attribute_name=name] -> {
        System.out.println("tr resolve " + `name);
        return `tr;
      }

      ptr@ResolveProcessTransition[tom_resolve_element_attribute_name=name] -> {
        System.out.println("tr process resolve " + `name);
        return `ptr;
      }

      Transition[name=name,incomings=sources,outgoings=targets] -> {
        String s = "";
        String t = "";
        %match {
         ArcEList(_*,Arc[kind=k,weight=w,source=node],_*) << sources && Place[name=placename]<< node -> {
           s += `placename + ((`k==`ArcKindread_arc())?"?":"*") + `w + " "; 
         }
         ArcEList(_*,Arc[kind=k,weight=w,target=node],_*) << targets && Place[name=placename]<< node -> { 
           t += `placename + ((`k==`ArcKindread_arc())?"?":"*") + `w + " "; 
         }
        }
        System.out.println("tr " + `name + " " + s + " --> " + t);
      }

      
    }
  }
  
  %strategy PrintPlace() extends Identity() {
    visit Place {
      pl@ResolveWorkDefinitionPlace[tom_resolve_element_attribute_name=name] -> {
        System.out.println("pl resolve " + `name);
        return `pl;
      }

      Place[name=name,initialMarking=w] && w!=0 -> {
        System.out.println("pl " + `name + " " + `w);
      }

    }
  }
  
}
