---
title: Documentation:Playing with EMF
permalink: /Documentation:Playing_with_EMF/
---

This section deals with using Tom to handle EMF models by using [Tom-EMF mappings generator](/Documentation:EMF_Mapping_Generator "wikilink") and the new `%transformation` construct in a Tom program.

We propose to illustrate the use of the EMF mappings generator and the `%transformation` with the following example: we specify a process in a simple PDL formalism, and we want to transform it into a Petri net. ([Here](http://www.irisa.fr/triskell/perso_pro/combemale/research/phd/2007/idm07-CCBV.pdf) is an explanation of the transformation SimplePDL2Tina -in french)

Our use case consist in a models transformation, from SimplePDL formalism to PetriNet formalism. In the [“MetaModels”](/Documentation:Playing_with_EMF#MetaModels "wikilink") section, we explain the two metamodels we use for the transformation; in the [“Example”](/Documentation:Playing_with_EMF#Example "wikilink") section we instantiate a source model; in the [“Writing and running SimplePDLToPetriNet transformation”](/Documentation:Playing_with_EMF#Writing_and_running_SimplePDLToPetriNet_transformation "wikilink") section, we detail the procedure to write the whole transformation: [how to generate the EMF model code](/Documentation:Playing_with_EMF#Generating_model_code "wikilink"), [how to generate EMF mappings](/Documentation:Playing_with_EMF#Generating_EMF_mappings "wikilink"), [how to use dedicated Tom constructs](/Documentation:Playing_with_EMF#Using_dedicated_Tom_constructs_to_write_the_transformation "wikilink"), [the whole source code](/Documentation:Playing_with_EMF#Full_code_of_the_transformation "wikilink") and finally [how to run the example](/Documentation:Playing_with_EMF#Running_the_example "wikilink"). The [last](/Documentation:Playing_with_EMF#Downloadable_resources "wikilink") section allows you to download all resources we use for this example.

Case study
==========

MetaModels
----------

Our models are defined as follow by using Eclipse with the modeling module.

### Source MetaModel: SimplePDL

The SimplePDL metamodel defines the concept of Process composed of ProcessElements. Each process element can be either a WorkDefinition or a WorkSequence. Work definitions are the activities that must be performed during a process. A work sequence defines a dependency relationship between two work definitions. The second work definition (successor) can be started — or finished — only when the first one (predecessor) is already started — or finished — according to the value of the attribute linkType (four possible values). Finally, a work definition can be defined as a nested process (process reference), allowing the definition of hierarchal processes.

[Image:simplePDL3c.png](/Image:simplePDL3c.png "wikilink")

To generate this diagram, you can import the [SimplePDLSemantics_updated.ecore](http://tom.loria.fr/wiki/uploads/SimplePDLSemantics_updated.ecore) file into your Eclipse project and use the 'generate ecore diagram' functionality. In this model, there is a notion of hierarchy: an activity (WorkDefinition) can be described by a process.

### Target MetaModel: Petri Net

A PetriNet is composed of Nodes that denote either a Place or a Transition. Nodes are linked by Arc. Arcs can be normal ones or read-arcs. An arc specifies the number of tokens (weight) consumed in the source place or produced in the target one when a transition is fired. A read-arc (second value in ArcKind enumeration) only checks tokens availability without removing them. A Petri net marking is defined by the number of tokens contained in each place (marking).

[Image:netofpetri_ecorediag.png](/Image:netofpetri_ecorediag.png "wikilink")

To generate this diagram, you can import the [PetriNetSemantics_updated.ecore](http://tom.loria.fr/wiki/uploads/PetriNetSemantics_updated.ecore) file into your Eclipse project and use the 'generate ecore diagram' functionality.

Example
-------

To execute this transformation, we instantiate a model as following:

-   the main Process is named root
-   root Process is composed of two WorkDefinitions (wd1/A & wd2/B) and one WorkSequence (ws1)
-   WorkDefinition B is described by an other Process</code>, named child
-   child Process is composed of two WorkDefinitions (wd3/C & wd4/D) and one WorkSequence (ws2)
-   WorkSequences connect the WorkDefinitions as follow:
    -   ws1: A has to to be started to start B (StartToStart)
    -   ws2: C has to be finished to start D (FinishToStart)

This process is translated in the Petri net formalism by the SimplePDLToPetriNet transformation, which is illustrated by the following figure:

<center>
[Image:Simplepdlusecase.png](/Image:Simplepdlusecase.png "wikilink") → [Image:CompleteProcess.png](/Image:CompleteProcess.png "wikilink")

</center>
Writing and running SimplePDLToPetriNet transformation
======================================================

Generating model code
---------------------

If you don't know how to generate model code using EMF, you should have a look at [this tutorial](http://help.eclipse.org/ganymede/index.jsp?topic=/org.eclipse.emf.doc/tutorials/clibmod/clibmod.html). In this example, we will use the following ecore metamodels you can import into your Eclipse project to generate the EMF Java code:

-   [SimplePDLSemantics_updated.ecore file](http://tom.loria.fr/wiki/uploads/SimplePDLSemantics_updated.ecore)
-   [PetriNetSemantics_updated.ecore file](http://tom.loria.fr/wiki/uploads/PetriNetSemantics_updated.ecore)

Another possible solution consists in downloading the two corresponding java files we have generated with Eclipse from previous Ecore metamodels:

-   [SimplePDL .jar file](http://tom.loria.fr/wiki/uploads/simplepdlsemantics_updated_1.2.jar)
-   [PetriNet .jar file](http://tom.loria.fr/wiki/uploads/petrinetsemantics_updated_1.2.jar)

Generating EMF mappings
-----------------------

To generate EMF mappings, you will need to compile the [EMF mapping generator](/Documentation:EMF_Mapping_Generator "wikilink").

Add needed EMF jars to CLASSPATH:

`` $ export CLASSPATH=`echo ${TOM_HOME}/lib/tools/org.eclipse.emf*.jar | tr ' ' ':'`:${CLASSPATH} ``

The easy way to generate EMF mappings for this example is to use the gen_mappings.sh script included in the full archive file available in the [download section](/Documentation:Playing_with_EMF#Downloadable_resources "wikilink") (or alternatively, you can use the provided mappings, included in the full archive).

In this script, the generator is called and takes the generated EPackage class as argument. For example:

`$ emf-generate-mappings -cp .:./lib/simplepdlsemantics_updated_1.2.jar $OPTS SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLPackage SimplePDLSemantics.EDMMSimplePDL.EDMMSimplePDLPackage SimplePDLSemantics.SDMMSimplePDL.SDMMSimplePDLPackage SimplePDLSemantics.TM3SimplePDL.TM3SimplePDLPackage`
`$ SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLPackage.tom DDMMSimplePDLPackage.tom`
`$ mv SimplePDLSemantics.EDMMSimplePDL.EDMMSimplePDLPackage.tom EDMMSimplePDLPackage.tom`
`$ mv SimplePDLSemantics.SDMMSimplePDL.SDMMSimplePDLPackage.tom SDMMSimplePDLPackage.tom`
`$ mv SimplePDLSemantics.TM3SimplePDL.TM3SimplePDLPackage.tom   TM3SimplePDLPackage.tom`

`$ emf-generate-mappings -cp .:./lib/petrinetsemantics_updated_1.2.jar $OPTS petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage petrinetsemantics.EDMMPetriNet.EDMMPetriNetPackage petrinetsemantics.SDMMPetriNet.SDMMPetriNetPackage petrinetsemantics.TM3PetriNet.TM3PetriNetPackage`
`$ mv petrinetsemantics.DDMMPetriNet.DDMMPetriNetPackage.tom DDMMPetriNetPackage.tom`
`$ mv petrinetsemantics.EDMMPetriNet.EDMMPetriNetPackage.tom EDMMPetriNetPackage.tom`
`$ mv petrinetsemantics.SDMMPetriNet.SDMMPetriNetPackage.tom SDMMPetriNetPackage.tom`
`$ mv petrinetsemantics.TM3PetriNet.TM3PetriNetPackage.tom   TM3PetriNetPackage.tom`

Alternatively, you can directly use the provided mappings, included in the full archive.

Using dedicated Tom constructs to write the transformation
----------------------------------------------------------

To write this transformation, one have to decompose it into elementary transformations:

-   Process2PetriNet
-   WorkDefinition2PetriNet
-   WorkSequence2PetriNet

Each of them is encoded as a Tom strategy which traverses the source model, visits Processes (respectively WorkDefinitions and WorkSequence) and creates the corresponding Petri net. In our approach, the user does **not** have to define the order of the application of the elementary transformations. However it can be problematic if an elementary transformation needs an element which is not yet created or completed in the output model (element which should be created in another elementary transformation). Therefore a mechanism has to be set to address this problem: we choosed to introduce temporary elements we call *Resolve elements* which are used during a transformation step as if it was the real elements. At the end of the whole transformation, an additional final step we called *Resolve strategy* is executed to find these temporary elements and to replace links referencing them by links referencing the real elements.

For more details concerning the involved mechanisms and the general method to transform models with Tom, please refer to [LDTA'12 article](https://dl.acm.org/citation.cfm?id=2427052) (with the same case study).

In this section, we will show the use of `%transformation`, `%resolve` and `%tracelink` constructs which rely on the proposed method, and we will explain them. These constructs also simplify the writing of an EMF model transformation presented in the paper.

A transformation is defined by a `%transformation` construct -`SimplePDLToPetriNet` in our case-, which is composed of named definitions (which encode elementary transformations), parameterized by a traversal strategy (used in the resolution phase). In our example, we have three definitions: `P2PN` (Process2PetriNet), `WD2PN` (WorkDefinition2PetriNet) and WS2PN (WorkSequence2PetriNet). Each definition is composed of rules, where the left-hand side is a pattern, and the right-hand side is block of Tom+Java code. The last step (resolution phase) is now fully generated and does not appear explicitely in the transformation code except its call in the main procedure. A `%transformation` construct can have parameters: in our example `tom__linkClass` of type `LinkClass` (links model, used to maintain links between source and target during the transformation) and `pn` of type `PetriNet` (resulting model which is build during the transformation). It also take two other parameters which are the source and the target .ecore metamodels.

Now, Let us detail the transformation:

The first definition, P2PN, creates the Petri net that corresponds to the image of a Process. This Petri net is composed of three places (p_ready , p_running and p_finished), two transitions (t_start and t_finish) and four arcs. During the transformation, interesting elements (i.e. transitions and places which will be necessary in another definition or that the developer wants to trace) have to be traced by using the `%tracelink` construct. This keyword allows to create and to keep a track of the created element by saving it in the links metamodel. It takes a name, a type and a backquote term as parameters. For instance, `` %tracelink(t_start:Transition, `Transition(n1, pn,ArcEList(), ArcEList(), 1, 1)); `` means that a `Transition` named `t_start` , represented by the term `` `Transition(n1, pn,ArcEList(), ArcEList(), 1, 1) `` is created and saved.

The second definition, WD2PN, creates a Petri net representing an activity. It is composed of four places (p_ready , p_running , p_started and p_finished), two transitions (t_start and t_finish), and five arcs. In this definition, four elements are traced by using the `%tracelink` construct we saw in the previous definition. As a Process is not represented by a single activity but by a Petri net, and as it is composed of many activities, it is necessary to link each activity to the parent process. Therefore, two Arcs are created between each activity and its parent process. However, the current term which is visited (a WorkDefinition) does not “know” other structures like Process. Therefore intermediate nodes are then created for those Arcs by using the `%resolve` construct which is dedicated to the creation of *resolve elements*. Internally, this construct also allows to generate an ad-hoc Java data-structure and its algebraic view.

The third definition, WS2PN, creates links between activities of a same process. In this part, we used two *resolve nodes* to create an Arc between two activities. In the Petri net case, this type of link is represented by a read-arc which controls that a transition is enabled (it checks if a token is present).

The following figures illustrate the Petri nets which are created by each definition:

<center>
<table>
<tr>
<td>
[frame|Petri net representing a Process](/Image:Process.png "wikilink")

</td>
<td>
[frame|Petri net representing an activity](/Image:WorkDefinition.png "wikilink")

</td>
<td>
[frame|Petri net representing a sequence](/Image:WorkSequence.png "wikilink")

</td>
</tr>
</table>
</center>
Full code of the transformation
-------------------------------

Let's write the transformation and explain it:

``` tom
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

  %transformation SimplePDLToPetriNet(tom__linkClass:LinkClass,pn:PetriNet) : "metamodels/SimplePDLSemantics_updated.ecore" -> "metamodels/PetriNetSemantics_updated.ecore" {

    definition P2PN traversal `TopDown(P2PN(tom__linkClass,pn)) {
      p@Process[name=name] -> {
        Place p_ready  = `Place(name + "_ready", pn,ArcEList(), ArcEList(), 1);
        Place p_running  = `Place(name + "_running", pn,ArcEList(), ArcEList(), 0);
        Place p_finished  = `Place(name + "_finished", pn,ArcEList(), ArcEList(), 0);
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
```

Running the example
-------------------

The easy way to compile and run the case study is to use the provided Makefile by typing `make`. You can also do it by typing step-by-step the following commands:

For Unix-like systems:

`tom -p SimplePDLToPetriNet.t`
`javac -cp petrinetsemantics_updated_1.2.jar:simplepdlsemantics_updated_1.2.jar:$CLASSPATH SimplePDLToPetriNet.java`
`java -cp petrinetsemantics_updated_1.2.jar:simplepdlsemantics_updated_1.2.jar:$CLASSPATH SimplePDLToPetriNet`

For Windows systems:

`tom -p SimplePDLToPetriNet.t`
`javac -cp `“`petrinetsemantics_updated_1.2.jar;simplepdlsemantics_updated_1.2.jar;%CLASSPATH%`”` SimplePDLToPetriNet.java`
`java -cp `“`petrinetsemantics_updated_1.2.jar;simplepdlsemantics_updated_1.2.jar;%CLASSPATH%`”` SimplePDLToPetriNet`

The execution of this transformation should produced the following output:

`tr root_start root_ready*1  --> root_running*1 A_ready*1 B_ready*1 `
`tr root_finish root_running*1 A_finished?1 B_finished?1  --> root_finished*1 `
`tr child_start child_ready*1  --> child_running*1 C_ready*1 D_ready*1 `
`tr child_finish child_running*1 C_finished?1 D_finished?1  --> child_finished*1 `
`tr B_start B_ready*1 A_started?1  --> B_started*1 B_running*1 child_ready*1 `
`tr B_finish B_running*1 child_finished?1  --> B_finished*1 `
`tr A_start A_ready*1  --> A_started*1 A_running*1 `
`tr A_finish A_running*1  --> A_finished*1 `
`tr C_start C_ready*1  --> C_started*1 C_running*1 `
`tr C_finish C_running*1  --> C_finished*1 `
`tr D_start D_ready*1  --> D_started*1 D_running*1 `
`tr D_finish D_running*1 C_started?1  --> D_finished*1 `
`pl root_ready 1`
`pl child_ready 1`
`pl A_ready 1`
`pl B_ready 1`
`pl C_ready 1`
`pl D_ready 1`

Downloadable resources
======================

You can download here useful files for this example:

-   [SimplePDLToPetriNet archive](http://tom.loria.fr/wiki/uploads/SimplePDLToPetriNet.tar.gz) containing all files to compile and run the SimplePDLToPetriNet example. This archive contains the following files:

`SimplePDLToPetriNet/ `
`SimplePDLToPetriNet/Makefile`
`SimplePDLToPetriNet/models/`
`SimplePDLToPetriNet/models/SimplePDLInstance2.xmi`
`SimplePDLToPetriNet/models/DefaultSimplePDLInstance.xmi`
`SimplePDLToPetriNet/gen_mappings.sh`
`SimplePDLToPetriNet/execute.sh`
`SimplePDLToPetriNet/expected_result`
`SimplePDLToPetriNet/mappings/`
`SimplePDLToPetriNet/mappings/TM3SimplePDLPackage.tom`
`SimplePDLToPetriNet/mappings/TM3PetriNetPackage.tom`
`SimplePDLToPetriNet/mappings/EDMMSimplePDLPackage.tom`
`SimplePDLToPetriNet/mappings/SDMMSimplePDLPackage.tom`
`SimplePDLToPetriNet/mappings/DDMMSimplePDLPackage.tom`
`SimplePDLToPetriNet/mappings/SDMMPetriNetPackage.tom`
`SimplePDLToPetriNet/mappings/EDMMPetriNetPackage.tom`
`SimplePDLToPetriNet/mappings/DDMMPetriNetPackage.tom`
`SimplePDLToPetriNet/metamodels/`
`SimplePDLToPetriNet/metamodels/PetriNetSemantics_updated.ecore`
`SimplePDLToPetriNet/metamodels/SimplePDLSemantics_updated.ecore`
`SimplePDLToPetriNet/exemple_gen_mappings.sh`
`SimplePDLToPetriNet/SimplePDLToPetriNet.t`
`SimplePDLToPetriNet/result.todiff`
`SimplePDLToPetriNet/lib/`
`SimplePDLToPetriNet/lib/simplepdlsemantics_updated_1.2.jar`
`SimplePDLToPetriNet/lib/petrinetsemantics_updated_1.2.jar`

-   [SimplePDLToPetriNet archive md5sum](http://tom.loria.fr/wiki/uploads/usecase_SimplePDLToPetriNet.tar.gz.md5sum)
-   [SimplePDL .ecore file](http://tom.loria.fr/wiki/uploads/SimplePDLSemantics_updated.ecore)
-   [PetriNet .ecore file](http://tom.loria.fr/wiki/uploads/PetriNetSemantics_updated.ecore)
-   [SimplePDL .jar file](http://tom.loria.fr/wiki/uploads/simplepdlsemantics_updated_1.2.jar)
-   [PetriNet .jar file](http://tom.loria.fr/wiki/uploads/petrinetsemantics_updated_1.2.jar)

[Category:Documentation](/Category:Documentation "wikilink")