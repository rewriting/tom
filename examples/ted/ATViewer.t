package ted;

import java.io.*;
import aterm.*;
import aterm.pure.SingletonFactory;
import java.util.*;
import java.lang.reflect.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.*;
import tom.library.traversal.*;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.Frame;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;

public class ATViewer extends JPanel {

  %include { atermmapping.tom }
  %include { mutraveler.tom }

  private JEditorPane htmlPane;
  private static ATermFactory atermFactory = SingletonFactory.getInstance();

  public ATViewer(String[] filenames) {
    super(new GridLayout(1,0));
    run(filenames);
  }
  
  public void run(String[] filenames) {

    // aterm to modify
    ATerm inputaterm = null; 
    
    JPanel intermediate = new JPanel();
    intermediate.setLayout(new java.awt.GridLayout(1,filenames.length));
    
    for (int i=0; i<filenames.length; i++){
    	
    	try {
    	      inputaterm = atermFactory.readFromFile(filenames[i]);
    	      
    	      DefaultMutableTreeNode top = createTree(inputaterm);
    	      JTree tree = new JTree(top);
    	      tree.getSelectionModel().setSelectionMode
    	        (TreeSelectionModel.SINGLE_TREE_SELECTION);
    	      
    	      intermediate.add(tree);
    	      
    	    } catch (IOException e) {
    	      System.err.println("Bad input");
    	      System.exit(1);
    	    }	
    }
    
    //Create the scroll pane and add the tree to it. 
    JScrollPane treeView = new JScrollPane(intermediate);

    //Create the HTML viewing pane.
    htmlPane = new JEditorPane();
    htmlPane.setEditable(false);
    // it will be nice to be able to run ted scripts typed in this area
    JScrollPane htmlView = new JScrollPane(htmlPane);

    //Add the scroll panes to a split pane.
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setTopComponent(treeView);
    splitPane.setBottomComponent(htmlView);

    //Add the split pane to this panel.
    add(splitPane);

  }

  public DefaultMutableTreeNode createTree(ATerm at) {
    %match(ATerm at) {
      ATermAppl(AFun[name=name],arglist) -> {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(`name);
        return appendToTree(node,`arglist);
      }
      ATermInt(i) -> {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("int: "+`i);
        return node;
      }
      ATermPlaceholder( type@ATermAppl[fun=AFun[name=name]] ) -> {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("Placeholder");
        node.add(createTree(`type));
        return node;
      }
      ATermList(_*) -> {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("List");
        return appendToTree(node,(ATermList)at);
      }

      /* Placeholder with wrong format */
      ATermPlaceholder(_) -> {
        System.err.println("Bad placeholder format");
        System.exit(1);
      }
    }
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("empty");
    return node;
  }

  public DefaultMutableTreeNode appendToTree(DefaultMutableTreeNode node, ATermList alist) {
    while(!alist.isEmpty()) {
      node.add(createTree(alist.getFirst()));
      alist=alist.getNext();
    }
    return node;
  }

  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event-dispatching thread.
   */
  private static void createAndShowGUI(String[] filenames) {
    //Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(true);

    //Create and set up the window.
    JFrame frame = new JFrame("ATViewer");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    

    //Create and set up the content pane.
    ATViewer newContentPane = new ATViewer(filenames);
    newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);

    //Display the window.
    frame.pack();
    frame.setVisible(true);    
  }

  public static void main(String[] argv) {
    final String[] filenames = argv;
    if (filenames.length < 1){
    	System.out.println("At least one filename should be specified !");
    	System.exit(0);
    }
    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
        createAndShowGUI(filenames);
        }
        });
  }

}
