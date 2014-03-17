import java.util.*;

import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.xmi.*;
import org.eclipse.emf.ecore.xmi.impl.*;

import java.io.File;
import java.io.FileOutputStream;

import SimplePDLSemantics.DDMMSimplePDL.*;

import tom.library.emf.*;

public class GenModel {

  //%include{ sl.tom }
  %include{ emf/ecore.tom }
  %include{ mappings/DDMMSimplePDLPackage.tom }

  /*
     Example #1:
      - 1 Process composed of N WD
      - variant :
        a) WD are linked by finish2start WS
        b) WD are independant, therefore there is no WS
        c) WD are fully independant, the Process is not modelized and
           synchronized with WD
   */

  /*public static SimplePDLSemantics.DDMMSimplePDL.Process getModel(int exNbr,
      char variant, int wdNbr) {
    switch(exNbr) {
      case 1:
        return example1(variant, wdNbr);
    }
    return null;
  }

  public static SimplePDLSemantics.DDMMSimplePDL.Process example1(char variant,
      int wdNbr) {
    switch(variant) {
      case 'a':
        return getModel1a(wdNbr, );
      case 'b':
      return ;
    }
    return null;
  }*/


  public static SimplePDLSemantics.DDMMSimplePDL.Process getModel(int wdNbr,
      boolean hasWS) {
    SimplePDLSemantics.DDMMSimplePDL.Process p_root = `Process("root",ProcessElementEList(),null);

    org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement> pel = `ProcessElementEList();

    WorkDefinition wd0 =
      `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),"WD0",null);
    pel = `ProcessElementEList(wd0);

    String name = "";
    WorkDefinition wd1 = null;
    if(hasWS) {
      WorkSequence ws1 = null;
      for(int i=1;i<wdNbr;i++) {
        name = "WD"+i;
        wd1 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),name,null);
        pel = `ProcessElementEList(wd1,pel*);
        ws1 = `WorkSequence(null,WorkSequenceTypefinishToStart(),wd0,wd1);
        pel = `ProcessElementEList(ws1,pel*);
        wd0 = wd1;
      }
    } else {
      for(int i=1;i<wdNbr;i++) {
        name = "WD"+i;
        wd1 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),name,null);
        pel = `ProcessElementEList(wd1,pel*);
      }
    }
    p_root = `Process("root",pel,null);
    %match(pel) {
      ProcessElementEList(_*,x,_*) -> {
        `x.setParent(p_root);
      }
    }
    return p_root;
  }

  public static void print(SimplePDLSemantics.DDMMSimplePDL.Process process) {
    %match(process) {
      Process(name, pelist, parent) -> {
        System.out.println("Process "+`name+((`parent==null)?"":" fils de"+`parent));
        print(`pelist);
      }
    }
  }

  public static void print(org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement> pelist) {
    %match(pelist) {
      ProcessElementEList(_*,WorkSequence(_,type,pre,succ),_*) -> {
        String typeStr = "";
        %match(type) {
          WorkSequenceTypestartToStart()   -> { typeStr="s2s"; }
          WorkSequenceTypestartToFinish()  -> { typeStr="s2f"; }
          WorkSequenceTypefinishToStart()  -> { typeStr="f2s"; }
          WorkSequenceTypefinishToFinish() -> { typeStr="f2f"; }
        }
        System.out.println("ws: "+`pre.getName()+" --"+typeStr+"--> "+`succ.getName());
      }
    }
  }

  public static void saveToXMI(SimplePDLSemantics.DDMMSimplePDL.Process process, String name) {
    XMIResource resource = new XMIResourceImpl();
    resource.getContents().add(process);
    File output = new File(".",name+".xmi");
    try {
      FileOutputStream outputStream = new FileOutputStream(output);
      resource.save(outputStream, new HashMap<Object,Object>());
      System.out.println("Model saved into .xmi file.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void genXMIModel(int wdNbr, boolean hasWS) {
    XMIResource resource = new XMIResourceImpl();
    //getModel(wdNbr)
    SimplePDLSemantics.DDMMSimplePDL.Process process = getModel(wdNbr, hasWS);
    resource.getContents().add(process);
    File output = new File(".","bench"+wdNbr+(hasWS?"wWS":"noWS")+".xmi");
    try {
    FileOutputStream outputStream = new FileOutputStream(output);
    resource.save(outputStream, new HashMap<Object,Object>());
System.out.println("Generated model saved.");
    //resource.save(Collections.EMPTY_MAP);

    //// Initialize the model
    //SimplePDLSemantics.DDMMSimplePDL.DDMMSimplePDLPackage.eINSTANCE.eClass();
    //// Retrieve the default factory singleton
    //DDMMSimplePDLFactory factory = DDMMSimplePDLFactory.eINSTANCE;
    //
    //// Create the content of the model via this program
    //SimplePDLSemantics.DDMMSimplePDL.Process process = getModel(wdNbr);
    //
    //// As of here we preparing to save the model content
    //
    //// Register the XMI resource factory for the .website extension
    //
    //Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
    //Map<String, Object> m = reg.getExtensionToFactoryMap();
    //m.put("simplepdl", new XMIResourceFactoryImpl());
    //
    //// Obtain a new resource set
    //ResourceSet resSet = new ResourceSetImpl();
    //
    //// Create a resource
    //Resource resource = resSet.createResource(URI
    //    .createURI("simplepdl/bench"+wdNbr+".simplepdl"));
    //// Get the first model element and cast it to the right type, in my
    //// example everything is hierarchical included in this first node
    //resource.getContents().add(process);
    //
    //// Now save the content.
    //try {
    //  resource.save(Collections.EMPTY_MAP);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    }



}
