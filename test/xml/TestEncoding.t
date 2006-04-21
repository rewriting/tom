package xml;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestEncoding extends TestCase {

  %include{adt/tnode/TNode.tom}

  private XmlTools xtools;

  public TestEncoding(){
    xtools = new XmlTools();
    xtools.setDeletingWhiteSpaceNodes(true);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestXml.class));
  }
  public void testFr(){
    TNode fileNode;
    //file opening needs to be improved...
    //case ant call from tom.home
    fileNode = (TNode)xtools.convertXMLToATerm("test/xml/data.xml");
    if (fileNode == null)
      //case ant call from tom.home/test
      fileNode = (TNode)xtools.convertXMLToATerm("xml/data.xml");

    fileNode = fileNode.getDocElem();
    TNode textNode = `xml(<data type="encoding"><datum lang="fr">#TEXT("eée")</datum></data>);

    System.out.println("fileNode: " + fileNode);
    System.out.println("textNode: " + textNode);
    //assertEquals("characters read from file and from textnode must be equals",textNode,fileNode);
  }
}
