package xml;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TestEncoding extends TestCase {

  %include{adt/tnode/TNode.tom}

  private XmlTools xtools;
  private static Logger logger;
  private static Level level = Level.FINE;

	public void setUp() {
    logger = Logger.getLogger(getClass().getName());
    xtools = new XmlTools();
    xtools.setDeletingWhiteSpaceNodes(true);
  }

  public static void main(String[] args) {
    level = Level.INFO;
    junit.textui.TestRunner.run(new TestSuite(TestEncoding.class));
  }

  public void testFr(){
    TNode fileNode = xtools.convertXMLToTNode("xml/data.xml");

    fileNode = fileNode.getdocElem();
    //TNode textNode = `xml(<data type="encoding"><datum lang="fr">#TEXT("eée")</datum></data>);
    TNode textNode = `xml(<data type="encoding"><datum lang="fr">#TEXT("e\u00E9e")</datum></data>);

    logger.log(level,"fileNode: " + fileNode);
    logger.log(level,"textNode: " + textNode);

    assertEquals("characters read from file and from textnode must be equals",
        textNode,fileNode);
  }
}
