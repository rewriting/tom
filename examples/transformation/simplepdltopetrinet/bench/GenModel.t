import java.util.*;

import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.xmi.*;
import org.eclipse.emf.ecore.xmi.impl.*;

import java.io.File;
import java.io.FileOutputStream;

import SimplePDLSemantics.DDMMSimplePDL.*;

public class GenModel {

  //%include{ sl.tom }
  %include{ emf/ecore.tom }
  %include{ mappings/DDMMSimplePDLPackage.tom }

  //public static void main(String[] args) {
  public static SimplePDLSemantics.DDMMSimplePDL.Process getModel(int wdNbr) {
    SimplePDLSemantics.DDMMSimplePDL.Process p_root = `Process("root",ProcessElementEList(),null);

    //ProcessElementEList pel = `ProcessElementEList();
    org.eclipse.emf.common.util.EList<SimplePDLSemantics.DDMMSimplePDL.ProcessElement> pel = `ProcessElementEList();

    WorkDefinition wd0 =
      `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),"WD0",null);
    pel = `ProcessElementEList(wd0);

    WorkDefinition wd1 = null;
    WorkSequence ws1 = null;
    String name = "";
    for(int i=1;i<wdNbr;i++) {
      name = "WD"+i;
      wd1 = `WorkDefinition(null,WorkSequenceEList(),WorkSequenceEList(),name,null);
      pel = `ProcessElementEList(wd1,pel*);
      //pel = pel.append(wd1);
      ws1 = `WorkSequence(null,WorkSequenceTypefinishToStart(),wd0,wd1);
      pel = `ProcessElementEList(ws1,pel*);
      //pel = pel.append(ws1);
      wd0 = wd1;
    }
    p_root = `Process("root",pel,null);
    %match(pel) {
      ProcessElementEList(_*,x,_*) -> {
        `x.setParent(p_root);
      }
    }
    //print(p_root);
    return p_root;
  }

  public static void print(SimplePDLSemantics.DDMMSimplePDL.Process process) {
    %match(process) {
      Process(name, pelist, parent) -> {
        System.out.println("Process "+`name+((`parent==null)?"":" fils de"+`parent));
        print(`pelist);
      }
    }
    //System.out.println("\n===\n");
  }

  //public static void print(SimplePDLSemantics.DDMMSimplePDL.ProcessElementEList pelist) {
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
  //public static void print(SimplePDLSemantics.DDMMSimplePDL.WorkDefinition wd) {
  //  System.out.println();
  //}

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

  public static void genXMIModel(int wdNbr) {
    XMIResource resource = new XMIResourceImpl();
    //getModel(wdNbr)
    SimplePDLSemantics.DDMMSimplePDL.Process process = getModel(wdNbr);
    resource.getContents().add(process);
    File output = new File(".","bench"+wdNbr+".xmi");
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
