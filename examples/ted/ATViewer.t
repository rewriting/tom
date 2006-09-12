package ted;

import java.io.*;
import aterm.*;
import aterm.pure.SingletonFactory;
import java.util.*;
import java.lang.reflect.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.*;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import javax.swing.JTree;
import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


public class ATViewer extends JPanel implements ActionListener {

  %include { atermmapping.tom }
  %include { mutraveler.tom }

  private JEditorPane commandPane;
  private JPanel intermediate;
  private ATerm[] inputaterms;
  private ATerm[] origaterms;
  private JButton btnCommand;
  private static ATermFactory atermFactory = SingletonFactory.getInstance();

  public ATViewer(String[] filenames) {
    super(new GridLayout(1,0));
    run(filenames);
  }
  
  public void run(String[] filenames) {

    // aterms to modify

    origaterms = new ATerm[filenames.length]; 
    inputaterms = new ATerm[filenames.length]; 
    
    intermediate = new JPanel();
    intermediate.setLayout(new java.awt.GridLayout(1,filenames.length));
    
    for (int i=0; i<filenames.length; i++){
    	
    	try {
    	      origaterms[i] = inputaterms[i] = atermFactory.readFromFile(filenames[i]);
    	      
    	      DefaultMutableTreeNode top = createTree(inputaterms[i]);
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
    commandPane = new JEditorPane();
    commandPane.setEditable(true);
    // it will be nice to be able to run ted scripts typed in this area
    JScrollPane htmlView = new JScrollPane(commandPane);

    //Add the scroll panes to a split pane.
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setTopComponent(treeView);
    splitPane.setBottomComponent(htmlView);

    //Add the split pane to this panel.
    this.setLayout(new javax.swing.BoxLayout(this,javax.swing.BoxLayout.Y_AXIS));
    add(splitPane);
    btnCommand = new JButton("Execute command");
    btnCommand.addActionListener(this);
    add(btnCommand);
  }
  
  public void addTreesToPanel(){
	  
	  intermediate.removeAll();
	  for (int i=0; i<inputaterms.length; i++){		  
	  
		  DefaultMutableTreeNode top = createTree(inputaterms[i]);
	      JTree tree = new JTree(top);
	      tree.getSelectionModel().setSelectionMode
	        (TreeSelectionModel.SINGLE_TREE_SELECTION);
	      
	      intermediate.add(tree);
	  }
      this.validate();
      this.repaint();
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

  public void actionPerformed(ActionEvent e){ 

    for(int i=0; i<origaterms.length; i++) {
      inputaterms[i] = origaterms[i];
    }

    String strategy = null ,action = null;
    int termNo;

    StringTokenizer st = new StringTokenizer(commandPane.getText());

    while(st.hasMoreTokens()) {
      try{
        termNo = Integer.parseInt(st.nextToken());
        strategy = st.nextToken();
        action = st.nextToken();

        String newTerm = Ted.run(inputaterms[termNo].toString(),strategy,action);

        inputaterms[termNo] = atermFactory.parse(newTerm);


      } catch(Exception ex) {
        JOptionPane.showMessageDialog(this, "Usage: AtermNumber Strategy Action (num begins at 0, with no spaces in action) \n (" + 
            ex.getMessage() + ")");
      }

    } // while

    addTreesToPanel();
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
