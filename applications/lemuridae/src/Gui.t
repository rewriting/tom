import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public final class Gui implements Observer {

  // Elements du Gui, zone de commande + zone des messages + fenetre principale
  protected CommandArea commandField;
  protected JTextArea msgArea;
  protected JFrame mainWindow;

  // communication Utils(ProofBuilder) <-> zone de commande
  private PipedInputStream inpipe;
  private PipedOutputStream outpipe;
  private OutputStreamWriter pipeWriter;

  // communication ProofBuilder <-> zone de messages
  private PipedOutputStream outpipe2;
  private PipedInputStream inpipe2;
  private BufferedReader reader2;

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

  // Quitter
  private AbstractAction quit = new AbstractAction ("Quit") {
    public void actionPerformed (ActionEvent event) {System.exit(0); }
  };

  // open file
  private AbstractAction open = new AbstractAction ("Open File") {
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
        }
        catch (Exception e) {
          System.out.println(e);
        }
      }
    }
  };
  
  // save file
  private AbstractAction save = new AbstractAction ("Save File") {
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
    
    // initialisation de la fenetre principale
    mainWindow = new JFrame("Lemuridae");
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // initialisation de la menubar
    JMenuBar mainMenuBar = new JMenuBar();
    
    // menu 1
    JMenu menu1 = new JMenu("File");
    // Action open
    JMenuItem menuItem = new JMenuItem("Open File");
    menuItem.setAction(open);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
    menu1.add(menuItem);
    // Action save
    menuItem = new JMenuItem("Save File");
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
    // et de son scrollpane
    JScrollPane scrollPane2 = new JScrollPane(msgArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    // initialisation du JSplitPane contenant les deux zones
    JSplitPane c = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,scrollPane1,scrollPane2);
    c.setDividerLocation(300);
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
  }

  // methode pour faire passer la commande de commandField a ProofBuilder et msgArea
  public void transmitCommand() {
    commandField.transmitCommand();
  }

  // methode appelle lorsque l'un des observables (un ProofBuilder) envoie un notify
  // on affiche le message dans la zone des messages
  public void update(Observable o, Object arg) {
    String s = (String) arg;
    try {
      s = new String(s.getBytes(), "UTF8");
    }
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

  // code a executer
  public static void main(String[] args) throws Exception {
    Gui newGui = new Gui();
    ProofBuilder test = new ProofBuilder();
    test.addObserver(newGui);
    Utils.setStream(newGui.inpipe);
    test.mainLoop();
  }

}
