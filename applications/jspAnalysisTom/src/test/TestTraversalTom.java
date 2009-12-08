package test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.jasper.JasperException;
import org.apache.jasper.compiler.Node;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.cyberneko.html.parsers.DOMParser;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import test.library.xml.XmlTools;
import test.tnode.types.TNode;

import com.redline.codebraces.jsptools.extractor.jasper.JasperCompiler;


class FileIterator implements Iterator<File> {

  private FileFilter filter;
  private File[] files;
  private int filePos = 0;
  private Stack<File> dirs = new Stack<File>();

  public FileIterator(File rootDir, FileFilter filter) {

    this.filter = filter;

    iterate(rootDir);
  }

  public boolean hasNext() {
    return (files.length > 0 && filePos < files.length) || (dirs.size() != 0);
  }

  public File next() {
    while (filePos < files.length) {

      File file = files[filePos++];
      if (file.isDirectory()) {
        dirs.push(file);
      }
      else {
        return file;
      }
    }

    if (dirs.size() > 0) {
      File dir = dirs.pop();
      if (dir != null) {
        iterate(dir);
        return next();
      }
    }
    return null;
  }

  private void iterate(File dir) {
    this.files = dir.listFiles(new FileFilter() {
      public boolean accept(File file) {
        return file.isDirectory() || filter.accept(file);
      }
    });
    this.filePos = 0;
  }

  public void remove() {
    throw new RuntimeException("Not supported.");
  }
}

public class TestTraversalTom {

  protected static Log log = LogFactory.getLog(TestTraversalTom.class);

  public static void main(String[] args)
    throws JasperException, IOException {
    System.out.println(System.getProperty("user.dir"));
    System.out.println(System.getProperty("uriroot"));

    Iterator<File> filesIt = new FileIterator(new File(System.getProperty("uriroot")),
      new FileFilter() {

        public boolean accept(File file) {
          return file.getName().endsWith("jclassViewer.jsp");
        }
      }); 

    //int counter = 0;
    while (filesIt.hasNext()) {
      File file = filesIt.next();
      if(file == null) {
        break; //todo
      }
      //if(counter > 0)
    	//  break;
      //counter++;
      System.out.println("Processing file " + file.getAbsolutePath());

      String jspFileName = file.getAbsolutePath().substring(System.getProperty("uriroot").length());

      Node.Root root = new JasperCompiler().execute(jspFileName,
        System.getProperty("classpath"), System.getProperty("uriroot"));

      final Node.Nodes nodes = root.getBody();

      Map<String, Integer> customTagCalls = new HashMap<String, Integer>();
      collectTagCalls(nodes, customTagCalls);

      // Debug
      StringBuffer htmlText = new StringBuffer();
      //JasperCompiler.setupPrinter();
      JasperCompiler.printNodes(nodes, htmlText, 0);

      System.out.println("Html page \n" + htmlText);

      DOMParser parser = new DOMParser();
      try {
        parser.parse(new InputSource(new StringReader(htmlText.toString())));
        final HTMLDocument document = (HTMLDocument)parser.getDocument();

        //Serialize DOM
        OutputFormat format = new OutputFormat(document);
        // as a String
        StringWriter stringOut = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(stringOut, format);
        serializer.serialize(document);

        System.out.println("\n THE pure XML form of the page " +
          "\n ------------------------- \n" + stringOut);

        /************ TOM ****************/
        
        XmlTools xtools = new XmlTools();
        InputStream is = new ByteArrayInputStream(stringOut.toString().getBytes("utf-8"));
        TNode tnodeDoc = (TNode)xtools.convertXMLToTNode(is);        
        TNode tagLibDoc = Utils.getTagLibVersion(tnodeDoc,customTagCalls.keySet());
        System.out.println("TOM:" + tagLibDoc);
        Utils.findTagLibsInChunk(tagLibDoc);
        
        /************ end TOM ****************/        
        
        for (Map.Entry<String, Integer> e : customTagCalls.entrySet()) {
          System.out.println("for key=" + e.getKey());
          final List tagCalls = new DOMXPath("//" + e.getKey()).selectNodes(document);
          System.out.println("found list=" + tagCalls);
          if(tagCalls.size() != e.getValue()) {
            System.out.println(
              "==============================" +
              "Found an imbalance on number of tags " + e.getKey() + " for " + jspFileName +
                "; diff = " + (e.getValue() - tagCalls.size()) +
              "==============================");
            //todo: hack to go around
            //assert e.getKey().equals("H_OUTPUTTEXT") /*&& (e.getValue() - tagCalls.size() == 1)*/;
          }
        }
        System.out.println("\n\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n\n");

//        final List tables = new DOMXPath("//TABLE").selectNodes(document);
//        assert tables.size() == 6;
//
//        final List bundles = new DOMXPath("//F_LOADBUNDLE").selectNodes(document);
//        assert bundles.size() == 1;
//
        final List<HTMLElement> forms = new DOMXPath("//H_FORM").selectNodes(document);
//        assert forms.size() == 3;

        for (HTMLElement form : forms) {
          System.out.println("" + form.getParentNode().getNodeName());
        }
      }
      catch (SAXException e) {
        e.printStackTrace();
      }
      catch (JaxenException e) {
        e.printStackTrace();
      }
    }
  }

  private static void collectTagCalls(Node.Nodes nodes, Map<String, Integer> customTagCalls) {
    if (nodes != null) {
      for (int i = 0; i < nodes.size(); i++) {
        Node node = nodes.getNode(i);
        if (node instanceof Node.CustomTag) {
          String key = ((Node.CustomTag)node).getPrefix() + "_" + node.getLocalName();
          key = key.toUpperCase();
          Integer count = customTagCalls.get(key);
          if (count == null) {
            count = 1;
          }
          else {
            count++;
          }
          customTagCalls.put(key, count);
          collectTagCalls(node.getBody(), customTagCalls);
        }
      }
    }
  }
}
