import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class CommandArea extends JTextArea implements KeyListener, CaretListener {

  // ligne courante : contient la prochaine ligne a envoyer au ProofBuilder
  int currentline=0;
  
  // point jusqu'auquel les commandes ont deja ete envoyee au ProofBuilder
  // le texte n'est pas editable avant ce point, mais apres.
  int offsetLimit=0;
  
  // action normale de back_space, disabled lorsqu'on se trouve au point offsetlimit
  private Object dobackspace = getInputMap().get(KeyStroke.getKeyStroke("BACK_SPACE"));
  
  // outpipe et pipewriter qui envoient les commandes au ProofBuilder
  private OutputStreamWriter pipeWriter;
  private OutputStream outpipe;

  // action associee a CTRL+DOWN
  private AbstractAction down = new AbstractAction ("Down") {
    public void actionPerformed(ActionEvent event) {
      transmitCommand();
    }
  };

  // action associee a CTRL+DOWN
  private AbstractAction up = new AbstractAction ("Up") {
    public void actionPerformed(ActionEvent event) {
      cancelLastCommand();
    }
  };

  // action associee a CTRL+PAGE_UP
  private AbstractAction allUp = new AbstractAction ("AllUp") {
    public void actionPerformed(ActionEvent event) {
      // System.out.println("up");
      cancelAllCommand();
    }
  };

  // action associee a CTRL+PAGE_UP
  private AbstractAction allDown = new AbstractAction ("AllDown") {
    public void actionPerformed(ActionEvent event) {
      // System.out.println("up");
      transmitAllCommand();
    }
  };

  // highlighter permettant la colorisation des commandes deja envoyees au ProofBuilder
  Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(new Color(176,212,255));

  // initialisation (KeyStrokes ...)
  public CommandArea() {
    //getInputMap().put(KeyStroke.getKeyStroke("ctrl pressed DOWN"), down);
    //getInputMap().put(KeyStroke.getKeyStroke("ctrl pressed UP"), up);
    //getInputMap().put(KeyStroke.getKeyStroke("ctrl pressed PAGE_UP"), allUp);
    //getInputMap().put(KeyStroke.getKeyStroke("ctrl pressed PAGE_DOWN"), allDown);
    setFont(new Font("Monospace",Font.PLAIN,12));
    putClientProperty(com.sun.java.swing.SwingUtilities2.AA_TEXT_PROPERTY_KEY,Boolean.TRUE );
  }
  
  // methode utilisee par le Gui pour passer les pipes communiquant vers le ProofBuilder
  public void setStreams(OutputStreamWriter writer, OutputStream outputS) {
    pipeWriter = writer;
    outpipe = outputS;
  }

  // methode qui envoie la commande suivante au ProofBuilder
  public void transmitCommand() {
    String cmd = getCurrentLine()+"\n";
    try {pipeWriter.write(cmd); pipeWriter.flush(); outpipe.flush(); }
    catch (Exception e) {System.out.println("Pipe Problem ?");}
  }

  public void transmitAllCommand() {
    int positionToReach = getLineCount();
    while (currentline < positionToReach) {transmitCommand(); }
    //if (currentline >= positionToReach) {
    //  for (;currentline<positionToReach; transmitCommand()) {}
    //}
  }

  // methode qui annule toutes les commandes envoyees au ProofBuilder
  public void cancelAllCommand() {
//    System.out.println("cancel");
    try {
      pipeWriter.write("init.\n"); pipeWriter.flush(); outpipe.flush();
      currentline=0;
      offsetLimit=0;
      refreshEditable();
      removeHighlights();
    }
    catch (Exception e) {System.out.println("Pipe Problem ?");}
  }

  // methode qui annule la derniere commande envoyee au ProofBuilder
  public void cancelLastCommand() {
//    System.out.println("cancel");
    int positionToReach = currentline-1;
    try {
      pipeWriter.write("init.\n"); pipeWriter.flush(); outpipe.flush();
      currentline=0;
      offsetLimit=0;
      removeHighlights();
    }
    catch (Exception e) {System.out.println("Pipe Problem ?");}
    // send all commands until the line positionToReach
    while (currentline < positionToReach) { transmitCommand() ;}
    //for (currentline=0; currentline < positionToReach ; transmitCommand() ) {}
  }

  // retourne la ligne n
  public String getLine(int n) {
    try {
      int start = getLineStartOffset(n);
      int stop = getLineEndOffset(n);
      return getText(start, stop-start);
    }
    catch (Exception e) {System.out.println(e.getMessage()); return null;}
  }

  // retourne la commande suivante
  public String getCurrentLine() {
    try {
      if (currentline==getLineCount()-1) {append("\n");}
      int supposedOffsetLimit = 0;
      try {supposedOffsetLimit = getLineEndOffset(currentline-1) ;} catch (Exception e) {System.out.println("it seems we are on the first line");}
      if (offsetLimit == supposedOffsetLimit) { // on a rien rajoute sur la derniere ligne lue
        offsetLimit = getLineEndOffset(currentline);
        currentline++;
        refreshEditable();
        highlight();
        return getLine(currentline-1);
      }
      else { // on a rajoute des trucs sur la derniere ligne lue, il faut lire la fin
        String resu =  getText(offsetLimit-1,supposedOffsetLimit-offsetLimit);
        offsetLimit = supposedOffsetLimit;
        refreshEditable();
        highlight();
        return resu;
      }
    }
    catch (Exception e) {System.out.println(e); return "";}
  }

  // Quand on tape ququchose, on regarde si on est arrive dans une zone editable ou non
  public void keyTyped(KeyEvent e) { refreshEditable();}
  
  public void keyPressed(KeyEvent e) {
    refreshEditable();
  }
  
  public void keyReleased(KeyEvent e) { refreshEditable();}

  // Quand on bouge le curseur, on regarde si on arrive dans une zone editable ou non
  public void caretUpdate(CaretEvent e) {
    refreshEditable();
  }

  // mets a jour si on est en mode editable ou non, en fonction de la position du curseur
  private void refreshEditable() {
    try {
      if (offsetLimit-1> getCaretPosition()) { // MODE NON EDITABLE
  //      System.out.println("Editable = false");
        setEditable(false);
      }
      else if (offsetLimit-1< getCaretPosition()) { // MODE EDITABLE, BACKSPACE ENABLED
  //      System.out.println("Editable = true");
        setEditable(true);
        getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"),dobackspace);
      }
      else {
   //     System.out.println("Mode intermediaire"); // MODE INTERMEDIARE : editable et BACKSPACE DISABLED
        setEditable(true); 
        getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"),"none");
      }
    }
    catch (Exception e) {System.out.println(e);}
  }
  
  // methode qui mets a jour le highlighting de la partie deja envoyee au ProofBuilder.
  public void highlight() {
    Highlighter hilite = getHighlighter();
    try {
      hilite.addHighlight(0, offsetLimit-2, myHighlightPainter);
    }
    catch (Exception e) {System.out.println(e);}
  }

  // enleve tous les highlight
  public void removeHighlights() {
    Highlighter hilite = getHighlighter();
    try {
      hilite.removeAllHighlights();
    }
    catch (Exception e) {System.out.println(e);}
  }

  // A private subclass of the default highlight painter
  class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
    public MyHighlightPainter(Color color) {
      super(color);
    }
  }

}
