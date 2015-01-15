/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the Inria nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package strategy;

import tom.library.sl.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;
import java.awt.*;

public class SwingIntrospectorDemo extends JPanel {

  %include { sl.tom }

  private Color newColor = Color.WHITE;
  private Strategy changeLabelColor = new AbstractStrategyBasic(new Identity()) {
      public <T> T visitLight(T any,tom.library.sl.Introspector i) throws VisitFailure {
        if (any instanceof JLabel) {
          ((JLabel) any).setBackground(newColor);
        }
        return any;
      }
  };


  public SwingIntrospectorDemo() {
    super(new GridLayout(3,1));
    final JColorChooser colorchooser = new JColorChooser();
    colorchooser.setPreviewPanel(new JPanel());
    colorchooser.setChooserPanels(new AbstractColorChooserPanel[]{colorchooser.getChooserPanels()[0]});
    colorchooser.getSelectionModel().addChangeListener( new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
          newColor = colorchooser.getColor();
          try {
          `TopDown(changeLabelColor).visit(SwingIntrospectorDemo.this, new SwingIntrospector());
          SwingIntrospectorDemo.this.revalidate();
          } catch(VisitFailure failure) {}
        }
    });
    add(colorchooser);

    JPanel p1 = new JPanel(new GridLayout(1,2));
    JPanel p2 = new JPanel(new GridLayout(1,2));
    JLabel label1 = new JLabel("label1",SwingConstants.CENTER);
    label1.setOpaque(true);
    JButton button1 = new JButton("button1");
    p1.add(label1);
    p1.add(button1);
    JLabel label2 = new JLabel("label2",SwingConstants.CENTER);
    label2.setOpaque(true);
    JButton button2 = new JButton("button2");
    p2.add(button2);
    p2.add(label2);
    add(p1);
    add(p2);
  }

  private static void createAndShowGUI() {
    JFrame frame = new JFrame("SwingIntrospector Demo");
    frame.setContentPane(new SwingIntrospectorDemo());
    frame.pack();
    frame.setVisible(true);
  }


  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        public void run() { createAndShowGUI(); }
    });
  }


}
