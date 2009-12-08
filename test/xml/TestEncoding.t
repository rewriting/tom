package xml;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class TestEncoding {

  %include{adt/tnode/TNode.tom}

  private XmlTools xtools;
  private static Logger logger;
  private static Level level = Level.FINE;

  @Before
	public void setUp() {
    logger = Logger.getLogger(getClass().getName());
    xtools = new XmlTools();
    xtools.setDeletingWhiteSpaceNodes(true);
  }

  public static void main(String[] args) {
    level = Level.INFO;
    org.junit.runner.JUnitCore.main(TestEncoding.class.getName());
  }

  @Test
  public void testFr(){
    TNode fileNode = xtools.convertXMLToTNode("xml/data.xml");

    fileNode = fileNode.getDocElem();
    //TNode textNode = `xml(<data type="encoding"><datum lang="fr">#TEXT("eée")</datum></data>);
    TNode textNode = `xml(<data type="encoding"><datum lang="fr">#TEXT("e\u00E9e")</datum></data>);

    logger.log(level,"fileNode: " + fileNode);
    logger.log(level,"textNode: " + textNode);

    assertEquals("characters read from file and from textnode must be equals",
        textNode,fileNode);
  }
}
