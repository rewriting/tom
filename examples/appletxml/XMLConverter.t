/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
package appletxml;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class XMLConverter extends JApplet implements ActionListener {

  private static final long serialVersionUID = 1L;

  XMLConverter xmlconv;
  JButton button;
  JTextArea inputArea, outputArea;
  String defaultText = %[
<?xml version="1.0" encoding="windows-1252"?>
  <authors domain="loria.fr">
  <author firstname="Pierre-Etienne" lastname="MOREAU"/>
  <author firstname="Julien" lastname="GUYON"/>
  <author firstname="Antoine" lastname="REILLES"/>
</authors>
]%.trim();

  public void init () {
    String text = "";
    try { //get a text param from html page
      text = getParameter("text");
    } catch(Exception e) {}
    if (text==null || text.equals("")) {
      text = defaultText;
    }
      // Create XMLAnalyser
    xmlconv = new XMLConverter();

    getContentPane().setBackground(Color.lightGray);
    getContentPane().setLayout(new FlowLayout());

    inputArea = new JTextArea(10, 45);
    inputArea.setText(text);
    outputArea = new JTextArea(10, 45);
    button = new JButton ("Generate solutions");

    inputArea.setBackground(Color.white);
    outputArea.setBackground(Color.white);

    getContentPane().add(inputArea);
    getContentPane().add(button);
    getContentPane().add(outputArea);

    button.addActionListener(this);
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button) {
      String result = xmlconv.convert(inputArea.getText());
      if(result.equals("")) {
        result = "No solution found";
      }
      outputArea.setText(result);
    }
  }

  public String convert(String s) {
    TomXMLAnalyser conv = new TomXMLAnalyser();
    return conv.run(s);
  }
}
