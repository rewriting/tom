/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
    Christophe Mayer            ESIAL Student

*/

package jtom;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class TomDemo extends JFrame{

  JPanel parserPanel = new JPanel();
  JPanel checkerPanel = new JPanel();
  JPanel compilerPanel = new JPanel();
  JPanel generatorPanel = new JPanel();
  JPanel verifierPanel = new JPanel();

  JPanel infoParserPanel = new JPanel();
  JPanel infoCheckerPanel = new JPanel();
  JPanel infoCompilerPanel = new JPanel();
  JPanel infoGeneratorPanel = new JPanel();
  JPanel infoVerifierPanel = new JPanel();

  ArrayList infoParser = new ArrayList();
  JTextArea textParser = new JTextArea();
  int whenParser = 0; 

  ArrayList infoChecker = new ArrayList();
  JTextArea textChecker = new JTextArea();
  int whenChecker = 0; 

  ArrayList infoVerifier = new ArrayList();
  JTextArea textVerifier = new JTextArea();
  int whenVerifier = 0;

  ArrayList infoCompiler = new ArrayList();
  JTextArea textCompiler = new JTextArea();
  int whenCompiler = 0; 

  ArrayList infoGenerator = new ArrayList();
  JTextArea textGenerator = new JTextArea();
  int whenGenerator = 0; 

  JPanel buttonPanel = new JPanel();
  JPanel progressPanel = new JPanel();
  JPanel infoPanel = new JPanel();	
  JPanel extraPanel = new JPanel();

  JProgressBar progressBar;

  Container container = getContentPane();

  javax.swing.Timer timer;
  Action workingAction;
  Action pauseAction;
  Action exitAction;   

  JPanel arrowParser = new JPanel(); 	    
  JPanel arrowChecker = new JPanel(); 
  JPanel arrowVerifier = new JPanel();
  JPanel arrowCompiler = new JPanel(); 	    
  JPanel arrowGenerator = new JPanel(); 
    
  int index = 0;

  String blank = "buttons/rb.gif";
  String did = "buttons/rbr.gif";

  JRadioButton radioButton11 = new JRadioButton("");
  JRadioButton radioButton12 = new JRadioButton("");
  JRadioButton radioButton13 = new JRadioButton("");
  JRadioButton radioButton14a = new JRadioButton("");
  JRadioButton radioButton14b = new JRadioButton("");
  JRadioButton radioButton14c = new JRadioButton("");
  JRadioButton radioButton21 = new JRadioButton("");
  JRadioButton radioButton22 = new JRadioButton("");
  JRadioButton radioButton23 = new JRadioButton("");
  JRadioButton radioButton24a = new JRadioButton("");
  JRadioButton radioButton24b = new JRadioButton("");
  JRadioButton radioButton24c = new JRadioButton("");
  JRadioButton radioButton31 = new JRadioButton("");
  JRadioButton radioButton32 = new JRadioButton("");
  JRadioButton radioButton33 = new JRadioButton("");
  JRadioButton radioButton34a = new JRadioButton("");
  JRadioButton radioButton34b = new JRadioButton("");
  JRadioButton radioButton34c = new JRadioButton("");
  JRadioButton radioButton41 = new JRadioButton("");
  JRadioButton radioButton42 = new JRadioButton("");
  JRadioButton radioButton43 = new JRadioButton("");
  JRadioButton radioButton44a = new JRadioButton("");
  JRadioButton radioButton44b = new JRadioButton("");
  JRadioButton radioButton44c = new JRadioButton("");
  JRadioButton radioButton51 = new JRadioButton("");
  JRadioButton radioButton52 = new JRadioButton("");
  JRadioButton radioButton53 = new JRadioButton("");
  JRadioButton radioButton54a = new JRadioButton("");
  JRadioButton radioButton54b = new JRadioButton("");
  JRadioButton radioButton54c = new JRadioButton("");

  ArrayList affectation = new ArrayList();

  Color color1 = Color.lightGray;
  Color color2 = new Color(132,178,255);
  Color color3 = new Color(132,228,232);
  Color color4 = new Color(132,232,147);
  Color color5 = new Color(228,232,132);
  Color color6 = new Color(232,173,132);



  public JPanel blank() {
    JPanel blankPanel = new JPanel();
    blankPanel.setBackground(null);
    return blankPanel;
  }



    /* Buttons for animated arrows */

  public JRadioButton createRadioButton(int i) {
    JRadioButton radioButton = new JRadioButton("");
    radioButton.setPressedIcon(createImageIcon((String)affectation.get(i),""));
    radioButton.setFocusPainted(false);
    return radioButton;
  }

  public ImageIcon createImageIcon(String filename, String description) {
    String path = "./"+filename;
    return new ImageIcon(getClass().getResource(path), description); 
  }

  public void arrowParser() {
    radioButton11.setIcon(createImageIcon(blank,""));
    radioButton12.setIcon(createImageIcon(blank,""));
    radioButton13.setIcon(createImageIcon(blank,""));
    radioButton14a.setIcon(createImageIcon(blank,""));
    radioButton14b.setIcon(createImageIcon(blank,""));
    radioButton14c.setIcon(createImageIcon(blank,""));
    radioButton11.setBackground(null);
    radioButton12.setBackground(null);
    radioButton13.setBackground(null);
    radioButton14a.setBackground(null);
    radioButton14b.setBackground(null);
    radioButton14c.setBackground(null);

    arrowParser.setLayout(new GridLayout(4,3));
    arrowParser.setBackground(null);
    arrowParser.add(blank());
    arrowParser.add(radioButton11);
    arrowParser.add(blank());
    arrowParser.add(blank());
    arrowParser.add(radioButton12);
    arrowParser.add(blank());
    arrowParser.add(radioButton14a);
    arrowParser.add(radioButton13);
    arrowParser.add(radioButton14b);
    arrowParser.add(blank());
    arrowParser.add(radioButton14c);
    arrowParser.add(blank());
  }

  public void arrowChecker() {
    radioButton21.setIcon(createImageIcon(blank,""));
    radioButton22.setIcon(createImageIcon(blank,""));
    radioButton23.setIcon(createImageIcon(blank,""));
    radioButton24a.setIcon(createImageIcon(blank,""));
    radioButton24b.setIcon(createImageIcon(blank,""));
    radioButton24c.setIcon(createImageIcon(blank,""));
    radioButton21.setBackground(null);
    radioButton22.setBackground(null);
    radioButton23.setBackground(null);
    radioButton24a.setBackground(null);
    radioButton24b.setBackground(null);
    radioButton24c.setBackground(null);

    arrowChecker.setLayout(new GridLayout(4,3));
    arrowChecker.setBackground(null);
    arrowChecker.add(blank());
    arrowChecker.add(radioButton21);
    arrowChecker.add(blank());
    arrowChecker.add(blank());
    arrowChecker.add(radioButton22);
    arrowChecker.add(blank());
    arrowChecker.add(radioButton24a);
    arrowChecker.add(radioButton23);
    arrowChecker.add(radioButton24b);
    arrowChecker.add(blank());
    arrowChecker.add(radioButton24c);
    arrowChecker.add(blank());
  }

  public void arrowVerifier() {
    radioButton31.setIcon(createImageIcon(blank,"")); 
    radioButton32.setIcon(createImageIcon(blank,""));
    radioButton33.setIcon(createImageIcon(blank,""));
    radioButton34a.setIcon(createImageIcon(blank,""));
    radioButton34b.setIcon(createImageIcon(blank,""));
    radioButton34c.setIcon(createImageIcon(blank,""));
    radioButton31.setBackground(null);
    radioButton32.setBackground(null);
    radioButton33.setBackground(null);
    radioButton34a.setBackground(null);
    radioButton34b.setBackground(null);
    radioButton34c.setBackground(null);

    arrowVerifier.setLayout(new GridLayout(4,3));
    arrowVerifier.setBackground(null);
    arrowVerifier.add(blank());
    arrowVerifier.add(radioButton31);
    arrowVerifier.add(blank());
    arrowVerifier.add(blank());
    arrowVerifier.add(radioButton32);
    arrowVerifier.add(blank());
    arrowVerifier.add(radioButton34a);
    arrowVerifier.add(radioButton33);
    arrowVerifier.add(radioButton34b);
    arrowVerifier.add(blank());
    arrowVerifier.add(radioButton34c);
    arrowVerifier.add(blank());
  }


  public void arrowCompiler() {
    radioButton41.setIcon(createImageIcon(blank,""));
    radioButton42.setIcon(createImageIcon(blank,""));
    radioButton43.setIcon(createImageIcon(blank,""));
    radioButton44a.setIcon(createImageIcon(blank,""));
    radioButton44b.setIcon(createImageIcon(blank,""));
    radioButton44c.setIcon(createImageIcon(blank,""));
    radioButton41.setBackground(null);
    radioButton42.setBackground(null);
    radioButton43.setBackground(null);
    radioButton44a.setBackground(null);
    radioButton44b.setBackground(null);
    radioButton44c.setBackground(null);

    arrowCompiler.setLayout(new GridLayout(4,3));
    arrowCompiler.setBackground(null);
    arrowCompiler.add(blank());
    arrowCompiler.add(radioButton41);
    arrowCompiler.add(blank());
    arrowCompiler.add(blank());
    arrowCompiler.add(radioButton42);
    arrowCompiler.add(blank());
    arrowCompiler.add(radioButton44a);
    arrowCompiler.add(radioButton43);
    arrowCompiler.add(radioButton44b);
    arrowCompiler.add(blank());
    arrowCompiler.add(radioButton44c);
    arrowCompiler.add(blank());
  }


  public void arrowGenerator() {
    radioButton51.setIcon(createImageIcon(blank,""));
    radioButton52.setIcon(createImageIcon(blank,""));
    radioButton53.setIcon(createImageIcon(blank,""));
    radioButton54a.setIcon(createImageIcon(blank,""));
    radioButton54b.setIcon(createImageIcon(blank,""));
    radioButton54c.setIcon(createImageIcon(blank,""));
    radioButton51.setBackground(null);
    radioButton52.setBackground(null);
    radioButton53.setBackground(null);
    radioButton54a.setBackground(null);
    radioButton54b.setBackground(null);
    radioButton54c.setBackground(null);

    arrowGenerator.setLayout(new GridLayout(4,3));
    arrowGenerator.setBackground(null);
    arrowGenerator.add(blank());
    arrowGenerator.add(radioButton51);
    arrowGenerator.add(blank());
    arrowGenerator.add(blank());
    arrowGenerator.add(radioButton52);
    arrowGenerator.add(blank());
    arrowGenerator.add(radioButton54a);
    arrowGenerator.add(radioButton53);
    arrowGenerator.add(radioButton54b);
    arrowGenerator.add(blank());
    arrowGenerator.add(radioButton54c);
    arrowGenerator.add(blank());
  }



    /* Buttons Working, Pause, Exit */

  public JButton createButton(Action a) {
    JButton b = new JButton();
    b.putClientProperty("displayAction", Boolean.TRUE);
    b.setAction(a);
    b.setFocusPainted(false);
    return b;
  } // create Button

  public Action createWorkingAction() {
    return new AbstractAction("Working TOM") {
        public void actionPerformed (ActionEvent e) {
          if(progressBar.getValue() < progressBar.getMaximum()) {
            progressBar.setValue(progressBar.getValue() + 1);

            index++;
            animatedArrows(index);

          } else {
            if(timer != null) {
              timer.stop();
              timer = null;
              workingAction.setEnabled(true);
              pauseAction.setEnabled(false);
            }
          }
        }
      };
  } // createWorkingAction

  public JButton createWorkingButton() {
    workingAction = new AbstractAction("Working") {
        public void actionPerformed(ActionEvent e) {
          if(timer == null) {
            workingAction.setEnabled(false);
            pauseAction.setEnabled(true);
            timer = new javax.swing.Timer(100, createWorkingAction());
            timer.start();
          }
        }
      };
    return createButton(workingAction);
  } // createWorkingButton

  public JButton createPauseButton() {
    pauseAction = new AbstractAction("Pause") {
        public void actionPerformed(ActionEvent e) {
          if(timer != null) {
            timer.stop();
            timer = null;
          }
          workingAction.setEnabled(true);
          pauseAction.setEnabled(false);
        }
      };
    return createButton(pauseAction);
  } // createPauseButton

  public JButton createExitButton() {
    exitAction = new AbstractAction("Exit") {
        public void actionPerformed(ActionEvent e){
          System.exit(0);
        }
      };
    return createButton(exitAction);
  } // createExitButton



    /* We animate arrows & print messages */ 

  public void animatedArrows(int index) {
    if ( 0 < index && index <= 25 ) {
      verifierPanel.setBackground(color1);
      parserPanel.setBackground(color1);
      radioButton11.setIcon(createImageIcon((String)affectation.get((index+3)%4),""));
      radioButton12.setIcon(createImageIcon((String)affectation.get((index+2)%4),""));
      radioButton14a.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton13.setIcon(createImageIcon((String)affectation.get((index+1)%4),""));
      radioButton14b.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton14c.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton31.setIcon(createImageIcon((String)affectation.get((index+3)%4),""));
      radioButton32.setIcon(createImageIcon((String)affectation.get((index+2)%4),""));
      radioButton34a.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton33.setIcon(createImageIcon((String)affectation.get((index+1)%4),""));
      radioButton34b.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton34c.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      messageParser(index);	    
      messageVerifier(index);
    }
    else if ( 25 < index && index <= 50 ) {
      parserPanel.setBackground(color2);
      checkerPanel.setBackground(color1);
      radioButton11.setIcon(createImageIcon(did,""));
      radioButton12.setIcon(createImageIcon(did,""));
      radioButton14a.setIcon(createImageIcon(did,""));
      radioButton13.setIcon(createImageIcon(did,""));
      radioButton14b.setIcon(createImageIcon(did,""));
      radioButton14c.setIcon(createImageIcon(did,""));
      radioButton31.setIcon(createImageIcon((String)affectation.get((index+3)%4),""));
      radioButton32.setIcon(createImageIcon((String)affectation.get((index+2)%4),""));
      radioButton34a.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton33.setIcon(createImageIcon((String)affectation.get((index+1)%4),""));
      radioButton34b.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton34c.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton21.setIcon(createImageIcon((String)affectation.get((index+3)%4),""));
      radioButton22.setIcon(createImageIcon((String)affectation.get((index+2)%4),""));
      radioButton24a.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton23.setIcon(createImageIcon((String)affectation.get((index+1)%4),""));
      radioButton24b.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton24c.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton31.setIcon(createImageIcon((String)affectation.get((index+3)%4),""));
      radioButton32.setIcon(createImageIcon((String)affectation.get((index+2)%4),""));
      radioButton34a.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton33.setIcon(createImageIcon((String)affectation.get((index+1)%4),""));
      radioButton34b.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton34c.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      messageChecker(index);
      messageVerifier(index);
    }
    else if ( 50 < index && index <= 75 ) {
      checkerPanel.setBackground(color3);
      verifierPanel.setBackground(color4);
      compilerPanel.setBackground(color1);
      radioButton21.setIcon(createImageIcon(did,""));
      radioButton22.setIcon(createImageIcon(did,""));
      radioButton24a.setIcon(createImageIcon(did,""));
      radioButton23.setIcon(createImageIcon(did,""));
      radioButton24b.setIcon(createImageIcon(did,""));
      radioButton24c.setIcon(createImageIcon(did,""));
      radioButton31.setIcon(createImageIcon(did,""));
      radioButton32.setIcon(createImageIcon(did,""));
      radioButton34a.setIcon(createImageIcon(did,""));
      radioButton33.setIcon(createImageIcon(did,""));
      radioButton34b.setIcon(createImageIcon(did,""));
      radioButton34c.setIcon(createImageIcon(did,""));
      radioButton41.setIcon(createImageIcon((String)affectation.get((index+3)%4),""));
      radioButton42.setIcon(createImageIcon((String)affectation.get((index+2)%4),""));
      radioButton44a.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton43.setIcon(createImageIcon((String)affectation.get((index+1)%4),""));
      radioButton44b.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton44c.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      messageCompiler(index);
    }
    else if ( 75 < index && index < 100 ) {
      compilerPanel.setBackground(color5);
      generatorPanel.setBackground(color1);
      radioButton41.setIcon(createImageIcon(did,""));
      radioButton42.setIcon(createImageIcon(did,""));
      radioButton44a.setIcon(createImageIcon(did,""));
      radioButton43.setIcon(createImageIcon(did,""));
      radioButton44b.setIcon(createImageIcon(did,""));
      radioButton44c.setIcon(createImageIcon(did,""));
      radioButton51.setIcon(createImageIcon((String)affectation.get((index+3)%4),""));
      radioButton52.setIcon(createImageIcon((String)affectation.get((index+2)%4),""));
      radioButton54a.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton53.setIcon(createImageIcon((String)affectation.get((index+1)%4),""));
      radioButton54b.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      radioButton54c.setIcon(createImageIcon((String)affectation.get((index)%4),""));
      messageGenerator(index);
    }
    else {
      generatorPanel.setBackground(color6);
      radioButton51.setIcon(createImageIcon(did,""));
      radioButton52.setIcon(createImageIcon(did,""));
      radioButton54a.setIcon(createImageIcon(did,""));
      radioButton53.setIcon(createImageIcon(did,""));
      radioButton54b.setIcon(createImageIcon(did,""));
      radioButton54c.setIcon(createImageIcon(did,""));
    }
  }



    /* Position of objects in JPanel */

  public void arrangeObjects(JPanel panel, String name, JPanel object1, JPanel object2) {
    GridBagLayout gridBag = new GridBagLayout();
    panel.setLayout(gridBag);

    panel.setBorder(BorderFactory.createTitledBorder(name)); 

    GridBagConstraints constraint;
    constraint = new GridBagConstraints();
    constraint.fill = GridBagConstraints.BOTH;
    constraint.weightx = 1;
    constraint.weighty = 1;
    gridBag.setConstraints(object1, constraint);
    panel.add(object1);

    constraint = new GridBagConstraints();
    constraint.fill = GridBagConstraints.BOTH;
    constraint.weightx = 20;
    constraint.weighty = 1;
    object2.setLayout(new GridLayout(0,1));
    object2.setBackground(null);
    gridBag.setConstraints(object2,constraint);
    panel.add(object2);
  }


	
    /* Configuration of messages */ 

  public JLabel label1(String text) {
    JLabel textIn = new JLabel(text,SwingConstants.CENTER);
    textIn.setFont(new Font("Serif",Font.ITALIC|Font.BOLD, 14));
    return textIn;
  }
	    
  public void info(String version, String fileName) {
    infoPanel.add(label1("To One Matching Compiler - Version "+version));
    infoPanel.add(label1("Input file : "+fileName+".t"));
  }

  public void messageParser(int index){
    if ( whenParser != 0 ) {
      int i = ( index / whenParser) - 1;
      if ( ( ( index % whenParser )== 0 ) && ( i <= infoParser.size() ) ) {
        if ( ( i != infoParser.size() ) ) {
          textParser.append((String)infoParser.get(i)+"\n");
        }
        else {
          textParser.append("       --> TomParser finished");
        }
      }
    }
    else {
      if ( index == 24 ) {
        textParser.append(" --> TomParser finished");
      }
    }
  }

  public void messageChecker(int index){
    if ( whenChecker != 0 ) {
      int i = ( (index - 25) / whenChecker) - 1;
      if ( ( ( (index - 25) % whenChecker )== 0 ) && ( i <= infoChecker.size() ) ) {
        if ( i != infoChecker.size() ) {
          textChecker.append((String)infoChecker.get(i)+"\n");
        }
        else {
          textChecker.append("       --> TomChecker finished");
        }
      }
    }
    else {
      if ( index == 49 ) {
        textChecker.append(" --> TomChecker finished");
      }
    }
  }

  public void messageVerifier(int index){
    if ( whenVerifier != 0 ) {
      int i = (index / whenVerifier) - 1;
      if ( ( ( index % whenVerifier )== 0 ) && ( i <= infoVerifier.size() ) ) {
        if ( i != infoVerifier.size() ) {
          textVerifier.append((String)infoVerifier.get(i)+"\n");
        }
        else {
          textVerifier.append("       --> TomVerifier finished");
        }
      }
    }
    else {
      if ( index == 49 ) {
        textVerifier.append(" --> TomVerifier finished");
      }
    }
  }

  public void messageCompiler(int index){
    if ( whenCompiler != 0 ) {
      int i = ( (index - 50) / whenCompiler) - 1;
      if ( ( ( (index - 50) % whenCompiler )== 0 ) && ( i <= infoCompiler.size() ) ) {
        if ( i != infoCompiler.size() ) {
          textCompiler.append((String)infoCompiler.get(i)+"\n");
        }
        else {
          textCompiler.append("       --> TomCompiler finished");
        }
      }
    }
    else {
      if ( index == 74 ) {
        textCompiler.append(" --> TomCompiler finished");
      }
    }
  }

  public void messageGenerator(int index){
    if ( whenGenerator != 0 ) {
      int i = ( (index - 75) / whenGenerator) - 1;
      if ( ( ( (index - 75) % whenGenerator )== 0 ) && ( i <= infoGenerator.size() ) ) {
        if ( i != infoGenerator.size() ) {
          textGenerator.append((String)infoGenerator.get(i)+"\n");
        }
        else {
          textGenerator.append("       --> TomGenerator finished");
        }
      }
    }
    else {
      if ( index == 98 ) {
        textGenerator.append(" --> TomGenerator finished");
      }
    }
  }

  public void initText(JPanel panel, JTextArea textArea) {
    textArea.setBackground(null);
    textArea.setFont(new Font("Dialog",Font.BOLD, 13));
    textArea.setForeground(Color.black);
    textArea.getPreferredScrollableViewportSize();
    panel.add(textArea);
  }

  public void complete(ArrayList parserList, ArrayList checkerList, ArrayList verifierList, ArrayList compilerList, ArrayList generatorList){
    infoParser = parserList;
    if ( !infoParser.isEmpty() ) {
      whenParser = 25 / (infoParser.size()+1);
    }
    initText(infoParserPanel,textParser);

    infoChecker = checkerList;
    if ( !infoChecker.isEmpty() ) {
      whenChecker = 25 / (infoChecker.size()+1);
    }
    initText(infoCheckerPanel,textChecker);

    infoVerifier = verifierList;
    if ( !infoVerifier.isEmpty() ) {
      whenVerifier = 50 / (infoVerifier.size()+1);
    }
    initText(infoVerifierPanel,textVerifier);

    infoCompiler = compilerList;
    if ( !infoCompiler.isEmpty() ) {
      whenCompiler = 25 / (infoCompiler.size()+1);
    }
    initText(infoCompilerPanel,textCompiler);

    infoGenerator = generatorList;
    if ( !infoGenerator.isEmpty() ) {
      whenGenerator = 24 / (infoGenerator.size()+1);
    }
    initText(infoGeneratorPanel,textGenerator);
  }



    /* The demo structure */
 
  public TomDemo() {
    super(" TOM Demo ");

    affectation.add("buttons/rbrs.gif");
    affectation.add("buttons/rbs.gif");
    affectation.add("buttons/rb.gif");
    affectation.add("buttons/rb.gif");

    infoPanel.setLayout(new GridLayout(0,1));
    infoPanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));

    buttonPanel.setLayout(new GridLayout(1,0));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));
    buttonPanel.add(createWorkingButton());
    buttonPanel.add(createPauseButton());
    buttonPanel.add(createExitButton());

      /* Progress bar */
    progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100) {
        public Dimension getPreferredSize() {
          return new Dimension(300, super.getPreferredSize().height);
        }
      };

    progressPanel.setLayout(new GridLayout(0,1));
    progressPanel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
    progressPanel.add(progressBar);
    progressPanel.add(blank());
    progressPanel.add(buttonPanel);	

    extraPanel.setLayout(new GridLayout(0,1));
    extraPanel.add(infoPanel);
    extraPanel.add(progressPanel);

    arrowParser();
    arrangeObjects(parserPanel, " Parser ", arrowParser, infoParserPanel);

    arrowChecker();
    arrangeObjects(checkerPanel, " Checker ", arrowChecker, infoCheckerPanel);

    arrowCompiler();
    arrangeObjects(compilerPanel, " Compiler ", arrowCompiler, infoCompilerPanel);
	
    arrowGenerator();
    arrangeObjects(generatorPanel, " Generator ", arrowGenerator, infoGeneratorPanel);

    arrowVerifier();
    arrangeObjects(verifierPanel, " Verifier ", arrowVerifier, infoVerifierPanel);
 
    container.setLayout(new GridLayout(0,1));
    container.add(parserPanel);
    container.add(checkerPanel);
    container.add(verifierPanel);
    container.add(compilerPanel);
    container.add(generatorPanel);
    container.add(extraPanel);

  } // interface

} // class TomDemo
