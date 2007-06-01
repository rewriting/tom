import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.JFileChooser;

public final class Gui implements Observer {

  // Elements du Gui, zone de commande + zone des messages + fenetre principale
  protected CommandArea commandField;
  protected JTextArea msgArea;
  protected JFrame mainWindow;
  protected JSplitPane c;
  
  // communication Utils(ProofBuilder) <-> zone de commande
  private PipedInputStream inpipe;
  private PipedOutputStream outpipe;
  private OutputStreamWriter pipeWriter;

  // communication ProofBuilder <-> zone de messages
  private PipedOutputStream outpipe2;
  private PipedInputStream inpipe2;
  private BufferedReader reader2;


  // Open Recent Files
  private HashSet<String> recentFiles = new HashSet<String>(5);
  private String recentFileStore;
  
  // quelques actions qui pourront etre associes a des boutons, menus, etc.

  //Envoyer la prochaine commande au ProofBuilder
  private AbstractAction down = new AbstractAction ("Forward") {
    public void actionPerformed(ActionEvent event) { commandField.transmitCommand(); }
  };

  //Annuler la derniere commande
  private AbstractAction up = new AbstractAction ("Backward") {
    public void actionPerformed(ActionEvent event) { commandField.cancelLastCommand(); }
  };

  //Envoyer toutes les commandes au ProofBuilder
  private AbstractAction allDown = new AbstractAction ("Go to End") {
    public void actionPerformed(ActionEvent event) { commandField.transmitAllCommand(); }
  };

  //Annuler tout
  private AbstractAction allUp = new AbstractAction ("Go to Start") {
    public void actionPerformed(ActionEvent event) { commandField.cancelAllCommand(); }
  };

  // Clear the recent file list
  private AbstractAction clear = new AbstractAction ("Clear") {
    public void actionPerformed (ActionEvent event) {recentFiles = new HashSet(); refreshMenuBar(); }
  };

  // Quitter
  private AbstractAction quit = new AbstractAction ("Quit") {
    public void actionPerformed (ActionEvent event) {System.exit(0); }
  };

  // New
  private AbstractAction newfile = new AbstractAction ("New") {
    public void actionPerformed (ActionEvent event) {commandField.cancelAllCommand(); commandField.setText("");}
  };

  // open file
  private AbstractAction open = new AbstractAction ("Open...") {
    public void actionPerformed (ActionEvent event) {
      FileDialog fileDil = new FileDialog(mainWindow, "Open File", FileDialog.LOAD);
      //System.out.println("file dialog created");
      fileDil.pack();
      fileDil.setVisible(true);
      String filename = fileDil.getFile();
      String directory = fileDil.getDirectory();
      //System.out.println(filename);
      //System.out.println(directory);
      // if filename == null, le FileDialog a ete annule par l'utilisateur
      if (filename != null) {
        String newname = directory+filename;
        try {
          commandField.cancelAllCommand();
          FileReader freader = new FileReader(newname);
          char[] buffer = new char[4096];    // Read 4K characters at a time
          int len;                           // How many chars read each time
          commandField.setText("");              // Clear the text area
          while((len = freader.read(buffer)) != -1) { // Read a batch of chars
            String s = new String(buffer, 0, len); // Convert to a string
            commandField.append(s);                    // And display them
          }
          mainWindow.setTitle("Lemuridae: " + filename);  // Set the window title
          commandField.setCaretPosition(0);              // Go to start of file
          recentFiles.add(newname);
          refreshMenuBar();
        }
        catch (Exception e) {
          System.out.println(e);
        }
      }
    }
  };
 
  private AbstractAction open(String filename) {
    final String name = filename;
    AbstractAction new_action = new AbstractAction (filename) {
      public void actionPerformed (ActionEvent event) {
        try {
          commandField.cancelAllCommand();
          FileReader freader = new FileReader(name);
          char[] buffer = new char[4096];    // Read 4K characters at a time
          int len;                           // How many chars read each time
          commandField.setText("");              // Clear the text area
          while((len = freader.read(buffer)) != -1) { // Read a batch of chars
            String s = new String(buffer, 0, len); // Convert to a string
            commandField.append(s);                    // And display them
          }
          mainWindow.setTitle("Lemuridae: " + name);  // Set the window title
          commandField.setCaretPosition(0);              // Go to start of file
        }
        catch (Exception e) {
          System.out.println(e);
        }
      }
    };
  return new_action;
  }

  // save file
  private AbstractAction save = new AbstractAction ("Save...") {
    public void actionPerformed (ActionEvent event) {
      FileDialog fileDil = new FileDialog(mainWindow, "Save File", FileDialog.SAVE);
      //System.out.println("file dialog created");
      fileDil.pack();
      fileDil.setVisible(true);
      String filename = fileDil.getFile();
      String directory = fileDil.getDirectory();
      System.out.println(filename);
      System.out.println(directory);
      // if filename == null, le FileDialog a ete annule par l'utilisateur
      if (filename != null) {
        String newname = directory+filename;
        try { 
          FileWriter fwriter = new FileWriter(newname);
          fwriter.write(commandField.getText());
          fwriter.close();
        }
        catch (Exception e) {
          System.out.println(e);
        }
        recentFiles.add(newname);
        refreshMenuBar();
      }
    }
  };

  // creation du gui
  public Gui () {
    
    // initialisation des pipes
    try {
      inpipe = new PipedInputStream();
      outpipe = new PipedOutputStream(inpipe);
      pipeWriter = new OutputStreamWriter(outpipe);
    }
    catch (IOException e) {System.out.println("Pipe problem ?");}

    // initialisation de la liste des fichiers recents
    try {
      JFileChooser fchoose = new JFileChooser();
      recentFileStore = fchoose.getFileSystemView().getHomeDirectory()+File.separator+".lemuRecentFiles.data";
      System.out.println(recentFileStore);
      FileInputStream f_in = new FileInputStream (recentFileStore);
      ObjectInputStream obj_in = new ObjectInputStream (f_in);
      Object obj = obj_in.readObject ();
      if (obj instanceof HashSet) {
        recentFiles = (HashSet) obj;
      }
      else {System.out.println("warning : cannot import list of recent files");}
    }
    catch (Exception e) {System.out.println(e);}
    
    // initialisation de la fenetre principale
    mainWindow = new JFrame("Lemuridae");
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // MenuBar
    refreshMenuBar();
   
    // panel contenant les elements
    JPanel myPanel = new JPanel();
    
    // initialisation de la zone de commande ...
    commandField = new CommandArea();
    commandField.addKeyListener(commandField);
    commandField.addCaretListener(commandField);
    commandField.setStreams(pipeWriter,outpipe);
    // et de son scrollpane
    JScrollPane scrollPane1 = new JScrollPane(commandField,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
    
    // initialisation de la zone de messages
    msgArea = new JTextArea();
    msgArea.setEditable(false);
    //msgArea.setFont(new Font("Courier new",Font.PLAIN,12));
    msgArea.setFont(new Font("Monospaced",Font.PLAIN,12));
    msgArea.putClientProperty(com.sun.java.swing.SwingUtilities2.AA_TEXT_PROPERTY_KEY,Boolean.TRUE );

    // et de son scrollpane
    JScrollPane scrollPane2 = new JScrollPane(msgArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    // initialisation du JSplitPane contenant les deux zones
    c = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrollPane1,scrollPane2);
    myPanel.setLayout(new BorderLayout());
    myPanel.add(c,BorderLayout.CENTER);
    
    // icone
    ImageIcon icon = new ImageIcon("../icon/lemu.gif");
    mainWindow.setIconImage(icon.getImage());
    
    // on rajoute le panel a la fenetre principale
    mainWindow.add(myPanel);

    // on rend le tous visible
    mainWindow.setSize(600,500);
    mainWindow.setVisible(true);

    // divider
    c.setDividerLocation(.5);
    c.setResizeWeight(.5);
  }

  // methode pour faire passer la commande de commandField a ProofBuilder et msgArea
  public void transmitCommand() {
    commandField.transmitCommand();
  }

  // methode appelle lorsque l'un des observables (un ProofBuilder) envoie un notify
  // on affiche le message dans la zone des messages
  public void update(Observable o, Object arg) {
    String s = (String) arg;
    try { s = new String(s.getBytes(), "UTF8"); }
    catch (Exception e) { System.out.println("error during converting to UTF8 :"+e);}
    if (s.equals("SESSIONRESTARTED")) {
      msgArea.setDocument(new PlainDocument());
      //System.out.println("Message Area restarted");
    }
    else {
      msgArea.append(s);
      msgArea.setCaretPosition(msgArea.getDocument().getLength());
    }
  }

  public void refreshMenuBar() {
    // initialisation de la menubar
    JMenuBar mainMenuBar = new JMenuBar();
    
    // menu 1
    JMenu menu1 = new JMenu("File");
    // Action new
    JMenuItem menuItem = new JMenuItem("New");
    menuItem.setAction(newfile);
    menu1.add(menuItem);
    // Action open
    menuItem = new JMenuItem("Open...");
    menuItem.setAction(open);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
    menu1.add(menuItem);
    //Action open recent
    JMenu submenu = new JMenu("Open Recent");
    for (String filename : recentFiles) {
      menuItem = new JMenuItem(filename);
      menuItem.setAction(open(filename));
      submenu.add(menuItem);
    }
    submenu.addSeparator();
    menuItem = new JMenuItem("Clear");
    menuItem.setAction(clear);
    submenu.add(menuItem);
    menu1.add(submenu);
    // Action save
    menuItem = new JMenuItem("Save...");
    menuItem.setAction(save);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    menu1.add(menuItem);
    // Separator
    menu1.addSeparator();
    // Action quit
    menuItem = new JMenuItem("Quit");
    menuItem.setAction(quit);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
    menu1.add(menuItem);

    //menu 2
    JMenu menu2 = new JMenu("Navigation");
    // Action Down
    menuItem = new JMenuItem("Forward");
    menuItem.setAction(down);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.CTRL_MASK));
    menu2.add(menuItem);
    // Action Up
    menuItem = new JMenuItem("Backward");
    menuItem.setAction(up);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK));
    menu2.add(menuItem);
    // Separator
    menu2.addSeparator();
    // Action AllDown
    menuItem = new JMenuItem("Go to end");
    menuItem.setAction(allDown);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, ActionEvent.CTRL_MASK));
    menu2.add(menuItem);
    // Action AllUp
    menuItem = new JMenuItem("Go to start");
    menuItem.setAction(allUp);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, ActionEvent.CTRL_MASK));
    menu2.add(menuItem);
    
    // Ajout des menus
    mainMenuBar.add(menu1);
    mainMenuBar.add(menu2);
    // Ajout de la menubar
    mainWindow.setJMenuBar(mainMenuBar);
    //mainWindow.setSize(600,500);
    mainWindow.setVisible(true);
    
    // save the list of Recent Files
    try {
      FileOutputStream f_out = new FileOutputStream(recentFileStore);
      ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
      obj_out.writeObject(recentFiles);
      obj_out.close();
      f_out.close();
    }
    catch (Exception e) {System.out.println(e);}
  }

  // code a executer
  public static void main(String[] args) throws Exception {
    Gui newGui = new Gui();
    ProofBuilder test = new ProofBuilder();
    test.addObserver(newGui);
    Utils.setStream(newGui.inpipe);
    test.mainLoop();
  }

}
