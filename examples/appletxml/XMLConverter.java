package appletxml;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class XMLConverter extends JApplet implements ActionListener {

  XMLConverter xmlconv;
  JButton button;
  JTextArea input, output;
  String defaultText = "<?xml version=\"1.0\" encoding=\"windows-1252\" ?>\n<authors domain=\"loria.fr\">\n\t<author firstname=\"Pierre-Etienne\" lastname=\"MOREAU\"/>\n\t<author firstname=\"Julien\" lastname=\"GUYON\"/>\n\t<author firstname=\"Antoine\" lastname=\"REILLES\"/>\n</authors>";

    /*<?xml version="1.0" encoding="windows-1252"?><authors domain="loria.fr"><author firstname="Pierre-Etienne" lastname="MOREAU"/><author firstname="Julien" lastname="GUYON"/></authors>*/
  
  public void init () {
    String text = "";
    try { //get a text param from html page
      text = (String) getParameter ("text");
    } catch(Exception e) {}
    if (text==null || text.equals("")) {
      text = defaultText;
    }
      // Create XMLAnalyser
    xmlconv = new XMLConverter();

    getContentPane().setBackground(Color.lightGray);
    getContentPane().setLayout(new FlowLayout());
    
    input = new JTextArea(text, 10, 45);
    output = new JTextArea(10, 45);
    button = new JButton ("Generate solutions");
    
    input.setBackground (Color.white);
    output.setBackground(Color.white);
    
    getContentPane().add(input);
    getContentPane().add(button);
    getContentPane().add(output);
    
    button.addActionListener(this);
  }    
  
  public void actionPerformed (ActionEvent e) {
    if (e.getSource() == button) {
      String result = xmlconv.convert (input.getText());
      if(result.equals("")) {
        result = "No solution found";
      }
      output.setText(result);
    }
  }
    
  public String convert (String s) {
    TomXMLAnalyser conv = new TomXMLAnalyser();
    return conv.run(s);
  }
  
} // class XMLConverter
