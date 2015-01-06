/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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
import aterm.pure.*;
import lsruntime.*;
import lsruntime.adt.lsystems.*;
import lsruntime.adt.lsystems.types.*;

import java.lang.reflect.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RunExamples {

  public RunExamples(String[] args) {
    runtime = new LsystemsRuntime(args,new Factory(new PureFactory()));
    this.args = args;
  }
  
  public LsystemsRuntime runtime;
  public String[] args;
  
  public void run() {
    
    String[] comment = {
      "Exemple simple",
      "Lsystem stochastic",
      "D2L-System context-sensitive",
      "Lsystem paramétré",
      "Lsystem param. | Arbre",
      "Tests context-sensitive",
      "Lsystems param. | Triangle",
      "Statistique de la Factory"
    };
    
    JFrame jframe = new JFrame("Lsystems experience");
    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    Container container = jframe.getContentPane();
    container.setLayout(new GridLayout(9, 2));
    
    for (int i = 1; i < 8; i++) {
      container.add(new JLabel(comment[i-1]));
      container.add(addbutton(i));
    }
    
    container.add(new JLabel(comment[7]));
    JButton stats = new JButton("Statistiques");
    stats.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          System.out.println(runtime.getLsystemsFactory().getPureFactory());
        }
      }
    );
    container.add(stats);
    
    container.add(new JLabel(""));
    final JCheckBox check = new JCheckBox("verbose",false);
    check.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (check.isSelected()) {
            runtime.verbose = true;
          } else {
            runtime.verbose = false;
          }
        }
      }
    );
    container.add(check);
    
    jframe.pack();
    jframe.setVisible(true);
  }
  
  public JButton addbutton(final int testno) {
    JButton result = new JButton("Lsystems" + testno);
    result.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          runtest(e.getActionCommand(),testno);
        }
      }
    );
    return result;
  }
  
  public void runtest(String name,int testno) {
    boolean old_verbose = false;
    try {
      // vive la réflexivité...
      Class classe = Class.forName(name);
      Class[] paramContruct = {java.lang.String[].class, lsruntime.LsystemsRuntime.class};
      Object[] initargs = {args, runtime};
      Object test = classe.getConstructor(paramContruct).newInstance(initargs);
      
      Class[] paramRun = { };
      Method runtest = classe.getMethod("run",paramRun);
      
      Object[] paramInvoke = { };
      if (testno == 4 || testno == 6) {
        old_verbose = runtime.verbose;
        runtime.verbose = true;
      }
      runtest.invoke(test, paramInvoke);
      if (testno == 4 || testno == 6) {
        runtime.verbose = old_verbose;
      }
      
      if (testno != 4 && testno != 6) {
        runtime.draw();
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }
  
  public final static void main(String[] args) {
    // add --draw as parameter
/*    String[] new_args = new String[args.length+1];
    int i;
    for (i=0;i<args.length-1;i++) {
      System.out.println(args[i]);
      new_args[i] = args[i];
    }
    new_args[i] = "--draw";*/
    
    RunExamples re = new RunExamples(args);
    re.run();
  }

}

