import java.util.*;
import java.lang.*;
import java.lang.reflect.*;
import lsruntime.*;
import lsruntime.adt.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RunExamples {

  public RunExamples(String[] args) {
    factory = new LsystemsFactory();
    runtime = new LsystemsRuntime(args, factory);
    this.args = args;
  }
  
  public LsystemsRuntime runtime;
  public LsystemsFactory factory;
  public String[] args;
  
  public void run() {
    
    String[] comment = {
      "",
      "Lsystem Stochastic",
      "",
      "",
      "Arbre",
      "",
      "Triangle",
      "Statistique de la Factory"
    };
    
    JFrame jframe = new JFrame("Lsystems experience");
    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    Container container = jframe.getContentPane();
    container.setLayout(new GridLayout(8, 2));
    
    for (int i = 1; i < 8; i++) {
      container.add(new JLabel(comment[i-1]));
      container.add(addbutton("Lsystems"+i));
    }
    
    container.add(new JLabel(comment[7]));
    JButton stats = new JButton("Statistiques");
    stats.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          System.out.println(factory);
        }
      }
    );
    container.add(stats);
    
    jframe.pack();
    jframe.setVisible(true);
  }
  
  public JButton addbutton(String name) {
    JButton result = new JButton(name);
    result.addActionListener(
      new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          runtest(e.getActionCommand());
        }
      }
    );
    return result;
  }
  
  public void runtest(String name) {
    try {
      // vive la réflexivité...
      Class classe = Class.forName(name);
      Class[] paramContruct = {java.lang.String[].class,lsruntime.adt.LsystemsFactory.class, lsruntime.LsystemsRuntime.class};
      Object[] initargs = {args, factory, runtime};
      Object test = classe.getConstructor(paramContruct).newInstance(initargs);
      
      Class[] paramRun = { };
      Method runtest = classe.getMethod("run",paramRun);
      
      Object[] paramInvoke = { };
      runtest.invoke(test, paramInvoke);
      runtime.draw();
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

